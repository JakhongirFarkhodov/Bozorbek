package com.example.bozorbek_vol2.ui.main.profile.fragment.menu.ready_packages.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bozorbek_vol2.R
import com.example.bozorbek_vol2.model.main.profile.ProfileReadyPackageId
import com.example.bozorbek_vol2.ui.OnDataStateChangeListener
import com.example.bozorbek_vol2.ui.main.profile.fragment.menu.ready_packages.adapters.ProfileReadyPackageIdAdapter
import com.example.bozorbek_vol2.util.Constants
import kotlinx.android.synthetic.main.fragment_pop_up_list_of_ready_packages_products.*


class PopUpListOfReadyPackagesProductsFragment : DialogFragment() {

    private val TAG = Constants.LOG
    private lateinit var adapter:ProfileReadyPackageIdAdapter
    private lateinit var onDataStateChangeListener: OnDataStateChangeListener


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



        pop_cancel.setOnClickListener {
            dismiss()
        }

//        setDataToUi(args.profileReadyPackageIdArrayList.list)

    }

    private fun setDataToUi(list: List<ProfileReadyPackageId>) {

        adapter = ProfileReadyPackageIdAdapter()
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