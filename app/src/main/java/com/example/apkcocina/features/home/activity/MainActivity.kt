package com.example.apkcocina.features.home.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.apkcocina.R
import com.example.apkcocina.databinding.ActivityMainBinding
import com.example.apkcocina.features.home.fragment.HomeFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container,HomeFragment()).commit()

    }

}