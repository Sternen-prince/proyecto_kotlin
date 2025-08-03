package com.example.rre.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.rre.MainActivity
import com.example.rre.R

class NotificationService(private val context: Context) {

    companion object {
        private const val CHANNEL_ID = "RRE_NOTIFICATIONS"
        private const val CHANNEL_NAME = "Publicaciones de Objetos"
        private const val CHANNEL_DESCRIPTION = "Notificaciones cuando se publican objetos perdidos/encontrados"
        private var notificationId = 1001
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = CHANNEL_DESCRIPTION
                enableVibration(true)
                enableLights(true)
            }

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showNotification(titulo: String, mensaje: String, autor: String) {
        // Intent para abrir la app cuando se toque la notificación
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context, 
            0, 
            intent, 
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Construir la notificación
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications_black_24dp)
            .setContentTitle("RRE - $titulo")
            .setContentText("$autor ha publicado: $mensaje")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("$autor ha publicado un objeto: $mensaje"))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(0, 500, 1000))
            .setLights(0xFF0000FF.toInt(), 3000, 3000)
            .build()

        // Mostrar la notificación
        try {
            with(NotificationManagerCompat.from(context)) {
                notify(notificationId++, notification)
            }
        } catch (e: SecurityException) {
            // Manejar el caso donde no hay permisos de notificación
            e.printStackTrace()
        }
    }

    fun showTestNotification() {
        showNotification(
            "Objeto Perdido",
            "Prueba de notificación push",
            "Sistema de Prueba"
        )
    }
}
