package com.example.bozorbek_vol2.ui.main.search.state

sealed class SearchStateEvent{

    class CheckPreviousAuthUser : SearchStateEvent()

    data class SearchProductEvent(val query:String):SearchStateEvent()

    class None : SearchStateEvent()

}
