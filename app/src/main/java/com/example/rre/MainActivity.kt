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
import com.example.rre.room.entities.UsuarioEntity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // 2. --- CAMBIO: La variable ahora es de tipo UsuarioEntity ---
    private var usuarioLogeado: UsuarioEntity? = null

    // 3. --- CAMBIO: La función ahora recibe el objeto UsuarioEntity completo ---
    fun setUsuarioLogeado(usuario: UsuarioEntity?) {
        usuarioLogeado = usuario
    }

    // 4. --- CAMBIO: La función ahora devuelve el objeto UsuarioEntity completo ---
    fun getUsuarioLogeado(): UsuarioEntity? {
        return usuarioLogeado
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
            if (destination.id == R.id.loginFragment || destination.id == R.id.registerFragment) {
                navView.visibility = View.GONE
                binding.fab.hide()
                supportActionBar?.hide()
            } else {
                navView.visibility = View.VISIBLE
                supportActionBar?.show()

                when (destination.id) {
                    R.id.navigation_home, R.id.navigation_notifications, R.id.navigation_dashboard -> {
                        supportActionBar?.setDisplayHomeAsUpEnabled(false)
                        binding.fab.show()
                    }
                    R.id.publicacionFragment, R.id.detalleNotificacionFragment -> {
                        supportActionBar?.setDisplayHomeAsUpEnabled(true)
                        binding.fab.hide()
                    }
                }
                // Esta parte de sincronizar el item seleccionado también la hace 'setupWithNavController',
                // por lo que podría ser redundante, pero no es tan problemática como el listener anterior.
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