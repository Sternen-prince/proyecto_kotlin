package com.example.rre.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rre.entidades.Publicacion
import com.example.rre.PublicacionProvider

class HomeViewModel : ViewModel() {

    private val _publicacion = MutableLiveData<List<Publicacion>>()
    val publicaciones: LiveData<List<Publicacion>> = _publicacion

    init {
        _publicacion.value = PublicacionProvider.publicacionLista
    }
}