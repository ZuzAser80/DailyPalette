package com.zuzaser.dailypalette.repository

import com.zuzaser.dailypalette.model.PaletteModel
import com.zuzaser.dailypalette.room.PaletteDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PaletteRepository (private val dao : PaletteDao) {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun addPalette(paletteModel : PaletteModel) {
        coroutineScope.launch(Dispatchers.IO) {
            dao.add(paletteModel)
        }
    }

    fun getAll() : List<PaletteModel> {
        return dao.getAllPalettes()
    }
}