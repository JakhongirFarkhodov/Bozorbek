package com.example.bozorbek_vol2.ui.auth.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.bozorbek_vol2.R
import com.example.bozorbek_vol2.ui.OnDataStateChangeListener
import com.example.bozorbek_vol2.ui.auth.state.AuthStateEvent
import kotlinx.android.synthetic.main.fragment_register.*


class RegisterFragment : BaseAuthFragment() {

    private lateinit var onDataStateChangeListener: OnDataStateChangeListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registration_to_log_text.setOnClickListener {
            navToLoginFragment()
        }

        registration_code_button.setOnClickListener {
            getVerifyCode()
        }

        observeData()
    }

    private fun getVerifyCode() {
        viewModel.setStateEvent(event = AuthStateEvent.RegistrationStateEvent(
            first_name = edT_register_first_name.text.toString(),
            phone_number = edT_register_phone_number.text.toString()
        ))
    }

    private fun observeData() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            onDataStateChangeListener.onDataStateChange(dataState)
            dataState.data?.let { data ->
                data.data?.let { event ->
                    event.getContentIfNotHandled()?.let { authViewState ->
                        authViewState.registrationValue?.let { registrationValue ->
                            viewModel.setRegistrationValue(registrationValue)
                        }
                    }
                }
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { authViewState ->
            authViewState.registrationValue?.let { registrationValue ->
                registrationValue.phone_number?.let { phone_number ->
                    navToVerifySmsCodeFragment(phone_number)
                }
            }
        })
    }

    private fun navToVerifySmsCodeFragment(phoneNumber: String) {
        if (phoneNumber.equals(edT_register_phone_number.text.toString()))
        {
            val action = RegisterFragmentDirections.actionRegisterFragmentToSmsVerificationFragment(phoneNumber = phoneNumber,firstName = edT_register_first_name.text.toString())
            findNavController().navigate(action)
        }
    }

    private fun navToLoginFragment() {
        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
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