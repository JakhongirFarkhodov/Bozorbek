package com.example.bozorbek_vol2.ui.main.home.state

import com.example.bozorbek_vol2.model.main.catalog.parametrs.ParametersValue
import com.example.bozorbek_vol2.model.main.home.HomeDiscountProducts
import com.example.bozorbek_vol2.model.main.home.HomeRandomProducts
import com.example.bozorbek_vol2.model.main.home.HomeSliderImage

data class HomeViewState(
    var listOfSliderImage:List<HomeSliderImage>? = ArrayList(),
    var listOfRandomProducts:List<HomeRandomProducts>? = ArrayList(),
    var listOfDiscountProducts:List<HomeDiscountProducts>? = ArrayList(),
    var parametersValue: ParametersValue? = ParametersValue()
)