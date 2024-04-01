package com.example.pexelsapp.presentation


import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
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
        navController = (binding.navHostFragment.getFragment<NavHostFragment>())
            .findNavController()


        binding.bottomNavigationView.setupWithNavController(navController)
        binding.bottomNavigationView.itemIconTintList = null
        setActiveIcon(0)
        binding.bottomNavigationView.setOnItemSelectedListener {

            if (navController.currentDestination?.id == it.itemId) {
                false
            } else {
                when (it.itemId) {
                    R.id.homeFragment -> {
                        navController.navigate(R.id.action_bookmarkFragment_to_homeFragment)
                        setActiveIcon(0)
                        true
                    }

                    R.id.bookmarkFragment -> {
                        navController.navigate(R.id.action_homeFragment_to_bookmarkFragment)
                        setActiveIcon(1)
                        true
                    }
                    else -> false
                }
            }
        }
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.detailsFragment) {
                binding.bottomNavigationView.visibility = View.GONE
            } else {
                binding.bottomNavigationView.visibility = View.VISIBLE
            }
        }
    }
    private fun setActiveIcon(item:Int){
        when(item){
            0 -> {  binding.bottomNavigationView.menu.getItem(0)
                .setIcon(R.drawable.home_icon_active)
                binding.bottomNavigationView.menu.getItem(1)
                    .setIcon(R.drawable.bookmark_icon_inactive)}
            1 -> {
                binding.bottomNavigationView.menu.getItem(0)
                    .setIcon(R.drawable.home_icon_inactive)
                binding.bottomNavigationView.menu.getItem(1)
                    .setIcon(R.drawable.bookmark_icon_active)
            }
        }
    }
}
