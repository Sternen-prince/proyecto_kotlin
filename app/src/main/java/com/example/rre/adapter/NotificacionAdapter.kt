package com.example.rre.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.rre.R
import com.example.rre.room.entities.NotificacionEntity

class NotificacionAdapter(
    private val onNotificacionClick: (NotificacionEntity) -> Unit,
    private val onEliminarClick: (NotificacionEntity) -> Unit
) : ListAdapter<NotificacionEntity, NotificacionAdapter.NotificacionViewHolder>(NotificacionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificacionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notificacion, parent, false)
        return NotificacionViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificacionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class NotificacionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvMensaje: TextView = itemView.findViewById(R.id.tv_mensaje_notificacion)
        private val tvFecha: TextView = itemView.findViewById(R.id.tv_fecha_notificacion)
        private val indicadorNoLeida: View = itemView.findViewById(R.id.indicador_no_leida)
        private val btnEliminar: ImageButton = itemView.findViewById(R.id.btn_eliminar_notificacion)

        fun bind(notificacion: NotificacionEntity) {
            tvMensaje.text = notificacion.mensaje
            tvFecha.text = notificacion.fecha
            
            // Mostrar indicador si no está leída
            indicadorNoLeida.visibility = if (notificacion.leida) View.GONE else View.VISIBLE
            
            // Cambiar estilo si no está leída
            if (!notificacion.leida) {
                itemView.setBackgroundResource(R.color.notificacion_no_leida)
                tvMensaje.setTextAppearance(android.R.style.TextAppearance_Material_Body2)
            } else {
                itemView.setBackgroundResource(android.R.color.transparent)
                tvMensaje.setTextAppearance(android.R.style.TextAppearance_Material_Body1)
            }

            // Click en la notificación para marcar como leída
            itemView.setOnClickListener {
                onNotificacionClick(notificacion)
            }

            // Click en el botón X para eliminar
            btnEliminar.setOnClickListener {
                onEliminarClick(notificacion)
            }
        }
    }

    class NotificacionDiffCallback : DiffUtil.ItemCallback<NotificacionEntity>() {
        override fun areItemsTheSame(oldItem: NotificacionEntity, newItem: NotificacionEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NotificacionEntity, newItem: NotificacionEntity): Boolean {
            return oldItem == newItem
        }
    }
}
