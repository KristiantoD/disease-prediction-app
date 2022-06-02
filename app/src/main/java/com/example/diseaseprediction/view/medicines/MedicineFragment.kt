package com.example.diseaseprediction.view.medicines

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
import com.example.diseaseprediction.databinding.FragmentMedicineBinding
import com.example.diseaseprediction.model.Preference
import com.example.diseaseprediction.view.ViewModelFactory
import com.example.diseaseprediction.view.adapter.ListItemAdapter

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MedicineFragment : Fragment() {
    private var _binding: FragmentMedicineBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MedicineViewModel
    private lateinit var accessToken: String
    private lateinit var refreshToken: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMedicineBinding.inflate(inflater, container, false)
        val root: View = binding.root

        accessToken = arguments?.getString("extra_token").toString()
        refreshToken = arguments?.getString("extra_refresh").toString()

        setupViewModel()
        setupAction()
        showLoading(true)
        viewModel.getAllMedicine(accessToken, refreshToken)


        val searchView: SearchView = binding.searchView
        searchView.clearFocus()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                viewModel.cancelJob()
                showLoading(true)
                viewModel.searchMedicine(accessToken, refreshToken, query ?: "")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText == "") {
                    viewModel.cancelJob()
                    showLoading(true)
                    viewModel.getAllMedicine(accessToken, refreshToken)
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
        )[MedicineViewModel::class.java]
    }

    private fun setupAction() {
        viewModel.authorize().observe(viewLifecycleOwner) { user ->
            accessToken = user.token
            refreshToken = user.refreshToken
        }

        viewModel.allItemResponse.observe(viewLifecycleOwner) { allItemResponse ->
            showLoading(true)
            setMedicineData(allItemResponse)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }

    private fun setMedicineData(medicineItems: List<ListItemResponse>) {
        val listItem = ArrayList<ListItemResponse>()
        for (medicine in medicineItems) {
            val itemsItem =
                ListItemResponse(medicine.img, medicine.name, medicine.slug, medicine.excerpt)
            listItem.add(itemsItem)
        }
        showRecyclerList(listItem)
    }

    private fun showRecyclerList(listItem: List<ListItemResponse>) {
        binding.rvMedicines.layoutManager = LinearLayoutManager(activity)
        val listUserAdapter = ListItemAdapter(listItem, false, accessToken, refreshToken)
        binding.rvMedicines.adapter = listUserAdapter
        showLoading(false)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        fun newInstance() = MedicineFragment()
    }
}