package com.example.rre.ui.perfil

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rre.MainActivity
import com.example.rre.databinding.FragmentPerfilBinding
import com.example.rre.entidades.Publicacion
import com.example.rre.adapter.PublicacionAdapter
import com.example.rre.OnPublicacionClickListener
import com.example.rre.repositories.PublicacionRepository
import com.example.rre.repositories.UsuarioRepository
import com.example.rre.room.DataBase.RREDatabase
import com.example.rre.ui.detallespublicacion.PublicacionCompletaActivity

class PerfilFragment : Fragment() {

    private var _binding: FragmentPerfilBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: PerfilViewModel
    private lateinit var adapter: PublicacionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPerfilBinding.inflate(inflater, container, false)

        // --- CAMBIO 1: Obtener el objeto UsuarioEntity completo ---
        val usuarioLogeado = (requireActivity() as MainActivity).getUsuarioLogeado()

        // Si por alguna razón el usuario no está logueado, mostramos un error y no continuamos.
        if (usuarioLogeado == null) {
            Toast.makeText(requireContext(), "Error: Usuario no encontrado. Por favor, inicie sesión.", Toast.LENGTH_LONG).show()
            // Aquí podrías navegar a la pantalla de login si quisieras
            return binding.root // Devolvemos la vista vacía
        }

        // --- CAMBIO 2: Crear repositorios (esto está bien como lo tienes) ---
        val database = RREDatabase.getInstance(requireContext())
        val usuarioRepo = UsuarioRepository(database.usuarioDao())
        val publicacionRepo = PublicacionRepository(database.publicacionDao())

        // --- CAMBIO 3: Pasamos el objeto 'usuarioLogeado' completo a la Factory ---
        val factory = PerfilViewModelFactory(usuarioRepo, publicacionRepo, usuarioLogeado)
        viewModel = ViewModelProvider(this, factory).get(PerfilViewModel::class.java)

        binding.perfilModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setupRecyclerView()
        setupObservers()

        return binding.root
    }

    private fun setupRecyclerView() {
        adapter = PublicacionAdapter(emptyList(), object : OnPublicacionClickListener {
            override fun onPublicacionClick(publicacion: Publicacion) {
                val intent = Intent(requireContext(), PublicacionCompletaActivity::class.java).apply {
                    putExtra("titulo", publicacion.titulo)
                    putExtra("descripcion", publicacion.descripcion)
                    putExtra("autor", publicacion.autor)
                    putExtra("fecha", publicacion.fecha)
                    putExtra("photo", publicacion.photo)
                }
                startActivity(intent)
            }
        })
        binding.recyclerViewPerfil.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewPerfil.adapter = adapter
    }

    private fun setupObservers() {
        // Observar publicaciones
        viewModel.publicaciones.observe(viewLifecycleOwner) { listaPublicaciones ->
            adapter.actualizarLista(listaPublicaciones)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
