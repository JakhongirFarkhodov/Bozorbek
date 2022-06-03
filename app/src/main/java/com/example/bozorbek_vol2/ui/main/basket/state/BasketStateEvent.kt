package com.example.bozorbek_vol2.ui.main.basket.state

sealed class BasketStateEvent{

    class GetBasketProductOrderList : BasketStateEvent()

    class GetBasketProfileInfo() : BasketStateEvent()

    data class AddAddressProductOrder(val full_address:String, val latitude:String, val longtitude:String) : BasketStateEvent()

    class GetBasketAddressOrderList : BasketStateEvent()

    data class ApproveOrder(val address_id:String):BasketStateEvent()

    class None : BasketStateEvent()

}
