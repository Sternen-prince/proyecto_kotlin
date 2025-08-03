package com.example.rre.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rre.entidades.Publicacion
import com.example.rre.repositories.PublicacionRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val publicacionRepository: PublicacionRepository) : ViewModel() {

    private val _publicaciones = MutableLiveData<List<Publicacion>>()
    val publicaciones: LiveData<List<Publicacion>> = _publicaciones

    init {
        cargarPublicaciones()
    }

    private fun cargarPublicaciones() {
        viewModelScope.launch {
            try {
                // Cargar publicaciones desde la base de datos únicamente
                val publicacionesDB = publicacionRepository.obtenerTodasLasPublicaciones()
                _publicaciones.value = publicacionesDB
            } catch (e: Exception) {
                // En caso de error, mostrar lista vacía
                _publicaciones.value = emptyList()
            }
        }
    }

    fun actualizarPublicaciones() {
        cargarPublicaciones()
    }
}