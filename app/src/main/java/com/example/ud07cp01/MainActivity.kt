package com.example.ud07cp01

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.example.ud07cp01.adapter.PeliculaAdapter
import com.example.ud07cp01.model.Pelicula
import com.example.ud07cp01.provider.PeliculaProvider

class MainActivity : AppCompatActivity() {

    private lateinit var recycler: RecyclerView
    private lateinit var swipe: SwipeRefreshLayout
    private lateinit var adapter: PeliculaAdapter
    private val peliculas = mutableListOf<Pelicula>()

    private val editarLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val id = result.data?.getIntExtra(EditarActivity.EXTRA_ID, -1) ?: -1
                val nombre = result.data?.getStringExtra(EditarActivity.EXTRA_NOMBRE)
                val pos = peliculas.indexOfFirst { it.id == id }
                if (pos >= 0 && nombre != null) {
                    peliculas[pos].nombre = nombre
                    adapter.notifyItemChanged(pos)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler = findViewById(R.id.recycler)
        swipe = findViewById(R.id.swipe)

        adapter = PeliculaAdapter(
            peliculas,
            onClick = { p ->
                startActivity(
                    Intent(this, DetalleActivity::class.java)
                        .putExtra(DetalleActivity.EXTRA_PELICULA, p)
                )
            },
            onEliminar = { p -> confirmarEliminar(p) },
            onEditar = { p ->
                editarLauncher.launch(
                    Intent(this, EditarActivity::class.java)
                        .putExtra(EditarActivity.EXTRA_ID, p.id)
                        .putExtra(EditarActivity.EXTRA_NOMBRE, p.nombre)
                        .putExtra(EditarActivity.EXTRA_IMAGEN, p.imagen)
                )
            }
        )
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        swipe.setOnRefreshListener { recargar() }
        recargar()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_recargar -> { recargar(); true }
        R.id.action_limpiar -> { confirmarLimpiar(); true }
        else -> super.onOptionsItemSelected(item)
    }

    private fun recargar() {
        peliculas.clear()
        peliculas.addAll(PeliculaProvider.listaCarga.map { it.copy() })
        adapter.notifyDataSetChanged()
        swipe.isRefreshing = false
    }

    private fun confirmarLimpiar() {
        AlertDialog.Builder(this)
            .setTitle(R.string.titulo_limpiar)
            .setMessage(R.string.mensaje_limpiar)
            .setNegativeButton(R.string.cerrar, null)
            .setPositiveButton(R.string.aceptar) { _, _ ->
                peliculas.clear()
                adapter.notifyDataSetChanged()
                Snackbar.make(recycler, R.string.lista_eliminada, Snackbar.LENGTH_SHORT).show()
            }
            .show()
    }

    private fun confirmarEliminar(p: Pelicula) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.titulo_eliminar, p.nombre))
            .setMessage(getString(R.string.mensaje_eliminar, p.nombre))
            .setNegativeButton(R.string.cerrar, null)
            .setPositiveButton(R.string.aceptar) { _, _ ->
                val pos = peliculas.indexOfFirst { it.id == p.id }
                if (pos >= 0) {
                    peliculas.removeAt(pos)
                    adapter.notifyItemRemoved(pos)
                }
            }
            .show()
    }
}