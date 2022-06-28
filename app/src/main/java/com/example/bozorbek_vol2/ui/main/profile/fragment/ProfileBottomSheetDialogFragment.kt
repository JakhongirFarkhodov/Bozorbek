package com.example.bozorbek_vol2.ui.main.profile.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.bozorbek_vol2.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_profile_bottom_sheet_dialog.*


class ProfileBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private val args: ProfileBottomSheetDialogFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_bottom_sheet_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navToProfilesFragment()
        setArgsToUI()
    }

    private fun setArgsToUI() {
        val requestOptions = RequestOptions().placeholder(android.R.color.transparent).error(R.drawable.default_image)
        val requestManager = Glide.with(requireActivity().application).setDefaultRequestOptions(requestOptions)
        requestManager.load(args.image).into(profile_imv)
        profile_name_mtv.setText(args.username)
    }

    private fun navToProfilesFragment() {
        profile_constratin_search.setOnClickListener {
            findNavController().navigate(R.id.action_profileBottomSheetDialogFragment_to_searchHistoryFragment)
        }

        profile_constratin_notification.setOnClickListener {
            findNavController().navigate(R.id.action_profileBottomSheetDialogFragment_to_notificationFragment)
        }

        profile_constratin_order_history.setOnClickListener {
            findNavController().navigate(R.id.action_profileBottomSheetDialogFragment_to_orderHistoryFragment)
        }

        profile_constratin_company_info.setOnClickListener {
            findNavController().navigate(R.id.action_profileBottomSheetDialogFragment_to_companyInfoFragment)
        }

        profile_constratin_settings.setOnClickListener {
            findNavController().navigate(R.id.action_profileBottomSheetDialogFragment_to_securitySettingsFragment)
        }

        profile_constratin_reviews.setOnClickListener {
            findNavController().navigate(R.id.action_profileBottomSheetDialogFragment_to_complaintsFragment)
        }

        profile_constratin_ready_package.setOnClickListener {
            findNavController().navigate(R.id.action_profileBottomSheetDialogFragment_to_readyPackagesFragment)
        }

        profile_constratin_auto_order.setOnClickListener {
            findNavController().navigate(R.id.action_profileBottomSheetDialogFragment_to_autoOrderFragment)
        }
    }

}