package com.example.bozorbek_vol2.persistance.auth

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bozorbek_vol2.model.auth.AuthToken

@Dao
interface AuthTokenDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuthToken(authToken: AuthToken):Long

    @Query("UPDATE auth_token SET access_token = null WHERE account_phone_number ==:account_phone_number")
    fun nullAccessToken(account_phone_number:String):Int

    @Query("SELECT * FROM auth_token")
    fun getAuthToken(): LiveData<AuthToken>?

    @Query("SELECT * FROM auth_token WHERE account_phone_number ==:phone_number")
    fun searchByPhoneNumber(phone_number:String):LiveData<AuthToken>?

}