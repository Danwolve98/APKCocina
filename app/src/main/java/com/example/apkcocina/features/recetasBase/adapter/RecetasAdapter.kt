package com.example.apkcocina.features.recetasBase.adapter

import android.graphics.drawable.Drawable
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
import com.example.apkcocina.utils.model.Receta
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class RecetasAdapter(var listRecetas : List<Receta>, val onClickRecetaListener : (Receta) -> Unit, var firebaseStorage: FirebaseStorage):RecyclerView.Adapter<RecetasAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_receta,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.renderReceta(listRecetas[position])
    }

    override fun getItemCount(): Int = listRecetas.size

    //TODO MANEJAR POSIBLE ERROR
    inner class ViewHolder(val view : View) : RecyclerView.ViewHolder(view) {

        fun renderReceta(receta: Receta) {
            val iv_receta = view.findViewById<ImageView>(R.id.iv_receta)
            val tv_receta = view.findViewById<TextView>(R.id.tv_nombre_receta)

            view.setOnClickListener { onClickRecetaListener(receta) }

            }
    }
}
