package com.example.bozorbek_vol2.ui.main.search.state

sealed class SearchStateEvent{

    class CheckPreviousAuthUser : SearchStateEvent()

    data class SearchProductEvent(val query:String):SearchStateEvent()



    data class GetCatalogViewProductListOfData(val category_slug:String, val product_slug:String):
        SearchStateEvent()

    data class GetCatalogViewProductBySortValue(val sort_value:String): SearchStateEvent()

    data class GetCatalogViewProductBySortAndProductOwnerValue(val sort_value:String, val productOwner_value:String):
        SearchStateEvent()

    data class GetCatalogViewProductBySortAndProductOwnerAndPaketValue(val sort_value:String, val productOwner_value:String, val paket_value:String):
        SearchStateEvent()

    data class GetCatalogViewProductByGramme(val sort_value:String, val productOwner_value:String, val paket_value:String, val gramme:Boolean) : SearchStateEvent()

    data class GetCatalogViewProductByPiece(val sort_value:String, val productOwner_value:String, val paket_value:String, val piece:Boolean) : SearchStateEvent()

    data class GetCatalogViewProductBySizeLarge(val sort_value:String, val productOwner_value:String, val paket_value:String, val gramme:Boolean, val piece:Boolean, val large:Boolean) : SearchStateEvent()
    data class GetCatalogViewProductBySizeMiddle(val sort_value:String, val productOwner_value:String, val paket_value:String, val gramme:Boolean, val piece:Boolean, val middle:Boolean) : SearchStateEvent()
    data class GetCatalogViewProductBySizeSmall(val sort_value:String, val productOwner_value:String, val paket_value:String, val gramme:Boolean, val piece:Boolean, val small:Boolean) : SearchStateEvent()

    data class AddCatalogOrderItem(val product_item_id:String, val quantity:Int, val unit:String, val size:String, val sortValue:String):
        SearchStateEvent()


    class None : SearchStateEvent()

}
