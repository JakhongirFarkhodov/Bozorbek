package com.example.bozorbek_vol2.ui.main.profile.fragment.menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bozorbek_vol2.R
import com.example.bozorbek_vol2.ui.main.profile.fragment.BaseProfileFragment


class OrderHistoryFragment : BaseProfileFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_history, container, false)
    }


}