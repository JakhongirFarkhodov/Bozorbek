package com.example.bozorbek_vol2.ui.main.profile.fragment.menu.ready_packages.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.bozorbek_vol2.R
import com.example.bozorbek_vol2.ui.main.profile.fragment.menu.ready_packages.fragments.model.CategoryAutoOrderData
import com.example.bozorbek_vol2.util.Constants
import kotlinx.android.synthetic.main.item_ready_packages_category.view.*

class ProfileAutoOrderChildAdapter(val requestManager: RequestManager) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ProfileReadyPackagesChildViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_ready_packages_category, parent, false), requestManager)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder)
        {
            is ProfileReadyPackagesChildViewHolder ->{
                holder.bind(differConfig.currentList[position])
//                holder.itemView.ready_child_item.animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.fade_scale_animation)
            }
        }
    }

    override fun getItemCount(): Int {
        return differConfig.currentList.size
    }

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CategoryAutoOrderData>()
    {
        override fun areItemsTheSame(oldItem: CategoryAutoOrderData, newItem: CategoryAutoOrderData): Boolean {
            return oldItem.category_id == newItem.category_id
        }

        override fun areContentsTheSame(oldItem: CategoryAutoOrderData, newItem: CategoryAutoOrderData): Boolean {
            return oldItem == newItem
        }


    }

    val differConfig = AsyncListDiffer(this,DIFF_CALLBACK)

    fun submitList(list:List<CategoryAutoOrderData>)
    {
        val newList = list.toMutableList()
        differConfig.submitList(newList)
    }

    inner class ProfileReadyPackagesChildViewHolder(itemView:View, val requestManager: RequestManager) : RecyclerView.ViewHolder(itemView)
    {
        fun bind(categoryData: CategoryAutoOrderData)
        {
            requestManager.load(Constants.BASE_URL + categoryData.get_image).into(itemView.item_ready_packages_category_imv)
            itemView.item_ready_packages_category_title.setText(categoryData.category_name)
        }
    }

}