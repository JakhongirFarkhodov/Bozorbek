package com.example.bozorbek_vol2.ui.main.basket.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.bozorbek_vol2.R
import com.example.bozorbek_vol2.model.main.basket.BasketOrderProduct
import com.example.bozorbek_vol2.ui.OnDataStateChangeListener
import com.example.bozorbek_vol2.ui.main.basket.adapter.BasketAdapter
import com.example.bozorbek_vol2.util.Constants
import kotlinx.android.synthetic.main.fragment_basket_show_dilaog.*

class BasketShowDialogFragment : DialogFragment(), BasketAdapter.OnBasketItemClickListener,
    BasketAdapter.OnBasketOrderRemoveItemListener {

    val TAG = Constants.LOG
    lateinit var onDataStateChangeListener: OnDataStateChangeListener
    private lateinit var adapter: BasketAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_basket_show_dilaog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        items_save_dismiss_btn.setOnClickListener {
            dismiss()
        }

        items_save_create_btn.setOnClickListener {
            val action = BasketShowDialogFragmentDirections.actionBasketShowDialogFragmentToBasketFragment(titleOfCreatedPackage = edText_title_of_created_package.text.toString())
            findNavController().navigate(action)
            onDataStateChangeListener.setSaveButtonClick(true)
        }

        observeData()
    }

    private fun observeData() {
        val list = onDataStateChangeListener.getListOfObjects()
        Log.d(TAG, "BasketShowDialogFragment list: ${list}")
        val requestOptions = RequestOptions().placeholder(android.R.color.transparent).error(R.drawable.default_image)
        val requestManager = Glide.with(requireActivity().application).setDefaultRequestOptions(requestOptions)
        adapter = BasketAdapter(this, this, requestManager)
        adapter.submitList(list)
        item_save_basket_recyclerView.adapter = adapter
        item_save_basket_recyclerView.layoutManager = LinearLayoutManager(this.requireContext(), LinearLayoutManager.VERTICAL, false)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            onDataStateChangeListener = context as OnDataStateChangeListener
        }
        catch (e:Exception)
        {
            Log.d(TAG, "onAttach: ${context} must implement OnDataStateChangeListener")
        }
    }

    override fun onBasketItemClick(position: Int, item: BasketOrderProduct) {
        TODO("Not yet implemented")
    }

    override fun onBasketItemRemove(position: Int, item: BasketOrderProduct) {
        TODO("Not yet implemented")
    }
}