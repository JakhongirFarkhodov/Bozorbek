package com.example.bozorbek_vol2.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException
import javax.inject.Inject
import javax.inject.Provider


class ViewModelProviderFactory @Inject constructor(val viewModels: MutableMap<Class<out ViewModel>, Provider<ViewModel>>) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val viewModel = viewModels[modelClass]
            ?: throw IllegalArgumentException("modelClass: ${modelClass} not found")

        return viewModel.get() as T
    }
}