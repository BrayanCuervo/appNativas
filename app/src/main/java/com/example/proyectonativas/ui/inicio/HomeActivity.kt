package com.example.proyectonativas.ui.inicio

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectonativas.R
import com.example.proyectonativas.ui.auth.Login

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        val btn = findViewById<Button>(R.id.buttonHome)
        btn.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
            finish()
    }
}
}