package com.example.rre.ui.perfil

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        // Obtener el correo del usuario logeado desde la MainActivity
        val correoUsuario = (requireActivity() as MainActivity).getCorreoUsuarioLogeado() ?: ""

        // Crear repositorios
        val database = RREDatabase.getInstance(requireContext())
        val usuarioRepo = UsuarioRepository(database.usuarioDao())
        val publicacionRepo = PublicacionRepository(database.publicacionDao())

        // Crear el ViewModel usando el Factory
        val factory = PerfilViewModelFactory(usuarioRepo, publicacionRepo, correoUsuario)
        viewModel = ViewModelProvider(this, factory).get(PerfilViewModel::class.java)

        // Inflar binding y conectar ViewModel
        _binding = FragmentPerfilBinding.inflate(inflater, container, false)
        binding.perfilModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        // Configurar adaptador del RecyclerView
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

        // Observar publicaciones
        viewModel.publicaciones.observe(viewLifecycleOwner) {
            adapter.actualizarLista(it)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
