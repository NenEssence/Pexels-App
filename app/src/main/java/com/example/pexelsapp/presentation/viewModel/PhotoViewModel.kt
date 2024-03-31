package com.example.pexelsapp.presentation.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pexelsapp.data.remote.model.FeaturedCollection
import com.example.pexelsapp.data.remote.model.Photo
import com.example.pexelsapp.domain.PhotoRepository
import kotlinx.coroutines.launch

class PhotoViewModel(private val repository: PhotoRepository) :
    ViewModel() {

    var photoList: MutableLiveData<List<Photo>> = MutableLiveData()
    var collectionList: MutableLiveData<List<FeaturedCollection>> = MutableLiveData()
    val viewState: MutableLiveData<ViewState> = MutableLiveData()

    private var query: String = "nature"
    private var page = 1


    init {
        viewState.value = ViewState()
        getFeaturedCollections()
        getPhoto(query)
    }


    fun getPhoto(newQuery: String) {


        //Search in loaded Featured Collections
        val findCollection = collectionList.value?.find { it.title == newQuery }
        if (findCollection != null) {
            viewState.value = currentViewState().copy(
                selectedCollection = collectionList.value!!.indexOf(findCollection)
            )
        }else{
            viewState.value = currentViewState().copy(
                selectedCollection = null)
        }


        viewModelScope.launch {
            query = newQuery
            page = 1
            val response = repository.loadPhoto(newQuery)
            photoList.postValue(response.body()?.photos)
            Log.d("RESPONSE", "${response.body()?.total_results}")
        }
    }

    fun loadMorePhoto() {
        viewState.value = currentViewState().copy(isLoading = true)
        viewModelScope.launch {
            page++
            viewState.value = currentViewState().copy(progress = 0)
            val response = repository.loadMorePhoto(page, query)
            photoList.value = photoList.value?.plus(response.body()?.photos as List<Photo>)
            viewState.value = currentViewState().copy(progress = 30)
            photoList.value?.plus(response.body()?.photos)
            viewState.value = currentViewState().copy(progress = 100)
            viewState.value = currentViewState().copy(isLoading = false)
            Log.d("loadMorePhoto()", "${response.body()}")
        }
    }

    fun getFeaturedCollections() {
        viewModelScope.launch {
            val response = repository.getFeaturedCollections()
            collectionList.postValue(response.body()?.collections)
        }
    }

    data class ViewState(
        val isLoading: Boolean = false,
        val isLastPage: Boolean = false,
        val isFCollections: Boolean = false,
        val progress: Int = 0,
        val selectedCollection: Int? = null
    )

    fun currentViewState(): ViewState = viewState.value!!
}