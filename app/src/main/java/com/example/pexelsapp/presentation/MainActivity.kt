package com.example.pexelsapp.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.pexelsapp.R
import com.example.pexelsapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = (binding.navHostFragment.getFragment<NavHostFragment>())
            .findNavController()

        //TODO Переделать
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> navController.navigate(R.id.action_bookmarkFragment_to_homeFragment)
                R.id.bookmarks -> navController.navigate(R.id.action_homeFragment_to_bookmarkFragment)
                else -> {
                }
            }
            true
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.bookmarkFragment) {

//                binding.bottomNavigationView.visibility = View.GONE
            } else {
//                binding.bottomNavigationView.visibility = View.VISIBLE
            }
        }
    }
}