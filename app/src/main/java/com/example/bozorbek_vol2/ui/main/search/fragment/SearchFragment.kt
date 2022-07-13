package com.example.bozorbek_vol2.ui.main.search.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bozorbek_vol2.R
import com.example.bozorbek_vol2.model.auth.AuthToken
import com.example.bozorbek_vol2.model.main.search.SearchProduct
import com.example.bozorbek_vol2.ui.OnDataStateChangeListener
import com.example.bozorbek_vol2.ui.main.search.adapter.SearchProductAdapter
import com.example.bozorbek_vol2.ui.main.search.state.SearchStateEvent
import kotlinx.android.synthetic.main.fragment_search.*


class SearchFragment : BaseSearchFragment(), SearchProductAdapter.OnSearchItemClickListener {

    private lateinit var onDataStateChangeListener: OnDataStateChangeListener
    lateinit var adapter:SearchProductAdapter

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
        searchOnClick()


    }

    private fun searchOnClick() {

        edT_search.setOnTouchListener { view, motionEvent ->
            if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                if(motionEvent.getRawX() >= (edT_search.getRight() - edT_search.getCompoundDrawables()[2].getBounds().width())) {
                    // your action here
                    viewModel.sessionManager.cachedAuthToken.value?.let { authToken ->
                        if (authToken != null)
                        {
                            viewModel.setStateEvent(SearchStateEvent.SearchProductEvent(edT_search.text.toString()))
                            search_rec_bg.visibility = View.GONE
                            search_overView.visibility = View.GONE
                            search_rv.visibility = View.VISIBLE
                            search_result_mtv.visibility = View.VISIBLE
                            search_rounded_corners.visibility = View.VISIBLE
                            val param = mcv_search.layoutParams as ViewGroup.MarginLayoutParams
                            param.setMargins(0,40,0,0)
                            mcv_search.layoutParams = param
                        }
                        else{
                            Toast.makeText(requireContext(), "Вы еще не зарегистрированы", Toast.LENGTH_LONG).show()
                        }
                    }
                    return@setOnTouchListener true
                }
            }
            return@setOnTouchListener false
        }


        edT_search.setOnEditorActionListener { textView, actionId, keyEvent ->

            if (actionId == EditorInfo.IME_ACTION_SEARCH)
            {
                viewModel.sessionManager.cachedAuthToken.value?.let { authToken ->
                    if (authToken != null)
                    {
                        viewModel.setStateEvent(SearchStateEvent.SearchProductEvent(edT_search.text.toString()))
                        search_rec_bg.visibility = View.GONE
                        search_overView.visibility = View.GONE
                        search_result_mtv.visibility = View.VISIBLE
                        search_rv.visibility = View.VISIBLE
                        search_rounded_corners.visibility = View.VISIBLE
                        val param = mcv_search.layoutParams as ViewGroup.MarginLayoutParams
                        param.setMargins(0,40,0,0)
                        mcv_search.layoutParams = param
                    }
                    else{
                        Toast.makeText(requireContext(), "Вы еще не зарегистрированы", Toast.LENGTH_LONG).show()
                    }
                }
            }

            return@setOnEditorActionListener false
        }
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
                        searchViewState.searchProductList?.let { list ->
                            if (!list.isEmpty())
                            {
                                Log.d(TAG, "searchProductList dataState: ${list}")
                                viewModel.setSearchProductList(list)
                            }
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

            searchViewState.searchProductList?.let { list ->
                if (!list.isEmpty())
                {
                    setSearchProductList(list)
                    Log.d(TAG, "searchProductList viewState: ${list}")
                }
            }
        })
    }

    private fun setSearchProductList(list: List<SearchProduct>) {
        adapter = SearchProductAdapter(requestManager, this)
        adapter.submitList(list)
        search_rv.adapter = adapter
        search_rv.layoutManager = LinearLayoutManager(this.requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    override fun onSearchItemClick(position: Int, searchProduct: SearchProduct) {
        val text = searchProduct.absolute_url.split("/")
        Log.d(TAG, "onSearchItemClick: ${text}")
        val category_slug = text[1].replace(" ","")
        val product_slug = text[2].replace(" ","")
        val action = SearchFragmentDirections.actionSearchFragmentToSearchCatalogViewProductFragment(
            categorySlug = category_slug,
            productSlug = product_slug
        )
        findNavController().navigate(action)

        Toast.makeText(this.requireContext(), "${category_slug}\n${product_slug}", Toast.LENGTH_LONG).show()
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