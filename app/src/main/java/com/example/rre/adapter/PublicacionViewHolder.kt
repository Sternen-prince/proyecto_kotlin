package com.example.rre.adapter

import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.rre.OnPublicacionClickListener
import com.example.rre.entidades.Publicacion
import com.example.rre.databinding.ItemPublicacionBinding

class PublicacionViewHolder (
    val binding: ItemPublicacionBinding,
    val listener: OnPublicacionClickListener
): RecyclerView.ViewHolder(binding.root){

    fun bind(publicacion: Publicacion){
        binding.publicacion = publicacion
        binding.clickListener = listener
        binding.executePendingBindings()

        binding.imagenPublicacion.setOnClickListener{
            Toast.makeText(
                binding.root.context,
                publicacion.titulo,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}