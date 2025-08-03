package com.example.rre.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rre.databinding.ItemComentarioDetalleBinding
import com.example.rre.room.entities.ComentarioEntity

class ComentarioDetalleAdapter : RecyclerView.Adapter<ComentarioDetalleAdapter.ComentarioViewHolder>() {

    private var comentarios: List<ComentarioEntity> = emptyList()

    fun actualizarComentarios(nuevosComentarios: List<ComentarioEntity>) {
        comentarios = nuevosComentarios
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComentarioViewHolder {
        val binding = ItemComentarioDetalleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ComentarioViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ComentarioViewHolder, position: Int) {
        holder.bind(comentarios[position])
    }

    override fun getItemCount(): Int = comentarios.size

    class ComentarioViewHolder(private val binding: ItemComentarioDetalleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(comentario: ComentarioEntity) {
            binding.tvAutorComentario.text = comentario.autorComentario
            binding.tvContenidoComentario.text = comentario.contenidoComentario
            binding.tvFechaComentario.text = comentario.fechaComentario
        }
    }
}
