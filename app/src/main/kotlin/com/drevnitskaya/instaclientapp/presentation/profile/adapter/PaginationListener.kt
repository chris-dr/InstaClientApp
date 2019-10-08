package com.drevnitskaya.instaclientapp.presentation.profile.adapter

import android.graphics.Rect
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PaginationListener(
    private val layoutManager: LinearLayoutManager,
    private val onShouldLoadMore: (localVisibleRectBottomPosition: Int, itemHeight: Int) -> Unit
) : RecyclerView.OnScrollListener() {
    private var dyHistoryScrolled = 0

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        dyHistoryScrolled = dy
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
        if (lastVisibleItemPosition == layoutManager.itemCount - 1) {
            val item = layoutManager.findViewByPosition(lastVisibleItemPosition)
            item?.let {
                val itemHeight = it.height

                val rect = Rect()
                it.getLocalVisibleRect(rect)
                val localVisibleRectBottomPosition = rect.bottom

                if (dyHistoryScrolled > 0) onShouldLoadMore(
                    localVisibleRectBottomPosition,
                    itemHeight
                )
            }
        }
    }
}