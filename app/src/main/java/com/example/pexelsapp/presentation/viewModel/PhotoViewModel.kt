package com.example.pexelsapp.presentation.viewModel

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Environment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pexelsapp.data.local.PhotoDbEntity
import com.example.pexelsapp.domain.PhotoRepository
import com.example.pexelsapp.domain.model.FeaturedCollection
import com.example.pexelsapp.domain.model.Photo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Timer
import javax.inject.Inject
import kotlin.concurrent.timerTask

@HiltViewModel
class PhotoViewModel @Inject constructor(private val repository: PhotoRepository) : ViewModel() {

    var photoList: MutableLiveData<List<Photo>> = MutableLiveData()
    var collectionList: MutableLiveData<List<FeaturedCollection>> = MutableLiveData()
    var detailsPhoto: MutableLiveData<Photo> = MutableLiveData()
    val viewState: MutableLiveData<ViewState> = MutableLiveData()

    private var currentQuery: String = ""
    private var page = 1
    private val handler = CoroutineExceptionHandler { _, _ ->
        viewState.value = currentViewState().copy(noInternerConnection = true)
    }

    init {
        viewState.value = ViewState()
        getFeaturedCollections()
        startLoad()
        getBookmarks()
    }

    fun getPhoto(newQuery: String) {
        if (newQuery != currentQuery) {
            viewState.value = currentViewState().copy(isLoading = true)
            currentQuery = newQuery
            viewState.value = currentViewState().copy(currentQuery = newQuery)
            when (newQuery) {
                "" -> {
                    viewState.value = currentViewState().copy(
                        noResaultsFound = false, progress = 50, selectedCollection = null
                    )
                    viewModelScope.launch(handler) {
                        page = 1
                        val response = repository.loadCuratedPhoto(page)
                        photoList.postValue(response.body()?.photos)
                        viewState.value =
                            currentViewState().copy(noInternerConnection = false, progress = 100)
                    }
                    Timer().schedule(timerTask {
                        viewState.postValue(
                            currentViewState().copy(isLoading = false, progress = 0)
                        )
                    }, 2000)
                }

                else -> {
                    checkCollection(newQuery)
                    viewModelScope.launch(handler) {

                        page = 1
                        val response = repository.loadPhoto(page, newQuery)
                        when (response.body()?.photos?.isEmpty()) {
                            true -> viewState.value =
                                currentViewState().copy(noResaultsFound = true)

                            false -> viewState.value =
                                currentViewState().copy(noResaultsFound = false)

                            else -> viewState.value =
                                currentViewState().copy(noResaultsFound = true)
                        }
                        photoList.postValue(response.body()?.photos)
                        viewState.value =
                            currentViewState().copy(noInternerConnection = false, progress = 100)
                    }
                    Timer().schedule(timerTask {
                        viewState.postValue(
                            currentViewState().copy(isLoading = false, progress = 0)
                        )
                    }, 2000)
                }
            }
        }
    }

    fun tryAgain() {
        getFeaturedCollections()
        viewState.value = currentViewState().copy(isLoading = true)
        when (currentQuery) {
            "" -> {
                viewState.value = currentViewState().copy(
                    noResaultsFound = false, progress = 50, selectedCollection = null
                )
                viewModelScope.launch(handler) {
                    page = 1
                    val response = repository.loadCuratedPhoto(page)
                    photoList.postValue(response.body()?.photos)
                    viewState.value =
                        currentViewState().copy(noInternerConnection = false, progress = 100)
                }
                Timer().schedule(timerTask {
                    viewState.postValue(
                        currentViewState().copy(isLoading = false, progress = 0)
                    )
                }, 2000)
            }

            else -> {
                checkCollection(currentQuery)
                viewState.value = currentViewState().copy(progress = 50)
                viewModelScope.launch(handler) {
                    page = 1
                    val response = repository.loadPhoto(page, currentQuery)
                    when (response.body()?.photos?.isEmpty()) {
                        true -> viewState.value = currentViewState().copy(noResaultsFound = true)
                        false -> viewState.value = currentViewState().copy(noResaultsFound = false)
                        else -> {}
                    }
                    photoList.postValue(response.body()?.photos)
                    viewState.value =
                        currentViewState().copy(noInternerConnection = false, progress = 100)
                }
                Timer().schedule(timerTask {
                    viewState.postValue(
                        currentViewState().copy(isLoading = false, progress = 0)
                    )
                }, 2000)
            }
        }
    }

    private fun startLoad() {
        viewModelScope.launch(handler) {
            page = 1
            val response = repository.loadCuratedPhoto(page)
            photoList.postValue(response.body()?.photos)
            viewState.value = currentViewState().copy(noInternerConnection = false)
        }
    }

    fun loadMorePhoto() {
        viewState.value = currentViewState().copy(isLoading = true)
        when (currentQuery) {
            "" -> viewModelScope.launch(handler) {
                page++
                val response = repository.loadCuratedPhoto(page)
                photoList.value = photoList.value?.plus(response.body()?.photos as List<Photo>)
                viewState.postValue(currentViewState().copy(isLoading = false))
            }

            else -> viewModelScope.launch(handler) {
                page++
                val response = repository.loadPhoto(page, currentQuery)
                photoList.value = photoList.value?.plus(response.body()?.photos as List<Photo>)
                viewState.value = currentViewState().copy(isLoading = false)
            }
        }
    }

    private fun checkCollection(newQuery: String) {
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
    }

    private fun getFeaturedCollections() {
        viewModelScope.launch(handler) {
            val response = repository.loadFeaturedCollections()
            collectionList.postValue(response.body()?.collections)
        }
    }

    fun getBookmarks(): Flow<List<Photo>> {
        return repository.getAllPhotos().map { it.toList().map { it1 -> it1.toPhoto() } }
    }

    fun bookmarksCheck(it: List<Photo>) {
        when (it) {
            emptyList<Photo>() -> viewState.postValue(currentViewState().copy(noBookmarksFound = true))
            else -> viewState.postValue(currentViewState().copy(noBookmarksFound = false))
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

    fun bookmarkPhoto(photo: Photo) {
        viewModelScope.launch {
            if (repository.findPhotoById(photo.id) != null) {
                repository.deletePhoto(photo.id)
                viewState.postValue(currentViewState().copy(isBookmarked = false))
            } else {
                repository.insertPhoto(
                    PhotoDbEntity(
                        photo.id,
                        photo.alt,
                        photo.avg_color,
                        photo.height,
                        photo.photographer,
                        photo.src.portrait,
                        photo.url,
                        photo.width
                    )
                )
                viewState.value = currentViewState().copy(isBookmarked = true)
            }
        }
    }

    fun checkBookmarked() {
        viewModelScope.launch {
            if (repository.findPhotoById(detailsPhoto.value!!.id) != null) {
                viewState.value = currentViewState().copy(isBookmarked = true)
            } else {
                viewState.value = currentViewState().copy(isBookmarked = false)
            }
        }
    }

    data class ViewState(
        val isLoading: Boolean = false,
        val isLastPage: Boolean = false,
        val isFCollections: Boolean = false,
        val isToastDownload: Boolean = false,
        val progress: Int = 0,
        val selectedCollection: Int? = null,
        val isBookmarked: Boolean = false,

        val noInternerConnection: Boolean = false,
        val noBookmarksFound: Boolean = false,
        val noResaultsFound: Boolean = false,
        val currentQuery: String = ""
    )

    fun currentViewState(): ViewState = viewState.value!!
}