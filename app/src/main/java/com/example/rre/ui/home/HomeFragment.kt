package com.example.rre.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rre.OnPublicacionClickListener
import com.example.rre.entidades.Publicacion
import com.example.rre.ui.detallespublicacion.PublicacionCompletaActivity
import com.example.rre.adapter.PublicacionAdapter
import com.example.rre.databinding.FragmentHomeBinding
import com.example.rre.repositories.PublicacionRepository
import com.example.rre.room.DataBase.RREDatabase
import kotlin.jvm.java

class HomeFragment : Fragment(), OnPublicacionClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var publicacionAdapter: PublicacionAdapter
    private lateinit var homeViewModel: HomeViewModel
    private val listaPublicaciones = mutableListOf<Publicacion>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Configurar repositorio y ViewModel
        val database = RREDatabase.getInstance(requireContext())
        val publicacionRepository = PublicacionRepository(database.publicacionDao())
        val viewModelFactory = HomeViewModelFactory(publicacionRepository)
        homeViewModel = ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]

        // Configurar RecyclerView
        binding.recyclerViewPublicaciones.layoutManager = LinearLayoutManager(context)
        publicacionAdapter = PublicacionAdapter(listaPublicaciones, this)
        binding.recyclerViewPublicaciones.adapter = publicacionAdapter

        // Observar cambios en las publicaciones
        homeViewModel.publicaciones.observe(viewLifecycleOwner) { publicaciones ->
            listaPublicaciones.clear()
            listaPublicaciones.addAll(publicaciones)
            publicacionAdapter.notifyDataSetChanged()
        }

        return root
    }

    override fun onResume() {
        super.onResume()
        // Actualizar publicaciones cuando el fragment se vuelve visible
        homeViewModel.actualizarPublicaciones()
    }

    override fun onPublicacionClick(publicacion: Publicacion) {
        // Lanzar la actividad de publicación completa
        val intent = Intent(activity, PublicacionCompletaActivity::class.java).apply {
            putExtra("EXTRA_PUBLICACION", publicacion)
        }
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}