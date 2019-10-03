package com.drevnitskaya.instaclientapp.presentation.profile

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.drevnitskaya.instaclientapp.R
import com.drevnitskaya.instaclientapp.data.remote.api.InstaMedia
import com.drevnitskaya.instaclientapp.extensions.loadImage
import kotlinx.android.synthetic.main.item_media.view.*
import kotlin.properties.Delegates

class MediaAdapter : RecyclerView.Adapter<MediaAdapter.MediaHolder>() {
    var media: List<InstaMedia> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_media, parent, false)
        return MediaHolder(itemView)
    }

    override fun getItemCount(): Int = media.size

    override fun onBindViewHolder(holder: MediaHolder, position: Int) {
        val item = media[position]
        holder.bind(item)
    }

    inner class MediaHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val placeholder = ContextCompat.getDrawable(
            itemView.context,
            R.drawable.ic_image_placeholder
        )

        init {
            placeholder?.setTint(ContextCompat.getColor(itemView.context, R.color.bridesmaid))
        }

        fun bind(item: InstaMedia) = with(itemView) {
            itemMediaImage.loadImage(placeholder, item.images?.standardResolutionImg?.url)
            itemMediaLikesLabel.text = context.getString(
                R.string.itemMedia_likesCountLabel,
                item.likes?.count ?: 0
            )
            itemMediaLocationLabel.text = item.location?.name ?: ""
        }
    }

}