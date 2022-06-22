package com.example.bozorbek_vol2.ui.main.basket.state

import com.example.bozorbek_vol2.model.main.basket.BasketOrderProduct
import com.example.bozorbek_vol2.model.main.profile.Profile
import com.example.bozorbek_vol2.network.main.network_services.basket.request.save_package.SaveReadyPackageRequest
import com.example.bozorbek_vol2.network.main.network_services.basket.response.GetBasketListAddressResponse

data class BasketViewState(
    var basketOrderProductList: BasketOrderProductList? = BasketOrderProductList(),
    var basketGetAddedAddressMessage:String? = null,
    var basketGetAddressOrderList: BasketGetAddressOrderList = BasketGetAddressOrderList(),
    var saveReadyPackageRequest: SaveReadyPackageRequest? = null,
    var profile:Profile? = null
)

data class BasketOrderProductList(
    var list: List<BasketOrderProduct>? = ArrayList()
)
data class BasketGetAddressOrderList(
    var list: List<GetBasketListAddressResponse>? = ArrayList()
)