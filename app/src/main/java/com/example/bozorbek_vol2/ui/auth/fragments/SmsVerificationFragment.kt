package com.example.bozorbek_vol2.ui.auth.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.bozorbek_vol2.R
import com.example.bozorbek_vol2.ui.OnDataStateChangeListener
import com.example.bozorbek_vol2.ui.auth.state.AuthStateEvent
import com.example.bozorbek_vol2.ui.auth.state.VerifySmsCodeValue
import com.example.bozorbek_vol2.util.SuccessHandling
import kotlinx.android.synthetic.main.fragment_sms_verification.*


class SmsVerificationFragment : BaseAuthFragment() {

    private val args: SmsVerificationFragmentArgs by navArgs()
    private lateinit var onDataStateChangeListener: OnDataStateChangeListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sms_verification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        verify_sms_code_button.setOnClickListener {
            setVerifySmsCodeData()
        }
        observeData()
    }

    private fun setVerifySmsCodeData() {
        viewModel.setStateEvent(event = AuthStateEvent.VerifySmsCodeStateEvent(phone_number = args.phoneNumber, sms_code = edT_sms_verification.text.toString()))
    }

    private fun observeData() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            onDataStateChangeListener.onDataStateChange(dataState)
            dataState.data?.let { data ->
                data.data?.let { event ->
                    event.getContentIfNotHandled()?.let { response ->
                        response.verifySmsCodeValue?.let { verifySmsCodeValue ->
                            verifySmsCodeValue.responseMessage?.let { message ->
                                viewModel.setSmsVerifyCodeValue(verifySmsCodeValue = VerifySmsCodeValue(responseMessage = message))
                            }
                        }
                    }
                }
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { authViewState ->
            authViewState.verifySmsCodeValue?.let { verifySmsCodeValue ->
                verifySmsCodeValue.responseMessage?.let { message ->
                    if (message.equals(SuccessHandling.RESPONSE_PHONE_NUMBER_VERIFIED))
                    {
                        navToCreatePasswordFragment()
                    }
                }
            }
        })
    }

    private fun navToCreatePasswordFragment() {
        val action = SmsVerificationFragmentDirections.actionSmsVerificationFragmentToCreatePasswordFragment(
            firstName = args.firstName, smsCode = edT_sms_verification.text.toString(), phoneNumber = args.phoneNumber
        )
        findNavController().navigate(action)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            onDataStateChangeListener = context as OnDataStateChangeListener
        } catch (e: Exception) {
            Log.d(TAG, "onAttach: ${context} must implement OnDataStateChangeListener")
        }
    }

}