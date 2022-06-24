package com.example.bozorbek_vol2.util

class ErrorHandling {

        companion object{

            const val RESPONSE_NULL = "Error unknown"
            const val RESPONSE_UNABLE_TO_RESOLVE_HOST = "Unable to resolve host"
            const val RESPONSE_UNABLE_TODO_OPERATION_WO_INTERNET = "Can't do that operation without an internet connection"

            //Registration part errors
            const val RESPONSE_PHONE_NUMBER_IS_NOT_VALID = "{\"phone_num\":[\"Enter a valid phone number.\"]}"
            const val RESPONSE_SMS_CODE_IS_NOT_CORRECT = "{\"message\":\"SMS code is incorrect\"}"
            const val RESPONSE_ACCOUNT_NOT_FOUND = "{\"detail\":\"No active account found with the given credentials\"}"
            const val RESPONSE_USER_EXISTS = "A user with that username already exists."
            const val RESPONSE_PHONE_NUMBER_SHOULD_BE_UNIQUE = "This field must be unique."
            const val RESPONSE_PHONE_NUMBER_EXISTS = "{\"message\":\"Phone number already registered.\"}"
            const val RESPONSE_INTERNAL_SERVER_ERROR_500 = "Internal Server Error"
            const val RESPONSE_UNAUTHORIZED_401 = "No active account found with the given credentials"
            const val RESPONSE_BAD_REQUEST_400 = "Bad Request"
            const val RESPONSE_NOT_FOUND_404 = "Not Found"
            const val RESPONSE_UNEXPECTED_STATUS_LINE = "Unexpected status line: {\"detail\":\"Success\"}HTTP/1.1 200 OK)"
            const val RESPONSE_204 = "HTTP 204 had non-zero Content-Length: 20"

            fun isNetworkError(msg: String): Boolean{
                when{
                    msg.contains(RESPONSE_UNABLE_TO_RESOLVE_HOST) -> return true
                    else-> return false
                }
            }
        }

}