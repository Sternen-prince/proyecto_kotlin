package com.example.rre

import com.example.rre.entidades.Publicacion

class PublicacionProvider {
    companion object{
        val publicacionLista = mutableListOf(
            Publicacion(
                titulo = "Cuaderno perdido",
                descripcion = "Es un cuaderno de kuromi, lo pillé en las AB",
                tipoAviso = "Objeto perdido",
                fecha = "23-08-2025",
                lugar = "Biblioteca AB",
                autor = "Armando Fuentes",
                photo = "https://www.librerialacentral.com.ar/wp-content/uploads/2024/10/cuaderno-kuromi-mooving-19-21-tipo-abc-bts-2025.jpg"
            ),
            Publicacion(
                titulo = "Almohada perdida",
                descripcion = "Es una almohada de nube, la pillé en las AA",
                tipoAviso = "Objeto perdido",
                fecha = "21-08-2025",
                lugar = "Sala AA",
                autor = "Armando Fuentes",
                photo = "https://m.media-amazon.com/images/I/41eNmgby7-L._UF894,1000_QL80_.jpg"
            )
        )
    }
}