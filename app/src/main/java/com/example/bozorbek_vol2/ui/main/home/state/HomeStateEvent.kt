package com.example.bozorbek_vol2.ui.main.home.state

sealed class HomeStateEvent {

    class GetHomeSliderImage : HomeStateEvent()

    class GetRandomProducts : HomeStateEvent()

    class GetDiscountProducts : HomeStateEvent()

    data class GetCatalogViewProductListOfData(val category_slug:String, val product_slug:String):
        HomeStateEvent()

    data class GetCatalogViewProductBySortValue(val sort_value:String): HomeStateEvent()

    data class GetCatalogViewProductBySortAndProductOwnerValue(val sort_value:String, val productOwner_value:String):
        HomeStateEvent()

    data class GetCatalogViewProductBySortAndProductOwnerAndPaketValue(val sort_value:String, val productOwner_value:String, val paket_value:String):
        HomeStateEvent()

    data class GetCatalogViewProductByGramme(val sort_value:String, val productOwner_value:String, val paket_value:String, val gramme:Boolean) : HomeStateEvent()

    data class GetCatalogViewProductByPiece(val sort_value:String, val productOwner_value:String, val paket_value:String, val piece:Boolean) : HomeStateEvent()

    data class GetCatalogViewProductBySizeLarge(val sort_value:String, val productOwner_value:String, val paket_value:String, val gramme:Boolean, val piece:Boolean, val large:Boolean) : HomeStateEvent()
    data class GetCatalogViewProductBySizeMiddle(val sort_value:String, val productOwner_value:String, val paket_value:String, val gramme:Boolean, val piece:Boolean, val middle:Boolean) : HomeStateEvent()
    data class GetCatalogViewProductBySizeSmall(val sort_value:String, val productOwner_value:String, val paket_value:String, val gramme:Boolean, val piece:Boolean, val small:Boolean) : HomeStateEvent()

    data class AddCatalogOrderItem(val product_item_id:String, val quantity:Int, val unit:String, val size:String, val sortValue:String):
        HomeStateEvent()


    class None : HomeStateEvent()

}