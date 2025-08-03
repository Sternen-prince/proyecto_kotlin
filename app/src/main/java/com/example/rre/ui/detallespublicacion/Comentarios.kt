package com.example.rre.ui.detallespublicacion

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Comentarios(
    val id: String,
    val userId: String,
    val content: String,
    val timestamp: Long
) : Parcelable