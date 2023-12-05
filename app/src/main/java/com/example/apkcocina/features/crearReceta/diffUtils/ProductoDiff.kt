package com.example.apkcocina.features.crearReceta.diffUtils

import androidx.recyclerview.widget.DiffUtil
import com.example.apkcocina.utils.extensions.removeAccents
import com.example.apkcocina.utils.model.Producto

class ProductoDiff(private val oldList: List<Producto>, private val newList: List<Producto>) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]
}