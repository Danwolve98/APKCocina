package com.example.apkcocina.features.home.adapter

import android.content.res.ColorStateList
import android.graphics.ColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.apkcocina.R
import com.example.apkcocina.features.home.fragment.HomeFragmentDirections
import com.example.apkcocina.features.settings.fragment.SettingsFragment
import com.example.apkcocina.utils.extensions.applyBrightness
import com.example.apkcocina.utils.extensions.applySaturation
import com.example.apkcocina.utils.extensions.invisible
import com.example.apkcocina.utils.extensions.notNull
import com.example.apkcocina.utils.extensions.visible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.collection.LLRBNode.Color
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

class MenuItemsAdapter(private var listItems : List<String>, val navController: NavController,val logged : Boolean) : RecyclerView.Adapter<MenuItemsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_menu,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.render(listItems[position],position)
    }

    override fun getItemCount(): Int = listItems.size

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view){
        fun render(item : String, position: Int){
            val imageView = view.findViewById<ImageView>(R.id.iv_item_menu)
            val textView =  view.findViewById<TextView>(R.id.tv_item_menu)
            val whiteBorder =  view.findViewById<View>(R.id.v_white_border)
            textView.text = item
            var imageID = 0

           when(position){

                0 -> {
                    imageID = R.drawable.im_recetas_base
                    view.setOnClickListener { navController.navigate(R.id.action_inicio_fragment_to_recetasBaseFragment) }
                }
                1->{
                    imageID = R.drawable.im_recetas_online
                   if(logged)
                       view.setOnClickListener { navController.navigate(R.id.action_inicio_fragment_to_recetasOnlineFragment) }
                    else {
                       disableBoton(imageView,textView,view,whiteBorder)
                   }

                }
                2->{
                    imageID = R.drawable.im_crear_receta
                    if(logged)
                        view.setOnClickListener { navController.navigate(R.id.action_inicio_fragment_to_crearRecetaFragment) }
                    else{
                        disableBoton(imageView,textView,view,whiteBorder)
                    }
                }
                3->{
                    imageID = R.drawable.im_que_cocino_hoy
                    if(logged)
                        view.setOnClickListener { navController.navigate(R.id.action_inicio_fragment_to_queCocinoHoyFragment) }
                    else {
                        disableBoton(imageView,textView,view,whiteBorder)
                    }
                }
                4->{
                    imageID = R.drawable.im_mas_info
                    view.setOnClickListener { navController.navigate(HomeFragmentDirections.actionInicioFragmentToInformacionFragment(SettingsFragment.INFORMACION_INFO)) }
                }
                else -> imageID = 0
            }

            imageView.setImageResource(imageID)
        }

        private fun disableBoton(imageView : ImageView,textView : TextView,view : View,whiteBorder : View){
            view.setOnClickListener {
                activarAnimacion(whiteBorder)
            }
            textView.setTextColor(view.context.getColor(R.color.white_trans))
            imageView.applyBrightness(0.2f)
            imageView.applySaturation(0.2f)
        }

        private fun activarAnimacion(view : View){
            view.visible()
            Toast.makeText(view.context, view.context.getString(R.string.inicie_sesion_para_esta_funcionalidad), Toast.LENGTH_SHORT).show()
            val animacion = AlphaAnimation(0f,1f).apply {
                duration = 1000L
                repeatCount = 1
                repeatMode = AlphaAnimation.REVERSE
            }
            animacion.setAnimationListener(object : Animation.AnimationListener{
                override fun onAnimationStart(p0: Animation?) {}
                override fun onAnimationEnd(p0: Animation?) { view.invisible() }
                override fun onAnimationRepeat(p0: Animation?) {}
            })

            view.startAnimation(animacion)
        }
    }
}