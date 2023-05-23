package com.example.apkcocina.features.recetasBase.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.apkcocina.R
import com.example.apkcocina.data.model.Receta
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class RecetasAdapter(var listRecetas : List<Receta>,val onClickRecetaListener : (Receta) -> Unit):RecyclerView.Adapter<RecetasAdapter.ViewHolder>(){

    private lateinit var firebaseStorage : StorageReference

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
            val iv_receta = view.findViewById<ImageView>(R.id.iv_item_receta)
            val tv_receta = view.findViewById<TextView>(R.id.tv_item_receta)

            view.setOnClickListener { onClickRecetaListener(receta) }
            firebaseStorage = FirebaseStorage.getInstance().getReference("tortilla_de_patata/tortilla_de_patata.jpg")

            firebaseStorage.downloadUrl.addOnSuccessListener {
                Glide.with(view.context).load(it.toString()).into(iv_receta)
            }



            tv_receta.text = receta.nombre
        }
    }
}