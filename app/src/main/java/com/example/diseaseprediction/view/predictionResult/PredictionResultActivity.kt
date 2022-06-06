package com.example.diseaseprediction.view.predictionResult

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.transition.TransitionManager
import com.example.diseaseprediction.R
import com.example.diseaseprediction.api.responses.ResultItem
import com.example.diseaseprediction.databinding.ActivityPredictionResultBinding
import com.example.diseaseprediction.model.Preference
import com.example.diseaseprediction.view.ViewModelFactory
import com.example.diseaseprediction.view.consultate.ConsultationFragment.Companion.EXTRA_COMPLAINT

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class PredictionResultActivity : AppCompatActivity() {
    private lateinit var viewModel: PredictionResultViewModel
    private lateinit var binding: ActivityPredictionResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPredictionResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAppbar()
        setupViewModel()

        showLoading(true)
        val complaint = intent.getStringExtra(EXTRA_COMPLAINT).toString()
        viewModel.predict(complaint)
        setupAction()
    }

    private fun setupAppbar() {
        val actionbar = supportActionBar
        actionbar!!.title =
            Html.fromHtml("<font color='#5D5D5D'>${getString(R.string.prediction_result)}</font>")
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        actionbar.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        actionbar.elevation = 10f
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(Preference.getInstance(dataStore))
        )[PredictionResultViewModel::class.java]
    }

    private fun setupAction() {
        viewModel.predictionResponse.observe(this) { predictionResponse ->
            setPredictionData(predictionResponse)
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun setPredictionData(predictionItems: List<ResultItem>) {
        val listItem = ArrayList<ResultItem>()
        for (item in predictionItems) {
            val itemsItem =
                ResultItem(item.disease, item.deskripsi, item.probability)
            Log.d("okhttp item", item.toString())
            listItem.add(itemsItem)
        }
        Log.d("okhttp listitem", listItem.toString())
        binding.tvPredictionText1.text = listItem[0].disease
        binding.tvPredictionDescription1.text = listItem[0].deskripsi
        binding.tvPredictionText2.text = listItem[1].disease
        binding.tvPredictionDescription2.text = listItem[1].deskripsi
        binding.tvPredictionText3.text = listItem[2].disease
        binding.tvPredictionDescription3.text = listItem[2].deskripsi

        val percent1 = listItem[0].probability * 100
        binding.tvPredictionPercentage1.text = String.format("%.2f", percent1)
        binding.percentageBar1.progress = percent1.toInt()

        val percent2 = listItem[1].probability * 100
        binding.tvPredictionPercentage2.text = String.format("%.2f", percent2)
        binding.percentageBar2.progress = percent2.toInt()

        val percent3 = listItem[2].probability * 100
        binding.tvPredictionPercentage3.text = String.format("%.2f", percent3)
        binding.percentageBar3.progress = percent3.toInt()
        playAnimation()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun playAnimation() {
        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(200)
        val card1 = ObjectAnimator.ofFloat(binding.predictionCard1, View.ALPHA, 1f).setDuration(200)
        val card2 = ObjectAnimator.ofFloat(binding.predictionCard2, View.ALPHA, 1f).setDuration(200)
        val card3 = ObjectAnimator.ofFloat(binding.predictionCard3, View.ALPHA, 1f).setDuration(200)

        AnimatorSet().apply {
            playSequentially(title, card1, card2, card3)
            start()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun expandCard1(view: View) {
        val v = if (binding.tvPredictionDescription1.visibility == View.GONE) {
            View.VISIBLE
        } else {
            View.GONE
        }
        TransitionManager.beginDelayedTransition(binding.predictionCard1Layout)
        binding.tvPredictionDescription1.visibility = v
    }

    fun expandCard2(view: View) {
        val v = if (binding.tvPredictionDescription2.visibility == View.GONE) {
            View.VISIBLE
        } else {
            View.GONE
        }
        TransitionManager.beginDelayedTransition(binding.predictionCard2Layout)
        binding.tvPredictionDescription2.visibility = v
    }

    fun expandCard3(view: View) {
        val v = if (binding.tvPredictionDescription3.visibility == View.GONE) {
            View.VISIBLE
        } else {
            View.GONE
        }
        TransitionManager.beginDelayedTransition(binding.predictionCard3Layout)
        binding.tvPredictionDescription3.visibility = v
    }
}