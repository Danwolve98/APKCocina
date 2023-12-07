package com.example.apkcocina

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUiSaveStateControl
import com.example.apkcocina.databinding.ActivityMainBinding
import com.example.apkcocina.utils.base.APKCocinaActionBar
import com.example.apkcocina.utils.base.FilterActionBar
import com.example.apkcocina.utils.base.InfoActionBar
import com.example.apkcocina.utils.base.PrincipalActionBar
import com.example.apkcocina.utils.extensions.invisible
import com.example.apkcocina.utils.extensions.notNull
import com.example.apkcocina.utils.extensions.visible
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class APKCocinaActivity : AppCompatActivity() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding : ActivityMainBinding
    private val navController by lazy { (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition{false}

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val analytics = FirebaseAnalytics.getInstance(this)
        analytics.logEvent("InitScreen",Bundle().apply { putString("message","Integración con FireBase completada") })

        initializeView()
    }

    @OptIn(NavigationUiSaveStateControl::class)
    private fun initializeView() {
        NavigationUI.setupWithNavController(binding.bottomNavigationView,navController,false)

        binding.btBackPressedActionBar.setOnClickListener{
            onSupportNavigateUp()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    fun configureActionBar(actionBar : APKCocinaActionBar){
        binding.apply {
            ivLogo.invisible()
            btFilterActionBar.invisible()
            btSeachActionBar.invisible()
            btInfoActionBar.invisible()
            ivLogo.invisible()
            tvTittleActionBar.text = actionBar.title

            if(actionBar.haveBack) btBackPressedActionBar.visible()
            else btBackPressedActionBar.invisible()

            when(actionBar){
                is PrincipalActionBar->{
                    ivLogo.visible()
                }

                is FilterActionBar ->{
                    btFilterActionBar.visible()
                    btSeachActionBar.visible()
                    btFilterActionBar.setOnClickListener { actionBar.searchFunction.notNull() }
                    btSeachActionBar.setOnClickListener { actionBar.filterFunction.notNull() }
                }

                is InfoActionBar ->{
                    btInfoActionBar.visible()
                    btInfoActionBar.setOnClickListener { actionBar.infoLayoutFunction.notNull() }
                }
            }
        }
    }

    fun setLoading(loading: Boolean){
        if(loading){
            binding.clLoading.visible()
            binding.bottomNavigationView.isClickable = false
        }else{
            binding.clLoading.invisible()
            binding.bottomNavigationView.isClickable = true
        }
    }


    fun navigate(accion: Int,bundle: Bundle? = null){
        navController.navigate(accion,bundle)
    }

    /**
     * Función para cambiar entre los menús de abajo manualmente desde código
     * @param menuItemId el id del botón a "pulsar"
     */
    fun changeMenuItem(menuItemId : Int) { binding.bottomNavigationView.selectedItemId = menuItemId }

}