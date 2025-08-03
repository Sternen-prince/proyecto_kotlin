package com.example.rre.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rre.adapter.NotificacionAdapter
import com.example.rre.databinding.FragmentNotificationsBinding
import com.example.rre.repositories.NotificacionRepository
import com.example.rre.room.DataBase.RREDatabase
import com.example.rre.room.entities.NotificacionEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var notificationsViewModel: NotificationsViewModel
    private lateinit var notificacionAdapter: NotificacionAdapter
    private lateinit var notificacionRepository: NotificacionRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        android.util.Log.d("NotificationsFragment", "onCreateView - Iniciando")
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupViewModel()
        setupRecyclerView()
        setupListeners()
        observeViewModel()

        android.util.Log.d("NotificationsFragment", "onCreateView - Completado")
        return root
    }

    private fun setupViewModel() {
        android.util.Log.d("NotificationsFragment", "setupViewModel - Iniciando")
        try {
            val database = RREDatabase.getInstance(requireContext())
            notificacionRepository = NotificacionRepository(database.notificacionDao())
            val factory = NotificationsViewModelFactory(notificacionRepository)
            notificationsViewModel = ViewModelProvider(this, factory).get(NotificationsViewModel::class.java)
            android.util.Log.d("NotificationsFragment", "setupViewModel - ViewModel creado exitosamente")
        } catch (e: Exception) {
            android.util.Log.e("NotificationsFragment", "setupViewModel - Error", e)
        }
    }

    private fun setupRecyclerView() {
        notificacionAdapter = NotificacionAdapter(
            onNotificacionClick = { notificacion ->
                onNotificacionClick(notificacion)
            },
            onEliminarClick = { notificacion ->
                onEliminarNotificacion(notificacion)
            }
        )
        
        binding.rvNotificaciones.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = notificacionAdapter
        }
    }

    private fun setupListeners() {
        // Ya no hay botón de prueba, este método puede estar vacío
        // o eliminarse completamente
    }

    private fun observeViewModel() {
        // Observar lista de notificaciones
        notificationsViewModel.notificaciones.observe(viewLifecycleOwner) { notificaciones ->
            android.util.Log.d("NotificationsFragment", "Notificaciones recibidas: ${notificaciones.size}")
            
            if (notificaciones.isEmpty()) {
                android.util.Log.d("NotificationsFragment", "Lista vacía - mostrando mensaje")
                binding.rvNotificaciones.visibility = View.GONE
                binding.tvNoNotificaciones.visibility = View.VISIBLE
            } else {
                android.util.Log.d("NotificationsFragment", "Mostrando ${notificaciones.size} notificaciones")
                binding.rvNotificaciones.visibility = View.VISIBLE
                binding.tvNoNotificaciones.visibility = View.GONE
                notificacionAdapter.submitList(notificaciones)
            }
        }

        // Observar estado de carga
        notificationsViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            // Aquí puedes agregar un progressbar si quieres
            android.util.Log.d("NotificationsFragment", "Estado de carga: $isLoading")
        }
    }

    private fun onNotificacionClick(notificacion: NotificacionEntity) {
        // Marcar como leída al hacer clic
        notificationsViewModel.marcarComoLeida(notificacion)
    }

    private fun onEliminarNotificacion(notificacion: NotificacionEntity) {
        // Mostrar diálogo de confirmación antes de eliminar
        android.app.AlertDialog.Builder(requireContext())
            .setTitle("Eliminar notificación")
            .setMessage("¿Estás seguro de que quieres eliminar esta notificación?")
            .setPositiveButton("Sí") { _, _ ->
                notificationsViewModel.eliminarNotificacion(notificacion)
                android.widget.Toast.makeText(
                    requireContext(),
                    "Notificación eliminada",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
            }
            .setNegativeButton("No", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}