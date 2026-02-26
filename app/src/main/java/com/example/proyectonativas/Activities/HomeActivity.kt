package com.example.proyectonativas.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proyectonativas.R
import kotlin.jvm.java

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