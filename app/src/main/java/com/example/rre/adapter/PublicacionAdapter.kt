package com.example.rre.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rre.OnPublicacionClickListener
import com.example.rre.entidades.Publicacion
import com.example.rre.databinding.ItemPublicacionBinding

class PublicacionAdapter(
    private var publicaciones: List<Publicacion>,
    private val listener: OnPublicacionClickListener
) : RecyclerView.Adapter<PublicacionAdapter.PublicacionViewHolder>() {

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
        holder.bind(publicaciones[position])
    }

    override fun getItemCount(): Int = publicaciones.size

    fun actualizarLista(nuevaLista: List<Publicacion>) {
        publicaciones = nuevaLista
        notifyDataSetChanged()
    }
}
