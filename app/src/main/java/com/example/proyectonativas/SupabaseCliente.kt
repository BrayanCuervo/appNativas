package com.example.proyectonativas

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseCliente {
    val cliente = createSupabaseClient(
        supabaseUrl = "https://tagzrsjvciqarvpemgij.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InRhZ3pyc2p2Y2lxYXJ2cGVtZ2lqIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzU5MzE0NjAsImV4cCI6MjA5MTUwNzQ2MH0.UTJu6Mr_TJGLlZsMuZnyX3I3mhWsr7zi4RrtfkJzD_M"
    ){
        install(Postgrest)
        install(Auth)
    }
}