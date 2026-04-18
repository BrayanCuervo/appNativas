package com.example.proyectonativas.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.proyectonativas.R
import com.example.proyectonativas.SupabaseCliente
import com.example.proyectonativas.ui.main.MainActivity
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.providers.builtin.IDToken
import kotlinx.coroutines.launch
import androidx.credentials.GetCredentialRequest
import androidx.credentials.CredentialManager
import com.example.proyectonativas.data.CredencialesManager
import io.github.jan.supabase.SupabaseClient
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricManager

class Login : AppCompatActivity() {

    private lateinit var tvIngresarConHuella: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)


        tvIngresarConHuella = findViewById(R.id.huella)
        //inicio de sesion con huella
        tvIngresarConHuella.setOnClickListener {
            mostrarDialogoHuella()
        }

        // Manejo del teclado para Android 15/16
        val rootView = findViewById<ViewGroup>(R.id.main_login)
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
            val bottomPadding = maxOf(a = systemBars.bottom, b = imeInsets.bottom)
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, bottomPadding)
            insets
        }
        // Listeners de los botones
        findViewById<android.widget.Button>(R.id.registrate_login)
            .setOnClickListener {
                startActivity(Intent(this, RegistroActivity::class.java))
                finish()
            }
        findViewById<android.widget.Button>(R.id.btn_ingresar)
            .setOnClickListener { iniciarSesion() }
        findViewById<android.widget.TextView>(R.id.recuperar_contrasena_login)
            .setOnClickListener {
                Toast.makeText(this, "Proximamente", Toast.LENGTH_SHORT).show()
            }
        findViewById<Button>(R.id.btnGoogle)
            .setOnClickListener { iniciarSesionConGoogle() }
    }

    override fun onResume() {
        super.onResume()
        configurarVisibilidadHuella()
    }
    private fun configurarVisibilidadHuella() {
        //verificar si hay credenciales guardadas localmente
        val huellaActiva = CredencialesManager.huellaActiva(this)

        //Verificar si el dispositivo tiene sensor de huella
        val biometricManager = BiometricManager.from(this)
        val huellaDisponible = biometricManager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG or
                    BiometricManager.Authenticators.BIOMETRIC_WEAK
        ) == BiometricManager.BIOMETRIC_SUCCESS

        tvIngresarConHuella.visibility = if (huellaDisponible && huellaActiva) View.VISIBLE else View.GONE
    }
    private fun iniciarSesion() {
        val correo = findViewById<android.widget.EditText>(R.id.usuario_login)
            .text.toString().trim()
        val contrasena = findViewById<android.widget.EditText>(R.id.contrasena_login)
            .text.toString()
        // Validaciones locales
        if (correo.isEmpty() || contrasena.isEmpty()) {
            Toast.makeText(this, "Por favor completa todos los campos",
                Toast.LENGTH_SHORT).show()
            return
        }
        if (contrasena.length < 6) {
            Toast.makeText(this, "La contraseña debe tener minimo 6 caracteres",
                Toast.LENGTH_SHORT).show()
            return
        }
        // Llamada a Supabase Auth
        lifecycleScope.launch {
            try {
                SupabaseCliente.cliente.auth.signInWith(Email) {
                    email = correo
                    password = contrasena
                }
            //Guarda las credenciales en el dispositivo
                CredencialesManager.guardarCredenciales(this@Login, correo, contrasena, true)
                startActivity(Intent(this@Login, MainActivity::class.java))
                finishAffinity()
            } catch (e: Exception) {
                val mensaje = when {
                    e.message?.contains("Invalid login credentials") == true ->
                        "Correo o contraseña incorrectos"
                    else -> "Error al iniciar sesion: ${e.message}"
                }
                Toast.makeText(this@Login, mensaje,
                    Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun iniciarSesionConGoogle() {
        lifecycleScope.launch {
            try {
                // 1. Configurar la solicitud de Google
                val googleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId("672803385495-uiq2nn5rv91loou9c2jpov2jd7unn3de.apps.googleusercontent.com")
                    .setAutoSelectEnabled(false)
                    .build()
                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()
                // 2. Mostrar el selector de cuentas de Google
                val credentialManager =
                    CredentialManager.create(this@Login)
                val result = credentialManager.getCredential(
                    this@Login, request
                )
                // 3. Obtener el token de Google
                val googleIdTokenCredential =
                    GoogleIdTokenCredential.createFrom(result.credential.data)
                // 4. Enviar el token a Supabase
                SupabaseCliente.cliente.auth.signInWith(IDToken) {
                    idToken = googleIdTokenCredential.idToken
                    provider = Google
                }
                startActivity(Intent(this@Login, MainActivity::class.java))
                finishAffinity()
            } catch (e: Exception) {
                Toast.makeText(
                    this@Login,
                    "Error al iniciar con Google: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun mostrarDialogoHuella (){
        val executor = ContextCompat.getMainExecutor(this)
        val biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                val correo = CredencialesManager.obtenerCorreo(this@Login)
                val contrasena = CredencialesManager.obtenerContrasena(this@Login)
                android.util.Log.d("HUELLA", "correo: $correo, contrasena: $contrasena")
                if (correo != null && contrasena != null) {
                    //singin credenciales normales
                    lifecycleScope.launch {
                        try {
                            SupabaseCliente.cliente.auth.signInWith(Email) {
                                email = correo
                                password = contrasena
                            }

                            startActivity(Intent(this@Login, MainActivity::class.java))
                            finishAffinity()
                        } catch (e: Exception) {
                            runOnUiThread {
                                Toast.makeText(this@Login, "Error al iniciar sesion: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }else{
                    //no hay credenciales- no logueado previamente - limpiar-ocultar
                    Toast.makeText(this@Login, "No hay credenciales", Toast.LENGTH_LONG).show()
                    CredencialesManager.limpiarCredenciales(this@Login)
                }
            }
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                if (errorCode != BiometricPrompt.ERROR_USER_CANCELED &&
                    errorCode != BiometricPrompt.ERROR_NEGATIVE_BUTTON){
                    Toast.makeText(this@Login, "Error biometrico: $errString", Toast.LENGTH_LONG).show()
                }
            }

            override fun onAuthenticationFailed() {
                Toast.makeText(this@Login, "Autenticacion fallida", Toast.LENGTH_LONG).show()

            }
        })
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Acceso con huella")
            .setSubtitle("Usa tu huella dactilar")
            .setNegativeButtonText("Cancelar")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }
}