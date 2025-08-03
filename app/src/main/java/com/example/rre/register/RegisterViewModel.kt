package com.example.rre.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rre.repositories.UsuarioRepository
import com.example.rre.room.entities.UsuarioEntity
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class RegisterViewModel(private val repository: UsuarioRepository) : ViewModel() {

    // Tus variables se mantienen igual
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
        val nombreVal = nombre.value.orEmpty().trim()
        val rutVal = rut.value.orEmpty().trim()
        val correoVal = correo.value.orEmpty().trim()
        val telefonoVal = telefono.value.orEmpty().trim()
        val contrasenaVal = contrasena.value.orEmpty()

        // --- Validaciones ---
        if (nombreVal.isBlank() || rutVal.isBlank() || correoVal.isBlank() || telefonoVal.isBlank() || contrasenaVal.isBlank()) {
            _errorMensaje.value = "Todos los campos son obligatorios"
            return
        }
        if (!isNombreValido(nombreVal)) {
            _errorMensaje.value = "El nombre solo debe contener letras y espacios"
            return
        }
        if (!isRutValido(rutVal)) {
            _errorMensaje.value = "El RUT ingresado no es válido"
            return
        }
        if (!isEmailValido(correoVal)) {
            _errorMensaje.value = "El formato del correo electrónico no es válido"
            return
        }
        if (!isTelefonoValido(telefonoVal)) {
            _errorMensaje.value = "El teléfono debe tener 9 dígitos y comenzar con 9"
            return
        }
        if (contrasenaVal.length < 6) {
            _errorMensaje.value = "La contraseña debe tener al menos 6 caracteres"
            return
        }

        viewModelScope.launch {
            try {
                // Verificación de existencia usando tus métodos del repositorio
                if (repository.obtenerPorCorreo(correoVal) != null) {
                    _errorMensaje.postValue("El correo electrónico ya está registrado")
                    return@launch
                }
                if (repository.obtenerPorRut(rutVal) != null) {
                    _errorMensaje.postValue("El RUT ya está registrado")
                    return@launch
                }

                val nuevoUsuario = UsuarioEntity(
                    nombre = nombreVal,
                    rut = rutVal,
                    correo = correoVal,
                    telefono = telefonoVal,
                    contrasena = contrasenaVal,
                    rol = "usuario"
                )
                // Inserción usando tu método del repositorio
                repository.insertar(nuevoUsuario)
                _registroExitoso.postValue(true)

            } catch (e: Exception) {
                _errorMensaje.postValue("Error en el registro: ${e.message}")
            }
        }
    }

    // --- Funciones de validación ---
    private fun isNombreValido(nombre: String): Boolean {
        return Pattern.compile("^[\\p{L} ]+\$").matcher(nombre).matches()
    }

    private fun isEmailValido(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isTelefonoValido(telefono: String): Boolean {
        return telefono.matches(Regex("^9[0-9]{8}\$"))
    }

    private fun isRutValido(rut: String): Boolean {
        if (!rut.matches(Regex("^[0-9]{7,8}-[0-9kK]\$"))) return false
        try {
            val rutSinGuion = rut.replace("-", "").lowercase()
            val cuerpo = rutSinGuion.substring(0, rutSinGuion.length - 1)
            val dv = rutSinGuion.substring(rutSinGuion.length - 1)
            var suma = 0
            var multiplo = 2
            for (i in cuerpo.length - 1 downTo 0) {
                suma += Integer.parseInt(cuerpo[i].toString()) * multiplo
                if (multiplo == 7) multiplo = 2 else multiplo++
            }
            val dvEsperado = 11 - (suma % 11)
            val dvCalculado = when (dvEsperado) {
                11 -> "0"
                10 -> "k"
                else -> dvEsperado.toString()
            }
            return dv == dvCalculado
        } catch (e: Exception) {
            return false
        }
    }

    fun limpiarError() {
        _errorMensaje.value = null
    }
}
