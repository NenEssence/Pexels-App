package com.example.pexelsapp.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.ViewModelFactoryDsl
import com.example.pexelsapp.domain.PhotoRepository

class PhotoViewModelFactory(private val repository: PhotoRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PhotoViewModel(repository) as T
    }
}