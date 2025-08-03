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
            // Navegar a RegisterFragment
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        viewModel.loginSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                // Guardar correo en MainActivity
                val correoLogeado = viewModel.username.value ?: ""
                Log.d("LoginFragment", "Correo logeado: $correoLogeado")
                (requireActivity() as MainActivity).setCorreoUsuarioLogeado(correoLogeado.toString())

                findNavController().navigate(R.id.navigation_home)
            } else {
                Toast.makeText(requireContext(), "Usuario o contraseña incorrecta", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
