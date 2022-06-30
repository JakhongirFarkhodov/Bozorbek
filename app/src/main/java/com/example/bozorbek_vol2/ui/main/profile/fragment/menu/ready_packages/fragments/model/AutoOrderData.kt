package com.example.bozorbek_vol2.ui.main.profile.fragment.menu.ready_packages.fragments.model

data class AutoOrderData(
    val auto_order:AutoOrder,
    val categoryAutoOrderData: List<CategoryAutoOrderData> = ArrayList()
)
data class AutoOrder(
    val package_id:Int,

    val package_name:String,

    val author:Int,

    val visibility:Boolean,

    val total_cost:Int,

    val items_count:Int
)

data class CategoryAutoOrderData(

    val category_id:Int,

    val category_name:String,

    val get_absolute_url:String,

    val get_image:String,

    val slug:String
)
