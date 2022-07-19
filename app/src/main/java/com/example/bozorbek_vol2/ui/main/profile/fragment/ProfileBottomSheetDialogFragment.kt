package com.example.bozorbek_vol2.ui.main.profile.fragment

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.bozorbek_vol2.R
import com.example.bozorbek_vol2.util.LocaleHelper
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yariksoffice.lingver.Lingver
import kotlinx.android.synthetic.main.fragment_profile_bottom_sheet_dialog.*
import java.util.*


class ProfileBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private val args: ProfileBottomSheetDialogFragmentArgs by navArgs()
    private lateinit var localeHelper: Context

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

        localeHelper = LocaleHelper().setLocale(requireContext(), Locale.getDefault().language)!!
        val resurs = localeHelper.resources
        changeLanguage(resurs)
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

        flag_rus.setOnClickListener {
//            LocaleHelper().setLocale(requireContext(), "ru")
//            localeHelper = LocaleHelper().setLocale(requireContext(), "ru")!!
//            val resurs = localeHelper.resources
//            changeLanguage(resurs)
//            findNavController().navigate(R.id.action_profileBottomSheetDialogFragment_to_profileFragment)
            Lingver.getInstance().setLocale(requireContext(), "ru")
            requireActivity().recreate()
        }

        flag_uz.setOnClickListener {
//            LocaleHelper().setLocale(requireContext(), "uz")
//            localeHelper = LocaleHelper().setLocale(requireContext(), "uz")!!
//            val resurs = localeHelper.resources
//            changeLanguage(resurs)
//            findNavController().navigate(R.id.action_profileBottomSheetDialogFragment_to_profileFragment)
            Lingver.getInstance().setLocale(requireContext(), "uz")
            requireActivity().recreate()
        }
    }

    private fun changeLanguage(resurs: Resources) {
        auto_order_mtv.text = resurs.getString(R.string.auto_order)
        profile_search_history_mtv.text = resurs.getString(R.string.search_history)
        profile_notification_mtv.text = resurs.getString(R.string.title_notification)
        profile_search_order_mtv.text = resurs.getString(R.string.title_search_order)
        profile_company_info_mtv.text = resurs.getString(R.string.title_company_info)
        profile_settings_mtv.text = resurs.getString(R.string.title_settings)
        profile_complaints_mtv.text = resurs.getString(R.string.complaints)
        profile_ready_package.text = resurs.getString(R.string.ready_package)
        profile_exit_mtv.text = resurs.getString(R.string.exit_profile)

    }

}