package com.zuzaser.dailypalette.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.zuzaser.dailypalette.model.PaletteModel

@Dao
interface PaletteDao {
    @Insert
    suspend fun add(paletteModel: PaletteModel)

    @Query("SELECT * FROM palettes")
    fun getAllPalettes(): List<PaletteModel>
}