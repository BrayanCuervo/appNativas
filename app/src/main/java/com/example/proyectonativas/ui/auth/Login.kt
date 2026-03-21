package com.example.proyectonativas.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proyectonativas.R
import com.example.proyectonativas.ui.main.MainActivity

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        val btn1 = findViewById<Button>(R.id.registrate_login)
        btn1.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
            finish()
        }
        val btn_ingresar = findViewById<Button>(R.id.btn_ingresar)
        btn_ingresar.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        val rootView = findViewById<ViewGroup>(R.id.main_login)
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
            val bottomPadding = maxOf(a = systemBars.bottom, b = imeInsets.bottom)
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, bottomPadding)
            insets
        }
    }
}