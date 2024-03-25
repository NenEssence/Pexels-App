package com.example.pexelsapp.presentation.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.pexelsapp.data.local.PhotoDbEntity
import com.example.pexelsapp.domain.PhotoRepository
import kotlinx.coroutines.launch

class PhotoViewModel(app: Application, private val photoRepository: PhotoRepository) :
    AndroidViewModel(app) {

    fun addPhoto(photo: PhotoDbEntity) {
        viewModelScope.launch {
        }
    }
}