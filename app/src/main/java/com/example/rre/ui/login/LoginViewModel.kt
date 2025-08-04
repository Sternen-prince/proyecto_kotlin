package com.example.rre.ui.login

import androidx.lifecycle.*
import com.example.rre.repositories.UsuarioRepository
import com.example.rre.room.entities.UsuarioEntity
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UsuarioRepository) : ViewModel() {


    val username = MutableLiveData<String>()
    val password = MutableLiveData<String>()


    private val _usuarioLogeado = MutableLiveData<UsuarioEntity?>()
    val usuarioLogeado: LiveData<UsuarioEntity?> get() = _usuarioLogeado

    private val _errorMensaje = MutableLiveData<String?>()
    val errorMensaje: LiveData<String?> get() = _errorMensaje



    fun login() {
        val correo = username.value ?: ""
        val contrasena = password.value ?: ""

        if (correo.isBlank() || contrasena.isBlank()) {
            _usuarioLogeado.postValue(null)
            return
        }

        viewModelScope.launch {
            val usuario = repository.login(correo, contrasena)
            // Publicamos el resultado en nuestra nueva LiveData.
            // Si 'usuario' no es nulo, es un éxito. Si es nulo, es un fallo.
            _usuarioLogeado.postValue(usuario)
        }
    }
    fun onMensajeErrorMostrado() {
        _errorMensaje.value = null
    }
}


// La ViewModelFactory se queda exactamente igual, está perfecta.
class LoginViewModelFactory(private val repository: UsuarioRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
