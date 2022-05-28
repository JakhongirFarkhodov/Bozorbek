package com.example.bozorbek_vol2.persistance.auth

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bozorbek_vol2.model.auth.AccountProperties

@Dao
interface AccountPropertiesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(accountProperties: AccountProperties):Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnore(accountProperties: AccountProperties):Long

    @Query("SELECT * FROM account_properties")
    fun getAccountProperties(): LiveData<AccountProperties>?

    @Query("SELECT * FROM account_properties")
    fun accountProperties():AccountProperties?

    @Query("SELECT * FROM account_properties WHERE phone_number ==:phone_number")
    fun searchByPhoneNumber(phone_number:String):LiveData<AccountProperties>?

    @Query("SELECT * FROM account_properties WHERE username ==:username")
    fun searchByUsername(username:String):AccountProperties?
}