package com.example.apkcocina.features.home.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.navigation.NavDirections
import com.example.apkcocina.R
import com.example.apkcocina.databinding.FrgHomeBinding
import com.example.apkcocina.MainActivity
import com.example.apkcocina.features.home.adapter.MenuItemsAdapter
import com.example.apkcocina.utils.base.APKCocinaActionBar
import com.example.apkcocina.utils.base.BaseFragment
import com.example.apkcocina.utils.base.PrincipalActionBar

class HomeFragment() : BaseFragment<FrgHomeBinding>() {

    override lateinit var actionBar: APKCocinaActionBar

    override fun assingActionBar() {
        actionBar = PrincipalActionBar(getString(R.string.inicio))
    }

    override fun initializeView() {
        val listItems : List<String> = resources.getStringArray(R.array.menu_items).toList()
        binding.rvMenu.adapter = MenuItemsAdapter(listItems,navController)
    }

}