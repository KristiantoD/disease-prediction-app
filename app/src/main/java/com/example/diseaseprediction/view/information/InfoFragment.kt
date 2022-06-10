package com.example.diseaseprediction.view.information

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.diseaseprediction.R
import com.example.diseaseprediction.databinding.FragmentInfoBinding
import com.example.diseaseprediction.model.Preference
import com.example.diseaseprediction.view.ViewModelFactory
import com.example.diseaseprediction.view.welcome.MainActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class InfoFragment : Fragment() {
    private var _binding: FragmentInfoBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: InfoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInfoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupViewModel()
        setupAction()
        playAnimation()

        val toolbar: Toolbar = binding.toolbar
        toolbar.inflateMenu(R.menu.logout_menu)
        toolbar.title = getString(R.string.informations)

        toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.menu1) {
                context?.let { fragmentContext ->
                    AlertDialog.Builder(fragmentContext).apply {
                        setTitle(getString(R.string.logout))
                        setMessage(getString(R.string.logout_message))
                        setPositiveButton(getString(R.string.logout)) { _, _ ->
                            viewModel.logout()
                            val intent = Intent(activity, MainActivity::class.java)
                            activity?.startActivity(intent)
                            activity?.finish()
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

        return root
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(Preference.getInstance(requireContext().dataStore))
        )[InfoViewModel::class.java]
    }

    private fun setupAction() {
        viewModel.authorize().observe(viewLifecycleOwner) { user ->
            binding.tvProfileUsername.text = buildString {
                append("Username: ")
                append(user.username)
            }
            binding.tvProfileName.text = buildString {
                append("Name: ")
                append(user.name)
            }
            binding.tvProfileEmail.text = buildString {
                append("Email: ")
                append(user.email)
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.infoImage, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val profileCard =
            ObjectAnimator.ofFloat(binding.cardProfile, View.ALPHA, 1f).setDuration(300)
        val aboutCard = ObjectAnimator.ofFloat(binding.cardAbout, View.ALPHA, 1f).setDuration(300)
        val profileTitle =
            ObjectAnimator.ofFloat(binding.tvProfileText, View.ALPHA, 1f).setDuration(300)
        val profileUsername =
            ObjectAnimator.ofFloat(binding.tvProfileUsername, View.ALPHA, 1f).setDuration(300)
        val profileName =
            ObjectAnimator.ofFloat(binding.tvProfileName, View.ALPHA, 1f).setDuration(300)
        val profileEmail =
            ObjectAnimator.ofFloat(binding.tvProfileEmail, View.ALPHA, 1f).setDuration(300)
        val aboutTitle =
            ObjectAnimator.ofFloat(binding.tvAboutTitle, View.ALPHA, 1f).setDuration(300)
        val aboutText = ObjectAnimator.ofFloat(binding.tvAboutText, View.ALPHA, 1f).setDuration(300)

        val together1 = AnimatorSet().apply {
            playTogether(profileCard, aboutCard)
        }

        val together2 = AnimatorSet().apply {
            playTogether(profileTitle, aboutTitle)
        }

        val together3 = AnimatorSet().apply {
            playTogether(profileUsername, aboutText)
        }

        AnimatorSet().apply {
            playSequentially(together1, together2, together3, profileName, profileEmail)
            start()
        }
    }


    companion object {
        fun newInstance() = InfoFragment()
    }

}