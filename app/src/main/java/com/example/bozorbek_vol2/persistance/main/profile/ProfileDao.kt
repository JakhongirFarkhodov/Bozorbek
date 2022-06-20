package com.example.bozorbek_vol2.persistance.main.profile

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bozorbek_vol2.model.main.profile.*

@Dao
interface ProfileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfileData(profile: Profile):Long

    @Query("SELECT * FROM profile_table")
    fun getProfileData():LiveData<Profile>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfileReadyPackages(profileReadyPackages: ProfileReadyPackages):Long

    @Query("SELECT * FROM profile_ready_packages")
    fun getProfileReadyPackages():LiveData<List<ProfileReadyPackages>>?

    @Query("DELETE FROM profile_ready_packages")
    fun deleteProfileReadyPackages()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllActiveOrHistoryOrderItem(profileActiveOrHistoryOrder: ProfileActiveOrHistoryOrder):Long

    @Query("SELECT * FROM profile_active_or_history_order")
    fun getAllActiveOrHistoryOrderItem():LiveData<List<ProfileActiveOrHistoryOrder>>?

    @Query("""
        SELECT * FROM profile_active_or_history_order 
        WHERE status LIKE '%' || :DELIVERED || '%' OR '%' || :CANCELLED || '%'
    """)
    fun getHistoryOrderData(DELIVERED:String, CANCELLED:String):LiveData<List<ProfileActiveOrHistoryOrder>>?

    @Query("""
        SELECT * FROM profile_active_or_history_order
        WHERE status LIKE 
        '%' || :UNAPPROVED || '%' OR
        '%' || :APPROVED || '%' OR
        '%' || :COLLECTING || '%' OR
        '%' || :COLLECTED || '%' OR
        '%' || :DELIVERING || '%'
    """)
    fun getActiveOrderData(
        UNAPPROVED:String,
        APPROVED:String,
        COLLECTING:String,
        COLLECTED:String,
        DELIVERING:String
    ):LiveData<List<ProfileActiveOrHistoryOrder>>?

    @Query("DELETE FROM profile_active_or_history_order")
    fun deleteAllActiveOrHistoryOrderItem()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(profileNotification: ProfileNotification):Long

    @Query("SELECT * FROM notification_table")
    fun getAllNotification():LiveData<List<ProfileNotification>>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfileReadyPackageId(profileReadyPackageId: ProfileReadyPackageId):Long

    @Query("SELECT * FROM profile_ready_package_id")
    fun getProfileReadyPackageId():LiveData<List<ProfileReadyPackageId>>?

    @Query("DELETE FROM profile_ready_package_id")
    suspend fun deleteAllProfileReadyPackageId():Int



}