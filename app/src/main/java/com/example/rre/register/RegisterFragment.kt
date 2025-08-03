package com.example.rre.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.rre.databinding.FragmentRegisterBinding
import com.example.rre.repositories.UsuarioRepository
import com.example.rre.room.DataBase.RREDatabase

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    // La inicialización del ViewModel ahora usará tu Repositorio
    private val viewModel: RegisterViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
                    // Asegúrate que la ruta a tu DAO es la correcta
                    val dao = RREDatabase.getInstance(requireContext()).usuarioDao()
                    val repository = UsuarioRepository(dao)
                    @Suppress("UNCHECKED_CAST")
                    return RegisterViewModel(repository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        binding.registerViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonRegistrar.setOnClickListener {
            viewModel.registrarUsuario()
        }

        // Observador para el registro exitoso
        viewModel.registroExitoso.observe(viewLifecycleOwner) { success ->
            if (success) {
                mostrarDialogo("Registro Exitoso", "Tu cuenta ha sido creada correctamente.") {
                    findNavController().navigateUp()
                }
            }
        }

        // Observador para los mensajes de error
        viewModel.errorMensaje.observe(viewLifecycleOwner) { error ->
            error?.let {
                mostrarDialogo("Error de Registro", it)
                viewModel.limpiarError()
            }
        }
    }

    // Función para mostrar el diálogo central
    private fun mostrarDialogo(titulo: String, mensaje: String, onDismiss: (() -> Unit)? = null) {
        if (!isAdded) return
        AlertDialog.Builder(requireContext())
            .setTitle(titulo)
            .setMessage(mensaje)
            .setPositiveButton("Aceptar") { dialog, _ ->
                dialog.dismiss()
            }
            .setOnDismissListener {
                onDismiss?.invoke()
            }
            .create()
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}