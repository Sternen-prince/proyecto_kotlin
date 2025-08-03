package com.example.rre.repositories

import androidx.lifecycle.LiveData
import com.example.rre.entidades.Publicacion
import com.example.rre.room.dao.PublicacionDao
import com.example.rre.room.entities.PublicacionEntity
import java.text.SimpleDateFormat
import java.util.*

class PublicacionRepository(private val publicacionDao: PublicacionDao) {

    // Obtener todas las publicaciones como LiveData de Entity
    fun obtenerTodasLasPublicacionesEntity(): LiveData<List<PublicacionEntity>> {
        return publicacionDao.getAllPublicaciones()
    }

    // Obtener todas las publicaciones como objetos Publicacion para UI
    suspend fun obtenerTodasLasPublicaciones(): List<Publicacion> {
        return publicacionDao.getAllPublicacionesSync().map { entity ->
            convertirEntityAPublicacion(entity)
        }
    }

    // Obtener publicaciones por autor
    fun obtenerPublicacionesPorAutor(autor: String): LiveData<List<PublicacionEntity>> {
        return publicacionDao.getPublicacionesPorAutor(autor)
    }

    // Obtener publicaciones por autor como objetos Publicacion
    suspend fun obtenerPublicacionesPorAutorSync(autor: String): List<Publicacion> {
        return publicacionDao.getPublicacionesPorAutorSync(autor).map { entity ->
            convertirEntityAPublicacion(entity)
        }
    }

    // Crear nueva publicación
    suspend fun crearPublicacion(
        titulo: String,
        tipoAviso: String,
        descripcion: String,
        lugar: String,
        imagenUri: String,
        autor: String
    ): Long {
        val fecha = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(Date())
        
        val publicacion = PublicacionEntity(
            titulo = titulo,
            tipoAviso = tipoAviso,
            descripcion = descripcion,
            lugar = lugar,
            imagenUri = imagenUri,
            autor = autor,
            fecha = fecha
        )
        
        return publicacionDao.insertPublicacion(publicacion)
    }

    // Obtener publicación por ID
    suspend fun obtenerPublicacionPorId(id: Int): PublicacionEntity? {
        return publicacionDao.getPublicacionById(id)
    }

    // Actualizar publicación
    suspend fun actualizarPublicacion(publicacion: PublicacionEntity) {
        publicacionDao.updatePublicacion(publicacion)
    }

    // Eliminar publicación
    suspend fun eliminarPublicacion(publicacion: PublicacionEntity) {
        publicacionDao.deletePublicacion(publicacion)
    }

    // Eliminar publicación por ID
    suspend fun eliminarPublicacionPorId(id: Int) {
        publicacionDao.deletePublicacionById(id)
    }

    // Contar publicaciones
    suspend fun contarPublicaciones(): Int {
        return publicacionDao.contarPublicaciones()
    }

    // Convertir PublicacionEntity a Publicacion para uso en UI
    private fun convertirEntityAPublicacion(entity: PublicacionEntity): Publicacion {
        return Publicacion(
            titulo = entity.titulo,
            descripcion = entity.descripcion,
            tipoAviso = entity.tipoAviso,
            fecha = entity.fecha,
            lugar = entity.lugar,
            autor = entity.autor,
            photo = entity.imagenUri
        )
    }
}
