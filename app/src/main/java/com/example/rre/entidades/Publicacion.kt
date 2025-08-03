package com.example.rre.entidades

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Publicacion (
    val titulo: String,
    val descripcion :String,
    val tipoAviso: String,
    val fecha: String,
    val lugar: String,
    val autor: String,
    val photo : String
) : Parcelable