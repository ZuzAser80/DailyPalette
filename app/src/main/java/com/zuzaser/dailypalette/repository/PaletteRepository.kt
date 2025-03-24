package com.zuzaser.dailypalette.repository

import androidx.lifecycle.LiveData
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

    fun removePaletteById(paletteModel: PaletteModel) {
        coroutineScope.launch(Dispatchers.IO) {
            dao.removePalette(paletteModel)
        }
    }

    fun clearTable() {
        coroutineScope.launch(Dispatchers.IO) {
            dao.removeAll()
        }
    }

    val paletteList: LiveData<List<PaletteModel>> = dao.getAllPalettes()
}