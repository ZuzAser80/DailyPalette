package com.zuzaser.dailypalette.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.zuzaser.dailypalette.model.PaletteModel

@Database(entities = [PaletteModel::class], version = 2, exportSchema = false)
abstract class PaletteDatabase : RoomDatabase() {

    abstract fun loginDao() : PaletteDao

    companion object {
        private var INSTANCE: PaletteDatabase? = null
        fun getInstance(context: Context): PaletteDatabase {

            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        PaletteDatabase::class.java,
                        "User_database"

                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}