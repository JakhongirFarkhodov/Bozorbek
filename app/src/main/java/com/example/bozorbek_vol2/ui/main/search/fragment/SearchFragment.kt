package com.example.bozorbek_vol2.ui.main.search.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.bozorbek_vol2.R
import com.example.bozorbek_vol2.model.auth.AuthToken
import com.example.bozorbek_vol2.ui.OnDataStateChangeListener
import com.example.bozorbek_vol2.ui.main.search.state.SearchStateEvent


class SearchFragment : BaseSearchFragment() {

    private lateinit var onDataStateChangeListener: OnDataStateChangeListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeData()

    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: Поиск пользывателя...")
        viewModel.setStateEvent(event = SearchStateEvent.CheckPreviousAuthUser())
    }

    private fun observeData() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->

            onDataStateChangeListener.onDataStateChange(dataState)
            dataState.data?.let { data ->
                data.data?.let { event ->
                    event.getContentIfNotHandled()?.let { searchViewState ->
                        Log.d(TAG, "searchFragment dataState: ${searchViewState}")
                        searchViewState.checkPreviousAuthUser?.let { checkPreviousAuthUser ->
                            viewModel.setTokens(checkPreviousAuthUser)
                        }
                    }
                }
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { searchViewState ->
            searchViewState.checkPreviousAuthUser?.let { checkPreviousAuthUser ->
                Log.d(TAG, "searchFragment viewState: ${checkPreviousAuthUser}")
                checkPreviousAuthUser.access_token?.let { access_token ->
                    checkPreviousAuthUser.refresh_token?.let { refresh_token ->
                        checkPreviousAuthUser.phone_number?.let { phone_number ->
                            viewModel.sessionManager.login(authToken = AuthToken(
                                access_token = access_token, refresh_token = refresh_token, account_phone_number = phone_number
                            ))
                        }
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