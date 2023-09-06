package com.example.apkcocina.features.home.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.apkcocina.R
import com.example.apkcocina.databinding.HomeFragmentBinding
import com.example.apkcocina.features.home.activity.MainActivity
import com.example.apkcocina.features.home.adapter.MenuItemsAdapter
import com.example.apkcocina.utils.base.APKCocinaActionBar
import com.example.apkcocina.utils.base.BaseFragment
import com.example.apkcocina.utils.base.PrincipalActionBar

class HomeFragment() : BaseFragment<HomeFragmentBinding>() {

    override lateinit var actionBar: APKCocinaActionBar
    private lateinit var mainActivity : MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = requireActivity() as MainActivity
    }


    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeView()
    }

    private fun initializeView() {
        actionBar = PrincipalActionBar(getString(R.string.inicio))
        (activity as MainActivity).configureActionBar(this)
        val listItems : List<String> = resources.getStringArray(R.array.menu_items).toList()
        binding.rvMenu.adapter = MenuItemsAdapter(listItems,mainActivity,{onMenuItemClickListener(it)})
    }

    private fun onMenuItemClickListener(fragmentID : Int){
        mainActivity.navigate(fragmentID,null)
    }


}