package com.example.bozorbek_vol2.ui.main.home.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.bozorbek_vol2.R
import com.example.bozorbek_vol2.model.main.home.HomeDiscountProducts
import com.example.bozorbek_vol2.model.main.home.HomeRandomProducts
import com.example.bozorbek_vol2.model.main.home.HomeSliderImage
import com.example.bozorbek_vol2.ui.OnDataStateChangeListener
import com.example.bozorbek_vol2.ui.main.home.fragment.adapter.HomeProductParentAdapter
import com.example.bozorbek_vol2.ui.main.home.fragment.adapter.SliderImageAdapter
import com.example.bozorbek_vol2.ui.main.home.fragment.adapter.model.HomeProduct
import com.example.bozorbek_vol2.ui.main.home.state.HomeStateEvent
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class HomeFragment : BaseHomeFragment(), HomeProductParentAdapter.OnPrentItemClickListener {

    private lateinit var onDataStateChangeListener:OnDataStateChangeListener
    private lateinit var homeImageAdapter: SliderImageAdapter
    private var list_of_products:ArrayList<HomeProduct> = ArrayList()
    private lateinit var parentAdapter: HomeProductParentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeData()
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: GetHomeSliderImage() triggered")
//        viewModel.setStateEvent(event = HomeStateEvent.GetHomeSliderImage())
        home_products_rv_parent.visibility = View.INVISIBLE

    }

    private fun observeData() {
        GlobalScope.launch(Main) {
            delay(300)

            viewModel.setStateEvent(event = HomeStateEvent.GetHomeData())
        }

        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            dataState.data?.let { data ->
                data.data?.let { event ->
                    event.getContentIfNotHandled()?.let { homeViewState ->
                        Log.d(TAG, "home dataState: ${homeViewState}")
                        homeViewState.listOfSliderImage?.let { slider ->
                            viewModel.setHomeSliderImage(slider)
                        }
                        homeViewState.listOfRandomProducts?.let { random ->
                            viewModel.setHomeRandomProduct(random)
                        }
                        homeViewState.listOfDiscountProducts?.let { discount ->
                            viewModel.setHomeDiscountProduct(discount)
                        }
                    }
                }
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState->
            Log.d(TAG, "home viewState: ${viewState}")
            viewState.listOfSliderImage?.let { list ->
                Log.d(TAG, "slider: ${list}")
                setImagesToUI(list)
            }
            viewState.listOfRandomProducts?.let { list ->
                Log.d(TAG, "random: ${list}")
                setRandomProduct(list)
            }
            viewState.listOfDiscountProducts?.let { list ->
                Log.d(TAG, "discount: ${list}")
                setDiscountProducts(list)
            }
        })
    }

    private fun setRandomProduct(list: List<HomeRandomProducts>) {
        list_of_products.clear()
        list_of_products.add(
            HomeProduct(
                "?????????????? ????????????????",
                list
            )
        )

    }

    private fun setDiscountProducts(list: List<HomeDiscountProducts>) {
        val list_discount = ArrayList<HomeRandomProducts>()
        for (item in list)
        {
            list_discount.add(
                HomeRandomProducts(
                    name = item.name,
                    get_absolute_url = item.get_absolute_url,
                    slug = item.slug,
                    category = item.category,
                    image = item.image,
                    price = item.price,
                    discount = item.discount,
                    unit = item.unit
                )
            )
        }
        list_of_products.add(
            HomeProduct(
            "?????????????? ????????????",list_discount
        )
        )
        Log.d(TAG, "setDiscountProducts: ${list_of_products}")
        parentAdapter = HomeProductParentAdapter(requestManager, this)
        parentAdapter.submitList(list_of_products.distinct().toList())
        home_products_rv_parent.visibility = View.VISIBLE
        home_products_rv_parent.adapter = parentAdapter
        home_products_rv_parent.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

    }



    private fun setImagesToUI(list: List<HomeSliderImage>) {
        homeImageAdapter = SliderImageAdapter(requestManager,homeViewPager2)
        homeImageAdapter.submitList(list)

        homeViewPager2.adapter = homeImageAdapter
        homeViewPager2.clipToPadding = false
        homeViewPager2.clipChildren = false
        homeViewPager2.offscreenPageLimit = 3
        homeViewPager2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(150))
        compositePageTransformer.addTransformer(object : ViewPager2.PageTransformer{
            override fun transformPage(page: View, position: Float) {
                val r:Float = 1 - Math.abs(position)
                page.scaleY = 1f + r * 0.15f
                page.scaleX = 1.1f + r * 0.15f
            }
        })

        homeViewPager2.setPageTransformer(compositePageTransformer)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            onDataStateChangeListener = context as OnDataStateChangeListener
        }
        catch (e:Exception)
        {
            Log.d(TAG, "onAttach: ${context} mustImplement OnDataStateChangeListener")
        }
    }

    override fun onItemClick(
        childPosition: Int,
        childItem: HomeRandomProducts,
        parentPosition: Int,
        parentItem: HomeProduct
    ) {
        val text = childItem.get_absolute_url.split("/")
        Log.d(TAG, "onItemClick: ${text}")
        val category_slug = text[1].replace(" ","")
        val product_slug = text[2].replace(" ","")
        val action = HomeFragmentDirections.actionHomeFragmentToHomeViewProductFragment(category_slug, product_slug)
        findNavController().navigate(action)
        Toast.makeText(requireContext(), "childPosition:$childPosition,\nchildItem:${childItem.name},\nparentPosition:$parentPosition,\nparentItem:${parentItem.title}",Toast.LENGTH_LONG).show()
    }

}