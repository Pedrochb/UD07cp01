package com.example.ud07cp01

import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ud07cp01.model.Pelicula

class DetalleActivity : AppCompatActivity() {
    companion object { const val EXTRA_PELICULA = "pelicula" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle)

        val p = if (Build.VERSION.SDK_INT >= 33)
            intent.getSerializableExtra(EXTRA_PELICULA, Pelicula::class.java)
        else
            @Suppress("DEPRECATION") intent.getSerializableExtra(EXTRA_PELICULA) as? Pelicula
        if (p == null) { finish(); return }

        findViewById<TextView>(R.id.tvTitulo).text = p.nombre
        findViewById<ImageView>(R.id.ivPoster).setImageResource(p.imagen)
        findViewById<TextView>(R.id.tvDescripcion).text = getString(R.string.descripcion, p.descripcion)
        findViewById<TextView>(R.id.tvAnio).text = getString(R.string.anio, p.anio)
        findViewById<TextView>(R.id.tvDuracion).text = getString(R.string.duracion, p.duracion)
        findViewById<TextView>(R.id.tvNacionalidad).text = getString(R.string.nacionalidad, p.nacionalidad)
    }
}