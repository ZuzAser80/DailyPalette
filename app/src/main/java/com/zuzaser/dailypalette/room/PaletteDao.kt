package com.zuzaser.dailypalette.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.zuzaser.dailypalette.model.PaletteModel

@Dao
interface PaletteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(paletteModel: PaletteModel)

    @Query("SELECT * FROM palettes")
    fun getAllPalettes(): LiveData<List<PaletteModel>>
}