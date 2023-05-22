package com.example.apkcocina.features.home.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.apkcocina.R
import com.example.apkcocina.databinding.ActivityMainBinding
import com.example.apkcocina.utils.base.BaseFragment
import com.example.apkcocina.utils.base.FilterActionBar
import com.example.apkcocina.utils.base.PrincipalActionBar
import com.example.apkcocina.utils.base.TitleActionBar
import com.example.apkcocina.utils.extensions.invisible
import com.example.apkcocina.utils.extensions.visible
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.intellij.lang.annotations.JdkConstants.TitledBorderTitlePosition


class MainActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    var user : FirebaseUser? = null
    private lateinit var binding : ActivityMainBinding
    private val navController by lazy { (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val analytics = FirebaseAnalytics.getInstance(this)
        analytics.logEvent("InitScreen",Bundle().apply { putString("message","Integración con FireBase completada") })

        initializeView()
    }

    private fun initializeView() {
        firebaseAuth = FirebaseAuth.getInstance()
        //firebaseAuth.signOut()
        user = firebaseAuth.currentUser
        setupWithNavController(binding.bottomNavigationView,navController)

        binding.btBackPressedActionBar.setOnClickListener{
            onSupportNavigateUp()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    fun configureActionBar(fragment : BaseFragment){
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
    }

    fun setCurrentUser(newUser : FirebaseUser) {
        user = newUser
    }


    fun navigate(accion: Int){
        navController.navigate(accion)
    }



}