package com.example.bozorbek_vol2.ui.main.profile.fragment.menu.ready_packages.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.bumptech.glide.RequestManager
import com.example.bozorbek_vol2.R
import com.example.bozorbek_vol2.ui.main.profile.fragment.menu.ready_packages.fragments.model.ReadyPackagesData
import kotlinx.android.synthetic.main.item_ready_package.view.*

class ProfileReadyPackagesParentAdapter(val requestManager: RequestManager, val onAddReadyPackageToBasketListener: OnAddReadyPackageToBasketListener, val onShowReadyPackageItemListener: OnShowReadyPackageItemListener, val onRemoveReadyPackageListener: OnRemoveReadyPackageListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ProfileReadyPackagesParentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_ready_package, parent, false), onShowReadyPackageItemListener, onAddReadyPackageToBasketListener, onRemoveReadyPackageListener, requestManager)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder)
        {
            is ProfileReadyPackagesParentViewHolder ->
            {
                holder.bind(differConfig.currentList[position])

            }
        }
    }

    override fun getItemCount(): Int {
        return differConfig.currentList.size
    }

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ReadyPackagesData>()
    {
        override fun areItemsTheSame(
            oldItem: ReadyPackagesData,
            newItem: ReadyPackagesData
        ): Boolean {
            return oldItem.packageData.package_id == newItem.packageData.package_id
        }

        override fun areContentsTheSame(
            oldItem: ReadyPackagesData,
            newItem: ReadyPackagesData
        ): Boolean {
            return oldItem == newItem
        }


    }

    val differConfig = AsyncListDiffer(this,DIFF_CALLBACK)

    fun submitList(list:List<ReadyPackagesData>)
    {
        val newList = list.toMutableList()
        differConfig.submitList(newList)
    }

    inner class ProfileReadyPackagesParentViewHolder(itemView: View, val onShowReadyPackageItemListener: OnShowReadyPackageItemListener , val onAddReadyPackageToBasketListener: OnAddReadyPackageToBasketListener, val onRemoveReadyPackageListener: OnRemoveReadyPackageListener, val requestManager: RequestManager) : RecyclerView.ViewHolder(itemView)
    {
        fun bind(readyPackagesData: ReadyPackagesData)
        {

            itemView.item_ready_packages_title.setText(readyPackagesData.packageData.package_name)
            itemView.item_ready_packages_all_count_sum.setText(readyPackagesData.packageData.total_cost.toString())
            itemView.item_ready_packages_all_count.setText("${readyPackagesData.categoryData.size} ??????????????????")
            val adapter = ProfileReadyPackagesChildAdapter(requestManager)
            adapter.submitList(readyPackagesData.categoryData)
            val staggeredGridLayoutManager = StaggeredGridLayoutManager(3,LinearLayoutManager.VERTICAL)
            itemView.item_category_rv.layoutManager = staggeredGridLayoutManager
            itemView.item_category_rv.adapter = adapter

            itemView.item_ready_packages_basket_mbt.setOnClickListener {
                onAddReadyPackageToBasketListener.addReadyPackageToBasket(absoluteAdapterPosition, readyPackagesData)
            }

            itemView.item_show_product_pop_up.setOnClickListener {
                onShowReadyPackageItemListener.onShowPackageItem(absoluteAdapterPosition, readyPackagesData)
            }

            itemView.item_ready_packages_delete_mbt.setOnClickListener {
                onRemoveReadyPackageListener.onRemoveReadyPackage(absoluteAdapterPosition, readyPackagesData)
            }

        }
    }

    interface OnAddReadyPackageToBasketListener{
        fun addReadyPackageToBasket(position: Int, readyPackagesData: ReadyPackagesData)
    }

    interface OnShowReadyPackageItemListener{
        fun onShowPackageItem(position:Int, readyPackagesData: ReadyPackagesData)
    }

    interface OnRemoveReadyPackageListener{
        fun onRemoveReadyPackage(position: Int, readyPackagesData: ReadyPackagesData)
    }



}