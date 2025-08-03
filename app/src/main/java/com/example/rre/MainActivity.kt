package com.example.rre

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.rre.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var correoUsuarioLogeado: String? = null

    fun setCorreoUsuarioLogeado(correo: String) {
        correoUsuarioLogeado = correo
    }

    fun getCorreoUsuarioLogeado(): String? {
        return correoUsuarioLogeado
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_notifications, R.id.navigation_dashboard
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Agregar listener específico para manejar la navegación del bottom navigation
        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    if (navController.currentDestination?.id != R.id.navigation_home) {
                        navController.navigate(R.id.navigation_home)
                    }
                    true
                }
                R.id.navigation_notifications -> {
                    if (navController.currentDestination?.id != R.id.navigation_notifications) {
                        navController.navigate(R.id.navigation_notifications)
                    }
                    true
                }
                R.id.navigation_dashboard -> {
                    if (navController.currentDestination?.id != R.id.navigation_dashboard) {
                        navController.navigate(R.id.navigation_dashboard)
                    }
                    true
                }
                else -> false
            }
        }

        binding.fab.setOnClickListener {
            navController.navigate(R.id.publicacionFragment)
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.loginFragment) {
                navView.visibility = View.GONE
                binding.fab.hide()
                supportActionBar?.hide()
            } else {
                navView.visibility = View.VISIBLE
                supportActionBar?.show()
                
                // Mostrar/ocultar botón de navegación hacia atrás según el fragmento
                when (destination.id) {
                    R.id.navigation_home, R.id.navigation_notifications, R.id.navigation_dashboard -> {
                        // En los fragmentos principales, ocultar la flecha de atrás y mostrar FAB
                        supportActionBar?.setDisplayHomeAsUpEnabled(false)
                        binding.fab.show()
                    }
                    R.id.publicacionFragment -> {
                        // En el fragmento de publicación, mostrar la flecha de atrás y ocultar FAB
                        supportActionBar?.setDisplayHomeAsUpEnabled(true)
                        binding.fab.hide()
                    }
                }
                
                // Sincronizar el bottom navigation con el destino actual
                when (destination.id) {
                    R.id.navigation_home -> navView.selectedItemId = R.id.navigation_home
                    R.id.navigation_notifications -> navView.selectedItemId = R.id.navigation_notifications
                    R.id.navigation_dashboard -> navView.selectedItemId = R.id.navigation_dashboard
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }


}