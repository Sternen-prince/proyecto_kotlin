package com.example.rre.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.rre.MainActivity
import com.example.rre.R
import com.example.rre.databinding.FragmentLoginBinding
import com.example.rre.repositories.UsuarioRepository
import com.example.rre.room.DataBase.RREDatabase

class loginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(
            UsuarioRepository(
                RREDatabase.getInstance(requireContext()).usuarioDao()
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.loginViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonLogin.setOnClickListener {
            viewModel.login()
        }

        binding.buttonRegistrarse.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        // --- BLOQUE DE OBSERVADORES COMPLETAMENTE NUEVO ---

        // Observador para el caso de ÉXITO (cuando el usuario no es nulo)
        viewModel.usuarioLogeado.observe(viewLifecycleOwner) { usuario ->
            if (usuario != null) {
                // Éxito. Guardamos el objeto UsuarioEntity completo en MainActivity.
                Log.d("LoginFragment", "Login exitoso para: ${usuario.nombre}")
                (requireActivity() as MainActivity).setUsuarioLogeado(usuario)

                // Navegamos a la pantalla de inicio
                findNavController().navigate(R.id.navigation_home)
            }
            // Si el usuario es nulo, podría ser un fallo de login.
            // El siguiente observador manejará el mensaje de error.
        }

        // Observador para el caso de FRACASO (cuando hay un mensaje de error)
        viewModel.errorMensaje.observe(viewLifecycleOwner) { mensaje ->
            mensaje?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                // Limpiamos el error para que el Toast no se muestre de nuevo (ej: al girar la pantalla)
                viewModel.onMensajeErrorMostrado()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}