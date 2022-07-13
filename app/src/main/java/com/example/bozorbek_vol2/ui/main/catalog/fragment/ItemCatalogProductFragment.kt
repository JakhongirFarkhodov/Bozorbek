package com.example.bozorbek_vol2.ui.main.catalog.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.bozorbek_vol2.R
import com.example.bozorbek_vol2.model.main.catalog.CatalogProduct
import com.example.bozorbek_vol2.ui.OnDataStateChangeListener
import com.example.bozorbek_vol2.ui.main.catalog.adapter.CatalogProductAdapter
import com.example.bozorbek_vol2.ui.main.catalog.fragment.model.CatalogModel
import com.example.bozorbek_vol2.ui.main.catalog.state.CatalogStateEvent
import kotlinx.android.synthetic.main.fragment_item_catalog_product.*


class ItemCatalogProductFragment : BaseCatalogFragment(),
    CatalogProductAdapter.OnCatalogProductItemClickListener {

    private lateinit var onDataStateChangeListener:OnDataStateChangeListener
    private lateinit var catalogProductAdapter: CatalogProductAdapter
    private var lastPosition: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_item_catalog_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeData()
    }

    override fun onResume() {
        super.onResume()
        catalog_product_recyclerView.visibility = View.INVISIBLE
        viewModel.setStateEvent(event = CatalogStateEvent.GetCatalogProductListOfData(slug = onDataStateChangeListener.getCatalogListOfObject()[onDataStateChangeListener.getCatalogProductPosition()].slug))
    }
    private fun observeData() {

        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            dataState?.let { dataState ->
                onDataStateChangeListener.onDataStateChange(dataState)

                dataState.data?.let { data ->
                    data.data?.let { event ->
                        event.getContentIfNotHandled()?.let { catalogViewState ->
                            catalogViewState.catalogProductList.let { catalogProductList ->
                                catalogProductList.list?.let { list ->
                                    Log.d(TAG, "CatalogProduct dataState: ${catalogProductList}")
                                    viewModel.setCatalogProductListOfData(list)
                                }
                            }
                        }
                    }
                }
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { catalogViewState ->
            catalogViewState.catalogProductList.let { list ->
                Log.d(TAG, "ItemCatalogProductFragment: ${list}")
                list.list?.let { list ->
                    if (!list.isEmpty())
                    {
                        setCatalogProductListToAdapter(list)
                    }
                }
            }
        })
    }

    private fun setCatalogProductListToAdapter(list: List<CatalogProduct>) {

        catalog_product_recyclerView.visibility = View.VISIBLE
        catalogProductAdapter = CatalogProductAdapter(this, requestManager)

        catalogProductAdapter.submitList(list)
        lastPosition = sharedPreferences.getInt("catalogProductPosition",0)
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        catalog_product_recyclerView.layoutManager = staggeredGridLayoutManager
        catalog_product_recyclerView.scrollToPosition(lastPosition)
        catalog_product_recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val layoutManager = recyclerView.layoutManager as StaggeredGridLayoutManager
                lastPosition = layoutManager.findFirstVisibleItemPositions(null)[0]
            }
        })
        catalog_product_recyclerView.adapter = catalogProductAdapter

    }

    override fun onCatalogProductItemClick(position: Int, item: CatalogProduct) {

        isAvailableToTrigger = true
        viewModel.setCatalogModel(catalogModel = CatalogModel(
            category_slug = onDataStateChangeListener.getCatalogListOfObject()[onDataStateChangeListener.getCatalogProductPosition()].slug,
            product_slug = item.slug
        ))

//            isAvailableToTrigger = true
//        Toast.makeText(requireContext(), "${item.name}", Toast.LENGTH_LONG).show()
    }

    companion object{
        fun newInstance() : ItemCatalogProductFragment
        {
            return ItemCatalogProductFragment()
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            onDataStateChangeListener = context as OnDataStateChangeListener
        } catch (e: Exception) {
            Log.d(TAG, "onAttach: ${context} must implement OnDataStateChangeListener")
        }

    }

}