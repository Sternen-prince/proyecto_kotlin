package com.example.rre

import com.example.rre.entidades.Publicacion

class PublicacionProvider {
    companion object{
        val publicacionLista = mutableListOf<Publicacion>(
            // Las publicaciones se agregarán dinámicamente cuando los usuarios las creen
        )
    }
}