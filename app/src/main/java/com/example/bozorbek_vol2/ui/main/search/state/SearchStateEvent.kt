package com.example.bozorbek_vol2.ui.main.search.state

sealed class SearchStateEvent{

    class CheckPreviousAuthUser : SearchStateEvent()

    class None : SearchStateEvent()

}
