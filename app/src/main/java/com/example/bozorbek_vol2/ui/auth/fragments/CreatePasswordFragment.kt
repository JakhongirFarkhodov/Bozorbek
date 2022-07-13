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
import kotlinx.android.synthetic.main.fragment_create_password.*


class CreatePasswordFragment : BaseAuthFragment() {

    private val args:CreatePasswordFragmentArgs by navArgs()
    private lateinit var onDataStateChangeListener: OnDataStateChangeListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_password, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registration_create_password_button.setOnClickListener {
            if (checkbox_political_agreement.isChecked) {
                viewModel.setStateEvent(event = AuthStateEvent.SetPasswordStateEvent(
                    phone_number = args.phoneNumber,
                    sms_code = args.smsCode,
                    first_name = args.firstName,
                    password = edT_new_password.text.toString(),
                    confirm_password = edT_confirm_password.text.toString()
                ))
            }
            else{
                Toast.makeText(requireContext(), "Пожалуйста согласитесть политикой конфидециальности.", Toast.LENGTH_LONG).show()
            }
        }


        observeData()
    }

    private fun observeData() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            onDataStateChangeListener.onDataStateChange(dataState)
            dataState.data?.let { data ->
                data.data?.let { event ->
                    event.getContentIfNotHandled()?.let { authViewState ->
                        authViewState.passwordValue?.let { passwordValue ->
                            viewModel.setPasswordValue(passwordValue)
                        }
                    }
                }
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { authViewState ->
            authViewState.passwordValue?.let { passwordValue ->
                passwordValue.username?.let { useraname ->
                    if (useraname.equals(args.phoneNumber))
                    {
                        Toast.makeText(requireContext(), "Пользыватель зарегистрирован", Toast.LENGTH_LONG).show()
                        findNavController().navigate(R.id.action_createPasswordFragment_to_successfullyRegisterFragment)
                    }
                }
            }
        })
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