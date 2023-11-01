package com.example.apkcocina

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.viewbinding.ViewBinding
import com.example.apkcocina.databinding.ActivityMainBinding
import com.example.apkcocina.utils.base.BaseFragment
import com.example.apkcocina.utils.base.FilterActionBar
import com.example.apkcocina.utils.base.PrincipalActionBar
import com.example.apkcocina.utils.extensions.invisible
import com.example.apkcocina.utils.extensions.navigateToMenuItem
import com.example.apkcocina.utils.extensions.visible
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

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

    private fun initializeView() {
        firebaseAuth.signOut()

        binding.bottomNavigationView.apply {
            setupWithNavController(navController)
            setOnItemSelectedListener {
                navController.navigateToMenuItem(it)
            }
        }


        binding.btBackPressedActionBar.setOnClickListener{
            onSupportNavigateUp()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    fun<vb : ViewBinding> configureActionBar(fragment : BaseFragment<vb>){
        val actionBar = fragment.actionBar
        binding.apply {
            ivLogo.invisible()
            btFilterActionBar.invisible()
            btSeachActionBar.invisible()
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
                }
            }
        }
    }

    fun setLoading(loading: Boolean){
        if(loading){
            binding.clLoading.visible()
        }else{
            binding.clLoading.invisible()
        }
        Log.e("ACTIVO",binding.clLoading.isVisible.toString())
    }


    fun navigate(accion: Int,bundle: Bundle? = null){
        navController.navigate(accion,bundle)
    }

}