package com.example.pexelsapp.presentation.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pexelsapp.data.remote.model.PexelsApiResponse
import com.example.pexelsapp.domain.PhotoRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class PhotoViewModel(private val repository: PhotoRepository) :
    ViewModel() {

    var photolist: MutableLiveData<Response<PexelsApiResponse>?> = MutableLiveData()

    init {
        getPhoto("nature")
    }

    fun getPhoto(s: String) {
        viewModelScope.launch {
            val response = repository.loadPhoto(s)
            photolist.postValue(response)
            Log.d("RESPONSE", "${response.body()?.total_results}")
        }
    }
}