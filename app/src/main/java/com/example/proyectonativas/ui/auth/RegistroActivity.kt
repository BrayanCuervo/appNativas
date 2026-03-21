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
import com.example.proyectonativas.ui.auth.Login

class RegistroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro)
        val btn2 = findViewById<Button>(R.id.login_registro)
        btn2.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
            finish()
        }
        val rootView = findViewById<ViewGroup>(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
            val bottomPadding = maxOf(a = systemBars.bottom, b = imeInsets.bottom)
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, bottomPadding)
            insets
        }
    }
}