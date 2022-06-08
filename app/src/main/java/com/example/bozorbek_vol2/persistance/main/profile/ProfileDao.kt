package com.example.bozorbek_vol2.persistance.main.profile

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bozorbek_vol2.model.main.profile.Profile
import com.example.bozorbek_vol2.model.main.profile.ProfileReadyPackages

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


}