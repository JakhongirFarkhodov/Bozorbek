package com.example.bozorbek_vol2.ui.main.catalog.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.bozorbek_vol2.R
import com.example.bozorbek_vol2.model.main.catalog.CatalogProduct
import com.example.bozorbek_vol2.ui.OnDataStateChangeListener
import com.example.bozorbek_vol2.ui.main.catalog.adapter.CatalogProductAdapter
import com.example.bozorbek_vol2.ui.main.catalog.state.CatalogStateEvent
import kotlinx.android.synthetic.main.fragment_view_catalog.*


class CatalogProductFragment : BaseCatalogFragment(),
    CatalogProductAdapter.OnCatalogProductItemClickListener {

    private val args: CatalogProductFragmentArgs by navArgs()

    private lateinit var onDataStateChangeListener: OnDataStateChangeListener
    private lateinit var catalogProductAdapter: CatalogProductAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_catalog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeData()
    }

    override fun onResume() {
        super.onResume()
        viewModel.setStateEvent(event = CatalogStateEvent.GetCatalogProductListOfData(slug = args.slug))
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
                                    viewModel.setCatalogProductListOfData(list)
                                }
                            }
                        }
                    }
                }
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { cataloViewState ->
            cataloViewState.catalogProductList.let { catalogProductList ->
                catalogProductList.list?.let { list ->
                    setCatalogProductListToAdapter(list)
                }
            }
        })
    }

    private fun setCatalogProductListToAdapter(list: List<CatalogProduct>) {
        catalogProductAdapter = CatalogProductAdapter(this, requestManager)
        catalogProductAdapter.submitList(list)
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        catalog_product_recyclerView.layoutManager = staggeredGridLayoutManager
        catalog_product_recyclerView.adapter = catalogProductAdapter

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            onDataStateChangeListener = context as OnDataStateChangeListener
        } catch (e: Exception) {
            Log.d(TAG, "onAttach: ${context} must implement OnDataStateChangeListener")
        }

    }

    override fun onCatalogProductItemClick(position: Int, item: CatalogProduct) {
        val action = CatalogProductFragmentDirections.actionCatalogProductFragmentToCatalogViewProductFragment(categorySlug = args.slug, productSlug = item.slug)
        findNavController().navigate(action)
        Toast.makeText(requireContext(), "${item.name}", Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        catalog_product_recyclerView.adapter = null
    }
}