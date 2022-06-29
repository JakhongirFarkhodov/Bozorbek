package com.example.bozorbek_vol2.ui.main.profile.fragment.menu.ready_packages.fragments

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.bozorbek_vol2.R
import com.example.bozorbek_vol2.network.main.network_services.profile.request.ProfileReadyPackageAutoOrder
import com.example.bozorbek_vol2.ui.OnDataStateChangeListener
import com.example.bozorbek_vol2.ui.main.profile.fragment.menu.ready_packages.adapters.ProfileReadyPackagesChildAdapter
import com.example.bozorbek_vol2.ui.main.profile.state.ProfileStateEvent
import com.example.bozorbek_vol2.ui.main.profile.viewmodel.ProfileViewModel
import com.example.bozorbek_vol2.util.Constants
import com.example.bozorbek_vol2.viewmodels.ViewModelProviderFactory
import dagger.android.support.DaggerDialogFragment
import kotlinx.android.synthetic.main.fragment_pop_up_of_auth_order.*
import javax.inject.Inject


class PopUpOfAuthOrderFragment : DaggerDialogFragment() {

    private val TAG = Constants.LOG

    private var week_list:ArrayList<String> = ArrayList()
    private var week_frequency:ArrayList<String> = ArrayList()
    private lateinit var week_adapter:ArrayAdapter<String>
    private lateinit var week_frequency_adapter:ArrayAdapter<String>
    private lateinit var onDataStateChangeListener: OnDataStateChangeListener
    private lateinit var adapter:ProfileReadyPackagesChildAdapter
    private var week_day:Int = 0
    private var week_frequency_day:Int = 0

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory
    lateinit var viewModel:ProfileViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pop_up_of_auth_order, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        viewModel = ViewModelProvider(this, providerFactory).get(ProfileViewModel::class.java)
        Log.d(TAG, "token: ${viewModel.sessionManager.cachedAuthToken.value}")

        observeData()

        item_auto_order_ready_package_mbt.setOnClickListener {
            onDataStateChangeListener.setProfileReadyPackageAutoOrderParameters(
                profileReadyPackageAutoOrder = ProfileReadyPackageAutoOrder(
                    package_id = onDataStateChangeListener.getCategoryReadyPackageId(),
                    week_day = week_day,
                    week_frequency = week_frequency_day
                )
            )
            viewModel.setStateEvent(event = ProfileStateEvent.SetReadyPackageToAutoOrder(
                profileReadyPackageAutoOrder = onDataStateChangeListener.getProfileReadyPackageAutoOrderParameters()
            ))
            onDataStateChangeListener.setTriggerProfileReadyPackageAutoOrderParameters(false)
            Toast.makeText(requireContext(), "id:${onDataStateChangeListener.getCategoryReadyPackageId()}\n,week:${week_day},\nweek_frequency:${week_frequency_day}", Toast.LENGTH_LONG).show()
            onDataStateChangeListener.setTriggerProfileReadyPackageAutoOrderParameters(true)
//            dismiss()
        }
    }
    

    private fun observeData() {
        
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            dataState.data?.let { data ->
                data.response?.let { event ->
                    event.peekContent()?.let { response ->
                        response.message?.let { message ->
                            if (message.equals("Пакет добавлен в автозаказ"))
                            {
                                val dialog = PopUpSuccessfullySavedReadyPackageInAutoOrderFragment()
                                dialog.show(requireActivity().supportFragmentManager, "fragmentSuccess")
                                dismiss()
                            }
                        }
                    }
                }
            }
        })

        week_list.add("Понедельник")
        week_list.add("Вторник")
        week_list.add("Среда")
        week_list.add("Четверг")
        week_list.add("Пятница")
        week_list.add("Суббота")
        week_list.add("Воскресение")

        week_frequency.add("За 1 неделю")
        week_frequency.add("За 2 неделю")
        week_frequency.add("За 3 неделю")
        week_frequency.add("За 4 неделю")

        week_adapter = ArrayAdapter(requireContext(), R.layout.item_drop_down,week_list)
        week_frequency_adapter = ArrayAdapter(requireContext(), R.layout.item_drop_down, week_frequency)

        week_autocomplete.setAdapter(week_adapter)
        week_frequency_autocomplete.setAdapter(week_frequency_adapter)

        week_autocomplete.setOnItemClickListener { adapterView, view, position, l ->
            week_day = position
        }

        week_frequency_autocomplete.setOnItemClickListener { adapterView, view, position, l ->
            week_frequency_day = position
        }

        val requestOptions = RequestOptions().placeholder(android.R.color.transparent).error(R.drawable.default_image)
        val requestManager = Glide.with(requireActivity().application).setDefaultRequestOptions(requestOptions)

        adapter = ProfileReadyPackagesChildAdapter(requestManager)
        adapter.submitList(onDataStateChangeListener.getCategoryReadyPackage())
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL)
        item_auto_order_category_rv.adapter = adapter
        item_auto_order_category_rv.layoutManager = staggeredGridLayoutManager

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
}