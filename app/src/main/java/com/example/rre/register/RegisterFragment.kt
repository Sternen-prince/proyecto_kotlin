package com.example.rre.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

    private val viewModel: RegisterViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
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

        viewModel.registroExitoso.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(requireContext(), "Registro exitoso", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        }

        viewModel.errorMensaje.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                viewModel.limpiarError()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
