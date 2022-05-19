package com.example.diseaseprediction.view.consultate

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.diseaseprediction.R

class ConsultationFragment : Fragment() {

    companion object {
        fun newInstance() = ConsultationFragment()
    }

    private lateinit var viewModel: ConsultationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_consultation, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ConsultationViewModel::class.java)
        // TODO: Use the ViewModel
    }

}