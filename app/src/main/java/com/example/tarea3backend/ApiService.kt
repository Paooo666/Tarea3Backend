package com.example.tarea3backend

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import com.google.gson.annotations.SerializedName

// Estructura del JSON que se envia a Node.js
data class UserRequest(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String
)

// Estructura del JSON que se recibe de Node.js
data class AuthResponse(
    @SerializedName("message") val message: String
)

interface ApiService {
    @GET("/")
    suspend fun verificarApi(): Response<ResponseBody>

    @POST("/register")
    suspend fun registrarUsuario(@Body user: UserRequest): Response<AuthResponse>

    @POST("/login")
    suspend fun loginUsuario(@Body user: UserRequest): Response<AuthResponse>
}