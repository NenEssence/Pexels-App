package com.example.pexelsapp.presentation.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pexelsapp.data.remote.model.PexelsApiEntity
import com.example.pexelsapp.domain.PhotoRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class PhotoViewModel(private val repository: PhotoRepository) :
    ViewModel() {

    var photolist: MutableLiveData<Response<PexelsApiEntity>> = MutableLiveData()

    init {
        getPhoto("nature")
    }

    fun getPhoto(s: String) {
        viewModelScope.launch {
            val response = repository.loadPhoto(s)
            Log.d("RESPONSE", "$response")
            photolist.postValue(response)
        }
    }
}