package com.example.apkcocina.features.crearReceta.adapter

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.apkcocina.R

class SpinerAdapter(context: Context,recursos : List<String>) : ArrayAdapter<String>(context, R.layout.sp_item,recursos) {

    init {
        setDropDownViewResource(R.layout.sp_item)
    }

    override fun isEnabled(position: Int): Boolean = position != 0

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view =  (super.getDropDownView(position, convertView, parent) as TextView)
        if(position == 0)
            view.setTextColor(Color.GRAY)
        return view
    }

}