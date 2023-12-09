package com.example.apkcocina.features.crearReceta.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.apkcocina.R
import com.example.apkcocina.features.crearReceta.diffUtils.ProductoDiff
import com.example.apkcocina.utils.extensions.invisible
import com.example.apkcocina.utils.extensions.visibilityCheck
import com.example.apkcocina.utils.model.Producto
import com.example.apkcocina.utils.model.Tipos
import com.google.android.material.button.MaterialButton

class CrearProductosAdapter(var listProductos: List<Producto>) :
    RecyclerView.Adapter<CrearProductosAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_producto_crear, parent, false))
    }

    override fun getItemCount(): Int = listProductos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private val delete: MaterialButton = view.findViewById(R.id.bt_delete)

        fun bind() {
            val producto = listProductos[adapterPosition]
            val nombre = view.findViewById<AppCompatEditText>(R.id.et_producto_nombre)
            val cantidad = view.findViewById<AppCompatEditText>(R.id.et_producto_cantidad)
            val spinner = view.findViewById<AppCompatSpinner>(R.id.sp_producto_medida)

            delete.invisible()
            nombre.addTextChangedListener { editable ->
                producto.nombre = editable.toString()
            }

            cantidad.addTextChangedListener { editable ->
                producto.cantidad = editable.toString().toFloat()
            }

            delete.setOnClickListener {
                val newList = listProductos.toMutableList()
                newList.remove(producto)
                updateRecetas(newList)
            }

            if(listProductos[adapterPosition].tipo == null){
                val tipos = Tipos.values().map { it.toString() }.toMutableList().apply { add(0,view.context.getString(R.string.unkown)) }
                spinner.adapter = SpinerAdapter(view.context, tipos)
                spinner.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parentView: AdapterView<*>?,
                        selectedItemView: View,
                        spinnerPosition: Int,
                        id: Long
                    ) {
                        producto.changeTipo((selectedItemView as TextView).text.toString())
                    }

                    override fun onNothingSelected(parentView: AdapterView<*>?) {  }
                }
            }
        }

        fun enableBorrar(enable : Boolean){
            delete.visibilityCheck(enable)
        }
    }

    fun updateRecetas(newList: List<Producto>) {
        val diff = ProductoDiff(listProductos, newList)
        val result = DiffUtil.calculateDiff(diff)
        listProductos = newList
        result.dispatchUpdatesTo(this)
    }
}