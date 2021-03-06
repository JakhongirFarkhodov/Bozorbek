package com.example.bozorbek_vol2.util

import android.util.Log
import retrofit2.Response

sealed class GenericApiResponse<T> {

    companion object {
        private const val TAG = Constants.LOG

        fun <T> create(error: Throwable): ApiErrorResponse<T> {
            Log.d(TAG, "error create: ${error.message}")
            return ApiErrorResponse(error_message = error.message ?: "Unknown error")
        }

        fun <T> create(response: Response<T>) : GenericApiResponse<T>
        {
            Log.d(TAG, "GenericApiResponse: response: ${response}")
            Log.d(TAG, "GenericApiResponse: raw: ${response.raw()}")
            Log.d(TAG, "GenericApiResponse: header: ${response.headers()}")
            Log.d(TAG, "GenericApiResponse: message: ${response.message()}")

            if (response.isSuccessful)
            {
                val body = response.body()
                Log.d(TAG, "response: ${response.errorBody()}")
                return if (body == null || response.code() == 204)
                {
                    ApiEmptyResponse()
                }
                else if (body.equals(ErrorHandling.RESPONSE_204))
                {
                    ApiSuccessResponse(body)
                }
                else if (response.code() == 401)
                {
                    ApiErrorResponse(error_message = "401 Unauthorized. Token may be invalid.")
                }
                else{
                    ApiSuccessResponse(body = body)
                }
            }
            else{
                val msg = response.errorBody()?.string()
                Log.d(TAG, "error body: ")
                val errorMsg = if (msg.isNullOrEmpty()) {
                    response.message()
                } else {
                    msg
                }
                return ApiErrorResponse(
                    errorMsg ?: "unknown error"
                )
            }

        }
    }

}

class ApiEmptyResponse<T> : GenericApiResponse<T>()

data class ApiSuccessResponse<T>(val body: T) : GenericApiResponse<T>()

data class ApiErrorResponse<T>(val error_message: String) : GenericApiResponse<T>()
