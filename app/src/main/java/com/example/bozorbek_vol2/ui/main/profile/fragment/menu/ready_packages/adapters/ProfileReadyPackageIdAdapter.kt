package com.example.bozorbek_vol2.ui.main.profile.fragment.menu.ready_packages.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.bozorbek_vol2.R
import com.example.bozorbek_vol2.model.main.profile.ProfileReadyPackageId
import kotlinx.android.synthetic.main.item_basket_container.view.*

class ProfileReadyPackageIdAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ProfileReadyPackageIdViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_basket_container, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder)
        {
            is ProfileReadyPackageIdViewHolder -> {
                holder.bind(differConfig.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differConfig.currentList.size
    }

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProfileReadyPackageId>()
    {
        override fun areItemsTheSame(
            oldItem: ProfileReadyPackageId,
            newItem: ProfileReadyPackageId
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ProfileReadyPackageId,
            newItem: ProfileReadyPackageId
        ): Boolean {
            return oldItem == newItem
        }

    }

    val differConfig = AsyncListDiffer(this, DIFF_CALLBACK)

    fun submitList(list: List<ProfileReadyPackageId>)
    {
        val newList = list.toMutableList()
        differConfig.submitList(newList)
    }

    inner class ProfileReadyPackageIdViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView)
    {
        fun bind(profileReadyPackageId: ProfileReadyPackageId)
        {
            itemView.item_tv_title_fruit_basket.setText(profileReadyPackageId.product_name)
            itemView.item_mtv_product_name.setText(profileReadyPackageId.name)
            itemView.item_price_basket.setText(profileReadyPackageId.price.toString())
//            requestManager.load(Constants.BASE_URL + profileReadyPackageId.main_image).into(itemView.item_fruit_image_basket)
        }
    }

}