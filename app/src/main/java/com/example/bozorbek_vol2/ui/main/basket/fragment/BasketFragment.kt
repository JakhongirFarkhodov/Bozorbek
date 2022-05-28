package com.example.bozorbek_vol2.ui.main.basket.fragment

import android.content.Context
import android.content.Intent
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
import com.example.bozorbek_vol2.model.main.basket.BasketOrderProduct
import com.example.bozorbek_vol2.ui.OnDataStateChangeListener
import com.example.bozorbek_vol2.ui.auth.AuthActivity
import com.example.bozorbek_vol2.ui.main.basket.adapter.BasketAdapter
import com.example.bozorbek_vol2.ui.main.basket.state.BasketStateEvent
import kotlinx.android.synthetic.main.fragment_basket.*


class BasketFragment : BaseBasketFragment(), BasketAdapter.OnBasketItemClickListener {

    private val args: BasketFragmentArgs by navArgs()
    private lateinit var latitude: String
    private lateinit var longtitude: String
    private lateinit var onDataStateChangeListener: OnDataStateChangeListener
    private lateinit var adapter: BasketAdapter
    private var addedMessage:String = ""
    private var orderId:Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_basket, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        basket_go_to_registration.setOnClickListener {
            Toast.makeText(requireContext(), "Click", Toast.LENGTH_LONG).show()
//            val intent = Intent(requireContext(), AuthActivity::class.java)
//            startActivity(intent)
        }

        btn_show_on_map.setOnClickListener {
            findNavController().navigate(R.id.action_basketFragment_to_basketMapFragment)
        }

        if (args.latitude != null && args.longitude != null) {
            edText_adress_basket.setText("${args.latitude.toString().substring(0,7)} : ${args.longitude.toString().substring(0,7)}")
        }

        basket_send_order_button.setOnClickListener {

            val fullAddress =
                "Имя: ${edText_name_basket.text.toString()}\nТелефон: ${edText_phone_basket.text.toString()}"
            args.latitude?.let { latitude ->
                args.longitude?.let { longitude ->
//                    Toast.makeText(requireContext(), "fullAddress:${fullAddress}\nlatitude:${latitude}\nlongtitude:${longitude}",Toast.LENGTH_LONG).show()
                    viewModel.setStateEvent(
                        event = BasketStateEvent.AddAddressProductOrder(
                            full_address = fullAddress,
                            latitude = latitude.substring(0, 7),
                            longtitude = longitude.substring(0, 7)
                        )
                    )
                }
            }
        }

        checkAuthUser()

    }

    private fun checkAuthUser() {
        val checkAuthUser = sessionManager.cachedAuthToken.value

        if (checkAuthUser == null) {
            basket_lottie_constraint.visibility = View.VISIBLE
//            basket_empty.visibility = View.GONE
            constraint_basket.visibility = View.GONE
        } else {
            basket_lottie_constraint.visibility = View.GONE
//            basket_empty.visibility = View.VISIBLE
            constraint_basket.visibility = View.VISIBLE
            observeData()
        }
    }

    private fun observeData() {
        viewModel.setStateEvent(event = BasketStateEvent.GetBasketProductOrderList())

        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            onDataStateChangeListener.onDataStateChange(dataState)
            dataState.data?.let { data ->

                data.response?.let { event ->
                    event.peekContent()?.let { response ->
                        response.message?.let { message ->
                            if (message.equals("Success"))
                            {
                                addedMessage = message
                                Log.d(TAG, "basket add address message ViewState: Данные добавлены для подтверждения.${message}")
                                viewModel.setStateEvent(event = BasketStateEvent.GetBasketAddressOrderList())
                            }
                        }
                    }
                }

                data.data?.let { event ->
                    event.getContentIfNotHandled()?.let { basketViewState ->

                        //Get list of ordered product
                        basketViewState.basketOrderProductList.list?.let { list ->
                            Log.d(TAG, "basket ordered product dataState: ${list}")
                            viewModel.setBasketProductOrderList(list)
                        }

                        if (addedMessage.equals("Success")) {
                            //Get list of ordered address
                            basketViewState.basketGetAddressOrderList.list?.let { list ->
                                Log.d(TAG, "basket ordered address dataState: ${list}")
                                viewModel.setBasketAddressOrderProductList(list)
                            }
                        }

                    }
                }
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { basketViewState ->

            //Get list of ordered product
            basketViewState.basketOrderProductList.list?.let { list ->
                Log.d(TAG, "basket ordered product viewState: ${list}")
                setListToRecyclerView(list)
            }

            if (addedMessage.equals("Success")) {
                //Get list ordered address
                basketViewState.basketGetAddressOrderList.list?.let { list ->
                    Log.d(TAG, "basket ordered address viewState: ${list}")
                    if(list.isNotEmpty())
                    {
                        viewModel.setStateEvent(event = BasketStateEvent.ApproveOrder(address_id = list[0].id))
                    }
                }
            }

        })
    }

    private fun setListToRecyclerView(list: List<BasketOrderProduct>) {
        adapter = BasketAdapter(this, requestManager)
        adapter.submitList(list)
        basket_recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        basket_recyclerView.adapter = adapter
    }


    override fun onBasketItemClick(position: Int, item: BasketOrderProduct) {
        Toast.makeText(requireContext(), "${item.name}", Toast.LENGTH_LONG).show()
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
        addedMessage = ""
    }

}