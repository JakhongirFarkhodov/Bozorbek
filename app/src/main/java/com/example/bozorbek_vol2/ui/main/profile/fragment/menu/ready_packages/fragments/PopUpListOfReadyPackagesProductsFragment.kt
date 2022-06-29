package com.example.bozorbek_vol2.ui.main.profile.fragment.menu.ready_packages.fragments

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.bozorbek_vol2.R
import com.example.bozorbek_vol2.model.main.profile.ProfileReadyPackageId
import com.example.bozorbek_vol2.ui.OnDataStateChangeListener
import com.example.bozorbek_vol2.ui.main.profile.fragment.menu.ready_packages.adapters.ProfileReadyPackageIdAdapter
import com.example.bozorbek_vol2.ui.main.profile.viewmodel.ProfileViewModel
import com.example.bozorbek_vol2.util.Constants
import com.example.bozorbek_vol2.viewmodels.ViewModelProviderFactory
import dagger.android.support.DaggerDialogFragment
import kotlinx.android.synthetic.main.fragment_pop_up_list_of_ready_packages_products.*
import javax.inject.Inject


class PopUpListOfReadyPackagesProductsFragment : DaggerDialogFragment() {

    private val TAG = Constants.LOG
    private lateinit var adapter:ProfileReadyPackageIdAdapter
    private lateinit var onDataStateChangeListener: OnDataStateChangeListener
    @Inject
    lateinit var providerFactory: ViewModelProviderFactory
    lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment_pop_up_list_of_ready_packages_products,
            container,
            false
        )
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        viewModel = ViewModelProvider(this, providerFactory).get(ProfileViewModel::class.java)
        Log.d(TAG, "token: ${viewModel.hashCode()}")

        pop_cancel.setOnClickListener {
            dismiss()
        }

        pop_up_order_setting.setOnClickListener {
            val dialog = PopUpOfAuthOrderFragment()
            dialog.show(requireActivity().supportFragmentManager, "AutoOrder")
            dismiss()
        }

        setDataToUi(onDataStateChangeListener.getReadyPackageListOfItems())

    }

    private fun setDataToUi(list: List<ProfileReadyPackageId>) {

        val requestOptions = RequestOptions().placeholder(android.R.color.transparent).error(R.drawable.default_image)
        val requestManager = Glide.with(requireActivity().application).setDefaultRequestOptions(requestOptions)
        adapter = ProfileReadyPackageIdAdapter(requestManager)
        adapter.submitList(list)
        pop_up_rv.adapter = adapter
        pop_up_rv.layoutManager = LinearLayoutManager(this.requireContext(), LinearLayoutManager.VERTICAL, false)
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