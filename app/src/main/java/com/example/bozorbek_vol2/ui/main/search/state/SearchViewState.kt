package com.example.bozorbek_vol2.ui.main.search.state

data class SearchViewState(
    var checkPreviousAuthUser: CheckPreviousAuthUser? = CheckPreviousAuthUser()
)
data class CheckPreviousAuthUser(
    val access_token:String? = null,
    val refresh_token:String? = null,
    val phone_number:String? = null
)