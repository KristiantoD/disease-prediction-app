package com.example.diseaseprediction.view.welcome


import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.transition.TransitionManager
import com.example.diseaseprediction.R
import com.example.diseaseprediction.databinding.ActivityAboutBinding
import com.example.diseaseprediction.model.Preference
import com.example.diseaseprediction.view.ViewModelFactory
import com.example.diseaseprediction.view.navigation.NavigationActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class AboutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutBinding
    private lateinit var viewModel: AboutViewModel
    private lateinit var accessToken: String
    private lateinit var refreshToken: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupAction()
        playAnimation()

        accessToken = intent.getStringExtra(NavigationActivity.EXTRA_TOKEN).toString()
        refreshToken = intent.getStringExtra(NavigationActivity.EXTRA_REFRESH).toString()


        supportActionBar?.hide()
        val toolbar: Toolbar = binding.toolbar
        toolbar.inflateMenu(R.menu.logout_menu)
        toolbar.title = getString(R.string.dipi)
        toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.menu1) {
                this.let {
                    AlertDialog.Builder(it).apply {
                        setTitle(getString(R.string.logout))
                        setMessage(getString(R.string.logout_message))
                        setPositiveButton(getString(R.string.logout)) { _, _ ->
                            viewModel.logout()
                            val intent = Intent(context, MainActivity::class.java)
                            it.startActivity(intent)
                            it.finish()
                        }
                        setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                            dialog.dismiss()
                        }
                        create()
                        show()
                    }
                }
            }
            true
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(Preference.getInstance(dataStore))
        )[AboutViewModel::class.java]
    }

    private fun setupAction() {
        viewModel.authorize().observe(this) { user ->
            binding.tvProfileUsername.text = user.username
            binding.tvProfileName.text = buildString {
                append(getString(R.string.hello))
                append(", ")
                append(user.name)
            }
            binding.tvProfileEmail.text = user.email
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.infoImage, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
        val duration = 200L
        val profileCard =
            ObjectAnimator.ofFloat(binding.cardProfile, View.ALPHA, 1f).setDuration(duration)
        val aboutCard =
            ObjectAnimator.ofFloat(binding.cardAbout, View.ALPHA, 1f).setDuration(duration)
        val consulCard =
            ObjectAnimator.ofFloat(binding.cardConsultation, View.ALPHA, 1f).setDuration(duration)

        val profileImage =
            ObjectAnimator.ofFloat(binding.profileImage, View.ALPHA, 1f).setDuration(duration)
        val profileUsername =
            ObjectAnimator.ofFloat(binding.tvProfileUsername, View.ALPHA, 1f).setDuration(duration)
        val profileName =
            ObjectAnimator.ofFloat(binding.tvProfileName, View.ALPHA, 1f).setDuration(duration)
        val profileEmail =
            ObjectAnimator.ofFloat(binding.tvProfileEmail, View.ALPHA, 1f).setDuration(duration)

        val consulTitle = ObjectAnimator.ofFloat(binding.tvConsultationTitle, View.ALPHA, 1f)
            .setDuration(duration)
        val consulImage =
            ObjectAnimator.ofFloat(binding.consultationImage, View.ALPHA, 1f).setDuration(duration)

        val aboutTitle =
            ObjectAnimator.ofFloat(binding.tvAboutTitle, View.ALPHA, 1f).setDuration(duration)
        val aboutText =
            ObjectAnimator.ofFloat(binding.tvAboutText, View.ALPHA, 1f).setDuration(duration)


        AnimatorSet().apply {
            playSequentially(
                profileCard,
                profileImage,
                profileName,
                profileEmail,
                profileUsername,
                consulCard,
                consulTitle,
                consulImage,
                aboutCard,
                aboutTitle,
                aboutText
            )
            start()
        }
    }

    fun expand(view: View) {
        val v = if (binding.tvAboutText.visibility == View.GONE) {
            View.VISIBLE
        } else {
            View.GONE
        }

        TransitionManager.beginDelayedTransition(binding.aboutLayout)
        binding.tvAboutText.visibility = v
    }


    fun startNavigation(view: View) {
        val intent = Intent(this, NavigationActivity::class.java)
        intent.putExtra(NavigationActivity.EXTRA_TOKEN, accessToken)
        intent.putExtra(NavigationActivity.EXTRA_REFRESH, refreshToken)
        startActivity(intent)
    }

}