package com.example.bozorbek_vol2.ui.main.catalog.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.bozorbek_vol2.model.main.catalog.Catalog
import com.example.bozorbek_vol2.ui.main.catalog.fragment.ItemCatalogProductFragment

class CatalogProductViewPagerAdapter(fragment:Fragment, val list: List<Catalog>) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        return ItemCatalogProductFragment.newInstance()
    }



}