package com.example.apkcocina.features.crearReceta.diffUtils

import androidx.recyclerview.widget.DiffUtil
import com.example.apkcocina.utils.model.Descripcion

class DescripcionDiff(private val oldList: List<Descripcion>, private val newList: List<Descripcion>) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = oldList[oldItemPosition] == newList[newItemPosition]
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = oldList[oldItemPosition] == newList[newItemPosition]
}