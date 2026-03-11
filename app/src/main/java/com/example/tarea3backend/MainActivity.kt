package com.example.tarea3backend

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.ResponseBody
import okhttp3.OkHttpClient

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // 1. Configurar Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.100.50:5000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiService::class.java)
        //conectarAlBackend()
        configurarBotones(api)
    }

   /* private fun conectarAlBackend() {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.100.50:5000") // Tu IP local
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //la api sitve para convertir los objetos kotlin a JSON
        val api = retrofit.create(ApiService::class.java)

        lifecycleScope.launch {
            try {
                val response = api.verificarApi()
                val tv = findViewById<TextView>(R.id.miTextView)

                if (response.isSuccessful) {
                    // Si todo sale bien (Ejercicio 1) [cite: 63, 64]
                    tv.text = response.body()?.string() ?: "Respuesta vacía"
                } else {
                    tv.text = "Error del servidor: ${response.code()}"
                }
            } catch (e: Exception) {
                // Manejo de errores
                findViewById<TextView>(R.id.miTextView).text = "Error de red: ${e.message}"
            }
        }
    }*/

    private fun configurarBotones(api: ApiService) {
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val etUser = findViewById<EditText>(R.id.etUsername)
        val etPass = findViewById<EditText>(R.id.etPassword)
        val tvStatus = findViewById<TextView>(R.id.miTextView)

        btnRegister.setOnClickListener {
            val user = etUser.text.toString()
            val pass = etPass.text.toString()

            if (user.isEmpty() || pass.isEmpty()) {
                tvStatus.text = "Por favor llena todos los campos"
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    val response = api.registrarUsuario(UserRequest(user, pass))

                    if (response.isSuccessful) {
                        // Caso exitoso
                        tvStatus.text = response.body()?.message
                    } else {
                        // Caso de usuario duplicado u otro error
                        tvStatus.text = "Error: Usuario ya existe"
                    }
                } catch (e: Exception) {
                    tvStatus.text = "Error de conexión"
                }
            }
        }

        btnLogin.setOnClickListener {
            val user = etUser.text.toString().trim()
            val pass = etPass.text.toString().trim()


            android.util.Log.d("DEBUG_ESCOM", "Usuario: '$user', Pass: '$pass'")
            lifecycleScope.launch {
                try {
                    val response = api.loginUsuario(UserRequest(user, pass))

                    //log para ver que pasa
                    android.util.Log.d("DEBUG_LOGIN", "Status Code: ${response.code()}")


                    if (response.isSuccessful) {
                        // SI EL LOGIN ES CORRECTO: Navegamos a la otra pantalla
                        val intent = Intent(this@MainActivity, Bienvenida::class.java)
                        intent.putExtra("USER_NAME", user) // Pasamos el nombre
                        startActivity(intent)
                    } else {
                        tvStatus.text = "Error: Usuario o contraseña incorrectos"
                    }
                } catch (e: Exception) {
                    tvStatus.text = "Error de red: No se pudo conectar"
                }
            }
        }

    }

}