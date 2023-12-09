package com.example.apkcocina.utils.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import com.example.apkcocina.R
import com.example.apkcocina.utils.extensions.invisible
import com.example.apkcocina.utils.extensions.notNull
import com.example.apkcocina.utils.extensions.playAnimation
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.button.MaterialButton
import java.lang.invoke.ConstantCallSite

class InfoRecetasDialog(context : Context, private val showManual : Boolean = true) : Dialog(context) {
    init {
        setCancelable(true)
        setCanceledOnTouchOutside(true)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dia_info_recetas)
        val btCancel = findViewById<MaterialButton>(R.id.bt_cancel)
        val cl = findViewById<ConstraintLayout>(R.id.cl_dialog_info_receta)

        window.notNull{
            it.setBackgroundDrawable(ColorDrawable(ResourcesCompat.getColor(context.resources,R.color.black_trans,null)))
            it.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        cl.setOnClickListener { dismiss() }
        btCancel.setOnClickListener {
            it.playAnimation(R.anim.click_animation)
            dismiss()
        }

        if(!showManual){
            findViewById<FlexboxLayout>(R.id.fl_manual).invisible()
            findViewById<AppCompatTextView>(R.id.tv_manual).invisible()
        }
    }


}