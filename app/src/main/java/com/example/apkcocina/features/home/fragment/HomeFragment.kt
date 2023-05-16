package com.example.apkcocina.features.home.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.apkcocina.R
import com.example.apkcocina.databinding.HomeFragmentBinding
import com.example.apkcocina.features.home.adapter.MenuItemsAdapter

class HomeFragment : Fragment() {


    private var _binding : HomeFragmentBinding? = null
    private val binding get() = _binding!!

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

        val listItems : List<String> = resources.getStringArray(R.array.menu_items).toList()
        binding.rvMenu.adapter = MenuItemsAdapter(listItems)
    }


}