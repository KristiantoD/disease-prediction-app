package com.example.diseaseprediction.view.consultate

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.diseaseprediction.R
import com.example.diseaseprediction.databinding.FragmentConsultationBinding
import com.example.diseaseprediction.model.Preference
import com.example.diseaseprediction.view.ViewModelFactory
import com.example.diseaseprediction.view.predictionResult.PredictionResultActivity
import com.example.diseaseprediction.view.welcome.MainActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ConsultationFragment : Fragment() {
    private var _binding: FragmentConsultationBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ConsultationViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConsultationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupViewModel()
        setupAction()

        val toolbar: Toolbar = binding.toolbar
        toolbar.setLogo(R.drawable.ic_baseline_arrow_back_24)
        toolbar.title = getString(R.string.consultation)

        val logo = toolbar.getChildAt(0)
        logo.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            activity?.finish()
        }
        playAnimation()

        return root
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(Preference.getInstance(requireContext().dataStore))
        )[ConsultationViewModel::class.java]
    }

    private fun setupAction() {
        binding.submitButton.setOnClickListener {
            val desc = binding.complainEditText.text.toString()
            if (desc.isNotBlank()) {
                val intent = Intent(context, PredictionResultActivity::class.java)
                intent.putExtra(EXTRA_COMPLAINT, desc)
                startActivity(intent)
            } else {
                Toast.makeText(
                    context,
                    "Please enter some complaint first",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        viewModel.authorize().observe(viewLifecycleOwner) { user ->
            binding.titleTextView.text = buildString {
                append(getString(R.string.hello))
                append(" ")
                append(user.name)
                append(getString(R.string.how_could_help))
            }
        }
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
        val complainInput =
            ObjectAnimator.ofFloat(binding.complainEditTextLayout, View.ALPHA, 1f).setDuration(200)
        val submit = ObjectAnimator.ofFloat(binding.submitButton, View.ALPHA, 1f).setDuration(200)

        AnimatorSet().apply {
            playSequentially(title, message, complainInput, submit)
            start()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = ConsultationFragment()
        const val EXTRA_COMPLAINT = "extra_complaint"
    }
}