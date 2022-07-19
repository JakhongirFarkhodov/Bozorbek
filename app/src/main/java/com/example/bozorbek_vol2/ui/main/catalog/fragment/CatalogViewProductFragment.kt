package com.example.bozorbek_vol2.ui.main.catalog.fragment

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
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.example.bozorbek_vol2.R
import com.example.bozorbek_vol2.model.main.catalog.CatalogViewProduct
import com.example.bozorbek_vol2.model.main.catalog.parametrs.ParametersValue
import com.example.bozorbek_vol2.model.main.catalog.parametrs.sort.Sort
import com.example.bozorbek_vol2.ui.OnDataStateChangeListener
import com.example.bozorbek_vol2.ui.main.catalog.state.CatalogStateEvent
import kotlinx.android.synthetic.main.fragment_view_catalog_product.*
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class CatalogViewProductFragment : BaseCatalogFragment() {

    private lateinit var onDataStateChangeListener: OnDataStateChangeListener

    private val args: CatalogViewProductFragmentArgs by navArgs()
    private lateinit var sort_adapter: ArrayAdapter<String>
    private lateinit var paket_adapter: ArrayAdapter<String>
    private lateinit var product_owner_adapter: ArrayAdapter<String>
    private lateinit var weight_adapter: ArrayAdapter<String>
    private lateinit var size_adapter: ArrayAdapter<String>

    private var sort_value_position: Int = 0
    private var product_owner_value_position: Int = 0
    private var paket_value_positiom: Int = 0
    private var weight_value_position: Int = 0
    private var size_value_position: Int = 0

    private var image_url:String = ""
    private var price:Float = 0f
    private var begin_price:Float = 0f
    private var changed_price:Float = 0f

    private var discount_in_piece:Float = 0f
    private var discount_in_gramme:Float = 0f
    private var discount_size:Float = 0f
    private var discount_large_size:Float = 0f
    private var discount_middle_size:Float = 0f
    private var discount_small_size:Float = 0f
    private var id_count:Float = 0f

    private var inGramme:Boolean = false
    private var inPiece:Boolean = false


    private var product_item_id: String = ""
    private var quantity: Int = 0
    private var unit: String = ""
    private var size: String = ""

    private var itemCount: Int = 0


    private var catalogHasBeenHandled:Boolean = false
    private var animationHasBeenHandled:Boolean = true

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
        return inflater.inflate(R.layout.fragment_view_catalog_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        catalogHasBeenHandled = false

        id_count = view_catalog_count.text.toString().toFloat()

        observeData()
        setChosenProductToBasket()
        increasePrice()
        decreasePrice()
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

    private fun observeData() {

        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            if (dataState != null) {
                onDataStateChangeListener.onDataStateChange(dataState)
                dataState.data?.let { data ->
                    data.data?.let { event ->
                        event.getContentIfNotHandled()?.let { catalogViewState ->
                            catalogViewState.parametersValue?.let { parametersValue ->
                                Log.d(TAG, "dataState: ${parametersValue}")
                                if (catalogHasBeenHandled) {
                                    viewModel.setParametersValue(parametersValue)
                                }
                                catalogHasBeenHandled = true
                                closeDropDown()
                            }
                        }
                    }
                }
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { catalogViewState ->
            catalogViewState.parametersValue?.let { parametersValue ->
                Log.d(TAG, "viewState: ${parametersValue}")
                if (catalogHasBeenHandled) {
                    setParameterValueToSpinner(parametersValue)
                }

            }
        })
    }

    private fun closeDropDown() {
        view_product_sort_autocomplete.dismissDropDown()
        view_product_product_owner_autocomplete.dismissDropDown()
        view_product_paket_autocomplete.dismissDropDown()
        view_product_weight_autocomplete.dismissDropDown()
        view_product_size_autocomplete.dismissDropDown()
    }

    private fun setParameterValueToSpinner(parametersValue: ParametersValue) {
        setSortItemsToSpinner(parametersValue.sort)
        setOtherItemsToSpinner(parametersValue.items)
    }

    private fun setSortItemsToSpinner(sort: List<Sort>) {
        if (sort.isNotEmpty())
        {
            sort_list.clear()

            for (item in sort)
            {
                sort_list.add(item.sort_value)
            }
            sort_adapter = ArrayAdapter(requireContext(), R.layout.item_drop_down, sort_list.distinct().toList())
            view_product_sort_autocomplete.apply {
                setText(sort_list[sort_value_position])
                setAdapter(sort_adapter)
                setOnItemClickListener { adapterView, view, position, l ->
                    sort_value_position = position

                    product_owner_list.clear()
                    paket_list.clear()
                    weight_list.clear()
                    size_list.clear()

                    product_owner_value_position = 0
                    paket_value_positiom = 0
                    weight_value_position = 0
                    size_value_position = 0

                    animationHasBeenHandled = true

                    viewModel.setStateEvent(CatalogStateEvent.GetCatalogViewProductBySortValue(sort_list[sort_value_position]))

                }
            }
        }
    }

    private fun setOtherItemsToSpinner(items: List<CatalogViewProduct>) {
        if (items.isNotEmpty())
        {
            setProductOwnerItemsToSpinner(items)
            setPaketItemsToSpinner(items)
            setWeightItemsToSpinner(items)
            setSizeItemsToSpinner(items)
            changeProductItem(items)
        }
    }

    private fun changeProductItem(items: List<CatalogViewProduct>) {
        for (product in items)
        {
            if (product.sort_value.equals(sort_list[sort_value_position]) && product.paket_value.equals(paket_list[paket_value_positiom]))
            {
                image_url = product.main_image
                if (inGramme)
                {
                    price = product.price_in_gramme
                    begin_price = product.price_in_gramme
                    discount_in_gramme = product.discount_in_gramme
                    product_item_id = product.id.toString()
                }
                if (inPiece)
                {
                    price = product.price_in_piece
                    begin_price = product.price_in_piece
                    discount_in_piece = product.discount_in_piece
                    product_item_id = product.id.toString()
                }
            }
        }

        item_view_project_image.visibility = View.VISIBLE
        view_product_title.visibility = View.VISIBLE

        if (animationHasBeenHandled)
        {
            view_product_title.animation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_down)
            item_view_project_image.animation = AnimationUtils.loadAnimation(requireContext(),R.anim.fade_scale_animation)
            animationHasBeenHandled = false
        }

        viewModel.requestManager.load(image_url).transition(withCrossFade()).into(item_view_project_image)
        view_product_title.setText(sort_list[sort_value_position])

        if (inGramme) {
            changed_price = (price + (price * discount_in_gramme)) * id_count
            changed_price = changed_price + (changed_price * discount_size)
            quantity = (id_count * 1000).toInt()
            view_product_price.setText("${changed_price} Сум")
            view_product_price_type.setText("(${begin_price} Сум - за 1 кг)")
            view_catalog_count.setText("${id_count}")
        }

        if (inPiece) {
            changed_price = price + (price * discount_in_piece)
            changed_price = changed_price + (changed_price * discount_size)
            quantity = id_count.toInt()
            view_product_price.setText("${changed_price} Сум")
            view_product_price_type.setText("(${begin_price} Сум - за 1 шт)")
            view_catalog_count.setText("${id_count.toInt()}")
        }
    }


    private fun setProductOwnerItemsToSpinner(items: List<CatalogViewProduct>) {

        for (product_owner in items)
        {
            if (product_owner.sort_value.equals(sort_list[sort_value_position]))
            {
                product_owner_list.add(product_owner.product_owner_value)
            }
        }

        product_owner_adapter = ArrayAdapter(requireContext(), R.layout.item_drop_down, product_owner_list.distinct().toList())
        view_product_product_owner_autocomplete.apply {
            setText(product_owner_list[product_owner_value_position])
            setAdapter(product_owner_adapter)
            setOnItemClickListener { adapterView, view, position, l ->
                product_owner_value_position = position
                paket_list.clear()

                paket_value_positiom = 0
                weight_value_position = 0
                size_value_position = 0

                viewModel.setStateEvent(CatalogStateEvent.GetCatalogViewProductBySortAndProductOwnerValue(
                    sort_value = sort_list[sort_value_position],
                    productOwner_value = product_owner_list[product_owner_value_position]
                ))
            }
        }
    }

    private fun setPaketItemsToSpinner(items: List<CatalogViewProduct>) {
        for (paket in items) {
            if (paket.sort_value.equals(sort_list[sort_value_position]) && paket.product_owner_value.equals(
                    product_owner_list[product_owner_value_position]
                )
            ) {
                paket_list.add(paket.paket_value)
            }
        }
            paket_adapter = ArrayAdapter(requireContext(), R.layout.item_drop_down, paket_list.distinct().toList())
            view_product_paket_autocomplete.apply {
                if (paket_list.isNotEmpty()) {
                    setText(paket_list[paket_value_positiom])
                }
                setAdapter(paket_adapter)
                setOnItemClickListener { adapterView, view, position, l ->
                    paket_value_positiom = position
                    weight_list.clear()
                    size_list.clear()

                    weight_value_position = 0
                    size_value_position = 0

                    viewModel.setStateEvent(CatalogStateEvent.GetCatalogViewProductBySortAndProductOwnerAndPaketValue(
                        sort_value = sort_list[sort_value_position],
                        productOwner_value = product_owner_list[product_owner_value_position],
                        paket_value = paket_list[paket_value_positiom]
                    ))
                }
            }

    }

    private fun setWeightItemsToSpinner(items: List<CatalogViewProduct>) {
        for (weight in items)
        {
            if (weight.sort_value.equals(sort_list[sort_value_position]) &&
                weight.product_owner_value.equals(product_owner_list[product_owner_value_position]) &&
                weight.paket_value.equals(paket_list[paket_value_positiom]) &&
                weight.in_gramme && !weight.in_piece)
            {
                weight_list.add("Килограмм")
            }
            else if (weight.sort_value.equals(sort_list[sort_value_position]) &&
                weight.product_owner_value.equals(product_owner_list[product_owner_value_position]) &&
                weight.paket_value.equals(paket_list[paket_value_positiom]) &&
                !weight.in_gramme && weight.in_piece)
            {
                weight_list.add("Штука")
            }
            else if (weight.sort_value.equals(sort_list[sort_value_position]) &&
                weight.product_owner_value.equals(product_owner_list[product_owner_value_position]) &&
                weight.paket_value.equals(paket_list[paket_value_positiom]) &&
                weight.in_gramme && weight.in_piece)
            {
                weight_list.add("Килограмм")
                weight_list.add("Штука")
            }


        }
        weight_adapter = ArrayAdapter(requireContext(), R.layout.item_drop_down, weight_list.distinct().toList())
        view_product_weight_autocomplete.apply {
            if (weight_list.isNotEmpty())
            {
                setText(weight_list[weight_value_position])
            }
            chooseUnitOfProduct()
            setAdapter(weight_adapter)
            setOnItemClickListener { adapterView, view, position, l ->
                weight_value_position = position
                size_value_position = 0
                chooseUnitOfProduct()
                if (inGramme)
                {
                    viewModel.setStateEvent(event = CatalogStateEvent.GetCatalogViewProductByGramme(
                        sort_value = sort_list[sort_value_position],
                        productOwner_value = product_owner_list[product_owner_value_position],
                        paket_value = paket_list[paket_value_positiom],
                        gramme = true
                    ))
                }
                if (inPiece)
                {
                    viewModel.setStateEvent(event = CatalogStateEvent.GetCatalogViewProductByPiece(
                        sort_value = sort_list[sort_value_position],
                        productOwner_value = product_owner_list[product_owner_value_position],
                        paket_value = paket_list[paket_value_positiom],
                        piece = true
                    ))
                }
            }
        }
    }



    private fun setSizeItemsToSpinner(items: List<CatalogViewProduct>) {
        for (size in items)
        {
            if (size.sort_value.equals(sort_list[sort_value_position]) &&
                size.product_owner_value.equals(product_owner_list[product_owner_value_position]) &&
                size.paket_value.equals(paket_list[paket_value_positiom]) &&
                size.in_gramme && !size.in_piece)
            {
                if (size.large)
                {
                    size_list.add("Большой")
                    discount_large_size = size.large_percent
                }
                if (size.middle)
                {
                    size_list.add("Средний")
                    discount_middle_size = size.middle_percent
                }
                if (size.small)
                {
                    size_list.add("Маленький")
                    discount_small_size = size.small_percent
                }
            }

            else if (size.sort_value.equals(sort_list[sort_value_position]) &&
                size.product_owner_value.equals(product_owner_list[product_owner_value_position]) &&
                size.paket_value.equals(paket_list[paket_value_positiom]) &&
                !size.in_gramme && size.in_piece)
            {
                if (size.large)
                {
                    size_list.add("Большой")
                    discount_large_size = size.large_percent
                }
                if (size.middle)
                {
                    size_list.add("Средний")
                    discount_middle_size = size.middle_percent
                }
                if (size.small)
                {
                    size_list.add("Маленький")
                    discount_small_size = size.small_percent
                }
            }
        }

        size_adapter = ArrayAdapter(requireContext(), R.layout.item_drop_down, size_list.distinct().toList())
        view_product_size_autocomplete.apply {
            if (size_list.isNotEmpty())
            {
                setText(size_list[size_value_position])
                if (size_list[size_value_position].equals("Большой"))
                {
                    discount_size = discount_large_size
                }
                else if (size_list[size_value_position].equals("Средний"))
                {
                    discount_size = discount_middle_size
                }
                else if (size_list[size_value_position].equals("Маленький"))
                {
                    discount_size = discount_small_size
                }
            }

            chooseSizeOfProduct()
            setAdapter(size_adapter)
            setOnItemClickListener { adapterView, view, position, l ->
                size_value_position = position
                chooseSizeOfProduct()
                if (inGramme)
                {
                    if (size_list[position].equals("Большой"))
                    {
                        viewModel.setStateEvent(event = CatalogStateEvent.GetCatalogViewProductBySizeLarge(
                            sort_value = sort_list[sort_value_position],
                            productOwner_value = product_owner_list[product_owner_value_position],
                            paket_value = paket_list[paket_value_positiom],
                            gramme = true,
                            piece = false,
                            large = true
                        ))
                    }
                    else if (size_list[position].equals("Средний"))
                    {
                        viewModel.setStateEvent(event = CatalogStateEvent.GetCatalogViewProductBySizeMiddle(
                            sort_value = sort_list[sort_value_position],
                            productOwner_value = product_owner_list[product_owner_value_position],
                            paket_value = paket_list[paket_value_positiom],
                            gramme = true,
                            piece = false,
                            middle = true
                        ))
                    }
                    else if (size_list[position].equals("Маленький"))
                    {
                        viewModel.setStateEvent(event = CatalogStateEvent.GetCatalogViewProductBySizeSmall(
                            sort_value = sort_list[sort_value_position],
                            productOwner_value = product_owner_list[product_owner_value_position],
                            paket_value = paket_list[paket_value_positiom],
                            gramme = true,
                            piece = false,
                            small = true
                        ))
                    }
                }
                else if (inPiece)
                {
                    if (size_list[position].equals("Большой"))
                    {
                        viewModel.setStateEvent(event = CatalogStateEvent.GetCatalogViewProductBySizeLarge(
                            sort_value = sort_list[sort_value_position],
                            productOwner_value = product_owner_list[product_owner_value_position],
                            paket_value = paket_list[paket_value_positiom],
                            gramme = false,
                            piece = true,
                            large = true
                        ))
                    }
                    else if (size_list[position].equals("Средний"))
                    {
                        viewModel.setStateEvent(event = CatalogStateEvent.GetCatalogViewProductBySizeMiddle(
                            sort_value = sort_list[sort_value_position],
                            productOwner_value = product_owner_list[product_owner_value_position],
                            paket_value = paket_list[paket_value_positiom],
                            gramme = false,
                            piece = true,
                            middle = true
                        ))
                    }
                    else if (size_list[position].equals("Маленький"))
                    {
                        viewModel.setStateEvent(event = CatalogStateEvent.GetCatalogViewProductBySizeSmall(
                            sort_value = sort_list[sort_value_position],
                            productOwner_value = product_owner_list[product_owner_value_position],
                            paket_value = paket_list[paket_value_positiom],
                            gramme = false,
                            piece = true,
                            small = true
                        ))
                    }
                }
            }
        }
    }

    private fun chooseUnitOfProduct() {
        if (weight_list[weight_value_position].equals("Килограмм"))
        {
            inGramme = true
            inPiece = false
            id_count = 0.5f
            unit = "GRAMME"
        }
        else if (weight_list[weight_value_position].equals("Штука")){
            inGramme = false
            inPiece = true
            id_count = 1f
            unit = "PIECE"
        }
    }

    private fun chooseSizeOfProduct() {
        if (size_list[size_value_position].equals("Большой"))
        {
            size = "LARGE"
        }
        else if (size_list[size_value_position].equals("Средний"))
        {
            size = "MIDDLE"
        }
        else if (size_list[size_value_position].equals("Маленький"))
        {
            size = "SMALL"
        }


    }

    private fun setChosenProductToBasket() {
        basket_button.setOnClickListener {
            val authToken = viewModel.sessionManager.cachedAuthToken.value
            if (authToken == null || authToken.access_token == null || authToken.refresh_token == null) {
                Toast.makeText(this.requireContext(),"Вы ещё не зарегистрированы", Toast.LENGTH_LONG).show()
            } else {

                Toast.makeText(this.requireContext(),"product_item_id:${product_item_id}\nquantity:${quantity}\nunit:${unit}\nsize:${size}", Toast.LENGTH_LONG).show()

                GlobalScope.launch(Main) {

                    viewModel.setStateEvent(
                        event = CatalogStateEvent.AddCatalogOrderItem(
                            product_item_id = product_item_id,
                            quantity = quantity,
                            unit = unit,
                            size = size, sortValue = args.productSlug
                        )
                    )
                    itemCount++
                    onDataStateChangeListener.getOnOrderItemCount(itemCount)

                }

            }

        }
    }

    private fun increasePrice() {
        view_catalog_add.setOnClickListener {

            if (inGramme)
            {
                id_count = id_count + 0.5f
                view_catalog_count.setText("${id_count}")
                changed_price = (price + (price * discount_in_gramme)) * id_count
                changed_price = changed_price + (changed_price * discount_size)
                quantity = (id_count * 1000).toInt()
                view_catalog_count.setText("${id_count}")
                view_product_price.setText("${changed_price} Сум")
            }
            if (inPiece){
                id_count = id_count + 1
                changed_price = (price + (price * discount_in_gramme)) * id_count
                changed_price = changed_price + (changed_price * discount_size)
                quantity = id_count.toInt()
                view_catalog_count.setText("${id_count}")
                view_product_price.setText("${changed_price} Сум")
            }
        }
    }

    private fun decreasePrice() {
        view_catalog_minus.setOnClickListener {
            if (inGramme)
            {
                id_count = id_count - 0.5f
                changed_price = (price + (price * discount_in_gramme)) * id_count
                changed_price = changed_price + (changed_price * discount_size)
                quantity = (id_count * 1000).toInt()
                view_catalog_count.setText("${id_count}")
                view_product_price.setText("${changed_price} Сум")
            }
            if (inPiece){
                id_count = id_count - 1
                changed_price = (price + (price * discount_in_gramme)) * id_count
                changed_price = changed_price + (changed_price * discount_size)
                quantity = id_count.toInt()
                view_catalog_count.setText("${id_count}")
                view_product_price.setText("${changed_price} Сум")
            }
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