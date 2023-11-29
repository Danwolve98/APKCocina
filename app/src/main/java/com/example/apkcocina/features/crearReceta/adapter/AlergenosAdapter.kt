package com.example.apkcocina.features.crearReceta.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.apkcocina.R
import com.example.apkcocina.utils.extensions.playAnimation
import com.example.apkcocina.utils.model.Alergenos
import com.google.android.material.button.MaterialButton

class AlergenosAdapter (val alergenos : List<Alergenos>) : RecyclerView.Adapter<AlergenosAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_alergeno_receta,parent,false))
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = alergenos.size

    inner class ViewHolder(val view : View) : RecyclerView.ViewHolder(view){
        fun bind(){
            val mb = view.findViewById<MaterialButton>(R.id.iv_alergeno)
            mb.setOnClickListener { it.playAnimation(R.anim.click_animation) }
            mb.addOnCheckedChangeListener { button, isChecked ->
                alergenos[adapterPosition].checked = isChecked
            }
            mb.icon = ResourcesCompat.getDrawable(view.context.resources,alergenos[adapterPosition].drawableId,null)
        }
    }

    fun getSelectedAlergenos() : List<Alergenos> = alergenos.filter { it.checked}

}