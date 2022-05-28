package com.example.bozorbek_vol2.ui

data class DataState<T>(
    val loading: Loading = Loading(false),
    val data:Data<T>? = null,
    val stateError:Event<StateError>? = null
)
{
    companion object{
        fun <T> error(response: Response):DataState<T>
        {
            return DataState(stateError = Event(content = StateError(response)))
        }

        fun <T> loading(loading: Boolean, cachedData:T?):DataState<T>
        {
            return DataState(loading = Loading(isLoading = loading), data = Data(data = Event.dataEvent(data = cachedData), response = null))
        }

        fun <T> data(data: T?, response: Response?):DataState<T>
        {
            return DataState(data = Data(data = Event.dataEvent(data = data), response = Event.responseEvent(response = response)))
        }
    }
}
