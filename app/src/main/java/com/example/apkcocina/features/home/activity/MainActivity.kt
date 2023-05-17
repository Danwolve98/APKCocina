package com.example.apkcocina.features.home.activity

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


class MainActivity : AppCompatActivity() {

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
        setupWithNavController(binding.bottomNavigationView,navController)
    }

    fun configureActionBar(fragment : BaseFragment){
        val actionBar = fragment.actionBar
        binding.apply {
            ivLogo.invisible()
            btFilterActionBar.invisible()
            btSeachActionBar.invisible()
            ivLogo.invisible()
            tvTittleActionBar.text = actionBar.title

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


}