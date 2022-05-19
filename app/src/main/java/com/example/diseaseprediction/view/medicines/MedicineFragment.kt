package com.example.diseaseprediction.view.medicines

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.diseaseprediction.R

class MedicineFragment : Fragment() {

    companion object {
        fun newInstance() = MedicineFragment()
    }

    private lateinit var viewModel: MedicineViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_medicine, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MedicineViewModel::class.java)
        // TODO: Use the ViewModel
    }

}