package com.demo.end.main.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.demo.end.R
import com.demo.end.foundatiion.mvi.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi

@FlowPreview
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@AndroidEntryPoint
class MainActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setupViews()

    }
    fun setupViews()
    {
        // Finding the Navigation Controller
        var navController = findNavController(R.id.fragNavHost)

        // Setting Navigation Controller with the BottomNavigationView
        bottomNavView.setupWithNavController(navController)
        var appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf (
                R.id.searchFragment,
                R.id.wishListFragment,
                R.id.productsListFragment,
                R.id.profileFragment,
                R.id.cartFragment,

            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }
    //Enabling Back button function
    override fun onSupportNavigateUp() =
        Navigation.findNavController(this, R.id.fragNavHost).navigateUp()
}