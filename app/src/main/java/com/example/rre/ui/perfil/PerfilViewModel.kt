package com.example.rre.ui.perfil

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.rre.entidades.Publicacion
import com.example.rre.repositories.PublicacionRepository
import com.example.rre.repositories.UsuarioRepository
import com.example.rre.room.entities.UsuarioEntity
import kotlinx.coroutines.launch

// --- VIEWMODEL CORREGIDO ---
class PerfilViewModel(
    private val usuarioRepository: UsuarioRepository, // Lo mantenemos por si lo necesitas para futuras funciones
    private val publicacionRepository: PublicacionRepository,
    // --- CAMBIO 1: Recibe el objeto UsuarioEntity completo, no un correo ---
    private val usuario: UsuarioEntity
) : ViewModel() {

    private val _publicaciones = MutableLiveData<List<Publicacion>>()
    val publicaciones: LiveData<List<Publicacion>> = _publicaciones

    // --- CAMBIO 2: Los datos del perfil se pueden establecer directamente desde el objeto usuario ---
    val nombre: LiveData<String> = MutableLiveData(usuario.nombre)
    val correo: LiveData<String> = MutableLiveData(usuario.correo)
    val telefono: LiveData<String> = MutableLiveData(usuario.telefono)

    init {
        // La función de inicialización ahora es más simple
        cargarPublicacionesDelUsuario()
    }

    // --- CAMBIO 3: La función de carga ha sido completamente reescrita ---
    private fun cargarPublicacionesDelUsuario() {
        viewModelScope.launch {
            try {
                // Ya no necesitamos buscar al usuario. Usamos su ID directamente.
                Log.d("PerfilViewModel", "Cargando publicaciones para usuario ID: ${usuario.userId}")

                // Llamamos a la nueva función del repositorio que busca por ID.
                val publicacionesDeUsuario = publicacionRepository.obtenerPublicacionesPorUsuarioIdSync(usuario.userId)

                _publicaciones.value = publicacionesDeUsuario

                Log.d("PerfilViewModel", "Se encontraron ${publicacionesDeUsuario.size} publicaciones.")

            } catch (e: Exception) {
                // Manejar cualquier posible error durante la carga
                Log.e("PerfilViewModel", "Error al cargar las publicaciones", e)
                _publicaciones.value = emptyList() // En caso de error, mostramos una lista vacía
            }
        }
    }
}


// --- FACTORY CORREGIDA ---
class PerfilViewModelFactory(
    private val usuarioRepository: UsuarioRepository,
    private val publicacionRepository: PublicacionRepository,
    // El parámetro ahora es el objeto completo, como debe ser
    private val usuario: UsuarioEntity
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PerfilViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PerfilViewModel(usuarioRepository, publicacionRepository, usuario) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}