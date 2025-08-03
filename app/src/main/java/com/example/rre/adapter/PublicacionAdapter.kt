package com.example.rre.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rre.OnPublicacionClickListener
import com.example.rre.entidades.Publicacion
import com.example.rre.databinding.ItemPublicacionBinding

class PublicacionAdapter(
    publicaciones: List<Publicacion>,
    private val listener: OnPublicacionClickListener
) : RecyclerView.Adapter<PublicacionAdapter.PublicacionViewHolder>() {

    // Lista completa
    private var todasLasPublicaciones: List<Publicacion> = publicaciones

    // Lista que se muestra (puede estar filtrada)
    private var publicacionesMostradas: List<Publicacion> = publicaciones

    class PublicacionViewHolder(
        private val binding: ItemPublicacionBinding,
        private val listener: OnPublicacionClickListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(publicacion: Publicacion) {
            binding.publicacion = publicacion
            binding.clickListener = listener
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PublicacionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPublicacionBinding.inflate(inflater, parent, false)
        return PublicacionViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: PublicacionViewHolder, position: Int) {
        holder.bind(publicacionesMostradas[position])
    }

    override fun getItemCount(): Int = publicacionesMostradas.size

    // Actualizar la lista completa y reiniciar la filtrada
    fun actualizarLista(nuevaLista: List<Publicacion>) {
        todasLasPublicaciones = nuevaLista
        publicacionesMostradas = nuevaLista
        notifyDataSetChanged()
    }

    // Filtro por título
    fun filtrar(query: String) {
        publicacionesMostradas = if (query.isEmpty()) {
            todasLasPublicaciones
        } else {
            todasLasPublicaciones.filter {
                it.titulo.contains(query, ignoreCase = true)
            }
        }
        notifyDataSetChanged()
    }
}

