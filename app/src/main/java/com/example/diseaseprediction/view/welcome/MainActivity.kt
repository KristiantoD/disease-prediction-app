package com.example.diseaseprediction.view.welcome

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import com.example.diseaseprediction.R
import com.example.diseaseprediction.databinding.ActivityMainBinding
import com.example.diseaseprediction.view.login.LoginActivity
import com.example.diseaseprediction.view.signup.SignupActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
//    private lateinit var welcomeViewModel: WelcomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
//        setupViewModel()
        setupAction()
        playAnimation()
    }

//    private fun setupViewModel() {
//        welcomeViewModel = ViewModelProvider(
//            this,
//            ViewModelFactory(Preference.getInstance(dataStore))
//        )[WelcomeViewModel::class.java]
//
//        welcomeViewModel.authorize().observe(this) { user ->
//            if (user.isLogin) {
//                val intent = Intent(this, MainActivity::class.java)
//                intent.putExtra(EXTRA_TOKEN, user.token)
//                startActivity(intent)
//                finish()
//            }
//        }
//    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.signupButton.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(500)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(500)
        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(500)


        val together = AnimatorSet().apply {
            playTogether(login, signup)
        }


        AnimatorSet().apply {
            playSequentially(title, together)
            start()
        }
    }
}
