package com.example.apkcocina.features.profile.fragment

import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.apkcocina.R
import com.example.apkcocina.databinding.FrgReestablecerContrasenaBinding
import com.example.apkcocina.features.profile.viewModel.ProfileViewModel
import com.example.apkcocina.utils.base.APKCocinaActionBar
import com.example.apkcocina.utils.base.BaseFragment
import com.example.apkcocina.utils.base.TitleActionBar
import com.example.apkcocina.utils.extensions.collectFlow
import com.example.apkcocina.utils.extensions.onTextChanged
import com.example.apkcocina.utils.states.ResetPassWordResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReestablecerContrasenaFragment : BaseFragment<FrgReestablecerContrasenaBinding>() {

    val profileViewModel: ProfileViewModel by viewModels()
    override lateinit var actionBar: APKCocinaActionBar

    override fun assingActionBar() {
        actionBar = TitleActionBar(getString(R.string.reestablecer_contrasena))
    }

    override fun initializeView() {

        binding.etCorreoReset.onTextChanged {
            profileViewModel.onFieldsChanged(it,"")
        }

        binding.btReset.setOnClickListener{
            profileViewModel.sendResetPasswordEmail(binding.etCorreoReset.text.toString())
        }
    }

    override fun initializeObservers() {
        collectFlow(profileViewModel.profileViewState){
            mainActivity.setLoading(it.isLoading)
            binding.btReset.isEnabled = it.isValidEmail
        }

        profileViewModel.resetEmailSent.observe(viewLifecycleOwner){event->
            event.getContentIfNotHandled()?.let{
                when(it){
                    is ResetPassWordResult.Error -> Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                    ResetPassWordResult.Sent -> Toast.makeText(requireContext(), getString(R.string.se_ha_enviado_un_correo_a_tu_email_para_reestablecer_la_contrasena), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}