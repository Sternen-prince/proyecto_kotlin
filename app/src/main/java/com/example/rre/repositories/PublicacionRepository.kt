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
        // AVISO: La conversión a 'Publicacion' ahora necesita el nombre del autor.
        // Esto debería manejarse en el ViewModel. Por ahora, el nombre del autor quedará en blanco.
        return publicacionDao.getAllPublicacionesSync().map { entity ->
            convertirEntityAPublicacion(entity)
        }
    }

    // --- CORREGIDO: Busca por ID de usuario y devuelve LiveData ---
    fun obtenerPublicacionesPorUsuarioId(autorId: Int): LiveData<List<PublicacionEntity>> {
        return publicacionDao.getPublicacionesPorUsuarioId(autorId)
    }

    // --- CORREGIDO: Versión síncrona que busca por ID de usuario ---
    suspend fun obtenerPublicacionesPorUsuarioIdSync(autorId: Int): List<Publicacion> {
        return publicacionDao.getPublicacionesPorUsuarioIdSync(autorId).map { entity ->
            convertirEntityAPublicacion(entity)
        }
    }

    // --- CORREGIDO: Ahora recibe el autorId (Int) en lugar del nombre (String) ---
    suspend fun crearPublicacion(
        titulo: String,
        tipoAviso: String,
        descripcion: String,
        lugar: String,
        imagenUri: String,
        autorId: Int // <-- CAMBIO CLAVE
    ): Long {
        val fecha = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(Date())

        val publicacion = PublicacionEntity(
            titulo = titulo,
            tipoAviso = tipoAviso,
            descripcion = descripcion,
            lugar = lugar,
            imagenUri = imagenUri,
            autorId = autorId, // <-- CAMBIO CLAVE
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

    // --- CORREGIDO: Función de conversión ---
    private fun convertirEntityAPublicacion(entity: PublicacionEntity): Publicacion {
        return Publicacion(
            titulo = entity.titulo,
            descripcion = entity.descripcion,
            tipoAviso = entity.tipoAviso,
            fecha = entity.fecha,
            lugar = entity.lugar,
            // AVISO IMPORTANTE:
            // La entidad ya no guarda el nombre del autor, solo su ID.
            // Para mostrar el nombre en la UI, deberás buscarlo en el ViewModel usando el 'autorId'.
            // Por ahora, lo dejamos como "Autor Desconocido" para que compile.
            autor = "Autor (ID: ${entity.autorId})",
            photo = entity.imagenUri
        )
    }
}