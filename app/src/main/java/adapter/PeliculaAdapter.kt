package com.example.ud07cp01.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ud07cp01.R
import com.example.ud07cp01.model.Pelicula

class PeliculaAdapter(
    private val peliculas: List<Pelicula>,
    private val onClick: (Pelicula) -> Unit,
    private val onEliminar: (Pelicula) -> Unit,
    private val onEditar: (Pelicula) -> Unit
) : RecyclerView.Adapter<PeliculaAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val ivPoster: ImageView = view.findViewById(R.id.ivPoster)
        private val tvNombre: TextView = view.findViewById(R.id.tvNombre)

        fun bind(pelicula: Pelicula) {
            ivPoster.setImageResource(pelicula.imagen)
            tvNombre.text = pelicula.nombre
            itemView.setOnClickListener { onClick(pelicula) }

            itemView.setOnCreateContextMenuListener { menu, _, _ ->
                menu.setHeaderTitle(pelicula.nombre)
                menu.add(R.string.eliminar).setOnMenuItemClickListener {
                    onEliminar(pelicula); true
                }
                menu.add(R.string.editar).setOnMenuItemClickListener {
                    onEditar(pelicula); true
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_pelicula, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(peliculas[position])

    override fun getItemCount() = peliculas.size
}