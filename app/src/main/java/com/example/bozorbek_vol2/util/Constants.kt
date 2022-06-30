package com.example.bozorbek_vol2.util

class Constants {

    companion object{
        const val LOG = "AppDebug"

        const val BASE_URL = "https://bazarbek.uz:8000"

        const val GALLERY_REQUEST_CODE:Int = 201
        const val PERMISSION_REQUEST_READ_STORAGE:Int = 301
        const val CROP_IMAGE_INTENT_CODE:Int = 401

        const val  HEADER_CACHE_CONTROL:String = "Cache-Control"
        const val HEADER_PRAGMA:String = "Pragma"
        const val cacheSize:Long = 5 * 1024 * 1024
    }

}