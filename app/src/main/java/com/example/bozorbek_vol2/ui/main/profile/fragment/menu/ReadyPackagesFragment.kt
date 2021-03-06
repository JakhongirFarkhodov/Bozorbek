package com.example.bozorbek_vol2.ui.main.profile.fragment.menu

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bozorbek_vol2.R
import com.example.bozorbek_vol2.model.main.profile.ProfileReadyPackageId
import com.example.bozorbek_vol2.model.main.profile.ProfileReadyPackages
import com.example.bozorbek_vol2.ui.OnDataStateChangeListener
import com.example.bozorbek_vol2.ui.main.profile.fragment.BaseProfileFragment
import com.example.bozorbek_vol2.ui.main.profile.fragment.menu.ready_packages.adapters.ProfileReadyPackagesParentAdapter
import com.example.bozorbek_vol2.ui.main.profile.fragment.menu.ready_packages.fragments.PopUpListOfReadyPackagesProductsFragment
import com.example.bozorbek_vol2.ui.main.profile.fragment.menu.ready_packages.fragments.model.CategoryData
import com.example.bozorbek_vol2.ui.main.profile.fragment.menu.ready_packages.fragments.model.PackagesData
import com.example.bozorbek_vol2.ui.main.profile.fragment.menu.ready_packages.fragments.model.ReadyPackagesData
import com.example.bozorbek_vol2.ui.main.profile.state.ProfileStateEvent
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_ready_packages.*


class ReadyPackagesFragment : BaseProfileFragment(),
    ProfileReadyPackagesParentAdapter.OnAddReadyPackageToBasketListener,
    ProfileReadyPackagesParentAdapter.OnShowReadyPackageItemListener,
    ProfileReadyPackagesParentAdapter.OnRemoveReadyPackageListener{


    lateinit var onDataStateChangeListener: OnDataStateChangeListener

    private val readyPackagesData = ArrayList<PackagesData>()
    private val readyPackagesCategory = ArrayList<CategoryData>()
    private val readyPackagesDataList = ArrayList<ReadyPackagesData>()

    lateinit var set: HashSet<List<ProfileReadyPackageId>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ready_packages, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        if (profile_order_history_tab_layout.selectedTabPosition == 0)
        {
            viewModel.setStateEvent(event = ProfileStateEvent.GetProfileReadyPackages(""))
        }



        profile_order_history_tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {

                tab?.let { tab ->

                    when(tab.position)
                    {
                        0 -> {
                            viewModel.setStateEvent(event = ProfileStateEvent.GetProfileReadyPackages(""))
                            ready_package_rv.visibility = View.INVISIBLE
                        }
                        1 -> {
                            viewModel.setStateEvent(event = ProfileStateEvent.GetProfileReadyPackages("mine"))
                            ready_package_rv.visibility = View.INVISIBLE
                        }
                        2 ->{
                            viewModel.setStateEvent(event = ProfileStateEvent.GetProfileReadyPackages("recommended"))
                            ready_package_rv.visibility = View.INVISIBLE
                        }
                        else ->{
                            viewModel.setStateEvent(event = ProfileStateEvent.GetProfileReadyPackages(""))
                            ready_package_rv.visibility = View.INVISIBLE
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
//                TODO("Not yet implemented")
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
//                TODO("Not yet implemented")
            }

        })

        observeData()
    }




    private fun observeData() {

        Log.d(TAG, "AllReadyPackagesFragment viewModel: ${viewModel.hashCode()}")
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            if (dataState != null) {
                onDataStateChangeListener.onDataStateChange(dataState)

                dataState.stateError?.let { event ->
                    event.peekContent()?.let { stateError ->
                        stateError.response?.let { response ->
                            response.message?.let { message ->
                                if (message.equals("??????????????"))
                                {
                                    viewModel.setStateEvent(event = ProfileStateEvent.GetProfileReadyPackages(""))
                                }

                            }
                        }
                    }
                }

                dataState.data?.let { data ->
                    data.response?.let { event ->
                        event.peekContent()?.let { response ->
                            response.message?.let { message ->
                                if (message.equals("?????????? ???????????????? ?? ??????????????")) {
                                    onDataStateChangeListener.getOnOrderItemCount(
                                        onDataStateChangeListener.getItemCount()
                                    )
                                }
                                if (message.equals("???????????????? ???????????? ????????????????"))
                                {
                                    val dialog = PopUpListOfReadyPackagesProductsFragment()
                                    dialog.show(requireActivity().supportFragmentManager, "Fragment")

                                }

                            }
                        }
                    }

                    data.data?.let { event ->
                        event.getContentIfNotHandled()?.let { profileViewState ->
                            profileViewState.readyPackagesList?.let { list ->
                                Log.d(TAG, "allPackages dataState: ${list}")
                                viewModel.setProfileAllPackagesList(list)
                            }
                            profileViewState.profileReadyPackageIdList?.let { list ->
                                if (!list.isEmpty())
                                {
                                    Log.d(TAG, "product package id: ${list} ")
                                    onDataStateChangeListener.setReadyPackageListOfItems(list)
                                }
                            }
                        }
                    }
                }
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { profileViewState ->
            profileViewState.readyPackagesList?.let { list ->
                Log.d(TAG, "allPackages viewState: ${list}")
                setListOfDataToUI(list)
            }

        })
    }

    private fun setListOfDataToUI(list: List<ProfileReadyPackages>) {
        readyPackagesData.clear()
        readyPackagesDataList.clear()
        for (package_item in list) {
            readyPackagesData.add(
                PackagesData(
                    package_id = package_item.id,
                    package_name = package_item.package_name,
                    author = package_item.author,
                    visibility = package_item.visibility,
                    total_cost = package_item.total_cost,
                    items_count = package_item.items_count
                )
            )
        }



        Log.d(TAG, "readyPackagesData_size: ${readyPackagesData.distinct().toList()}")

        for ((ready_index, ready_package_data_item) in readyPackagesData.distinct().toList()
            .withIndex()) {
            for ((category_index, category_item) in list.withIndex()) {
                if (ready_package_data_item.package_id == category_item.id) {
                    Log.d(
                        TAG,
                        "ready_index:${ready_index}, ready_package_data_item:${category_item}"
                    )
                    readyPackagesCategory.add(
                        CategoryData(
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
            readyPackagesDataList.add(
                ready_index,
                ReadyPackagesData(
                    packageData = ready_package_data_item,
                    categoryData = readyPackagesCategory.distinct().toList()
                )
            )
            readyPackagesCategory.clear()
        }

        for (item in readyPackagesDataList) {
            Log.d(TAG, "setListOfDataToUI: ${item}")
        }

        val adapter = ProfileReadyPackagesParentAdapter(requestManager = requestManager,
            this, this, this)
        adapter.submitList(readyPackagesDataList.distinct().toList())
        ready_package_rv.visibility = View.VISIBLE
        ready_package_rv.layoutManager =
            LinearLayoutManager(this.requireContext(), LinearLayoutManager.VERTICAL, false)
        ready_package_rv.adapter = adapter


    }

    override fun addReadyPackageToBasket(position: Int, readyPackagesData: ReadyPackagesData) {
        Toast.makeText(
            this.requireContext(),
            "${readyPackagesData.packageData.package_id}",
            Toast.LENGTH_LONG
        ).show()
        viewModel.setStateEvent(
            event = ProfileStateEvent.AddItemReadyPackagesToBasket(
                package_item_id = readyPackagesData.packageData.package_id.toString()
            )
        )
    }

    override fun onShowPackageItem(position: Int, readyPackagesData: ReadyPackagesData) {
        viewModel.setStateEvent(event = ProfileStateEvent.SetReadyPackageId(readyPackagesData.packageData.package_id))
        onDataStateChangeListener.setCategoryReadyPackage(readyPackagesDataList[position].categoryData)
        onDataStateChangeListener.setCategoryReadyPackageId(readyPackagesData.packageData.package_id)

    }

    override fun onRemoveReadyPackage(position: Int, readyPackagesData: ReadyPackagesData) {
        viewModel.setStateEvent(event = ProfileStateEvent.RemoveReadyPackageItem(readyPackagesData.packageData.package_id))
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            onDataStateChangeListener = context as OnDataStateChangeListener
        }
        catch (e:Exception)
        {
            Log.d(TAG, "onAttach: ${context} must implement onDataStateChangeListener")
        }
    }

}