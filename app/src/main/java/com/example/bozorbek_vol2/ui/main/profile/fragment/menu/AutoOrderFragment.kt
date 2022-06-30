package com.example.bozorbek_vol2.ui.main.profile.fragment.menu

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bozorbek_vol2.R
import com.example.bozorbek_vol2.model.main.profile.ProfileAutoOrder
import com.example.bozorbek_vol2.ui.OnDataStateChangeListener
import com.example.bozorbek_vol2.ui.main.profile.fragment.BaseProfileFragment
import com.example.bozorbek_vol2.ui.main.profile.fragment.menu.ready_packages.adapters.ProfileAutoOrderParentAdapter
import com.example.bozorbek_vol2.ui.main.profile.fragment.menu.ready_packages.fragments.model.AutoOrder
import com.example.bozorbek_vol2.ui.main.profile.fragment.menu.ready_packages.fragments.model.AutoOrderData
import com.example.bozorbek_vol2.ui.main.profile.fragment.menu.ready_packages.fragments.model.CategoryAutoOrderData
import com.example.bozorbek_vol2.ui.main.profile.state.ProfileStateEvent
import kotlinx.android.synthetic.main.fragment_auto_order.*

class AutoOrderFragment : BaseProfileFragment(),
    ProfileAutoOrderParentAdapter.OnAddReadyPackageToBasketListener,
    ProfileAutoOrderParentAdapter.OnShowReadyPackageItemListener,
    ProfileAutoOrderParentAdapter.OnRemoveReadyPackageListener {

    private lateinit var onDataStateChangeListener: OnDataStateChangeListener

    private val autoOrderData = ArrayList<AutoOrder>()
    private val autoOrderCategory = ArrayList<CategoryAutoOrderData>()
    private val autoOrderDataList = ArrayList<AutoOrderData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_auto_order, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeData()
    }

    override fun onResume() {
        super.onResume()
        viewModel.setStateEvent(ProfileStateEvent.GetProfileAutoOrderList())
    }

    private fun observeData() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            dataState.data?.let { data ->
                data.data?.let { event ->
                    event.getContentIfNotHandled()?.let { profileViewState ->
                        profileViewState.profileAutoOrderList?.let { list ->
                            if (!list.isEmpty())
                            {
                                Log.d(TAG, "profileAutoOrder dataState: ${list}")
                                viewModel.setAutoOrderList(list)
                            }
                        }
                    }
                }
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState.profileAutoOrderList?.let { list ->
                if (!list.isEmpty())
                {
                    Log.d(TAG, "profileAutoOrder viewState:${list}")
                    setAllAutoOrderToUI(list)
                }
            }
        })
    }

    private fun setAllAutoOrderToUI(list: List<ProfileAutoOrder>) {
        for (package_item in list) {
            autoOrderData.add(
                AutoOrder(
                    package_id = package_item.id,
                    package_name = package_item.package_name,
                    author = package_item.author,
                    visibility = package_item.visibility,
                    total_cost = package_item.total_cost,
                    items_count = package_item.items_count
                )
            )
        }

        for ((ready_index, ready_package_data_item) in autoOrderData.distinct().toList()
            .withIndex()) {
            for ((category_index, category_item) in list.withIndex()) {
                if (ready_package_data_item.package_id == category_item.id) {
                    Log.d(
                        TAG,
                        "ready_index:${ready_index}, ready_package_data_item:${category_item}"
                    )
                    autoOrderCategory.add(
                        CategoryAutoOrderData(
                            category_id = category_item.id,
                            category_name = category_item.category_name,
                            get_absolute_url = category_item.get_absolute_url,
                            get_image = category_item.get_image,
                            slug = category_item.slug
                        )
                    )
                }
            }
            Log.d(TAG, "setListOfDataToUI: finish")
            autoOrderDataList.add(
                ready_index,
                AutoOrderData(
                    auto_order = ready_package_data_item,
                    categoryAutoOrderData = autoOrderCategory.distinct().toList()
                )
            )
            autoOrderCategory.clear()
        }

        val adapter = ProfileAutoOrderParentAdapter(requestManager = requestManager,
            this, this, this)
        adapter.submitList(autoOrderDataList.distinct().toList())
        auto_order_rv.layoutManager =
            LinearLayoutManager(this.requireContext(), LinearLayoutManager.VERTICAL, false)
        auto_order_rv.adapter = adapter
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

    override fun addReadyPackageToBasket(position: Int, readyPackagesData: AutoOrderData) {
        TODO("Not yet implemented")
    }

    override fun onShowPackageItem(position: Int, readyPackagesData: AutoOrderData) {
        TODO("Not yet implemented")
    }

    override fun onRemoveReadyPackage(position: Int, readyPackagesData: AutoOrderData) {
        TODO("Not yet implemented")
    }

}