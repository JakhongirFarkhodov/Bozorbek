package com.example.bozorbek_vol2.ui.main.profile.fragment.menu.ready_packages.fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bozorbek_vol2.R
import dagger.android.support.DaggerDialogFragment
import kotlinx.android.synthetic.main.fragment_pop_up_successfully_saved_ready_package_in_auto_order.*


class PopUpSuccessfullySavedReadyPackageInAutoOrderFragment : DaggerDialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment_pop_up_successfully_saved_ready_package_in_auto_order,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dismiss_popUp.setOnClickListener {
            dismiss()
        }
    }


}