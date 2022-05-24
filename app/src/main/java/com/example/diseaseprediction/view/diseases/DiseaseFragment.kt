package com.example.diseaseprediction.view.diseases

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.diseaseprediction.R
import com.example.diseaseprediction.databinding.FragmentDiseaseBinding
import com.example.diseaseprediction.databinding.FragmentMedicineBinding
import com.example.diseaseprediction.view.medicines.MedicineViewModel

class DiseaseFragment : Fragment() {
    private var _binding: FragmentDiseaseBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: DiseaseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[DiseaseViewModel::class.java]
        _binding = FragmentDiseaseBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val searchView: SearchView = binding.searchView
        searchView.clearFocus()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        return root
    }

    companion object {
        fun newInstance() = DiseaseFragment()
    }
}