package com.zuzaser.dailypalette.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "palettes")
data class PaletteModel (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val color0: String,
    val color1: String,
    val color2: String,
    val color3: String,
    val color4: String,
    val color5: String,
    val name : String
) {

    fun getColors() : List<String> {
        return listOf(color0, color1, color2, color3, color4, color5)
    }
}