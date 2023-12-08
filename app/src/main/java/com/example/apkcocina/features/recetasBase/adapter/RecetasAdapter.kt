package com.example.apkcocina.features.recetasBase.adapter

import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.apkcocina.R
import com.example.apkcocina.utils.extensions.loadImage
import com.example.apkcocina.utils.model.Receta
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class RecetasAdapter(private var listRecetas : List<Receta>, val onClickRecetaListener : (Receta) -> Unit):RecyclerView.Adapter<RecetasAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_receta,parent,false))
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.renderReceta(listRecetas[position])
    override fun getItemCount(): Int = listRecetas.size

    inner class ViewHolder(val view : View) : RecyclerView.ViewHolder(view) {

        fun renderReceta(receta: Receta) {
            val ivReceta = view.findViewById<ImageView>(R.id.iv_receta)
            val tvReceta = view.findViewById<TextView>(R.id.tv_nombre_receta)
            val tvTiempoReceta = view.findViewById<TextView>(R.id.tv_tiempo_receta)

            ivReceta.loadImage(receta.descripcion?.get(1)?.string)
            tvReceta.text = receta.nombre
            tvTiempoReceta.text = formatearTiempo(receta.tiempoPreparacion)

            view.setOnClickListener { onClickRecetaListener(receta) }

        }

        private fun formatearTiempo(tiempo:Int?) : String =
            if(tiempo == null)
                "?"
            else{
                val hours = tiempo / 60
                val minutosRestantes = tiempo % 60

                "%dh/%dmins".format(hours,minutosRestantes)
            }
    }
}
