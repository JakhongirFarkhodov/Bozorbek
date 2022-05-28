package com.example.bozorbek_vol2.di.main

import androidx.lifecycle.ViewModel
import com.example.bozorbek_vol2.di.ViewModelKey
import com.example.bozorbek_vol2.ui.main.basket.viewmodel.BasketViewModel
import com.example.bozorbek_vol2.ui.main.catalog.viewmodel.CatalogViewModel
import com.example.bozorbek_vol2.ui.main.profile.viewmodel.ProfileViewModel
import com.example.bozorbek_vol2.ui.main.search.viewmodel.SearchViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(CatalogViewModel::class)
    abstract fun bindsCatalogViewModel(catalogViewModel: CatalogViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun bindsSearchViewModel(searchViewModel: SearchViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BasketViewModel::class)
    abstract fun bindsBasketViewModel(basketViewModel: BasketViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    abstract fun bindsProfileViewModel(profileViewModel: ProfileViewModel):ViewModel

}