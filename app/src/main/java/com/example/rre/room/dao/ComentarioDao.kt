package com.example.rre.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.rre.room.entities.ComentarioEntity

@Dao
interface ComentarioDao {
    
    @Query("SELECT * FROM comentarios WHERE notificacionId = :notificacionId ORDER BY fechaComentario DESC")
    fun getComentariosPorNotificacion(notificacionId: Int): LiveData<List<ComentarioEntity>>
    
    @Query("SELECT * FROM comentarios WHERE notificacionId = :notificacionId ORDER BY fechaComentario DESC")
    suspend fun getComentariosPorNotificacionSync(notificacionId: Int): List<ComentarioEntity>
    
    @Insert
    suspend fun insertComentario(comentario: ComentarioEntity): Long
    
    @Delete
    suspend fun deleteComentario(comentario: ComentarioEntity)
    
    @Query("DELETE FROM comentarios WHERE notificacionId = :notificacionId")
    suspend fun deleteComentariosPorNotificacion(notificacionId: Int)
}
