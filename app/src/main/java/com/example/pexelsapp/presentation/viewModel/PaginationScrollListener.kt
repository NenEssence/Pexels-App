package com.example.pexelsapp.presentation.viewModel

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

abstract class PaginationScrollListener(private val layoutManager: StaggeredGridLayoutManager) :
    RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        var firstVisibleItemPosition = 0

        val firstVisibleItemPositions =
            layoutManager.findFirstVisibleItemPositions(null)

        firstVisibleItemPosition = firstVisibleItemPositions[0]

        if (!isLoading && !isLastPage) {
            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                && firstVisibleItemPosition >= 0
            ) {
                loadMoreItems()
            }
        }
    }

    protected abstract fun loadMoreItems()
    abstract val  isLastPage: Boolean
    abstract val  isLoading: Boolean
}