package com.example.bozorbek_vol2.ui.main.profile.fragment.menu

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.bozorbek_vol2.R
import com.example.bozorbek_vol2.ui.OnDataStateChangeListener
import com.example.bozorbek_vol2.ui.main.profile.fragment.BaseProfileFragment
import com.example.bozorbek_vol2.ui.main.profile.state.ProfileStateEvent
import kotlinx.android.synthetic.main.fragment_complaints.*

class ComplaintsFragment : BaseProfileFragment() {

    lateinit var onDataStateChangeListener: OnDataStateChangeListener
    lateinit var text:String
    lateinit var title:String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_complaints, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        obsevereData()
        setComplaints()
    }

    private fun setComplaints() {
        setComplaint_btn.setOnClickListener {
            text = complaints_edT.text.toString()
            val str = StringBuffer()
            if (rude_courier.isChecked) {
                str.append(rude_courier.text.toString() + "\n")
            }
            if (poor_quality.isChecked) {
                str.append(poor_quality.text.toString() + "\n")
            }
            if (products_spilled.isChecked) {
                str.append(products_spilled.text.toString() + "\n")
            }
            if (order_took_too_long.isChecked) {
                str.append(order_took_too_long.text.toString() + "\n")
            }
            if (bad_delivery.isChecked) {
                str.append(bad_delivery.text.toString() + "\n")
            }
            if (other.isChecked) {
                str.append(other.text.toString() + "\n")
            }
            title = str.toString()
            Toast.makeText(this.requireContext(), "${title}",Toast.LENGTH_LONG).show()
            viewModel.setStateEvent(event = ProfileStateEvent.SetComplaints(title, text))
        }
    }

    private fun obsevereData() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            onDataStateChangeListener.onDataStateChange(dataState)

        })
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