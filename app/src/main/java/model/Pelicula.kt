package com.example.ud07cp01.model

import java.io.Serializable

data class Pelicula(
    val id: Int,
    var nombre: String,
    val descripcion: String,
    val imagen: Int,
    val duracion: Int,
    val anio: Int,
    val nacionalidad: String
) : Serializable