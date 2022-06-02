package com.example.diseaseprediction.view.navigation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavArgument
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.diseaseprediction.R
import com.example.diseaseprediction.databinding.ActivityNavigationBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

@Suppress("DEPRECATION")
class NavigationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNavigationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)


        supportActionBar?.hide()

        val accessToken = intent.getStringExtra(EXTRA_TOKEN).toString()
        val refreshToken = intent.getStringExtra(EXTRA_REFRESH).toString()


        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_navigation)


        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_medicine, R.id.navigation_consultation, R.id.navigation_disease
            )
        )

        val accessArgument = NavArgument.Builder().setDefaultValue(accessToken).build()
        val refreshArgument = NavArgument.Builder().setDefaultValue(refreshToken).build()

        val navDisease = navController.graph.findNode(R.id.navigation_disease)
        navDisease?.addArgument("extra_token", accessArgument)
        navDisease?.addArgument("extra_refresh", refreshArgument)

        val navMedicine = navController.graph.findNode(R.id.navigation_medicine)
        navMedicine?.addArgument("extra_token", accessArgument)
        navMedicine?.addArgument("extra_refresh", refreshArgument)

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    companion object {
        const val EXTRA_TOKEN = "extra_token"
        const val EXTRA_REFRESH = "extra_refresh"
    }
}