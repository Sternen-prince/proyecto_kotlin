package com.example.rre.room.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.rre.room.entities.NotificacionEntity

@Dao
interface NotificacionDao {
    
    @Query("SELECT * FROM notificaciones ORDER BY fecha DESC")
    fun getAllNotificaciones(): LiveData<List<NotificacionEntity>>
    
    @Query("SELECT * FROM notificaciones WHERE leida = 0 ORDER BY fecha DESC")
    fun getNotificacionesNoLeidas(): LiveData<List<NotificacionEntity>>
    
    @Insert
    suspend fun insertNotificacion(notificacion: NotificacionEntity)
    
    @Update
    suspend fun updateNotificacion(notificacion: NotificacionEntity)
    
    @Query("UPDATE notificaciones SET leida = 1 WHERE id = :id")
    suspend fun marcarComoLeida(id: Int)
    
    @Query("DELETE FROM notificaciones WHERE id = :id")
    suspend fun deleteNotificacion(id: Int)
    
    @Query("SELECT COUNT(*) FROM notificaciones WHERE leida = 0")
    suspend fun contarNotificacionesNoLeidas(): Int
}
