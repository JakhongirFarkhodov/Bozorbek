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
import com.example.bozorbek_vol2.model.auth.AuthToken
import com.example.bozorbek_vol2.ui.OnDataStateChangeListener
import com.example.bozorbek_vol2.ui.auth.state.AuthStateEvent
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : BaseAuthFragment() {

    private lateinit var onDataStateChangeListener: OnDataStateChangeListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        log_to_register_text.setOnClickListener {
            navToRegistration()
        }

        login_button.setOnClickListener {
            login()
        }

        observeData()

    }

    private fun observeData() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            onDataStateChangeListener.onDataStateChange(dataState)
            dataState.data?.let { data ->
                data.data?.let { event ->
                    event.getContentIfNotHandled()?.let { authViewState ->
                        authViewState.loginValue?.let { loginValue ->
                            viewModel.setLoginValue(loginValue)
                        }
                    }
                }
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { authViewState ->
            authViewState.loginValue?.let { loginValue ->
                Log.d(TAG, "observeData: ${loginValue}")
                loginValue.access_token?.let { access_token ->
                    loginValue.refreshToken?.let { refresh_token ->
                        loginValue.phone_number?.let { phone_number ->
                            sessionManager.login(authToken = AuthToken(
                                account_phone_number = phone_number,
                                access_token = access_token,
                                refresh_token = refresh_token
                            ))
                        }
                    }
                }
            }
        })
    }

    private fun login() {
        viewModel.setStateEvent(event = AuthStateEvent.LoginStateEvent(
            phone_number = edT_login_username.text.toString(),
            password = edT_login_password.text.toString()
        ))
    }

    private fun navToRegistration() {
        findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
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