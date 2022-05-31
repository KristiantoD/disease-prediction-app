package com.example.diseaseprediction.view.diseases

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.diseaseprediction.api.responses.ListItemResponse
import com.example.diseaseprediction.databinding.FragmentDiseaseBinding
import com.example.diseaseprediction.model.Preference
import com.example.diseaseprediction.view.ViewModelFactory
import com.example.diseaseprediction.view.adapter.ListItemAdapter

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DiseaseFragment : Fragment() {
    private var _binding: FragmentDiseaseBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: DiseaseViewModel
    private lateinit var accessToken: String
    private lateinit var refreshToken: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiseaseBinding.inflate(inflater, container, false)
        val root: View = binding.root

        accessToken = arguments?.getString("extra_token").toString()
        refreshToken = arguments?.getString("extra_refresh").toString()

        setupViewModel()
        setupAction()
        showLoading(true)
        viewModel.getAllDisease(accessToken, refreshToken)


        val searchView: SearchView = binding.searchView
        searchView.clearFocus()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                showLoading(true)
                viewModel.searchDiseases(accessToken, refreshToken, query ?: "")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText == "") {
                    showLoading(true)
                    viewModel.getAllDisease(accessToken, refreshToken)
                }
                return false
            }
        })
        return root
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(Preference.getInstance(requireContext().dataStore))
        )[DiseaseViewModel::class.java]
    }

    private fun setupAction() {
        viewModel.authorize().observe(viewLifecycleOwner) { user ->
            accessToken = user.token
            refreshToken = user.refreshToken
        }

        viewModel.allItemResponse.observe(viewLifecycleOwner) { allStoryResponse ->
            showLoading(true)
            setDiseasesData(allStoryResponse)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }

    private fun setDiseasesData(diseaseItems: List<ListItemResponse>) {
        val listItem = ArrayList<ListItemResponse>()
        for (disease in diseaseItems) {
            val itemsItem = ListItemResponse(disease.img, disease.name, disease.excerpt)
            listItem.add(itemsItem)
        }
        showRecyclerList(listItem)
    }

    private fun showRecyclerList(listItem: List<ListItemResponse>) {
        binding.rvDiseases.layoutManager = LinearLayoutManager(activity)
        val listUserAdapter = ListItemAdapter(listItem)
        binding.rvDiseases.adapter = listUserAdapter
        showLoading(false)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        fun newInstance() = DiseaseFragment()
    }
}