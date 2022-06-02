package com.example.diseaseprediction.view.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Patterns.EMAIL_ADDRESS
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.diseaseprediction.R
import com.example.diseaseprediction.api.ApiConfig
import com.example.diseaseprediction.api.responses.RegisterResponse
import com.example.diseaseprediction.databinding.ActivitySignupBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        setupAction()
        playAnimation()
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val username = binding.usernameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val confPassword = binding.confirmPasswordEditText.text.toString()
            when {
                name.isEmpty() -> {
                    binding.nameEditTextLayout.error = getString(R.string.name_empty)
                }
                username.isEmpty() -> {
                    binding.usernameEditTextLayout.error = getString(R.string.username_empty)
                }
                email.isEmpty() -> {
                    binding.emailEditTextLayout.error = getString(R.string.email_empty)
                }
                !EMAIL_ADDRESS.matcher(email).matches() -> {
                    binding.emailEditTextLayout.error = getString(R.string.email_invalid)
                }
                (password.isEmpty() or (password.length < 6)) -> {
                    binding.passwordEditTextLayout.error = getString(R.string.min_password_6_char)
                }
                confPassword != password -> {
                    binding.confirmPasswordEditTextLayout.error =
                        getString(R.string.confpassword_notsame_password)
                }
                else -> {
                    showLoading(true)
                    val jsonObject = JSONObject()
                        .put("email", email)
                        .put("name", name)
                        .put("username", username)
                        .put("password", password)
                        .put("confpassword", confPassword)

                    val requestBody =
                        jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
                    val client = ApiConfig.getApiService().register(requestBody)
                    client.enqueue(object : Callback<RegisterResponse> {
                        override fun onResponse(
                            call: Call<RegisterResponse>,
                            response: Response<RegisterResponse>
                        ) {
                            showLoading(false)
                            if (response.isSuccessful) {
                                val responseBody = response.body()
                                if (responseBody != null) {
                                    Toast.makeText(
                                        this@SignupActivity,
                                        getString(R.string.signup_success),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    gotoWelcome()
                                }
                            } else {
                                try {
                                    val jObjError = JSONObject(response.errorBody()!!.string())
                                    Toast.makeText(
                                        this@SignupActivity,
                                        jObjError.getString("msg"),
                                        Toast.LENGTH_LONG
                                    ).show()
                                } catch (e: Exception) {
                                    Toast.makeText(
                                        this@SignupActivity,
                                        response.errorBody().toString(),
                                        Toast.LENGTH_LONG
                                    )
                                        .show()
                                }
                            }
                        }

                        override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                            Toast.makeText(
                                this@SignupActivity,
                                getString(R.string.api_fail),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
                }
            }
        }
    }

    private fun gotoWelcome() {
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
        val name = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(200)
        val nameInput =
            ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(200)
        val username =
            ObjectAnimator.ofFloat(binding.usernameTextView, View.ALPHA, 1f).setDuration(200)
        val usernameInput =
            ObjectAnimator.ofFloat(binding.usernameEditTextLayout, View.ALPHA, 1f).setDuration(200)
        val email = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(200)
        val emailInput =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(200)
        val password =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(200)
        val passwordInput =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(200)
        val confPassword =
            ObjectAnimator.ofFloat(binding.confirmPasswordTextView, View.ALPHA, 1f).setDuration(200)
        val confPasswordInput =
            ObjectAnimator.ofFloat(binding.confirmPasswordEditTextLayout, View.ALPHA, 1f)
                .setDuration(200)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(200)

        AnimatorSet().apply {
            playSequentially(
                title,
                name,
                nameInput,
                username,
                usernameInput,
                email,
                emailInput,
                password,
                passwordInput,
                confPassword,
                confPasswordInput,
                signup
            )
            start()
        }
    }
}