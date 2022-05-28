package com.example.bozorbek_vol2.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.bozorbek_vol2.util.Constants

abstract class BaseViewModel<StateEvent, ViewState> : ViewModel(){

    val TAG = Constants.LOG

    protected var _stateEvent: MutableLiveData<StateEvent> = MutableLiveData()
    protected var _viewState: MutableLiveData<ViewState> = MutableLiveData()

    val viewState: LiveData<ViewState>
        get() = _viewState

    val dataState: LiveData<DataState<ViewState>> = Transformations.switchMap(_stateEvent){ stateEvent ->
        stateEvent?.let { stateEvent ->
            handleStateEvent(stateEvent)
        }
    }

    fun getCurrentViewStateOrCreateNew():ViewState
    {
        val value = _viewState.value?.let {
            it
        }?:initNewViewState()
        return value
    }

    fun setStateEvent(event: StateEvent)
    {
        _stateEvent.value = event!!
    }

    abstract fun initNewViewState(): ViewState

    abstract fun handleStateEvent(stateEvent: StateEvent): LiveData<DataState<ViewState>>


}