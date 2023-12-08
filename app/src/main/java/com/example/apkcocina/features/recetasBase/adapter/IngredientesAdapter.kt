package com.example.apkcocina.features.recetasBase.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.apkcocina.R
import com.google.android.material.textview.MaterialTextView

class IngredientesAdapter (private val listIngrediente : MutableList<String>, context: Context) : RecyclerView.Adapter<IngredientesAdapter.ViewHolder>() {
    init {
        listIngrediente.apply {
            add(0,context.getString(R.string.ingredientes))
            add("")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_ingrediente,parent,false))
    override fun getItemCount(): Int = listIngrediente.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(listIngrediente[position])

    inner class ViewHolder(val view : View) : RecyclerView.ViewHolder(view){
        fun bind(ingrediente : String){
            val textView = view.findViewById<MaterialTextView>(R.id.tv_ingrediente)
            textView.text = ingrediente
        }
    }

}