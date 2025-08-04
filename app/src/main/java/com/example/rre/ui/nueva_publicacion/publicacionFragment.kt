package com.example.rre.ui.nueva_publicacion

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.rre.MainActivity
import com.example.rre.R
import com.example.rre.databinding.FragmentPublicacionBinding
import com.example.rre.room.entities.UsuarioEntity // <-- Importamos UsuarioEntity

class PublicacionFragment : Fragment() {

    private var _binding: FragmentPublicacionBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: PublicacionViewModel

    private var fotoUriTemp: Uri? = null

    private val seleccionarImagenLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.actualizarImagen(it.toString())
        }
    }

    private val tomarFotoLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            fotoUriTemp?.let { uri ->
                viewModel.actualizarImagen(uri.toString())
            }
        } else {
            Toast.makeText(requireContext(), "No se pudo tomar la foto", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPublicacionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        configurarSpinnerTipoAviso()
        configurarObservadores()
        configurarListeners()
        configurarSeleccionImagen()
    }

    private fun configurarSpinnerTipoAviso() {
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.tipos_aviso,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerTipoAviso.adapter = adapter

        binding.spinnerTipoAviso.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position > 0) {
                    val tipoSeleccionado = parent?.getItemAtPosition(position).toString()
                    viewModel.actualizarTipoAviso(tipoSeleccionado)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupViewModel() {
        try {
            val database = com.example.rre.room.DataBase.RREDatabase.getInstance(requireContext())
            val notificacionRepository = com.example.rre.repositories.NotificacionRepository(database.notificacionDao())
            val publicacionRepository = com.example.rre.repositories.PublicacionRepository(database.publicacionDao())
            // Suponiendo que tienes una PublicacionViewModelFactory que acepta los repositorios
            val factory = PublicacionViewModelFactory(notificacionRepository, publicacionRepository)
            viewModel = ViewModelProvider(this, factory)[PublicacionViewModel::class.java]
        } catch (e: Exception) {
            viewModel = ViewModelProvider(this)[PublicacionViewModel::class.java]
        }
    }

    private fun configurarObservadores() {
        // ... (Tu código de observadores no necesita cambios)
        viewModel.titulo.observe(viewLifecycleOwner) { titulo ->
            if (binding.etTitulo.text.toString() != titulo) {
                binding.etTitulo.setText(titulo)
            }
        }
        viewModel.tipoAviso.observe(viewLifecycleOwner) { tipo ->
            val adapter = binding.spinnerTipoAviso.adapter
            for (i in 0 until adapter.count) {
                if (adapter.getItem(i).toString() == tipo) {
                    if (binding.spinnerTipoAviso.selectedItemPosition != i) {
                        binding.spinnerTipoAviso.setSelection(i)
                    }
                    break
                }
            }
        }
        viewModel.descripcion.observe(viewLifecycleOwner) { descripcion ->
            if (binding.etDescripcion.text.toString() != descripcion) {
                binding.etDescripcion.setText(descripcion)
            }
        }
        viewModel.lugar.observe(viewLifecycleOwner) { lugar ->
            if (binding.etLugar.text.toString() != lugar) {
                binding.etLugar.setText(lugar)
            }
        }
        viewModel.imagenUri.observe(viewLifecycleOwner) { uriString ->
            if (!uriString.isNullOrEmpty()) {
                val uri = Uri.parse(uriString)
                binding.ivImagenAviso.setImageURI(uri)
                binding.ivImagenAviso.visibility = View.VISIBLE
                binding.llPlaceholderImagen.visibility = View.GONE
            } else {
                binding.ivImagenAviso.visibility = View.GONE
                binding.llPlaceholderImagen.visibility = View.VISIBLE
            }
        }
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.btnPublicar.isEnabled = !isLoading
            binding.btnPublicar.text = if (isLoading) "Publicando..." else "Publicar"
        }
        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                viewModel.limpiarError()
            }
        }
        viewModel.publicacionExitosa.observe(viewLifecycleOwner) { exitosa ->
            if (exitosa) {
                Toast.makeText(requireContext(), "¡Publicación creada exitosamente!", Toast.LENGTH_SHORT).show()
                // Navegar a la pantalla principal
                findNavController().popBackStack(R.id.navigation_home, false)
                viewModel.resetearEstadoExito()
            }
        }
    }

    private fun configurarListeners() {
        // ... (Tu código de listeners no necesita cambios)
        binding.etTitulo.addTextChangedListener { text -> viewModel.actualizarTitulo(text.toString()) }
        binding.etDescripcion.addTextChangedListener { text -> viewModel.actualizarDescripcion(text.toString()) }
        binding.etLugar.addTextChangedListener { text -> viewModel.actualizarLugar(text.toString()) }
        binding.btnCancelar.setOnClickListener { findNavController().popBackStack() }
        binding.btnPublicar.setOnClickListener { publicarAviso() }
    }

    private fun configurarSeleccionImagen() {
        // ... (Tu código de selección de imagen no necesita cambios)
        binding.btnSeleccionarImagen.setOnClickListener { seleccionarImagenLauncher.launch("image/*") }
        binding.btnTomarFoto.setOnClickListener {
            fotoUriTemp = crearImagenTempUri(requireContext())
            fotoUriTemp?.let { uri -> tomarFotoLauncher.launch(uri) }
                ?: run { Toast.makeText(requireContext(), "No se pudo crear archivo para la foto", Toast.LENGTH_SHORT).show() }
        }
        binding.llPlaceholderImagen.setOnClickListener { seleccionarImagenLauncher.launch("image/*") }
    }

    private fun crearImagenTempUri(context: Context): Uri? {
        val contentResolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "foto_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    }

    // --- FUNCIÓN CORREGIDA ---
    private fun publicarAviso() {
        binding.etTitulo.error = null
        binding.etDescripcion.error = null
        binding.etLugar.error = null

        val titulo = binding.etTitulo.text.toString()
        val validacionTitulo = viewModel.validarTitulo(titulo)
        if (!validacionTitulo.isValid) {
            binding.etTitulo.error = validacionTitulo.errorMessage
            binding.etTitulo.requestFocus()
            return
        }

        val tipoAviso = if (binding.spinnerTipoAviso.selectedItemPosition > 0) {
            binding.spinnerTipoAviso.selectedItem.toString()
        } else { "" }
        val validacionTipo = viewModel.validarTipoAviso(tipoAviso)
        if (!validacionTipo.isValid) {
            Toast.makeText(requireContext(), validacionTipo.errorMessage, Toast.LENGTH_SHORT).show()
            return
        }

        val descripcion = binding.etDescripcion.text.toString()
        val validacionDescripcion = viewModel.validarDescripcion(descripcion)
        if (!validacionDescripcion.isValid) {
            binding.etDescripcion.error = validacionDescripcion.errorMessage
            binding.etDescripcion.requestFocus()
            return
        }

        val lugar = binding.etLugar.text.toString()
        val validacionLugar = viewModel.validarLugar(lugar)
        if (!validacionLugar.isValid) {
            binding.etLugar.error = validacionLugar.errorMessage
            binding.etLugar.requestFocus()
            return
        }

        val usuarioActual = obtenerUsuarioActual()
        if (usuarioActual == null) {
            Toast.makeText(requireContext(), "Error: No se pudo identificar al usuario. Por favor, inicie sesión de nuevo.", Toast.LENGTH_LONG).show()
            return
        }

        // Llamamos a la nueva función del ViewModel con los parámetros correctos
        viewModel.publicarAviso(
            autorId = usuarioActual.userId,
            autorNombre = usuarioActual.nombre
        )
    }

    // --- FUNCIÓN CORREGIDA ---
    private fun obtenerUsuarioActual(): UsuarioEntity? {
        return try {
            (requireActivity() as MainActivity).getUsuarioLogeado()
        } catch (e: Exception) {
            null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}