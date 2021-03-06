package com.example.bozorbek_vol2.ui.main.catalog.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.bozorbek_vol2.R
import com.example.bozorbek_vol2.model.main.catalog.Catalog
import com.example.bozorbek_vol2.ui.OnDataStateChangeListener
import com.example.bozorbek_vol2.ui.main.catalog.adapter.CatalogAdapter
import com.example.bozorbek_vol2.ui.main.catalog.state.CatalogStateEvent
import kotlinx.android.synthetic.main.fragment_catalog.*


class CatalogFragment : BaseCatalogFragment(), CatalogAdapter.OnCatalogItemClickListener {

    private lateinit var onDataStateChangeListener: OnDataStateChangeListener
    private lateinit var catalogAdapter: CatalogAdapter
    private var countDataState = 0
    private var countViewState = 0
    private var global_list: ArrayList<Catalog> = ArrayList()
    private var isTrigger: Boolean = true
    private var lastPosition:Int = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_catalog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        observeData()
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: Поиск каталога...")
        viewModel.setStateEvent(event = CatalogStateEvent.GetCatalogListOfData())
        catalog_recyclerView.visibility = View.INVISIBLE
        editor.putInt("catalogProductPosition", 0).commit()
    }

    private fun observeData() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            onDataStateChangeListener.onDataStateChange(dataState)
            dataState.data?.let { data ->
                data.data?.let { event ->
                    event.getContentIfNotHandled()?.let { catalogViewState ->
                        catalogViewState.catalogList.let { catalogList ->
                            catalogList.list?.let { list ->
                                Log.d(TAG, "catalog dataState: ${list}")
                                if (dataState.loading.isLoading == false) {
                                    viewModel.setCatalogList(list)
                                }
                            }
                        }
                    }
                }
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { catalogViewState ->
            catalogViewState.catalogList.let { catalogList ->
                catalogList.list?.let { list ->
                    Log.d(TAG, "catalog viewState: ${list}")
                    setCatalogListToAdapter(list)
                    onDataStateChangeListener.setCatalogListOfObject(list)
                }
            }
        })
    }

    private fun setCatalogListToAdapter(list: List<Catalog>) {

        catalog_recyclerView.visibility = View.VISIBLE
        catalogAdapter = CatalogAdapter(this, requestManager)
        catalogAdapter.submitList(list)
        lastPosition = sharedPreferences.getInt("catalogPosition",0)

        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        catalog_recyclerView.layoutManager = staggeredGridLayoutManager
        catalog_recyclerView.scrollToPosition(lastPosition)
        catalog_recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val layoutManager = recyclerView.layoutManager as StaggeredGridLayoutManager
                lastPosition = layoutManager.findFirstVisibleItemPositions(null)[0]
            }
        })
        catalog_recyclerView.adapter = catalogAdapter

    }




    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            onDataStateChangeListener = context as OnDataStateChangeListener
        } catch (e: Exception) {
            Log.d(TAG, "onAttach: ${context} must implement OnDataStateChangeListener")
        }
    }

    override fun onCatalogItemClickListener(item: Catalog, position: Int) {
        val action =
            CatalogFragmentDirections.actionCatalogFragmentToCatalogProductFragment(slug = item.slug, position)
        findNavController().navigate(action)
        onDataStateChangeListener.setCatalogProductPosition(position)
        Toast.makeText(requireContext(), "${item.name}", Toast.LENGTH_LONG).show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        editor.putInt("catalogPosition",lastPosition).apply()
    }

}