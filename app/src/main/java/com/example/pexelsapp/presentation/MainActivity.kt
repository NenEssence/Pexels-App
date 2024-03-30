package com.example.pexelsapp.presentation

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.pexelsapp.R
import com.example.pexelsapp.data.PhotoRepositoryImpl
import com.example.pexelsapp.data.local.PhotoDatabase
import com.example.pexelsapp.data.remote.RetrofitInstance
import com.example.pexelsapp.databinding.ActivityMainBinding
import com.example.pexelsapp.presentation.viewModel.PhotoViewModel
import com.example.pexelsapp.presentation.viewModel.PhotoViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    lateinit var viewModel: PhotoViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        navController = (binding.navHostFragment.getFragment<NavHostFragment>())
            .findNavController()

        val repository = PhotoRepositoryImpl(RetrofitInstance.api)
        val viewModelFactory = PhotoViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[PhotoViewModel::class.java]

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