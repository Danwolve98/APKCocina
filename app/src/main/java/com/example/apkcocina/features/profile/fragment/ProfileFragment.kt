package com.example.apkcocina.features.profile.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.apkcocina.R
import com.example.apkcocina.databinding.ProfileFragmentBinding
import com.example.apkcocina.features.home.activity.MainActivity
import com.example.apkcocina.features.home.adapter.MenuItemsAdapter
import com.example.apkcocina.utils.base.APKCocinaActionBar
import com.example.apkcocina.utils.base.BaseFragment
import com.example.apkcocina.utils.base.PrincipalActionBar
import com.example.apkcocina.utils.base.TitleActionBar
import com.example.apkcocina.utils.extensions.invisible
import com.example.apkcocina.utils.extensions.visible
import com.google.apphosting.datastore.testing.DatastoreTestTrace.TimelineTestTrace
import com.google.firebase.auth.ktx.userProfileChangeRequest

class ProfileFragment : BaseFragment() {

    override lateinit var actionBar: APKCocinaActionBar
    private var _binding : ProfileFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = requireActivity() as MainActivity
        actionBar = TitleActionBar(getString(R.string.perfil)).apply { haveBack=false }
        (activity as MainActivity).configureActionBar(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ProfileFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeView()
    }

    private fun initializeView() {
        val user = mainActivity.user
        binding.apply {
            if (user == null) {
                fragmentContainerLogin.visible()
            } else {
                fragmentContainerLogin.invisible()
                if(user.displayName == null)
                   user.updateProfile(userProfileChangeRequest {
                       displayName = getString(R.string.nombre)
                   })
                tvProfileNombre.text = user.displayName.toString()
            }
        }
    }

}