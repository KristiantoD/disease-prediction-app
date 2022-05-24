package com.example.diseaseprediction.view.consultate

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.diseaseprediction.R
import com.example.diseaseprediction.databinding.FragmentConsultationBinding
import com.example.diseaseprediction.view.diseases.DiseaseFragment
import com.example.diseaseprediction.view.welcome.MainActivity

class ConsultationFragment : Fragment() {
    private var _binding: FragmentConsultationBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ConsultationViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[ConsultationViewModel::class.java]

        _binding = FragmentConsultationBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }

        val toolbar: Toolbar = binding.toolbar
        toolbar.inflateMenu(R.menu.logout_menu)
        toolbar.setLogo(R.drawable.ic_outline_account_circle_24)
        playAnimation()

        toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.menu1) {
                context?.let { fragmentContext ->
                    AlertDialog.Builder(fragmentContext).apply {
                        setTitle(getString(R.string.logout))
                        setMessage(getString(R.string.logout_message))
                        setPositiveButton(getString(R.string.logout)) { _, _ ->
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
    }
}