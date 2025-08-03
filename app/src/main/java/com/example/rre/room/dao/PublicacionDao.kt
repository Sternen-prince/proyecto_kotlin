package com.example.rre.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.rre.room.entities.PublicacionEntity

@Dao
interface PublicacionDao {
    
    @Query("SELECT * FROM publicaciones ORDER BY fecha DESC")
    fun getAllPublicaciones(): LiveData<List<PublicacionEntity>>
    
    @Query("SELECT * FROM publicaciones WHERE autor = :autor ORDER BY fecha DESC")
    fun getPublicacionesPorAutor(autor: String): LiveData<List<PublicacionEntity>>
    
    @Query("SELECT * FROM publicaciones WHERE id = :id")
    suspend fun getPublicacionById(id: Int): PublicacionEntity?
    
    @Insert
    suspend fun insertPublicacion(publicacion: PublicacionEntity): Long
    
    @Update
    suspend fun updatePublicacion(publicacion: PublicacionEntity)
    
    @Delete
    suspend fun deletePublicacion(publicacion: PublicacionEntity)
    
    @Query("DELETE FROM publicaciones WHERE id = :id")
    suspend fun deletePublicacionById(id: Int)
    
    @Query("SELECT COUNT(*) FROM publicaciones")
    suspend fun contarPublicaciones(): Int
}
