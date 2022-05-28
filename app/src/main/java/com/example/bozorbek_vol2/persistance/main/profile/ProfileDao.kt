package com.example.bozorbek_vol2.persistance.main.profile

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bozorbek_vol2.model.main.profile.Profile

@Dao
interface ProfileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfileData(profile: Profile):Long

    @Query("SELECT * FROM profile_table")
    fun getProfileData():LiveData<Profile>?

}