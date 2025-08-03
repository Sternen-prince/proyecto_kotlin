package com.example.rre.ui.nueva_publicacion

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rre.PublicacionProvider
import com.example.rre.entidades.Publicacion
import com.example.rre.repositories.NotificacionRepository
import com.example.rre.repositories.PublicacionRepository
import kotlinx.coroutines.launch

data class ValidationResult(val isValid: Boolean, val errorMessage: String? = null)

class PublicacionViewModel(
    private val notificacionRepository: NotificacionRepository? = null,
    private val publicacionRepository: PublicacionRepository? = null
) : ViewModel() {

    private val _titulo = MutableLiveData<String>()
    val titulo: LiveData<String> = _titulo

    private val _tipoAviso = MutableLiveData<String>()
    val tipoAviso: LiveData<String> = _tipoAviso

    private val _descripcion = MutableLiveData<String>()
    val descripcion: LiveData<String> = _descripcion

    private val _lugar = MutableLiveData<String>()
    val lugar: LiveData<String> = _lugar

    private val _imagenUri = MutableLiveData<String>()
    val imagenUri: LiveData<String> = _imagenUri

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _publicacionExitosa = MutableLiveData<Boolean>()
    val publicacionExitosa: LiveData<Boolean> = _publicacionExitosa

    // Actualizadores
    fun actualizarTitulo(nuevoTitulo: String) { _titulo.value = nuevoTitulo }
    fun actualizarTipoAviso(nuevoTipo: String) { _tipoAviso.value = nuevoTipo }
    fun actualizarDescripcion(nuevaDescripcion: String) { _descripcion.value = nuevaDescripcion }
    fun actualizarLugar(nuevoLugar: String) { _lugar.value = nuevoLugar }
    fun actualizarImagen(uri: String?) { _imagenUri.value = uri ?: "" }

    // Validaciones
    fun validarTitulo(titulo: String) = when {
        titulo.isBlank() -> ValidationResult(false, "El título es obligatorio")
        titulo.length < 3 -> ValidationResult(false, "El título debe tener al menos 3 caracteres")
        else -> ValidationResult(true)
    }

    fun validarTipoAviso(tipo: String) = when {
        tipo.isBlank() -> ValidationResult(false, "El tipo de aviso es obligatorio")
        tipo.length < 3 -> ValidationResult(false, "El tipo debe tener al menos 3 caracteres")
        else -> ValidationResult(true)
    }

    fun validarDescripcion(descripcion: String) = when {
        descripcion.isBlank() -> ValidationResult(false, "La descripción es obligatoria")
        descripcion.length < 10 -> ValidationResult(false, "Debe tener al menos 10 caracteres")
        else -> ValidationResult(true)
    }

    fun validarLugar(lugar: String) = when {
        lugar.isBlank() -> ValidationResult(false, "El lugar es obligatorio")
        lugar.length < 2 -> ValidationResult(false, "El lugar debe tener al menos 2 caracteres")
        else -> ValidationResult(true)
    }

    fun validarFormularioCompleto(): ValidationResult {
        validarTitulo(_titulo.value ?: "").takeIf { !it.isValid }?.let { return it }
        validarTipoAviso(_tipoAviso.value ?: "").takeIf { !it.isValid }?.let { return it }
        validarDescripcion(_descripcion.value ?: "").takeIf { !it.isValid }?.let { return it }
        validarLugar(_lugar.value ?: "").takeIf { !it.isValid }?.let { return it }
        return ValidationResult(true)
    }

    fun publicarAviso(autorId: Int, autorNombre: String) {
        _isLoading.value = true

        val publicacion = Publicacion(
            titulo = _titulo.value ?: "",
            tipoAviso = _tipoAviso.value ?: "",
            descripcion = _descripcion.value ?: "",
            lugar = _lugar.value ?: "",
            autor = autorNombre, // El objeto de UI puede seguir usando el nombre
            fecha = obtenerFechaActual(),
            photo = _imagenUri.value ?: ""
        )

        if (publicacionRepository != null) {
            // Pasamos ambos datos a la función que guarda
            guardarEnBaseDatos(publicacion, autorId, autorNombre)
        } else {
            simularGuardado(publicacion)
        }
    }

    // --- CAMBIO CLAVE 2: Esta función ahora recibe el autorId (Int) ---
    private fun guardarEnBaseDatos(publicacion: Publicacion, autorId: Int, autorNombre: String) {
        viewModelScope.launch {
            try {
                // --- CAMBIO CLAVE 3: La llamada al repositorio ahora usa 'autorId' ---
                val publicacionId = publicacionRepository!!.crearPublicacion(
                    titulo = publicacion.titulo,
                    tipoAviso = publicacion.tipoAviso,
                    descripcion = publicacion.descripcion,
                    lugar = publicacion.lugar,
                    imagenUri = publicacion.photo,
                    autorId = autorId // <-- ¡LA LÍNEA CORREGIDA!
                )

                notificacionRepository?.crearNotificacionNuevaPublicacion(
                    autorPublicacion = autorNombre, // La notificación usa el nombre
                    tituloPublicacion = publicacion.titulo
                )

                _isLoading.value = false
                _publicacionExitosa.value = true
                limpiarFormulario()

            } catch (e: Exception) {
                _isLoading.value = false
                _errorMessage.value = "Error al guardar publicación: ${e.message}"
            }
        }
    }

    private fun simularGuardado(publicacion: Publicacion) {
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            PublicacionProvider.publicacionLista.add(publicacion)
            
            // Crear notificación cuando se guarde la publicación
            notificacionRepository?.let { repository ->
                viewModelScope.launch {
                    repository.crearNotificacionNuevaPublicacion(
                        autorPublicacion = publicacion.autor,
                        tituloPublicacion = publicacion.titulo
                    )
                }
            }
            
            _isLoading.value = false
            _publicacionExitosa.value = true
            limpiarFormulario()
        }, 1000)
    }

    private fun obtenerFechaActual(): String {
        val sdf = java.text.SimpleDateFormat("dd-MM-yyyy")
        return sdf.format(java.util.Date())
    }

    fun limpiarFormulario() {
        _titulo.value = ""
        _tipoAviso.value = ""
        _descripcion.value = ""
        _lugar.value = ""
        _imagenUri.value = ""
        _errorMessage.value = null
        _publicacionExitosa.value = false
    }

    fun resetearEstadoExito() {
        _publicacionExitosa.value = false
    }

    fun limpiarError() {
        _errorMessage.value = null
    }
}
