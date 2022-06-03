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
import com.example.bozorbek_vol2.model.main.catalog.parametrs.sort.Sort
import com.example.bozorbek_vol2.ui.OnDataStateChangeListener
import com.example.bozorbek_vol2.ui.main.catalog.state.CatalogStateEvent
import kotlinx.android.synthetic.main.fragment_view_catalog_product.*
import javax.inject.Inject


class CatalogViewProductFragment : BaseCatalogFragment() {

    private lateinit var onDataStateChangeListener: OnDataStateChangeListener

    private val args: CatalogViewProductFragmentArgs by navArgs()
    private lateinit var sort_adapter:ArrayAdapter<String>
    private lateinit var paket_adapter:ArrayAdapter<String>
    private lateinit var product_owner_adapter:ArrayAdapter<String>
    private lateinit var weight_adapter:ArrayAdapter<String>
    private lateinit var size_adapter:ArrayAdapter<String>

    private var sort_value_position:Int = 0
    private var change_count_type:String = ""
    private var change_price_type:String = ""

    private var in_gramme:Boolean = false
    private var in_pieace:Boolean = false
    private var price_in_gramme:Float = 0f
    private var price_in_pieace:Float = 0f

    private var product_item_id:String = ""
    private var quantity:Int = 0
    private var unit:String = ""
    private var size:String = ""

    private var itemCount:Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_catalog_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeData()
        onClickPriceChange()
    }

    private fun onClickPriceChange() {

        view_catalog_add.setOnClickListener {
            if (in_gramme) {
                change_count_type = String.format("%.1f",view_catalog_count.text.toString().toFloat() + 0.1f).replace(",",".")
                change_price_type = (price_in_gramme * change_count_type.toFloat()).toInt().toString()
                view_catalog_count.setText(change_count_type)
                view_product_price.setText("${change_price_type} Сум")

                quantity = (change_count_type.toFloat() * 1000).toInt()
            }
            else if (in_pieace)
            {
                change_count_type = (view_catalog_count.text.toString().toInt() + 1).toString()
                change_price_type = (price_in_pieace * change_count_type.toInt()).toString()
                view_catalog_count.setText(change_count_type)
                view_product_price.setText("${change_price_type} Сум")
                quantity = change_count_type.toInt()
            }
        }

        view_catalog_minus.setOnClickListener {
            if (in_gramme)
            {
                if (change_count_type.equals("0.0"))
                {
                    view_catalog_count.setText("0")
                }
                else{
                    change_count_type = String.format("%.1f",view_catalog_count.text.toString().toFloat() - 0.1f).replace(",",".")
                    change_price_type = (price_in_gramme * change_count_type.toFloat()).toInt().toString()
                    view_catalog_count.setText(change_count_type)
                    view_product_price.setText("${change_price_type} Сум")
                }
               quantity = (change_price_type.toFloat() * 1000).toInt()
            }
            else if (in_pieace)
            {
                if (change_count_type.equals("0"))
                {
                    view_catalog_count.setText("0")
                }
                else{
                    change_count_type = (view_catalog_count.text.toString().toInt() - 1).toString()
                    change_price_type = (price_in_pieace * change_count_type.toInt()).toString()
                    view_catalog_count.setText(change_count_type)
                    view_product_price.setText("${change_price_type} Сум")
                    quantity = change_count_type.toInt()
                }
            }
        }

        basket_button.setOnClickListener {
            val authToken = viewModel.sessionManager.cachedAuthToken.value
            if (authToken == null || authToken.access_token == null || authToken.refresh_token == null)
            {
                Toast.makeText(this.requireContext(), "Вы ещё не зарегистрированы", Toast.LENGTH_LONG).show()
            }
            else{

                Toast.makeText(this.requireContext(), "product_item_id:${product_item_id}\nquantity:${quantity}\nunit:${unit}\nsize:${size}", Toast.LENGTH_LONG).show()


                viewModel.setStateEvent(event = CatalogStateEvent.AddCatalogOrderItem(
                    product_item_id = product_item_id,
                    quantity = quantity,
                    unit = unit,
                    size = size
                ))
                itemCount++
                onDataStateChangeListener.getOnOrderItemCount(itemCount)
            }
        }
    }

    private fun observeData() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            if(dataState != null)
            {
                onDataStateChangeListener.onDataStateChange(dataState)
                dataState.data?.let { data ->
                    data.data?.let { event ->
                        event.getContentIfNotHandled()?.let { catalogViewState ->
                            catalogViewState.parametersValue?.let { parametersValue ->
                                Log.d(TAG, "dataState: ${parametersValue}")
                                viewModel.setParametersValue(parametersValue)
                            }
                        }
                    }
                }
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { catalogViewState ->
            catalogViewState.parametersValue?.let { parametersValue ->
                Log.d(TAG, "viewState: ${parametersValue}")
                setParameterValueToSpinner(parametersValue)
            }
        })
    }

    private fun setParameterValueToSpinner(parametersValue: ParametersValue) {

        val sort_list = ArrayList<String>()
        val paket_list = ArrayList<String>()
        val product_owner_list = ArrayList<String>()
        val weight_list = ArrayList<String>()
        val size_list = ArrayList<String>()

        for (sort in parametersValue.sort)
        {
            sort_list.add(sort.sort_value)
        }
        for (paket in parametersValue.paket)
        {
            paket_list.add(paket.paket_value)
        }
        for (product_owner in parametersValue.productOwner)
        {
            product_owner_list.add(product_owner.product_owner_value)
        }

        if (!parametersValue.items.isEmpty())
        {
            item_view_project_image.animation = AnimationUtils.loadAnimation(this.requireContext(),R.anim.fade_scale_animation)
            product_item_id = parametersValue.items[0].id.toString()
            for (items in parametersValue.items)
            {
                if (items.sort_value.equals(sort_list[sort_value_position]))
                {
                    requestManager.load(items.main_image).transition(withCrossFade()).into(item_view_project_image)
                    view_product_title.setText(items.sort_value)
                    if (items.in_gramme && !items.in_piece)
                    {
                        view_product_price.setText("${items.price_in_gramme.toInt()} Сум")
                        view_product_price_type.setText("(${items.price_in_gramme.toInt()} Сум - за 1 кг)")
                        view_catalog_count.setText("0.5")
                        change_count_type = "0.5"

                        weight_list.add("Килограмм")

                        in_gramme = true
                        in_pieace = false
                        price_in_gramme = items.price_in_gramme
                        quantity = (change_count_type.toFloat() * 1000).toInt()
                    }
                    else if (items.in_piece && !items.in_gramme)
                    {
                        view_product_price.setText("${items.price_in_piece.toInt()} Cум")
                        view_product_price_type.setText("(${items.price_in_piece.toInt()} Сум - за 1 шт)")
                        view_catalog_count.setText("1")
                        change_count_type = "1"

                        weight_list.add("Штука")

                        in_gramme = false
                        in_pieace = true
                        price_in_pieace = items.price_in_piece
                        quantity = (change_count_type.toFloat()).toInt()
                    }
                    else if (items.in_piece && items.in_gramme)
                    {
                        weight_list.add("Килограмм")
                        weight_list.add("Штука")
                    }

                    if (items.large)
                    {
                        size_list.add("Большой")
                    }
                    if (items.middle)
                    {
                        size_list.add("Средний")
                    }
                    if (items.small)
                    {
                        size_list.add("Маленький")
                    }

                    view_product_overview.setText(items.description)

                }
            }

        }

        sort_adapter = ArrayAdapter(this.requireContext(), R.layout.item_drop_down, sort_list)
        paket_adapter = ArrayAdapter(this.requireContext(), R.layout.item_drop_down, paket_list.distinct().toList())
        product_owner_adapter = ArrayAdapter(this.requireContext(), R.layout.item_drop_down, product_owner_list.distinct().toList())
        weight_adapter = ArrayAdapter(this.requireContext(), R.layout.item_drop_down, weight_list.distinct().toList())
        size_adapter = ArrayAdapter(this.requireContext(), R.layout.item_drop_down, size_list.distinct().toList())


        view_product_sort_autocomplete.apply {
            if (!sort_list.isEmpty()) {
                setText(sort_list[sort_value_position])
                setAdapter(sort_adapter)
            }
        }
        view_product_paket_autocomplete.apply {
            if (!paket_list.isEmpty())
            {
                setText(paket_list[0])
                setAdapter(paket_adapter)
            }
        }
        view_product_product_owner_autocomplete.apply {
            if (!product_owner_list.isEmpty())
            {
                setText(product_owner_list[0])
                setAdapter(product_owner_adapter)
            }
        }
        view_product_weight_autocomplete.apply {
            if (!weight_list.isEmpty())
            {
                setText(weight_list[0])
                setAdapter(weight_adapter)
            }
        }
        view_product_size_autocomplete.apply {
            if (!size_list.isEmpty())
            {
                setText(size_list[0])
                setAdapter(size_adapter)
            }
        }

        view_product_sort_autocomplete.setOnItemClickListener { adapterView, view, position, l ->
            Toast.makeText(this.requireContext(), "${sort_list[position]}", Toast.LENGTH_LONG).show()
            viewModel.setStateEvent(event = CatalogStateEvent.GetCatalogViewProductBySortValue(sort_value = sort_list[position]))
            sort_value_position = position
        }

        view_product_weight_autocomplete.setOnItemClickListener { adapterView, view, position, l ->
           if (weight_list[position].equals("Килограмм"))
           {
                unit = "GRAMME"
           }
            else if (weight_list[position].equals("Штука"))
           {
                unit = "PIECE"
           }
        }

        view_product_size_autocomplete.setOnItemClickListener { adapterView, view, position, l ->
            if (size_list[position].equals("Маленький"))
            {
                size = "SMALL"
            }
            else if (size_list[position].equals("Средний"))
            {
                size = "MIDDLE"
            }
            else if (size_list[position].equals("Большой"))
            {
                size = "LARGE"
            }
        }
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