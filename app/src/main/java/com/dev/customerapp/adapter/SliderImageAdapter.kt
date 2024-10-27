package com.dev.customerapp.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.dev.customerapp.R
import com.dev.customerapp.models.BannerModel
import com.smarteist.autoimageslider.SliderViewAdapter

class SliderImageAdapter(private val sliderList: List<BannerModel>) :
    SliderViewAdapter<SliderImageAdapter.SliderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup): SliderViewHolder {
        val inflate =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.auto_image_slider_layout, parent, false)
        return SliderViewHolder(inflate)
    }

    override fun onBindViewHolder(viewHolder: SliderViewHolder, position: Int) {
        Glide.with(viewHolder.itemView)
            .load(sliderList[position].imageUrl)
            .fitCenter()
            .into(viewHolder.imageView)

    }

    override fun getCount(): Int {
        return sliderList.size
    }

    class SliderViewHolder(itemView: View) : SliderViewAdapter.ViewHolder(itemView) {
        val imageView: AppCompatImageView = itemView.findViewById(R.id.imageSlider)
    }
}