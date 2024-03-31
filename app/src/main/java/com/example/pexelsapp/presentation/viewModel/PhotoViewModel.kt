package com.example.pexelsapp.presentation.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pexelsapp.data.remote.model.Photo
import com.example.pexelsapp.domain.PhotoRepository
import kotlinx.coroutines.launch

class PhotoViewModel(private val repository: PhotoRepository) :
    ViewModel() {

    var photolist: MutableLiveData<List<Photo>> = MutableLiveData()
    val viewState: MutableLiveData<ViewState> = MutableLiveData()
    private var query: String = "nature"
    private var page = 1

    private var ct = true

    init {
        viewState.value = ViewState()
        getPhoto(query)
    }


    fun getPhoto(newQuery: String) {
        viewModelScope.launch {
            query = newQuery
            page = 1
            val response = repository.loadPhoto(newQuery)
            photolist.postValue(response.body()?.photos)
            Log.d("RESPONSE", "${response.body()?.total_results}")
        }
    }

    fun loadMorePhoto() {
        viewState.value = currentViewState().copy(isLoading = true)
        viewModelScope.launch {
            page++
            viewState.value = currentViewState().copy(progress = 0)
            val response = repository.loadMorePhoto(page, query)
            photolist.value = photolist.value?.plus(response.body()?.photos as List<Photo>)
            viewState.value = currentViewState().copy(progress = 30)
            photolist.value?.plus(response.body()?.photos)
            viewState.value = currentViewState().copy(progress = 100)
            viewState.value = currentViewState().copy(isLoading = false)
            Log.d("loadMorePhoto()", "${response.body()}")
        }
    }

    data class ViewState(
        val isLoading: Boolean = false,
        val isLastPage: Boolean = false,
        val progress: Int = 0
    )

    fun currentViewState(): ViewState = viewState.value!!
}