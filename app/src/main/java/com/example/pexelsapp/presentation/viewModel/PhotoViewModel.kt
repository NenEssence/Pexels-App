package com.example.pexelsapp.presentation.viewModel

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pexelsapp.data.remote.model.FeaturedCollection
import com.example.pexelsapp.data.remote.model.Photo
import com.example.pexelsapp.domain.PhotoRepository
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date


class PhotoViewModel(private val repository: PhotoRepository) : ViewModel() {

    var photoList: MutableLiveData<List<Photo>> = MutableLiveData()
    var collectionList: MutableLiveData<List<FeaturedCollection>> = MutableLiveData()
    var detailsPhoto: MutableLiveData<Photo> = MutableLiveData()
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
        } else {
            viewState.value = currentViewState().copy(
                selectedCollection = null
            )
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

    fun setDetailsState(photo: Photo) {
        detailsPhoto.postValue(photo)
    }

    @SuppressLint("SimpleDateFormat")
    fun saveImage(drawable: Drawable) {
        val file = getDisc()
        if (!file.exists() && !file.mkdirs()) {
            file.mkdir()
        }
        val simpleDateFormat = SimpleDateFormat("yyyymmsshhmmss")
        val date = simpleDateFormat.format(Date())
        val name = "IMG" + date + ".jpg"
        val fileName = file.absolutePath + "/" + name
        val newFile = File(fileName)

        try {
            val draw = drawable as BitmapDrawable
            val bitmap = draw.bitmap
            val fileOutPutStream = FileOutputStream(newFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutPutStream)
            fileOutPutStream.flush()
            fileOutPutStream.close()
            viewState.value = currentViewState().copy(isToastDownload = true)
            viewState.value = currentViewState().copy(isToastDownload = false)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    private fun getDisc(): File {
        val file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        return File(file, "PexelsApp")
    }

    data class ViewState(
        val isLoading: Boolean = false,
        val isLastPage: Boolean = false,
        val isFCollections: Boolean = false,
        val isToastDownload: Boolean = false,
        val progress: Int = 0,
        val selectedCollection: Int? = null
    )

    fun currentViewState(): ViewState = viewState.value!!

}