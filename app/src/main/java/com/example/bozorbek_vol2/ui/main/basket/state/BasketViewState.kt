package com.example.bozorbek_vol2.ui.main.basket.state

import com.example.bozorbek_vol2.model.main.basket.BasketOrderProduct
import com.example.bozorbek_vol2.model.main.profile.Profile
import com.example.bozorbek_vol2.network.main.network_services.basket.response.GetBasketListAddressResponse
import com.example.bozorbek_vol2.ui.main.profile.state.ProfileStateEvent

data class BasketViewState(
    var basketOrderProductList: BasketOrderProductList? = BasketOrderProductList(),
    var basketGetAddedAddressMessage:String? = null,
    var basketGetAddressOrderList: BasketGetAddressOrderList = BasketGetAddressOrderList(),
    var profile:Profile? = null
)

data class BasketOrderProductList(
    var list: List<BasketOrderProduct>? = ArrayList()
)
data class BasketGetAddressOrderList(
    var list: List<GetBasketListAddressResponse>? = ArrayList()
)