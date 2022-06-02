package com.example.diseaseprediction.view.detail

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.transition.TransitionManager
import com.bumptech.glide.Glide
import com.example.diseaseprediction.R
import com.example.diseaseprediction.api.responses.ListItemResponse
import com.example.diseaseprediction.databinding.ActivityDetailMedicineBinding
import com.example.diseaseprediction.model.Preference
import com.example.diseaseprediction.view.ViewModelFactory
import com.example.diseaseprediction.view.adapter.ListItemAdapter
import com.example.diseaseprediction.view.navigation.NavigationActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Suppress("DEPRECATION")
class DetailMedicineActivity : AppCompatActivity() {
    private lateinit var viewModel: DetailViewModel
    private lateinit var binding: ActivityDetailMedicineBinding
    private lateinit var accessToken: String
    private lateinit var refreshToken: String
    private lateinit var slug: String
    private var isLoading: Boolean = true
    private var v: Int = View.GONE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMedicineBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAppbar()
        setupData()
        setupViewModel()
        viewModel.getDetail(accessToken, refreshToken, slug, false)
        setupAction()
    }

    private fun setupAppbar() {
        val actionbar = supportActionBar
        actionbar!!.title =
            Html.fromHtml("<font color='#5D5D5D'>${getString(R.string.medicine_detail)}</font>")
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        actionbar.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        actionbar.elevation = 10f
    }

    private fun setupData() {
        val item =
            intent.getParcelableExtra<ListItemResponse>(ListItemAdapter.EXTRA_ITEM) as ListItemResponse
        accessToken = intent.getStringExtra(NavigationActivity.EXTRA_TOKEN).toString()
        refreshToken = intent.getStringExtra(NavigationActivity.EXTRA_REFRESH).toString()
        slug = item.slug
        Glide.with(applicationContext)
            .load(item.img)
            .into(binding.imgItemPhoto)
        binding.tvDetailName.text = item.name
        binding.tvSummaryDescription.text = item.excerpt
    }

    private fun setupAction() {
        viewModel.detailItemResponse.observe(this) { allStoryResponse ->
            binding.tvDetailDescription.text = allStoryResponse[0].description
        }

        viewModel.isLoading.observe(this) {
            isLoading = it
            showLoading(it)
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(Preference.getInstance(dataStore))
        )[DetailViewModel::class.java]
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
}