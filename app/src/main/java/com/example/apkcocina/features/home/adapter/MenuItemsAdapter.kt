package com.example.apkcocina.features.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.apkcocina.R
import com.example.apkcocina.features.home.fragment.HomeFragmentDirections
import com.example.apkcocina.features.settings.fragment.SettingsFragment
import com.example.apkcocina.utils.extensions.notNull
import com.example.apkcocina.utils.extensions.visible
import com.google.firebase.auth.FirebaseAuth
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
            view.findViewById<TextView>(R.id.tv_item_menu).text = item
            var imageID = 0

           when(position){
                0 -> {
                    imageID = R.drawable.im_recetas_base
                    view.setOnClickListener { navController.navigate(R.id.action_inicio_fragment_to_recetasBaseFragment) }
                }
                1->{
                    imageID = R.drawable.im_recetas_online
                    view.findViewById<ImageView>(R.id.iv_internet).visible()
                   if(logged)
                       view.setOnClickListener { navController.navigate(R.id.action_inicio_fragment_to_crearRecetaFragment) }
                    else
                       view.isEnabled = false

                }
                2->{
                    imageID = R.drawable.im_crear_receta
                    view.setOnClickListener { navController.navigate(R.id.action_inicio_fragment_to_crearRecetaFragment) }
                }
                3->{
                    imageID = R.drawable.im_que_cocino_hoy
                    view.setOnClickListener { navController.navigate(R.id.action_inicio_fragment_to_queCocinoHoyFragment) }
                }
                4->{
                    imageID = R.drawable.im_mas_info
                    view.setOnClickListener { navController.navigate(HomeFragmentDirections.actionInicioFragmentToInformacionFragment(SettingsFragment.INFORMACION_INFO)) }
                }
                else -> imageID = 0
            }

            view.findViewById<ImageView>(R.id.iv_item_menu).setImageResource(imageID)
        }
    }

}