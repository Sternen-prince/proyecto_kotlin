package com.example.rre.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rre.OnPublicacionClickListener
import com.example.rre.entidades.Publicacion
import com.example.rre.ui.detallespublicacion.PublicacionCompletaActivity
import com.example.rre.PublicacionProvider
import com.example.rre.adapter.PublicacionAdapter
import com.example.rre.databinding.FragmentHomeBinding
import kotlin.jvm.java

class HomeFragment : Fragment(), OnPublicacionClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var publicacionAdapter: PublicacionAdapter
    private val listaPublicaciones = PublicacionProvider.publicacionLista.toMutableList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Configurar RecyclerView
        binding.recyclerViewPublicaciones.layoutManager = LinearLayoutManager(context)

        // Inicializar el adaptador, pasándole la lista y 'this' como listener
        publicacionAdapter = PublicacionAdapter(listaPublicaciones, this)
        binding.recyclerViewPublicaciones.adapter = publicacionAdapter

        return root
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