package com.example.bozorbek_vol2.ui.main.catalog.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.example.bozorbek_vol2.R
import com.example.bozorbek_vol2.model.main.catalog.CatalogViewProduct
import com.example.bozorbek_vol2.model.main.catalog.parametrs.ParametersValue
import com.example.bozorbek_vol2.ui.OnDataStateChangeListener
import com.example.bozorbek_vol2.ui.main.catalog.state.CatalogStateEvent
import kotlinx.android.synthetic.main.fragment_view_catalog_product.*
import javax.inject.Inject


class CatalogViewProductFragment : BaseCatalogFragment() {

    private lateinit var onDataStateChangeListener: OnDataStateChangeListener


    private val args: CatalogViewProductFragmentArgs by navArgs()
    private var positionViewCatalogProduct: Int = 0
    private var productPrice: Float = 0F
    private var productCount: Float = 0F

    private lateinit var product_item_id: String
    private var price_in_pieace: Boolean = false
    private var price_in_gramme: Boolean = false
    private lateinit var unit: String

    private var count: Int = 0

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var sharedPrefEditor: SharedPreferences.Editor


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_catalog_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        spinnerOnClick()
        observeData()
    }

    private fun spinnerOnClick() {
//        view_product_sort_autocomplete.setOnItemClickListener { adapterView, view, position, l ->  }

    }


    private fun observeData() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            if (dataState != null) {
                onDataStateChangeListener.onDataStateChange(dataState)
                dataState.data?.let { data ->
                    data.data?.let { event ->
                        event.getContentIfNotHandled()?.let { catalogViewState ->
                            catalogViewState.parametersValue?.let { parametersValue ->
                                Log.d(TAG, "dataState parametersValue: ${parametersValue}")
                                viewModel.setParametersValue(parametersValue)
                            }
                        }
                    }
                }
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { catalogViewState ->
            if (catalogViewState != null) {
                catalogViewState.parametersValue?.let { parametersValue ->
                    Log.d(TAG, "viewState parametersValue: ${parametersValue}")
                    setParametersValue(parametersValue)
                }
            }
        })
    }

    private fun setParametersValue(parametersValue: ParametersValue) {


        val sortList = ArrayList<String>()
        val product_owner_list = ArrayList<String>()
        val paketList = ArrayList<String>()

        for (sort_name in parametersValue.sort) {
            sortList.add(sort_name.sort_value)
        }

        for (product_owner_name in parametersValue.productOwner) {
            product_owner_list.add(product_owner_name.product_owner_value)
        }

        for (paket in parametersValue.paket) {
            paketList.add(paket.paket_value)
        }


        val sortAdapter = ArrayAdapter(requireContext(), R.layout.item_drop_down, sortList)
        val productOwnerAdapter = ArrayAdapter(requireContext(), R.layout.item_drop_down, product_owner_list)
        val paketAdapter = ArrayAdapter(requireContext(), R.layout.item_drop_down, paketList)

        view_product_sort_autocomplete.setAdapter(sortAdapter)
        view_product_sort_autocomplete.setOnItemClickListener { adapterView, view, position, l ->
            Toast.makeText(requireContext(), "${sortList[position]}", Toast.LENGTH_LONG).show()
            viewModel.setStateEvent(
                event = CatalogStateEvent.GetCatalogViewProductBySortValue(
                    sort_value = sortList[position]
                )
            )

            for ((index, item) in parametersValue.items.withIndex())
            {
                if (sortList[position].equals(item.sort_value))
                {
                    positionViewCatalogProduct = index
                }
            }

            setCatalogViewProductDataToUI(items = parametersValue.items)
        }

        view_product_product_owner_autocomplete.setAdapter(productOwnerAdapter)
        view_product_paket_autocomplete.setAdapter(paketAdapter)



    }

    private fun setCatalogViewProductDataToUI(items: List<CatalogViewProduct>) {


        requestManager.load(items[positionViewCatalogProduct].main_image).transition(withCrossFade()).into(item_view_project_image)
        view_product_title.setText(items[positionViewCatalogProduct].name)
        price_in_pieace = items[positionViewCatalogProduct].in_piece
        price_in_gramme = items[positionViewCatalogProduct].in_gramme

        if (price_in_pieace == true && price_in_gramme == false)
        {
            view_product_price.setText("${(items[positionViewCatalogProduct].price_in_gramme * 1000).toInt()} Сум")
            view_product_price_type.setText("(${(items[positionViewCatalogProduct].price_in_gramme * 1000).toInt()} Сум - за 1 кг)")

        }
        else if (price_in_pieace == false && price_in_gramme == true)
        {
            view_product_price.setText("${(items[positionViewCatalogProduct].price_in_piece).toInt()} Сум")
            view_product_price_type.setText("(${(items[positionViewCatalogProduct].price_in_piece).toInt()} Сум - за 1 кг)")

        }

        view_product_overview.setText(items[positionViewCatalogProduct].description)

    }

    override fun onResume() {
        super.onResume()

        viewModel.setStateEvent(
            event = CatalogStateEvent.GetCatalogViewProductListOfData(
                category_slug = args.categorySlug,
                product_slug = args.productSlug
            )
        )
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