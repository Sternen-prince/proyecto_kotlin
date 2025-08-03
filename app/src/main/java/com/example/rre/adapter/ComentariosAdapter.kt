package com.example.rre

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rre.ui.detallespublicacion.Comentarios

class ComentariosAdapter(private val comentariosList: MutableList<Comentarios>) :
    RecyclerView.Adapter<ComentariosAdapter.ComentariosViewHolder>() {

    class ComentariosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val TextViewComentarioUser: TextView = itemView.findViewById(R.id.TextView_ComentarioUser)
        val TextViewContenidoComentario: TextView = itemView.findViewById(R.id.TextView_Contenido_Comentario)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComentariosViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_comentario, parent, false)
        return ComentariosViewHolder(view)
    }

    override fun onBindViewHolder(holder: ComentariosViewHolder, position: Int) {
        val comentario = comentariosList[position]
        holder.TextViewComentarioUser.text = "Usuario: ${comentario.userId}"
        holder.TextViewContenidoComentario.text = comentario.content
    }

    override fun getItemCount(): Int = comentariosList.size

    // Función para añadir un comentario dinámicamente
    fun addComment(comentario: Comentarios) {
        comentariosList.add(comentario)
        notifyItemInserted(comentariosList.size - 1)
        // Opcional: notifyDataSetChanged() si hay problemas de actualización
    }

    fun setComments(newComentario: List<Comentarios>) {
        comentariosList.clear()
        comentariosList.addAll(newComentario)
        notifyDataSetChanged()
    }
}