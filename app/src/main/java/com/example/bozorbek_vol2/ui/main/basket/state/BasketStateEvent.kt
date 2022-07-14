package com.example.bozorbek_vol2.ui.main.basket.state

import com.example.bozorbek_vol2.network.main.network_services.basket.request.save_package.SaveReadyPackageRequest

sealed class BasketStateEvent{

    class GetBasketProductOrderList : BasketStateEvent()

    class GetBasketProfileInfo() : BasketStateEvent()

    data class RemoveBasketOrderProductById(val product_item_id:String):BasketStateEvent()

    data class AddAddressProductOrder(val full_address:String, val latitude:String, val longtitude:String) : BasketStateEvent()

    class GetBasketAddressOrderList : BasketStateEvent()

    data class ApproveOrder(val address_id:String, val name:String, val phone_num:String, val comment:String):BasketStateEvent()

    data class SetCreatedReadyPackage(val saveReadyPackageRequest: SaveReadyPackageRequest):BasketStateEvent()

    class None : BasketStateEvent()

}
