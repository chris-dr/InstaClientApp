package com.drevnitskaya.instaclientapp.presentation.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.drevnitskaya.instaclientapp.R
import com.drevnitskaya.instaclientapp.data.remote.api.FeedItem
import com.drevnitskaya.instaclientapp.extensions.loadImage
import kotlinx.android.synthetic.main.item_feed.view.*
import kotlin.properties.Delegates

class FeedAdapter : RecyclerView.Adapter<FeedAdapter.FeedItemHolder>() {
    var feed: List<FeedItem> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedItemHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_feed, parent, false)
        return FeedItemHolder(itemView)
    }

    override fun getItemCount(): Int = feed.size

    override fun onBindViewHolder(holder: FeedItemHolder, position: Int) {
        val item = feed[position]
        holder.bind(item)
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

}