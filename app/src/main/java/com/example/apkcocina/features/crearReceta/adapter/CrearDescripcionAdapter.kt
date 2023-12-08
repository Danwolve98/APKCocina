package com.example.apkcocina.features.crearReceta.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.apkcocina.R
import com.example.apkcocina.features.crearReceta.diffUtils.DescripcionDiff
import com.example.apkcocina.utils.extensions.base64toByteArray
import com.example.apkcocina.utils.extensions.invisible
import com.example.apkcocina.utils.extensions.loadImage
import com.example.apkcocina.utils.extensions.visibilityCheck
import com.example.apkcocina.utils.model.Descripcion
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import java.lang.IllegalArgumentException

class CrearDescripcionAdapter(var listDescripcion : List<Descripcion>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    enum class ViewType(val int : Int){
        TEXT(0),
        IMAGE(1)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder = when(viewType){
            ViewType.IMAGE.int-> ViewHolderImagen(LayoutInflater.from(parent.context).inflate(R.layout.item_foto,parent,false))
            ViewType.TEXT.int-> ViewHolderTexto(LayoutInflater.from(parent.context).inflate(R.layout.item_texto_editable,parent,false))
            else -> throw IllegalArgumentException("Error tipo, no identificado")
    }

    override fun getItemCount(): Int = listDescripcion.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is ViewHolderTexto->holder.bind(listDescripcion[position])
            is ViewHolderImagen->holder.bind(listDescripcion[position])
        }
    }

    override fun getItemViewType(position: Int): Int =
        if(listDescripcion[position].image)
            ViewType.IMAGE.int
        else
            ViewType.TEXT.int

    fun updateDescripcion(newList : List<Descripcion>){
        val diff = DescripcionDiff(listDescripcion,newList)
        val result = DiffUtil.calculateDiff(diff)
        listDescripcion = newList
        result.dispatchUpdatesTo(this)
    }

    inner class ViewHolderTexto(val view : View) : RecyclerView.ViewHolder(view),ViewHolderDeletable{
        val btDelete = view.findViewById<MaterialButton>(R.id.bt_delete)
        fun bind(item : Descripcion){
            btDelete.invisible()
            btDelete.setOnClickListener {
                val newList = listDescripcion.toMutableList()
                newList.remove(item)
                updateDescripcion(newList)
            }
            val editText = view.findViewById<AppCompatEditText>(R.id.tv_descripcion_item)
            editText.addTextChangedListener {
                listDescripcion[adapterPosition].string = it.toString()
            }
        }
        override fun enableBorrar(enable : Boolean){btDelete.visibilityCheck(enable)}
    }

    inner class ViewHolderImagen(val view : View) : RecyclerView.ViewHolder(view),ViewHolderDeletable {
        private val btDelete: MaterialButton = view.findViewById(R.id.bt_delete)
        fun bind(item: Descripcion) {
            btDelete.invisible()
            btDelete.setOnClickListener {
                val newList = listDescripcion.toMutableList()
                newList.remove(item)
                updateDescripcion(newList)
            }
            val textView = view.findViewById<ShapeableImageView>(R.id.iv_descripcion_item)
            textView.loadImage(item.string.base64toByteArray())
        }

        override fun enableBorrar(enable: Boolean) {
            btDelete.visibilityCheck(enable)
        }
    }

}

interface ViewHolderDeletable{
    fun enableBorrar(enable : Boolean)
}