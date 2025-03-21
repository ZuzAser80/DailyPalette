package com.zuzaser.dailypalette.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "palettes")
data class PaletteModel (
    @PrimaryKey var id: Int,
    var color0: String,
    var color1: String,
    var color2: String,
    var color3: String,
    var color4: String,
    var color5: String,
    var name : String
) {
    fun getColors() : List<String> {
        return listOf(color0, color1, color2, color3, color4, color5)
    }
}