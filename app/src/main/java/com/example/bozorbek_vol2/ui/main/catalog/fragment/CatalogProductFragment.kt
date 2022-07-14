package com.example.bozorbek_vol2.ui.main.catalog.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.bozorbek_vol2.R
import com.example.bozorbek_vol2.ui.OnDataStateChangeListener
import com.example.bozorbek_vol2.ui.main.catalog.adapter.CatalogProductAdapter
import com.example.bozorbek_vol2.ui.main.catalog.adapter.CatalogProductViewPagerAdapter
import com.example.bozorbek_vol2.ui.main.catalog.fragment.model.CatalogModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_view_catalog.*


class CatalogProductFragment : BaseCatalogFragment() {

    private val args: CatalogProductFragmentArgs by navArgs()

    private lateinit var onDataStateChangeListener: OnDataStateChangeListener
    private lateinit var catalogProductAdapter: CatalogProductAdapter
    private lateinit var slug:String

    private var lastPosition: Int = 0

    private lateinit var adapter: CatalogProductViewPagerAdapter
    private var onTabIsSelected:Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_catalog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        adapter = CatalogProductViewPagerAdapter(this, onDataStateChangeListener.getCatalogListOfObject())
        catalog_view_pager2.adapter = adapter

        TabLayoutMediator(catalog_tab_layout, catalog_view_pager2) { tab, index ->

            tab.text = onDataStateChangeListener.getCatalogListOfObject()[index].name

        }.attach()

        catalog_view_pager2.setCurrentItem(onDataStateChangeListener.getCatalogProductPosition())


        onDataStateChangeListener.setCatalogProductPosition(onDataStateChangeListener.getCatalogProductPosition())
        catalog_tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let { tab ->

                    slug = onDataStateChangeListener.getCatalogListOfObject()[tab.position].slug
                    Log.d(TAG, "onTabSelected: ${slug}")
                    onDataStateChangeListener.setCatalogProductPosition(tab.position)
                    catalog_view_pager2.visibility = View.VISIBLE
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                catalog_view_pager2.visibility = View.INVISIBLE
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
//                TODO("Not yet implemented")
            }

        })


        observeData()

    }

    private fun observeData() {
       viewModel.viewState.observe(viewLifecycleOwner, Observer { catalogViewState ->
           catalogViewState.catalogModel?.let { catalogModel ->
               if (!catalogModel.category_slug.isNullOrBlank() && !catalogModel.product_slug.isNullOrBlank())
               {
                   Log.d(TAG, "observeData: ${catalogModel}")
                   val action = CatalogProductFragmentDirections.actionCatalogProductFragmentToCatalogViewProductFragment(
                       categorySlug = catalogModel.category_slug!!,
                       productSlug = catalogModel.product_slug!!
                   )
                   viewModel.setCatalogModel(catalogModel = CatalogModel(null, null))
                   findNavController().navigate(action)
               }
           }
       })
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            onDataStateChangeListener = context as OnDataStateChangeListener
        } catch (e: Exception) {
            Log.d(TAG, "onAttach: ${context} must implement OnDataStateChangeListener")
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        editor.putInt("catalogProductPosition", lastPosition).apply()
    }
}