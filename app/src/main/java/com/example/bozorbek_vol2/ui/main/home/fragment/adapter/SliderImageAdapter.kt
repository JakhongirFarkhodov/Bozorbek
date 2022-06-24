package com.example.bozorbek_vol2.ui.main.home.fragment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.RequestManager
import com.example.bozorbek_vol2.R
import com.example.bozorbek_vol2.model.main.home.HomeSliderImage
import kotlinx.android.synthetic.main.item_slider.view.*

class SliderImageAdapter(val requestManager: RequestManager, viewPager2: ViewPager2) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SliderImageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_slider, parent, false), requestManager)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder)
        {
            is SliderImageViewHolder ->{
                holder.bind(differConfig.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differConfig.currentList.size
    }

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HomeSliderImage>()
    {
        override fun areItemsTheSame(oldItem: HomeSliderImage, newItem: HomeSliderImage): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(
            oldItem: HomeSliderImage,
            newItem: HomeSliderImage
        ): Boolean {
            return oldItem.name == newItem.name
        }

    }

    val differConfig = AsyncListDiffer(this, DIFF_CALLBACK)

    fun submitList(list: List<HomeSliderImage>)
    {
        val newList = list.toMutableList()
        differConfig.submitList(newList)
    }

    inner class SliderImageViewHolder(itemView: View, val requestManager: RequestManager) : RecyclerView.ViewHolder(itemView)
    {
        fun bind(item: HomeSliderImage)
        {
            requestManager.load(item.image).into(itemView.item_image_slider)
        }
    }

}