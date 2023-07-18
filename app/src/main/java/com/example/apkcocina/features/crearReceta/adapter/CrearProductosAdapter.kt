package com.example.apkcocina.features.crearReceta.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.apkcocina.R
import com.example.apkcocina.utils.model.Producto

class CrearProductosAdapter(var listProductos : List<Producto>) : RecyclerView.Adapter<CrearProductosAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_producto_crear,parent,false))
    }

    override fun getItemCount(): Int = listProductos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int){

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val nombre = view.findViewById<EditText>(R.id.et_producto_nombre)
    }

    fun updateRecetas(newList:List<Producto>){
        val diff = ProductoDiff(listProductos,newList)
        val result = DiffUtil.calculateDiff(diff)
        listProductos = newList
        result.dispatchUpdatesTo(this)
    }
}