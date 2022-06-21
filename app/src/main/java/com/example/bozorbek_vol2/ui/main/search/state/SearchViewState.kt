package com.example.bozorbek_vol2.ui.main.search.state

import com.example.bozorbek_vol2.model.main.catalog.parametrs.ParametersValue
import com.example.bozorbek_vol2.model.main.search.SearchProduct

data class SearchViewState(
    var checkPreviousAuthUser: CheckPreviousAuthUser? = CheckPreviousAuthUser(),
    var searchProductList:List<SearchProduct>? = ArrayList(),
    var parametersValue: ParametersValue? = ParametersValue(),
    var message:String? = null
)
data class CheckPreviousAuthUser(
    val access_token:String? = null,
    val refresh_token:String? = null,
    val phone_number:String? = null
)