package com.example.rre.ui.login

import androidx.lifecycle.*
import com.example.rre.repositories.UsuarioRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UsuarioRepository) : ViewModel() {

    val username = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean> get() = _loginSuccess

    fun login() {
        val correo = username.value ?: ""
        val contrasena = password.value ?: ""

        viewModelScope.launch {
            val usuario = repository.login(correo, contrasena)
            _loginSuccess.postValue(usuario != null)
        }
    }
}

// Puedes dejar la factory justo debajo del ViewModel
class LoginViewModelFactory(private val repository: UsuarioRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
