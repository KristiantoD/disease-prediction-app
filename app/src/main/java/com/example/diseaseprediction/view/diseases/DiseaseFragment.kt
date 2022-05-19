package com.example.diseaseprediction.view.diseases

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.diseaseprediction.R

class DiseaseFragment : Fragment() {

    companion object {
        fun newInstance() = DiseaseFragment()
    }

    private lateinit var viewModel: DiseaseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_disease, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DiseaseViewModel::class.java)
        // TODO: Use the ViewModel
    }

}