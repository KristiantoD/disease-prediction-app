package com.example.diseaseprediction.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.diseaseprediction.R
import com.example.diseaseprediction.api.ApiConfig
import com.example.diseaseprediction.api.responses.LoginResponse
import com.example.diseaseprediction.databinding.ActivityLoginBinding
import com.example.diseaseprediction.model.AuthorizationModel
import com.example.diseaseprediction.model.Preference
import com.example.diseaseprediction.view.ViewModelFactory
import com.example.diseaseprediction.view.navigation.NavigationActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        setupViewModel()
        setupAction()
        playAnimation()
    }

    private fun setupViewModel() {
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(Preference.getInstance(dataStore))
        )[LoginViewModel::class.java]
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            when {
                email.isEmpty() -> {
                    binding.emailEditTextLayout.error = getString(R.string.email_empty)
                }
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    binding.emailEditTextLayout.error = getString(R.string.email_invalid)
                }
                (password.isEmpty() or (password.length < 6)) -> {
                    binding.passwordEditTextLayout.error = getString(R.string.min_password_6_char)
                }
                else -> {
                    showLoading(true)
                    val jsonObject = JSONObject()
                        .put("email", email)
                        .put("password", password)

                    val requestBody =
                        jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
                    val client = ApiConfig.getApiService().login(requestBody)
                    client.enqueue(object : Callback<LoginResponse> {
                        override fun onResponse(
                            call: Call<LoginResponse>,
                            response: Response<LoginResponse>
                        ) {
                            showLoading(false)
                            Log.d("response login", response.toString())
                            if (response.isSuccessful) {
                                val responseBody = response.body()
                                if (responseBody != null) {
                                    loginViewModel.saveAuthorization(
                                        AuthorizationModel(
                                            responseBody.accessToken,
                                            responseBody.refreshToken,
                                            responseBody.user?.get(0)?.name ?: "",
                                            responseBody.user?.get(0)?.username ?: "",
                                            responseBody.user?.get(0)?.email ?: "",
                                            false
                                        )
                                    )
                                    loginViewModel.login()
                                    Toast.makeText(
                                        this@LoginActivity,
                                        getString(R.string.login_success),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    gotoConsultation(
                                        responseBody.accessToken,
                                        responseBody.refreshToken
                                    )
                                }
                            } else {
                                try {
                                    val jObjError = JSONObject(response.errorBody()!!.string())
                                    Toast.makeText(
                                        this@LoginActivity,
                                        jObjError.getString("msg"),
                                        Toast.LENGTH_LONG
                                    ).show()
                                } catch (e: Exception) {
                                    Toast.makeText(
                                        this@LoginActivity,
                                        response.body().toString(),
                                        Toast.LENGTH_LONG
                                    )
                                        .show()
                                }
                            }
                        }

                        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                            showLoading(false)
                            Toast.makeText(
                                this@LoginActivity,
                                getString(R.string.api_fail),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
                }
            }
        }
    }

    private fun gotoConsultation(accessToken: String, refreshToken: String) {
        val intent = Intent(this, NavigationActivity::class.java)
        intent.putExtra(NavigationActivity.EXTRA_TOKEN, accessToken)
        intent.putExtra(NavigationActivity.EXTRA_REFRESH, refreshToken)
        startActivity(intent)
        finish()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(200)
        val message =
            ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(200)
        val email = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(200)
        val emailInput =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(200)
        val password =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(200)
        val passwordInput =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(200)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(200)

        AnimatorSet().apply {
            playSequentially(title, message, email, emailInput, password, passwordInput, login)
            start()
        }
    }

//    companion object {
//        const val EXTRA_TOKEN = "extra_token"
//    }
}