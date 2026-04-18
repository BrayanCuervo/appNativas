package com.example.proyectonativas.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.proyectonativas.R
import com.example.proyectonativas.ui.auth.Login
import com.example.proyectonativas.ui.auth.RegistroActivity
import com.example.proyectonativas.ui.main.admin.AdminFragment
import com.example.proyectonativas.ui.main.admin.UsuariosFragment
import com.example.proyectonativas.ui.main.perfil.AjustesFragment
import com.example.proyectonativas.ui.main.perfil.EditarPefilFragment
import com.example.proyectonativas.ui.main.perfil.PerfilFragment
import com.example.proyectonativas.ui.main.productos.FavoritosFragment
import com.example.proyectonativas.ui.main.productos.HomeFragment
import com.example.proyectonativas.ui.main.productos.ProductosFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: androidx.drawerlayout.widget.DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        drawerLayout = findViewById(R.id.drawer_layout)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        val navView = findViewById<NavigationView>(R.id.nav_menu)

        setSupportActionBar(toolbar)

        val toogle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        drawerLayout.addDrawerListener(toogle)
        toogle.syncState()

        cargarFragment(HomeFragment())
        bottomNav.selectedItemId = R.id.nav_home

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId){
                R.id.nav_home -> cargarFragment(HomeFragment())
                R.id.nav_bottom_productos -> cargarFragment(ProductosFragment())
                R.id.nav_bottom_perfil -> cargarFragment(EditarPefilFragment())
                R.id.nav_bottom_ajustes -> cargarFragment(AjustesFragment())
            }
            true
        }
        navView.setNavigationItemSelectedListener { item ->
            when (item.itemId){



                R.id.nav_lat_Inicio -> cargarFragment(HomeFragment())
                R.id.nav_lat_admin -> cargarFragment(AdminFragment())
                R.id.nav_lat_usuarios -> cargarFragment(UsuariosFragment())
                R.id.nav_lat_favoritos -> cargarFragment(FavoritosFragment())
                //cierra el menu lateral y abre el menu de incio
                R.id.nav_lat_Inicio_sesion -> {
                    startActivity(Intent(this, Login::class.java))
                    drawerLayout.closeDrawers()
                }
            }
            true
        }

    }

    private fun cargarFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()

    }
}