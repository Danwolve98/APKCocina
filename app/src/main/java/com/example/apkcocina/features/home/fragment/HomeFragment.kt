package com.example.apkcocina.features.home.fragment

import com.example.apkcocina.R
import com.example.apkcocina.databinding.FrgHomeBinding
import com.example.apkcocina.features.home.adapter.MenuItemsAdapter
import com.example.apkcocina.utils.base.APKCocinaActionBar
import com.example.apkcocina.utils.base.BaseFragment
import com.example.apkcocina.utils.base.PrincipalActionBar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment() : BaseFragment<FrgHomeBinding>() {

    override lateinit var actionBar: APKCocinaActionBar
    @Inject
    lateinit var auth : FirebaseAuth
    override fun assingActionBar() {
        actionBar = PrincipalActionBar(getString(R.string.inicio))
    }

    override fun initializeView() {
        val listItems : List<String> = resources.getStringArray(R.array.menu_items).toList()
        binding.rvMenu.adapter = MenuItemsAdapter(listItems,navController,auth.currentUser != null)
    }

}