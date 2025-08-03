package com.example.rre.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rre.MainActivity
import com.example.rre.adapter.ComentarioDetalleAdapter
import com.example.rre.databinding.FragmentDetalleNotificacionBinding
import com.example.rre.repositories.NotificacionRepository
import com.example.rre.room.DataBase.RREDatabase
import com.example.rre.room.entities.ComentarioEntity
import com.example.rre.room.entities.NotificacionEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class DetalleNotificacionFragment : Fragment() {

    private var _binding: FragmentDetalleNotificacionBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var comentarioAdapter: ComentarioDetalleAdapter
    private lateinit var notificacionRepository: NotificacionRepository
    private var notificacion: NotificacionEntity? = null
    private var notificacionId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetalleNotificacionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Obtener ID de la notificación de los argumentos
        notificacionId = arguments?.getInt("notificacionId", -1) ?: -1
        
        if (notificacionId == -1) {
            Toast.makeText(requireContext(), "Error: No se encontró la notificación", Toast.LENGTH_SHORT).show()
            return
        }
        
        setupRepository()
        setupRecyclerView()
        setupListeners()
        cargarDatos()
    }

    private fun setupRepository() {
        val database = RREDatabase.getInstance(requireContext())
        notificacionRepository = NotificacionRepository(database.notificacionDao())
    }

    private fun setupRecyclerView() {
        comentarioAdapter = ComentarioDetalleAdapter()
        binding.rvComentarios.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = comentarioAdapter
        }
    }

    private fun setupListeners() {
        binding.btnComentar.setOnClickListener {
            val contenidoComentario = binding.etNuevoComentario.text.toString().trim()
            
            if (contenidoComentario.isEmpty()) {
                Toast.makeText(requireContext(), "Por favor escribe un comentario", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            agregarComentario(contenidoComentario)
        }
    }

    private fun cargarDatos() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val database = RREDatabase.getInstance(requireContext())
                val notificacionEncontrada = database.notificacionDao().getNotificacionById(notificacionId)
                
                CoroutineScope(Dispatchers.Main).launch {
                    notificacionEncontrada?.let { notif ->
                        notificacion = notif
                        mostrarDatosNotificacion(notif)
                        
                        // Observar comentarios en el hilo principal
                        database.comentarioDao().getComentariosPorNotificacion(notificacionId).observe(viewLifecycleOwner) { comentarios ->
                            comentarioAdapter.actualizarComentarios(comentarios)
                        }
                    }
                }
                
            } catch (e: Exception) {
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(requireContext(), "Error al cargar datos: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun mostrarDatosNotificacion(notificacion: NotificacionEntity) {
        binding.tvTituloNotificacion.text = notificacion.tituloPublicacion
        binding.tvMensajeNotificacion.text = notificacion.mensaje
        binding.tvFechaNotificacion.text = notificacion.fecha
    }

    private fun agregarComentario(contenido: String) {
        val usuarioActual = obtenerUsuarioActual()
        
        if (usuarioActual.isEmpty()) {
            Toast.makeText(requireContext(), "Error: No se pudo identificar al usuario", Toast.LENGTH_SHORT).show()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val database = RREDatabase.getInstance(requireContext())
                val fechaActual = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(Date())
                
                // Crear nuevo comentario
                val nuevoComentario = ComentarioEntity(
                    notificacionId = notificacionId,
                    autorComentario = usuarioActual,
                    contenidoComentario = contenido,
                    fechaComentario = fechaActual
                )
                
                database.comentarioDao().insertComentario(nuevoComentario)
                
                // Crear notificación sobre el comentario
                notificacion?.let { notif ->
                    val mensajeNotificacion = "$usuarioActual ha comentado la publicación de ${notif.tituloPublicacion}"
                    notificacionRepository.crearNotificacionComentario(
                        autorComentario = usuarioActual,
                        tituloPublicacion = notif.tituloPublicacion
                    )
                }
                
                CoroutineScope(Dispatchers.Main).launch {
                    binding.etNuevoComentario.setText("")
                    Toast.makeText(requireContext(), "Comentario agregado exitosamente", Toast.LENGTH_SHORT).show()
                    
                    // Regresar automáticamente después de comentar
                    findNavController().navigateUp()
                }
                
            } catch (e: Exception) {
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(requireContext(), "Error al agregar comentario: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun obtenerUsuarioActual(): String {
        // Obtener usuario del MainActivity
        return try {
            (requireActivity() as MainActivity).getCorreoUsuarioLogeado() ?: "Usuario Anónimo"
        } catch (e: Exception) {
            "Usuario Anónimo"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(notificacionId: Int): DetalleNotificacionFragment {
            val fragment = DetalleNotificacionFragment()
            val args = Bundle()
            args.putInt("notificacionId", notificacionId)
            fragment.arguments = args
            return fragment
        }
    }
}
