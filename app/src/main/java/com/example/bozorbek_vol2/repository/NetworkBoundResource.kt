package com.example.bozorbek_vol2.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.example.bozorbek_vol2.ui.DataState
import com.example.bozorbek_vol2.ui.Response
import com.example.bozorbek_vol2.ui.ResponseType
import com.example.bozorbek_vol2.util.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

abstract class NetworkBoundResource<ResponseObject, CacheObject, ViewStateType> constructor(
    val isNetworkRequest:Boolean,
    val isNetworkAvailable:Boolean,
    val shouldUseCacheObject:Boolean,
    val cancelJobIfNoInternet:Boolean
){

    protected val TAG = Constants.LOG
    private lateinit var job: CompletableJob
    private lateinit var coroutineScope: CoroutineScope
    protected var result:MediatorLiveData<DataState<ViewStateType>> = MediatorLiveData()

    init {
        setJob(initNewJob())
        setValue(dataState = DataState.loading(loading = true, cachedData = null))

        if (shouldUseCacheObject)
        {
            val loadCache = loadFromCache()
            result.addSource(loadCache, Observer { viewState ->
                result.removeSource(loadCache)
                setValue(dataState = DataState.loading(loading = true, cachedData = viewState))
            })
        }

        if (isNetworkRequest)
        {
            if (isNetworkAvailable)
            {
                handleNetworkResponse()
            }
            else{
                if (cancelJobIfNoInternet){
                    onErrorReturn(error_message = ErrorHandling.RESPONSE_UNABLE_TODO_OPERATION_WO_INTERNET, shouldUseToast = false, shouldUseDialog = true)
                }
                else{
                    handleCacheResponse()
                }
            }
        }
        else{
            handleCacheResponse()
        }
    }

    private fun handleNetworkResponse() {
        coroutineScope.launch {
            withContext(Main)
            {
                val request = createCall()
                result.addSource(request, Observer { response ->
                    Log.d(TAG, "handleNetworkResponse Request:${request} ")
                    result.removeSource(request)
                    coroutineScope.launch {
                        Log.d(TAG, "handleNetworkResponse Response: ${response}")
                        handleApiResponse(response)
                    }
                })
            }
        }
    }

    private fun handleCacheResponse() {
        coroutineScope.launch {
            createCacheAndReturn()
        }
    }

    private suspend fun handleApiResponse(response: GenericApiResponse<ResponseObject>?) {
        when(response)
        {
            is ApiSuccessResponse ->{
                handleSuccessResponse(response)
            }

            is ApiErrorResponse ->{
                if (response.error_message.equals(ErrorHandling.RESPONSE_UNEXPECTED_STATUS_LINE) || response.error_message.equals(ErrorHandling.RESPONSE_204))
                {
                    onCompleteJob(dataState = DataState.data(data = null, response = Response(message = "Успешно", responseType = ResponseType.Toast())))
                }
                else{
                    onErrorReturn(error_message = response.error_message, shouldUseToast = false, shouldUseDialog = true)
                }

            }

            is ApiEmptyResponse ->{
                onErrorReturn(error_message = ErrorHandling.RESPONSE_NOT_FOUND_404, shouldUseToast = true, shouldUseDialog = false)
            }

        }
    }


    @OptIn(InternalCoroutinesApi::class)
    fun initNewJob():Job
    {
        job = Job()
        job.invokeOnCompletion(onCancelling = true, invokeImmediately = true, handler = object : CompletionHandler{
            override fun invoke(cause: Throwable?) {
                cause?.let {
                    if (it.equals(ErrorHandling.RESPONSE_204) || it.equals(ErrorHandling.RESPONSE_UNEXPECTED_STATUS_LINE)) {
                        onCompleteJob(dataState = DataState.data(data = null, response = Response(message = it.message, ResponseType.None())))
                    }
                    else{
                        onErrorReturn(error_message = it.message, shouldUseToast = false, shouldUseDialog = true)
                    }
                }
            }
        })

        coroutineScope = CoroutineScope(IO + job)

        return job
    }

    fun onErrorReturn(error_message:String?, shouldUseToast:Boolean, shouldUseDialog:Boolean)
    {
        var msg = error_message
        var responseType:ResponseType = ResponseType.None()

        if (msg == null)
        {
            msg = ErrorHandling.RESPONSE_NULL
        }
        else if ((ErrorHandling.isNetworkError(msg)))
        {
            msg = ErrorHandling.RESPONSE_UNABLE_TODO_OPERATION_WO_INTERNET
        }
        else if (msg.equals(ErrorHandling.RESPONSE_INTERNAL_SERVER_ERROR_500))
        {
            msg = "Данный запрос невозможно обработать сервером."
        }
        else if (msg.equals(ErrorHandling.RESPONSE_UNAUTHORIZED_401))
        {
            msg = "Пользыватель не найден. Пожалуйста убедитесь что вы ввели все корректно."
        }
        else if (msg.equals(ErrorHandling.RESPONSE_PHONE_NUMBER_EXISTS))
        {
            msg = "Данный пользыватель с таким номером уже сушествует"
        }
        else if (msg.equals(ErrorHandling.RESPONSE_PHONE_NUMBER_IS_NOT_VALID))
        {
            msg = "Введите кооректный номер телефона."
        }
        else if (msg.equals(ErrorHandling.RESPONSE_SMS_CODE_IS_NOT_CORRECT))
        {
            msg = "Введенный код неправельный."
        }
        else if (msg.equals(ErrorHandling.RESPONSE_SMS_CODE_IS_NOT_CORRECT))
        {
            msg = "Код подтверждения не правельный."
        }
        else if (msg.equals(ErrorHandling.RESPONSE_ACCOUNT_NOT_FOUND))
        {
            msg = "Аккаунт не найден."
        }



        Log.d(TAG, "onErrorReturn: ${msg}")
        if (shouldUseToast)
        {
            responseType = ResponseType.Toast()
        }
        if (shouldUseDialog)
        {
            responseType = ResponseType.Dialog()
        }

        onCompleteJob(dataState = DataState.error(response = Response(message = msg, responseType = responseType)))
    }

    fun onCompleteJob(dataState: DataState<ViewStateType>) {
        GlobalScope.launch(Main) {
            job.complete()
            setValue(dataState)
        }
    }

    private fun setValue(dataState: DataState<ViewStateType>) {
        result.value = dataState
    }

    fun asLiveData() = result as LiveData<DataState<ViewStateType>>

    abstract suspend fun createCacheAndReturn()
    abstract fun loadFromCache(): LiveData<ViewStateType>
    abstract suspend fun updateCache(cacheObject: CacheObject?)
    abstract suspend fun handleSuccessResponse(response: ApiSuccessResponse<ResponseObject>)
    abstract fun createCall(): LiveData<GenericApiResponse<ResponseObject>>
    abstract fun setJob(job: Job)
}