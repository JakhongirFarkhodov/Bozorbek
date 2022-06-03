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
import com.example.bozorbek_vol2.model.main.profile.Profile
import com.example.bozorbek_vol2.ui.OnDataStateChangeListener
import com.example.bozorbek_vol2.ui.auth.AuthActivity
import com.example.bozorbek_vol2.ui.main.basket.adapter.BasketAdapter
import com.example.bozorbek_vol2.ui.main.basket.state.BasketStateEvent
import kotlinx.android.synthetic.main.fragment_basket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main


class BasketFragment : BaseBasketFragment(), BasketAdapter.OnBasketItemClickListener {

    private val args: BasketFragmentArgs by navArgs()
    private lateinit var latitude: String
    private lateinit var longtitude: String
    private lateinit var onDataStateChangeListener: OnDataStateChangeListener
    private lateinit var adapter: BasketAdapter
    private var addedMessage: String = ""
    private var orderId: Int = 0
    private var order_sum_price: Int = 0
    private lateinit var coroutineScope: CoroutineScope

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_basket, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onDataStateChangeListener.getOnOrderItemCount(0)

        mb_basket_go_to_registration.setOnClickListener {
            Toast.makeText(requireContext(), "Click", Toast.LENGTH_LONG).show()
            val intent = Intent(requireContext(), AuthActivity::class.java)
            startActivity(intent)
        }

        btn_show_on_map.setOnClickListener {
            findNavController().navigate(R.id.action_basketFragment_to_basketMapFragment)
        }

        if (args.latitude != null && args.longitude != null) {
            edText_adress_basket.setText("${args.latitude.toString().substring(0, 7)} : ${args.longitude.toString().substring(0, 7)}"
            )
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
            lottie_constraint_basket.visibility = View.VISIBLE
            constraint_basket.visibility = View.GONE
        } else {
            lottie_constraint_basket.visibility = View.GONE
            constraint_basket.visibility = View.VISIBLE
            observeData()
        }
    }

    private fun observeData() {

        GlobalScope.launch(Main) {
            launch {
                viewModel.setStateEvent(event = BasketStateEvent.GetBasketProductOrderList())
            }.join()
            launch {
                delay(500)
                viewModel.setStateEvent(event = BasketStateEvent.GetBasketProfileInfo())
            }.join()
        }

        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            if (dataState != null) {
                onDataStateChangeListener.onDataStateChange(dataState)
                dataState.data?.let { data ->
                    data.data?.let { event ->
                        event.getContentIfNotHandled()?.let { basketViewState ->
                            basketViewState.profile?.let { profile ->
                                viewModel.setProfileInfo(profile)
                            }

                            basketViewState.basketOrderProductList?.let { basketOrderProductList ->
                                basketOrderProductList.list?.let { list ->
                                    Log.d(TAG, "basketOrderProductList dataState: ${list}")
                                    viewModel.setBasketProductOrderList(list)
                                }
                            }
                        }
                    }
                }
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { basketViewState ->
            basketViewState.profile?.let { profile ->
                setProfileDataToView(profile)
            }
            basketViewState.basketOrderProductList?.let { basketOrderProductList ->
                basketOrderProductList.list?.let { list ->
                    Log.d(TAG, "basketOrderProductList viewState: ${list}")
                    setBasketOrderListToView(list)
                }
            }
        })

    }

    private fun setBasketOrderListToView(list: List<BasketOrderProduct>) {
        if (!list.isEmpty())
        {

        }
        adapter = BasketAdapter(this, requestManager)
        adapter.submitList(list)
        basket_recyclerView.adapter = adapter
        basket_recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun setProfileDataToView(profile: Profile) {
        edText_name_basket.setText(profile.first_name)
        edText_phone_basket.setText(profile.username)
    }


    override fun onBasketItemClick(position: Int, item: BasketOrderProduct) {
        TODO("Not yet implemented")
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