package com.example.bozorbek_vol2.ui.main.profile.fragment.menu.active_orders.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.navArgs
import com.example.bozorbek_vol2.R
import com.example.bozorbek_vol2.ui.OnDataStateChangeListener
import com.example.bozorbek_vol2.ui.main.profile.fragment.BaseProfileFragment
import kotlinx.android.synthetic.main.fragment_orders_process_info.*


class OrdersProcessInfoFragment : BaseProfileFragment() {

    private lateinit var onDataStateChangeListener: OnDataStateChangeListener
    private val args:OrdersProcessInfoFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_orders_process_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sendDataToUI()
    }

    private fun sendDataToUI() {
        number_of_order.setText("Заказ номер:" + args.profileActiveOrHistoryOrder.id)

        if (args.profileActiveOrHistoryOrder.status.equals("UNAPPROVED"))
        {
            process_line_1.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
            process_line_2.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
        }

        else if (args.profileActiveOrHistoryOrder.status.equals("APPROVED"))
        {
            process_text_1.visibility = View.INVISIBLE
            process_image_1.visibility = View.VISIBLE
            order_process_check_1.borderWidth = 60
            process_line_1.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
            process_line_2.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
        }

        else if (args.profileActiveOrHistoryOrder.status.equals("COLLECTING"))
        {
            process_text_1.visibility = View.INVISIBLE
            process_image_1.visibility = View.VISIBLE
            order_process_check_1.borderWidth = 60
            process_line_1.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.fruit_light_green))
            process_line_2.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
        }
        else if (args.profileActiveOrHistoryOrder.status.equals("COLLECTED"))
        {
            process_text_1.visibility = View.INVISIBLE
            process_image_1.visibility = View.VISIBLE
            order_process_check_1.borderWidth = 60
            process_text_2.visibility = View.INVISIBLE
            process_image_2.visibility = View.VISIBLE
            order_process_check_2.borderWidth = 60
            process_line_1.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.fruit_light_green))
            process_line_2.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
        }
        else if (args.profileActiveOrHistoryOrder.status.equals("DELIVERING"))
        {
            process_text_1.visibility = View.INVISIBLE
            process_image_1.visibility = View.VISIBLE

            process_text_2.visibility = View.INVISIBLE
            process_image_2.visibility = View.VISIBLE

            process_text_3.visibility = View.INVISIBLE
            process_image_3.visibility = View.VISIBLE

            order_process_check_1.borderWidth = 60
            order_process_check_2.borderWidth = 60
            order_process_check_3.borderWidth = 60
            process_line_1.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.fruit_light_green))
            process_line_2.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.fruit_light_green))
        }
        else if (args.profileActiveOrHistoryOrder.status.equals("DELIVERED"))
        {
            process_text_1.visibility = View.INVISIBLE
            process_image_1.visibility = View.VISIBLE

            process_text_2.visibility = View.INVISIBLE
            process_image_2.visibility = View.VISIBLE

            process_text_3.visibility = View.INVISIBLE
            process_image_3.visibility = View.VISIBLE

            order_process_check_1.borderWidth = 60
            order_process_check_2.borderWidth = 60
            order_process_check_3.borderWidth = 60
            process_line_1.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.fruit_light_green))
            process_line_2.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.fruit_light_green))
        }
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