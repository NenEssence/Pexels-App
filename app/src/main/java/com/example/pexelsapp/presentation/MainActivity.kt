package com.example.pexelsapp.presentation


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.pexelsapp.R
import com.example.pexelsapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(3000)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        navController = (binding.navHostFragment.getFragment<NavHostFragment>()).findNavController()

        binding.bottomNavigationView.let {
            it.setupWithNavController(navController)
            it.itemIconTintList = null
        }

        setActiveIcon(0)
        binding.bottomNavigationView.setOnItemSelectedListener {

            if (navController.currentDestination?.id == it.itemId) {
                false
            } else {
                when (it.itemId) {
                    R.id.homeFragment -> {
                        navController.navigate(R.id.action_bookmarkFragment_to_homeFragment)
                        true
                    }

                    R.id.bookmarkFragment -> {
                        navController.navigate(R.id.action_homeFragment_to_bookmarkFragment)
                        true
                    }

                    else -> false
                }
            }
        }
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.detailsFragment) {
                binding.bottomNavigationView.visibility = View.GONE
                binding.selectorLayout.visibility = View.GONE
            } else {
                binding.bottomNavigationView.visibility = View.VISIBLE
                binding.selectorLayout.visibility = View.VISIBLE
            }
            when (destination.id) {
                R.id.homeFragment -> setActiveIcon(0)
                R.id.bookmarkFragment -> setActiveIcon(1)
            }
        }

    }

    @SuppressLint("ResourceAsColor")
    fun setActiveIcon(item: Int) {
        when (item) {
            0 -> {
                binding.bottomNavigationView.menu.getItem(0).setIcon(R.drawable.home_icon_active)
                binding.bottomNavigationView.menu.getItem(1)
                    .setIcon(R.drawable.bookmark_icon_inactive)
                binding.homeSelector.setCardBackgroundColor(
                    ContextCompat.getColor(
                        this, R.color.red
                    )
                )
                binding.bookmarkSelector.setCardBackgroundColor(
                    ContextCompat.getColor(
                        this, R.color.white
                    )
                )
            }

            1 -> {
                binding.bottomNavigationView.menu.getItem(0).setIcon(R.drawable.home_icon_inactive)
                binding.bottomNavigationView.menu.getItem(1)
                    .setIcon(R.drawable.bookmark_icon_active)
                binding.homeSelector.setCardBackgroundColor(
                    ContextCompat.getColor(
                        this, R.color.white
                    )
                )
                binding.bookmarkSelector.setCardBackgroundColor(
                    ContextCompat.getColor(
                        this, R.color.red
                    )
                )
            }
        }
    }
}
