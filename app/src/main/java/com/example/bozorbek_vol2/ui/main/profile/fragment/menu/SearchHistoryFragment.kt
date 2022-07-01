package com.example.bozorbek_vol2.ui.main.profile.fragment.menu

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bozorbek_vol2.R
import com.example.bozorbek_vol2.network.main.network_services.search.response.SearchHistoryResponse
import com.example.bozorbek_vol2.ui.OnDataStateChangeListener
import com.example.bozorbek_vol2.ui.main.profile.fragment.BaseProfileFragment
import com.example.bozorbek_vol2.ui.main.profile.state.ProfileStateEvent
import com.example.bozorbek_vol2.ui.main.search.adapter.SearchHistoryAdapter
import kotlinx.android.synthetic.main.fragment_search_history.*


class SearchHistoryFragment : BaseProfileFragment() {

    lateinit var onDataStateChangeListener: OnDataStateChangeListener
    lateinit var adapter:SearchHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeData()
    }

    override fun onResume() {
        super.onResume()
        viewModel.setStateEvent(event = ProfileStateEvent.GetSearchHistory())
    }

    private fun observeData() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            onDataStateChangeListener.onDataStateChange(dataState)
            dataState.data?.let { data ->
                data.data?.let { event ->
                    event.getContentIfNotHandled()?.let { profileViewState ->
                        profileViewState.searchHistoryOrder?.let { list ->
                            viewModel.setSearchHistoryList(list)
                        }
                    }
                }
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState.searchHistoryOrder?.let { list ->
                setSearchHistoryListToUi(list)
            }
        })
    }

    private fun setSearchHistoryListToUi(list: List<SearchHistoryResponse>) {
        adapter = SearchHistoryAdapter()
        adapter.submitList(list)
        search_history_rv.adapter = adapter
        search_history_rv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
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