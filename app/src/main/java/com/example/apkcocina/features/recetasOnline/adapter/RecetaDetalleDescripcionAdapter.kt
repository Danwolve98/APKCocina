package com.example.apkcocina.features.recetasOnline.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.apkcocina.R
import com.example.apkcocina.utils.extensions.loadImage
import com.example.apkcocina.utils.model.Descripcion
import com.google.android.material.imageview.ShapeableImageView
import java.lang.IllegalArgumentException

class RecetaDetalleDescripcionAdapter(var listDescripcion : List<Descripcion>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    enum class ViewType(val int : Int){
        TEXT(0),
        IMAGE(1)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder = when(viewType){
        ViewType.IMAGE.int-> ViewHolderImagen(LayoutInflater.from(parent.context).inflate(R.layout.item_foto,parent,false))
        ViewType.TEXT.int-> ViewHolderTexto(LayoutInflater.from(parent.context).inflate(R.layout.item_texto,parent,false))
        else -> throw IllegalArgumentException("Error tipo, no identificado")
    }

    override fun getItemCount(): Int = listDescripcion.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is ViewHolderTexto->holder.bind()
            is ViewHolderImagen->holder.bind(listDescripcion[position].string)
        }
    }

    override fun getItemViewType(position: Int): Int =
        if(listDescripcion[position].image)
            ViewType.IMAGE.int
        else
            ViewType.TEXT.int

    inner class ViewHolderTexto(val view : View) : RecyclerView.ViewHolder(view){
        fun bind(){
            val text = view.findViewById<AppCompatTextView>(R.id.tv_descripcion_item)
            text.text = listDescripcion[adapterPosition].string
        }
    }

    inner class ViewHolderImagen(val view : View) : RecyclerView.ViewHolder(view){
        fun bind(foto : String){
            val textView = view.findViewById<ShapeableImageView>(R.id.iv_descripcion_item)
            textView.loadImage(foto)
        }
    }

}