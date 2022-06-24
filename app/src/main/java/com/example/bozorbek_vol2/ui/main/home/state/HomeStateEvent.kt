package com.example.bozorbek_vol2.ui.main.home.state

sealed class HomeStateEvent {

    class GetHomeSliderImage : HomeStateEvent()

    class GetRandomProducts : HomeStateEvent()

    class GetDiscountProducts : HomeStateEvent()

    class None : HomeStateEvent()

}