package com.example.rre.ui.perfil

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.rre.entidades.Publicacion
import com.example.rre.PublicacionProvider
import com.example.rre.repositories.UsuarioRepository
import kotlinx.coroutines.launch

class PerfilViewModel(
    private val usuarioRepository: UsuarioRepository,
    private val correoUsuario: String
) : ViewModel() {

    private val _publicaciones = MutableLiveData<List<Publicacion>>()
    val publicaciones: LiveData<List<Publicacion>> = _publicaciones

    private val _nombre = MutableLiveData<String>()
    val nombre: LiveData<String> = _nombre

    private val _correo = MutableLiveData<String>()
    val correo: LiveData<String> = _correo

    private val _telefono = MutableLiveData<String>()
    val telefono: LiveData<String> = _telefono

    init {
        cargarUsuarioActualYPublicaciones()
    }

    private fun cargarUsuarioActualYPublicaciones() {
        viewModelScope.launch {
            Log.d("PerfilViewModel", "correoUsuario recibido: $correoUsuario")
            val usuario = usuarioRepository.obtenerPorCorreo(correoUsuario)
            Log.d("PerfilViewModel", "Usuario: $usuario")
            if (usuario != null) {
                _nombre.value = usuario.nombre
                _correo.value = usuario.correo
                _telefono.value = usuario.telefono

                val todasPublicaciones = PublicacionProvider.publicacionLista
                _publicaciones.value = todasPublicaciones.filter { it.autor == usuario.nombre }
            } else {
                _nombre.value = "Desconocido"
                _correo.value = "sin.correo@desconocido.com"
                _telefono.value = ""
                _publicaciones.value = emptyList()
            }
        }
    }
}

class PerfilViewModelFactory(
    private val usuarioRepository: UsuarioRepository,
    private val correoUsuario: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PerfilViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PerfilViewModel(usuarioRepository, correoUsuario) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}