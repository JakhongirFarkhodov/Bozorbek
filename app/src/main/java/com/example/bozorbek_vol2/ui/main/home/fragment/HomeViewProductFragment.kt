package com.example.bozorbek_vol2.ui.main.home.fragment

import android.content.Context
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
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.bozorbek_vol2.R
import com.example.bozorbek_vol2.model.main.catalog.parametrs.ParametersValue
import com.example.bozorbek_vol2.ui.OnDataStateChangeListener
import com.example.bozorbek_vol2.ui.main.home.state.HomeStateEvent
import kotlinx.android.synthetic.main.fragment_home_view_product.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class HomeViewProductFragment : BaseHomeFragment() {

    private lateinit var onDataStateChangeListener: OnDataStateChangeListener

    private val args: HomeViewProductFragmentArgs by navArgs()
    private lateinit var sort_adapter: ArrayAdapter<String>
    private lateinit var paket_adapter: ArrayAdapter<String>
    private lateinit var product_owner_adapter: ArrayAdapter<String>
    private lateinit var weight_adapter: ArrayAdapter<String>
    private lateinit var size_adapter: ArrayAdapter<String>

    private var sort_value_position:Int = 0
    private var product_owner_value_position:Int = 0
    private var paket_value_positiom:Int = 0
    private var weight_value_position:Int = 0
    private var size_value_position:Int = 0

    private var change_count_type:String = ""
    private var change_price_type:String = ""

    private var in_gramme:Boolean = false
    private var in_pieace:Boolean = false
    private var in_large_selected:Boolean = false
    private var in_middle_selected:Boolean = false
    private var in_small_selected:Boolean = false


    private var price_in_gramme:Float = 0f
    private var price_in_pieace:Float = 0f

    private var product_item_id:String = ""
    private var product_item_paket_id:String = ""
    private var quantity:Int = 0
    private var unit:String = ""
    private var size:String = ""

    private var itemCount:Int = 0

    private var filter_selected:Int = 0
    private var filer_other:Int = 0

    private var sum_of_price:String = ""

    private var catalogImageHasBeenHandeld:Boolean = true
    private var catalogProductOwnerHasBeenHandled:Boolean = true
    private var catalogPaketHasBeenHandled:Boolean = true

    private var catalogIsUnit:Boolean = true
    private var catalogIsSize:Boolean = true

    val sort_list = ArrayList<String>()
    val paket_list = ArrayList<String>()
    val product_owner_list = ArrayList<String>()
    val weight_list = ArrayList<String>()
    val size_list = ArrayList<String>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_view_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeData()
        onClickPriceChange()
    }

    private fun onClickPriceChange() {

        home_view_catalog_add.setOnClickListener {
            if (in_gramme) {
                change_count_type = String.format("%.1f",home_view_catalog_count.text.toString().toFloat() + 0.5f).replace(",",".")
                change_price_type = String.format("%,d",(price_in_gramme * change_count_type.toFloat()).toInt()).replace(",", ".")
                home_view_catalog_count.setText(change_count_type)
                home_view_product_price.setText("${change_price_type} Сум")

                quantity = (change_count_type.toFloat() * 1000).toInt()
            }
            else if (in_pieace)
            {
                change_count_type = (home_view_catalog_count.text.toString().toInt() + 1).toString()
                change_price_type = String.format("%,d", price_in_pieace.toInt() * change_count_type.toInt()).replace(",",".")
                home_view_catalog_count.setText(change_count_type)
                home_view_product_price.setText("${change_price_type} Сум")
                quantity = change_count_type.toInt()
            }
        }

        home_view_catalog_minus.setOnClickListener {
            if (in_gramme)
            {
                if (change_count_type.equals("0.0"))
                {
                    home_view_catalog_count.setText("0")
                }
                else{
                    change_count_type = String.format("%.1f",home_view_catalog_count.text.toString().toFloat() - 0.5f).replace(",",".")
                    change_price_type = String.format("%,d",(price_in_gramme * change_count_type.toFloat()).toInt()).replace(",", ".")
                    home_view_catalog_count.setText(change_count_type)
                    home_view_product_price.setText("${change_price_type} Сум")
                }
                quantity = (change_price_type.toFloat() * 1000).toInt()
            }
            else if (in_pieace)
            {
                if (change_count_type.equals("0"))
                {
                    home_view_catalog_count.setText("0")
                }
                else{
                    change_count_type = (home_view_catalog_count.text.toString().toInt() - 1).toString()
                    change_price_type = String.format("%,d",price_in_pieace.toInt() * change_count_type.toInt()).replace(",",".")
                    home_view_catalog_count.setText(change_count_type)
                    home_view_product_price.setText("${change_price_type} Сум")
                    quantity = change_count_type.toInt()
                }
            }
        }

        home_basket_button.setOnClickListener {
            val authToken = viewModel.sessionManager.cachedAuthToken.value
            if (authToken == null || authToken.access_token == null || authToken.refresh_token == null)
            {
                Toast.makeText(this.requireContext(), "Вы ещё не зарегистрированы", Toast.LENGTH_LONG).show()
            }
            else{

                Toast.makeText(this.requireContext(), "product_item_id:${product_item_id}\nquantity:${quantity}\nunit:${unit}\nsize:${size}", Toast.LENGTH_LONG).show()

                GlobalScope.launch(Dispatchers.Main) {

                    viewModel.setStateEvent(event = HomeStateEvent.AddCatalogOrderItem(
                        product_item_id = product_item_id,
                        quantity = quantity,
                        unit = unit,
                        size = size, sortValue = args.productSlug
                    ))
                    itemCount++
                    onDataStateChangeListener.getOnOrderItemCount(itemCount)

                }

            }
        }
    }

    private fun observeData() {

        viewModel.setStateEvent(
            event = HomeStateEvent.GetCatalogViewProductListOfData(
                category_slug = args.categorySlug,
                product_slug = args.productSlug
            )
        )

        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            if(dataState != null)
            {
                onDataStateChangeListener.onDataStateChange(dataState)
                dataState.data?.let { data ->
                    data.response?.let { event ->
                        event.getContentIfNotHandled()?.let { response ->
                            response.message?.let { message ->
                                if (message.equals("Продукт добавлен в корзину"))
                                {
                                    observeWeightClickListener(
                                        position = weight_value_position,
                                    )
                                }
                            }
                        }
                    }
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

        sort_list.clear()

        if (!parametersValue.sort.isEmpty()) {
            for (sort in parametersValue.sort)
            {
                sort_list.add(sort.sort_value)
            }
        }


        if (!parametersValue.items.isEmpty())
        {
            Log.d(TAG, "parametersValue.items is not empty: ")
            if (catalogImageHasBeenHandeld) {
                home_item_view_project_image.animation = AnimationUtils.loadAnimation(this.requireContext(),R.anim.fade_scale_animation)
                catalogImageHasBeenHandeld = false
            }
            if (catalogProductOwnerHasBeenHandled) {
                product_owner_list.clear()
            }
            for (items in parametersValue.items)
            {

                if (!sort_list.isEmpty() && items.sort_value.equals(sort_list[sort_value_position]))
                {
                    Log.d(TAG, "setParameterValueToSpinner: ${items.product_owner_value}")

                    if (catalogProductOwnerHasBeenHandled) {
                        product_owner_list.add(items.product_owner_value)
                    }

                    if (catalogIsUnit) {
                        if (items.in_gramme && !items.in_piece)
                        {
                            unit = "GRAMME"
                        }
                        else if (!items.in_gramme && items.in_piece)
                        {
                            unit = "PIECE"
                        }
                        else if (items.in_gramme && !items.in_piece)
                        {
                            unit = "GRAMME"
                        }
                    }

                    if (catalogIsSize) {
                        if ((items.large && items.middle && items.small) || (items.large && items.middle && !items.small) || (items.large && !items.middle && items.small))
                        {
                            size = "LARGE"
                        }
                        else if ((!items.large && items.middle && items.small) || (!items.large && items.middle && !items.small) || (items.large && items.middle && !items.small))
                        {
                            size = "MIDDLE"
                        }
                        else if ((!items.large && !items.middle && items.small) || (!items.large && items.middle && items.small) || (!items.large && items.middle && items.small))
                        {
                            size = "SMALL"
                        }
                        else{
                            size = "SMALL"
                        }
                    }


                    paket_list.add(items.paket_value)

                    requestManager.load(items.main_image).transition(DrawableTransitionOptions.withCrossFade()).into(home_item_view_project_image)
                    home_view_product_title.setText(items.sort_value)

                    if (items.in_gramme && !items.in_piece)
                    {
                        sum_of_price = String.format("%,d",items.price_in_gramme.toInt() + (items.discount_in_gramme * items.price_in_gramme.toInt()).toInt()).replace(",", ".")

                        if (in_small_selected)
                        {
                            sum_of_price = String.format("%,d",items.price_in_gramme.toInt() + (items.discount_in_gramme * items.price_in_gramme.toInt()).toInt() + (items.small_percent * items.price_in_gramme.toInt()).toInt()).replace(",", ".")
                        }
                        else if (in_middle_selected)
                        {
                            sum_of_price = String.format("%,d",items.price_in_gramme.toInt() + (items.discount_in_gramme * items.price_in_gramme.toInt()).toInt() + (items.middle_percent * items.price_in_gramme.toInt()).toInt() ).replace(",", ".")
                        }
                        else if (in_large_selected)
                        {
                            sum_of_price = String.format("%,d",items.price_in_gramme.toInt() + (items.discount_in_gramme * items.price_in_gramme.toInt()).toInt() + (items.large_percent * items.price_in_gramme.toInt()).toInt()).replace(",", ".")
                        }

                        home_view_product_price.setText("${sum_of_price} Сум")
                        home_view_product_price_type.setText("(${sum_of_price} Сум - за 1 кг)")
                        home_view_catalog_count.setText("0.5")
                        change_count_type = "0.5"

                        product_item_id = items.id.toString()

                        weight_list.add("Килограмм")

                        in_gramme = true
                        in_pieace = false
                        price_in_gramme = items.price_in_gramme
                        quantity = (change_count_type.toFloat() * 1000f).toInt()
                    }
                    else if (items.in_piece && !items.in_gramme)
                    {
                        sum_of_price = String.format("%,d", items.price_in_piece.toInt() + (items.discount_in_piece * items.price_in_piece.toInt()).toInt()).replace(",", ".")

                        if (in_small_selected)
                        {
                            sum_of_price = String.format("%,d",items.price_in_piece.toInt() + (items.discount_in_piece * items.price_in_piece.toInt()).toInt() + (items.small_percent * items.price_in_piece.toInt()).toInt()).replace(",", ".")
                        }
                        else if (in_middle_selected)
                        {
                            sum_of_price = String.format("%,d",items.price_in_piece.toInt() + (items.discount_in_piece * items.price_in_piece.toInt()).toInt() + (items.middle_percent * items.price_in_piece.toInt()).toInt() ).replace(",", ".")
                        }
                        else if (in_large_selected)
                        {
                            sum_of_price = String.format("%,d",items.price_in_piece.toInt() + (items.discount_in_piece * items.price_in_piece.toInt()).toInt() + (items.large_percent * items.price_in_piece.toInt()).toInt()).replace(",", ".")
                        }

                        home_view_product_price.setText("${sum_of_price} Cум")
                        home_view_product_price_type.setText("(${sum_of_price} Сум - за 1 шт)")
                        home_view_catalog_count.setText("1")
                        change_count_type = "1"

                        product_item_id = items.id.toString()

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

                    home_view_product_overview.setText(items.description)

                    in_small_selected = items.small
                    in_middle_selected = items.middle
                    in_large_selected = items.large


                }

            }
        }
        else{
            filter_selected = 0
        }

        if (filter_selected == 1)
        {
            filer_other = 1
            home_view_product_txIL_product_owner.visibility = View.VISIBLE
            home_view_product_txIL_paket.visibility = View.GONE
            home_view_product_txIL_weight.visibility = View.GONE
            home_view_product_txIL_size.visibility = View.GONE
            catalogProductOwnerHasBeenHandled = false
        }
        else if (filter_selected == 2)
        {
            filer_other = 2
            home_view_product_txIL_paket.visibility = View.VISIBLE
            home_view_product_txIL_weight.visibility = View.GONE
            home_view_product_txIL_size.visibility = View.GONE
        }
        else if (filter_selected == 3)
        {
            filer_other = 3
            home_view_product_txIL_weight.visibility = View.VISIBLE
            home_view_product_txIL_size.visibility = View.GONE
        }
        else if (filter_selected == 4)
        {
            filer_other = 4
            if (in_small_selected || in_middle_selected || in_large_selected) {
                home_view_product_txIL_size.visibility = View.VISIBLE
            }
        }
        else if (filter_selected == 0)
        {
            if (filer_other == 1)
            {
                home_view_product_txIL_paket.visibility = View.GONE
                home_view_product_txIL_weight.visibility = View.GONE
                home_view_product_txIL_size.visibility = View.GONE
            }
            else if (filer_other == 2)
            {
                home_view_product_txIL_weight.visibility = View.GONE
                home_view_product_txIL_size.visibility = View.GONE
            }
            else if (filer_other == 3)
            {
                home_view_product_txIL_size.visibility = View.GONE
            }
        }

        sort_adapter = ArrayAdapter(this.requireContext(), R.layout.item_drop_down, sort_list.distinct().toList())
        paket_adapter = ArrayAdapter(this.requireContext(), R.layout.item_drop_down, paket_list.distinct().toList())
        product_owner_adapter = ArrayAdapter(this.requireContext(), R.layout.item_drop_down, product_owner_list.distinct().toList())
        weight_adapter = ArrayAdapter(this.requireContext(), R.layout.item_drop_down, weight_list.distinct().toList())
        size_adapter = ArrayAdapter(this.requireContext(), R.layout.item_drop_down, size_list.distinct().toList())


        home_view_product_sort_autocomplete.apply {
            if (!sort_list.isEmpty()) {
                setText(sort_list[sort_value_position])
                setAdapter(sort_adapter)
            }
        }
        home_view_product_paket_autocomplete.apply {
            if (!paket_list.isEmpty())
            {
                setText(paket_list[paket_value_positiom])
                setAdapter(paket_adapter)
            }
        }
        home_view_product_product_owner_autocomplete.apply {
            if (!product_owner_list.isEmpty())
            {
                setText(product_owner_list[product_owner_value_position])
                setAdapter(product_owner_adapter)
            }
        }
        home_view_product_weight_autocomplete.apply {
            if (!weight_list.isEmpty())
            {
                setText(weight_list[weight_value_position])
                setAdapter(weight_adapter)
            }
        }
        home_view_product_size_autocomplete.apply {
            if (!size_list.isEmpty())
            {
                setText(size_list[size_value_position])
                setAdapter(size_adapter)
            }
        }


        home_view_product_sort_autocomplete.setOnItemClickListener { adapterView, view, position, l ->

            catalogIsUnit = true
            catalogIsSize = true
            Toast.makeText(this.requireContext(), "${sort_list[position]}", Toast.LENGTH_LONG).show()
            catalogProductOwnerHasBeenHandled = true
            viewModel.setStateEvent(event = HomeStateEvent.GetCatalogViewProductBySortValue(sort_value = sort_list[position]))
            sort_value_position = position
            filter_selected = 1

        }

        home_view_product_product_owner_autocomplete.setOnItemClickListener { adapterView, view, position, l ->
            Toast.makeText(this.requireContext(), "${product_owner_list[position]}", Toast.LENGTH_LONG).show()
            viewModel.setStateEvent(event = HomeStateEvent.GetCatalogViewProductBySortAndProductOwnerValue(sort_value = sort_list[sort_value_position], productOwner_value = product_owner_list[position]))
            product_owner_value_position = position
            filter_selected = 2
            catalogProductOwnerHasBeenHandled = false
            paket_list.clear()
        }

        home_view_product_paket_autocomplete.setOnItemClickListener { adapterView, view, position, l ->
            paket_value_positiom = position
            viewModel.setStateEvent(event = HomeStateEvent.GetCatalogViewProductBySortAndProductOwnerAndPaketValue(
                sort_value = sort_list[sort_value_position],
                productOwner_value = product_owner_list[product_owner_value_position],
                paket_value = paket_list[paket_value_positiom]
            ))
            filter_selected = 3
            size_value_position = 0
            size_list.clear()
        }

        home_view_product_weight_autocomplete.setOnItemClickListener { adapterView, view, position, l ->
            observeWeightClickListener(position)
        }


        home_view_product_size_autocomplete.setOnItemClickListener { adapterView, view, position, l ->

            catalogIsSize = false
            if (size_list[position].equals("Маленький"))
            {
                size = "SMALL"
                in_small_selected = true
                viewModel.setStateEvent(event = HomeStateEvent.GetCatalogViewProductBySizeSmall(
                    sort_value = sort_list[sort_value_position],
                    productOwner_value = product_owner_list[product_owner_value_position],
                    paket_value = paket_list[paket_value_positiom],
                    gramme = in_gramme,
                    piece = in_pieace,
                    small = in_small_selected
                ))

            }
            else if (size_list[position].equals("Средний"))
            {
                size = "MIDDLE"
                in_middle_selected = true
                viewModel.setStateEvent(event = HomeStateEvent.GetCatalogViewProductBySizeMiddle(
                    sort_value = sort_list[sort_value_position],
                    productOwner_value = product_owner_list[product_owner_value_position],
                    paket_value = paket_list[paket_value_positiom],
                    gramme = in_gramme,
                    piece = in_pieace,
                    middle = in_middle_selected
                ))
            }
            else if (size_list[position].equals("Большой"))
            {
                size = "LARGE"
                in_large_selected = true
                viewModel.setStateEvent(event = HomeStateEvent.GetCatalogViewProductBySizeLarge(
                    sort_value = sort_list[sort_value_position],
                    productOwner_value = product_owner_list[product_owner_value_position],
                    paket_value = paket_list[paket_value_positiom],
                    gramme = in_gramme,
                    piece = in_pieace,
                    large = in_large_selected
                ))
            }
            size_value_position = position

        }

    }

    private fun observeWeightClickListener(position: Int) {
        catalogIsUnit = false

        if (weight_list[position].equals("Килограмм"))
        {
            viewModel.setStateEvent(
                HomeStateEvent.GetCatalogViewProductByGramme(
                sort_value = sort_list[sort_value_position],
                productOwner_value = product_owner_list[product_owner_value_position],
                paket_value = paket_list[paket_value_positiom],
                true
            ))
            unit = "GRAMME"
            in_gramme = true
            in_pieace = false
        }
        else if (weight_list[position].equals("Штука"))
        {
            viewModel.setStateEvent(
                HomeStateEvent.GetCatalogViewProductByPiece(
                sort_value = sort_list[sort_value_position],
                productOwner_value = product_owner_list[product_owner_value_position],
                paket_value = paket_list[paket_value_positiom],
                true
            ))
            unit = "PIECE"
            in_gramme = false
            in_pieace = true

        }
        filter_selected = 4
        weight_value_position = position
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