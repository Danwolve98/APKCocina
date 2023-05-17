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

class HomeFragment : BaseFragment() {

    override lateinit var actionBar: APKCocinaActionBar
    private var _binding : HomeFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = HomeFragmentBinding.inflate(inflater,container,false)
        return binding.root
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
        binding.rvMenu.adapter = MenuItemsAdapter(listItems)
    }


}