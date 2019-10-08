package com.drevnitskaya.instaclientapp.presentation.profile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.drevnitskaya.instaclientapp.R
import com.drevnitskaya.instaclientapp.data.entities.FeedItem
import com.drevnitskaya.instaclientapp.extensions.loadImage
import kotlinx.android.synthetic.main.item_feed.view.*
import kotlinx.android.synthetic.main.item_feed_error.view.*
import java.lang.IllegalArgumentException
import kotlin.properties.Delegates

class FeedAdapter(
    private val onRetryClicked: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    init {
        setHasStableIds(true)
    }

    var showLoadMore: Boolean by Delegates.observable(false) { _, _, _ ->
        prepareItemsList()
    }

    var showLoadMoreError: Boolean by Delegates.observable(false) { _, _, _ ->
        prepareItemsList()
    }

    var feed: List<FeedItem> by Delegates.observable(emptyList()) { _, _, _ ->
        prepareItemsList()
    }

    private var items: List<Item<out Any>> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    private fun prepareItemsList() {
        val newItems: MutableList<Item<out Any>> = feed.map { Item(it) }.toMutableList()
        if (showLoadMore) {
            newItems.add(Progress)
        }
        if (showLoadMoreError) {
            newItems.add(Error)
        }
        items = newItems
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            FeetItemType.FEED_ITEM.value -> {
                val itemView = inflater.inflate(R.layout.item_feed, parent, false)
                FeedItemHolder(itemView)
            }
            FeetItemType.PROGRESS.value -> {
                val itemView = inflater.inflate(R.layout.item_feed_progress, parent, false)
                ProgressItemHolder(itemView)
            }
            FeetItemType.ERROR.value -> {
                val itemView = inflater.inflate(R.layout.item_feed_error, parent, false)
                ErrorItemHolder(itemView)
            }
            else -> {
                throw IllegalArgumentException("Unknown feed view type")
            }
        }
    }

    override fun getItemCount(): Int = items.size

    override fun getItemId(position: Int): Long {
        val item = items[position]
        return when (getItemViewType(position)) {
            FeetItemType.FEED_ITEM.value -> (item.data as FeedItem).id.hashCode().toLong()
            FeetItemType.PROGRESS.value -> item.hashCode().toLong()
            FeetItemType.ERROR.value -> item.hashCode().toLong()
            else -> throw IllegalArgumentException("Unknown feed view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            FeetItemType.FEED_ITEM.value -> {
                val item = items[position]
                (holder as FeedItemHolder).bind(item.data as FeedItem)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is Progress -> FeetItemType.PROGRESS.value
            is Error -> FeetItemType.ERROR.value
            else -> FeetItemType.FEED_ITEM.value
        }
    }

    inner class FeedItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val placeholder = ContextCompat.getDrawable(
            itemView.context,
            R.drawable.ic_image_placeholder
        )?.apply {
            setTint(ContextCompat.getColor(itemView.context, R.color.bridesmaid))
        }

        fun bind(item: FeedItem) = with(itemView) {
            itemFeedImage.loadImage(placeholder, item.images?.standardResolutionImg?.url)
            val likesCount = item.likes?.count ?: 0
            itemFeedLikesLabel.text = if (likesCount > 0) {
                context.getString(R.string.itemFeed_likesCountLabel, likesCount)
            } else {
                context.getString(R.string.itemFeed_noLikesLabel)
            }
            itemFeedLocationLabel.text = item.location?.name ?: ""
        }
    }

    inner class ProgressItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class ErrorItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.itemFeedErrorRetryBtn.setOnClickListener {
                onRetryClicked()
            }
        }
    }

    enum class FeetItemType(val value: Int) {
        FEED_ITEM(0),
        PROGRESS(1),
        ERROR(2)
    }

    private open class Item<T>(val data: T)
    private object Progress : Item<Int>(FeetItemType.PROGRESS.value)
    private object Error : Item<Int>(FeetItemType.ERROR.value)

}