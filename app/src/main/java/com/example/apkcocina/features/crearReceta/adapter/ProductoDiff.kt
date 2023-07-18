package com.example.apkcocina.features.crearReceta.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.apkcocina.utils.extensions.removeAccents
import com.example.apkcocina.utils.model.Producto

class ProductoDiff(val oldList:List<Producto>,val newList:List<Producto>): DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int =newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = (oldList[oldItemPosition].nombre ?: "").trim().removeAccents().equals((newList[newItemPosition].nombre ?: "").trim().removeAccents(),true)

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = oldList[oldItemPosition] == newList[newItemPosition]
}