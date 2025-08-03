package com.example.rre.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rre.repositories.UsuarioRepository
import com.example.rre.room.entities.UsuarioEntity
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: UsuarioRepository) : ViewModel() {

    val nombre = MutableLiveData<String>()
    val rut = MutableLiveData<String>()
    val correo = MutableLiveData<String>()
    val telefono = MutableLiveData<String>()
    val contrasena = MutableLiveData<String>()

    private val _registroExitoso = MutableLiveData<Boolean>()
    val registroExitoso: LiveData<Boolean> get() = _registroExitoso

    private val _errorMensaje = MutableLiveData<String?>()
    val errorMensaje: LiveData<String?> get() = _errorMensaje

    fun registrarUsuario() {
        val nombreVal = nombre.value.orEmpty()
        val rutVal = rut.value.orEmpty()
        val correoVal = correo.value.orEmpty()
        val telefonoVal = telefono.value.orEmpty()
        val contrasenaVal = contrasena.value.orEmpty()

        if (nombreVal.isBlank() || rutVal.isBlank() || correoVal.isBlank() || telefonoVal.isBlank() || contrasenaVal.isBlank()) {
            _errorMensaje.value = "Todos los campos son obligatorios"
            return
        }

        if (!isEmailValido(correoVal)) {
            _errorMensaje.value = "Correo no válido"
            return
        }

        if (!isRutValido(rutVal)) {
            _errorMensaje.value = "RUT no válido"
            return
        }

        viewModelScope.launch {
            try {
                val nuevoUsuario = UsuarioEntity(
                    nombre = nombreVal,
                    rut = rutVal,
                    correo = correoVal,
                    telefono = telefonoVal,
                    contrasena = contrasenaVal,
                    rol = "usuario"
                )
                repository.insertar(nuevoUsuario)
                _registroExitoso.postValue(true)
            } catch (e: Exception) {
                _errorMensaje.postValue("Error en registro: ${e.message}")
            }
        }
    }

    private fun isEmailValido(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isRutValido(rut: String): Boolean {
        return rut.matches(Regex("[0-9]{7,8}-[0-9kK]"))
    }

    fun limpiarError() {
        _errorMensaje.value = null
    }
}
