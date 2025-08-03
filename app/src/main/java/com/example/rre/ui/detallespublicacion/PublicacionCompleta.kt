package com.example.rre.ui.detallespublicacion

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.rre.ComentariosAdapter
import com.example.rre.R
import com.example.rre.databinding.ActivityPublicacionCompletaBinding
import com.example.rre.entidades.Publicacion

class PublicacionCompletaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPublicacionCompletaBinding
    private lateinit var publicacion: Publicacion
    private lateinit var comentariosAdapter: ComentariosAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Inflar el layout usando DataBinding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_publicacion_completa)

        // 2. Recuperar los datos de la publicación del Intent
        publicacion = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("EXTRA_PUBLICACION", Publicacion::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("EXTRA_PUBLICACION")
        } ?: run {
            Toast.makeText(this, "Error al cargar la publicación.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // 3. Pasar el objeto 'publicacion' al layout a través del binding
        binding.publicacion = publicacion

        // 4. Lógica para cargar la imagen (Si el 'photo' es una URL)
        if (!publicacion.photo.isNullOrEmpty()) {
            // Ejemplo de cómo cargarías una imagen con una librería como Glide.
            Glide.with(this).load(publicacion.photo).into(binding.ivPostPhoto)
        }

        // 5. Configurar los Listeners para los botones de acción y comentarios
        setupActionButtons(publicacion)
        setupCommentSection(publicacion)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupActionButtons(publicacion: Publicacion) {
        binding.btnReportPost.setOnClickListener {
            Toast.makeText(this, "Publicación '${publicacion.titulo}' reportada.", Toast.LENGTH_SHORT).show()
        }

        binding.btnMarkFound.setOnClickListener {
            Toast.makeText(this, "Publicación marcada como encontrada.", Toast.LENGTH_SHORT).show()
        }

        binding.btnEditPost.setOnClickListener {
            Toast.makeText(this, "Editando publicación '${publicacion.titulo}'...", Toast.LENGTH_SHORT).show()
        }

        binding.btnDeletePost.setOnClickListener {
            Toast.makeText(this, "Publicación '${publicacion.titulo}' eliminada.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupCommentSection(publicacion: Publicacion) {
        comentariosAdapter = ComentariosAdapter(mutableListOf())
        binding.rvComments.layoutManager = LinearLayoutManager(this)
        binding.rvComments.adapter = comentariosAdapter

        binding.btnSubmitComment.setOnClickListener {
            val newCommentText = binding.etComment.text.toString().trim()
            if (newCommentText.isNotEmpty()) {
                // Simulación de adición de comentario
                val newComment = Comentarios(
                    id = "comment_${System.currentTimeMillis()}",
                    userId = "UsuarioActual",
                    content = newCommentText,
                    timestamp = System.currentTimeMillis()
                )
                comentariosAdapter.addComment(newComment)
                binding.etComment.setText("")
                Toast.makeText(this, "Comentario añadido.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "El comentario no puede estar vacío.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        finish()  // Cierra esta Activity y vuelve a MainActivity (donde estabas)
        return true
    }

}