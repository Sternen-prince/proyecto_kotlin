package com.example.rre.room.DataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.rre.room.daos.NotificacionDao
import com.example.rre.room.daos.UsuarioDao
import com.example.rre.room.entities.NotificacionEntity
import com.example.rre.room.entities.UsuarioEntity

@Database(entities = [UsuarioEntity::class, NotificacionEntity::class], version = 3)
abstract class RREDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun notificacionDao(): NotificacionDao

    companion object {
        @Volatile
        private var INSTANCE: RREDatabase? = null

        fun getInstance(context: Context): RREDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RREDatabase::class.java,
                    "app_database"
                ).fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}