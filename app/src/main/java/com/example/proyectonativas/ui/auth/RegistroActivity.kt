package com.example.proyectonativas.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.proyectonativas.R
import com.example.proyectonativas.SupabaseCliente
import com.example.proyectonativas.ui.auth.Login
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

class RegistroActivity : AppCompatActivity(){

    private lateinit var etNombres: EditText
    private lateinit var etApellidos: EditText
    private lateinit var etCorreo: EditText
    private lateinit var etContrasena: EditText
    private lateinit var etReContrasena: EditText
    private lateinit var checkTerminos: CheckBox
    private lateinit var btnRegistro: Button
    private lateinit var tvCuenta: TextView

    @Serializable
    data class Usuario(
        val id: String,
        val nombre:String,
        val apellidos: String

        )

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
        etNombres = findViewById(R.id.nombres_registro)
        etApellidos = findViewById(R.id.apellidos_registro)
        etCorreo = findViewById(R.id.correo_registro)
        etContrasena = findViewById(R.id.password_registro)
        etReContrasena = findViewById(R.id.password2_registro)
        tvCuenta = findViewById(R.id.tienes_cuenta_registro)
        checkTerminos = findViewById(R.id.check_terminos_registro)
        btnRegistro = findViewById(R.id.btn_registro)

        //Escuchar el boton de registros

        btnRegistro.setOnClickListener {
            val nombres = etNombres.text.toString().trim()
            val apellidos = etApellidos.text.toString().trim()
            val contrasena = etContrasena.text.toString().trim()
            val correo = etCorreo.text.toString().trim()
            val reContrasena = etReContrasena.text.toString().trim()

            //validaciones


            if(nombres.isEmpty() || apellidos.isEmpty() || contrasena.isEmpty() || reContrasena.isEmpty() || correo.isEmpty()){
                Toast.makeText(this, "Porfavor llena todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(contrasena.length<8){
                Toast.makeText(this, "La contaseña debe tener al menos 8 caracteres", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(contrasena != reContrasena){
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(!checkTerminos.isChecked){
                Toast.makeText(this, "Debes aceptar los terminos y condiciones", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(!correo.contains("@")){
                Toast.makeText(this, "El correo no es valido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            //Registro en Supabase
            lifecycleScope.launch {
                try {
                    //Registrar usuario en supabase
                    val resultado = SupabaseCliente.cliente.auth.signUpWith(Email){
                        email = correo
                        password = contrasena
                    }

                    //Guardar datos en la base de datos
                    val userId = SupabaseCliente.cliente.auth.currentUserOrNull()?.id ?: ""
                    SupabaseCliente.cliente.postgrest["usuarios"].insert(
                        Usuario(
                        id = userId,
                        nombres,
                        apellidos)
                    )
                    //Redirigir al usuario al Login
                    runOnUiThread {
                        Toast.makeText(this@RegistroActivity, "Registro exitoso", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@RegistroActivity, Login::class.java))
                        finish()
                    }


                }catch (e: Exception){
                    runOnUiThread {
                        Toast.makeText(this@RegistroActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }

            }

            btnRegistro.setOnClickListener {
                startActivity(Intent(this, Login::class.java))
                finish()
            }




        }

    }
}