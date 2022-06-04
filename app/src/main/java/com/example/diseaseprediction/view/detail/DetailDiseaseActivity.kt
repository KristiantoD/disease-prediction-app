package com.example.diseaseprediction.view.detail

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionManager
import com.bumptech.glide.Glide
import com.example.diseaseprediction.R
import com.example.diseaseprediction.api.responses.ListItemResponse
import com.example.diseaseprediction.databinding.ActivityDetailDiseaseBinding
import com.example.diseaseprediction.model.Preference
import com.example.diseaseprediction.view.ViewModelFactory
import com.example.diseaseprediction.view.adapter.ListItemAdapter
import com.example.diseaseprediction.view.adapter.ListItemAdapter.Companion.EXTRA_ITEM
import com.example.diseaseprediction.view.navigation.NavigationActivity.Companion.EXTRA_REFRESH
import com.example.diseaseprediction.view.navigation.NavigationActivity.Companion.EXTRA_TOKEN
import com.example.diseaseprediction.view.welcome.MainActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Suppress("DEPRECATION")
class DetailDiseaseActivity : AppCompatActivity() {
    private lateinit var viewModel: DetailViewModel
    private lateinit var binding: ActivityDetailDiseaseBinding
    private lateinit var accessToken: String
    private lateinit var refreshToken: String
    private lateinit var slug: String
    private var isLoading: Boolean = true
    private var isLoadingMed: Boolean = true
    private var v: Int = View.GONE
    private var vMed: Int = View.GONE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailDiseaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAppbar()
        setupData()
        setupViewModel()
        viewModel.getDetail(accessToken, refreshToken, slug, true)
        setupAction()
    }

    private fun setupAppbar() {
        val actionbar = supportActionBar
        actionbar!!.title =
            Html.fromHtml("<font color='#5D5D5D'>${getString(R.string.disease_detail)}</font>")
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        actionbar.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        actionbar.elevation = 10f
    }

    private fun setupData() {
        val item = intent.getParcelableExtra<ListItemResponse>(EXTRA_ITEM) as ListItemResponse
        accessToken = intent.getStringExtra(EXTRA_TOKEN).toString()
        refreshToken = intent.getStringExtra(EXTRA_REFRESH).toString()
        slug = item.slug
        Glide.with(applicationContext)
            .load(item.img)
            .into(binding.imgItemPhoto)
        binding.tvDetailName.text = item.name
        binding.tvSummaryDescription.text = item.excerpt
    }

    private fun setupAction() {
        viewModel.detailItemResponse.observe(this) { allStoryResponse ->
            binding.tvDetailDescription.text = Html.fromHtml(allStoryResponse[0].description)
        }

        viewModel.medicineResponse.observe(this) { allItemResponse ->
            showLoading(true)
            setMedicineData(allItemResponse)
        }

        viewModel.isLoading.observe(this) {
            isLoading = it
            showLoading(it)
        }

        viewModel.isLoadingMed.observe(this) {
            isLoadingMed = it
            showLoadingMed(it)
        }

        viewModel.authorize().observe(this) { user ->
            if (!user.isLogin) {
                AlertDialog.Builder(this).apply {
                    setTitle(getString(R.string.session_expired))
                    setMessage("Please login again")
                    setPositiveButton(getString(R.string.continuestring)) { _, _ ->
                        viewModel.logout()
                        gotoWelcome()
                    }
                    create()
                    show()
                }
            }
        }
    }

    private fun gotoWelcome() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(Preference.getInstance(dataStore))
        )[DetailViewModel::class.java]
    }

    private fun setMedicineData(medicineItems: List<ListItemResponse>) {
        val listItem = ArrayList<ListItemResponse>()
        val listSlug = ArrayList<String>()
        for (medicine in medicineItems) {
            if (medicine.slug in listSlug) {
                continue
            } else {
                listSlug.add(medicine.slug)
                val itemsItem =
                    ListItemResponse(medicine.img, medicine.name, medicine.slug, medicine.excerpt)
                listItem.add(itemsItem)
            }
        }
        if (listSlug.isEmpty()) {
            binding.tvMedicineNotfound.visibility = View.VISIBLE
        }
        showRecyclerList(listItem)
    }

    private fun showRecyclerList(listItem: List<ListItemResponse>) {
        binding.rvMedicines.layoutManager = LinearLayoutManager(this)
        val listUserAdapter = ListItemAdapter(listItem, false, accessToken, refreshToken)
        binding.rvMedicines.adapter = listUserAdapter
        showLoading(false)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility =
            if (isLoading and (v == View.VISIBLE)) View.VISIBLE else View.GONE
    }

    fun expand(view: View) {
        v = if (binding.tvDetailDescription.visibility == View.GONE) {
            View.VISIBLE
        } else {
            View.GONE
        }
        showLoading(isLoading)

        TransitionManager.beginDelayedTransition(binding.descriptionLayout)
        binding.tvDetailDescription.visibility = v
    }

    private fun showLoadingMed(isLoadingMed: Boolean) {
        binding.progressBarMedicine.visibility =
            if (isLoadingMed and (vMed == View.VISIBLE)) View.VISIBLE else View.GONE
    }

    fun expandMed(view: View) {
        vMed = if (binding.rvMedicines.visibility == View.GONE) {
            View.VISIBLE
        } else {
            View.GONE
        }
        showLoadingMed(isLoadingMed)

        TransitionManager.beginDelayedTransition(binding.descriptionLayout)
        binding.rvMedicines.visibility = vMed
    }
}