package com.example.apkcocina.features.profile.fragment

import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.apkcocina.R
import com.example.apkcocina.databinding.FrgReestablecerContrasenaBinding
import com.example.apkcocina.features.profile.viewModel.ProfileViewModel
import com.example.apkcocina.utils.base.APKCocinaActionBar
import com.example.apkcocina.utils.base.BaseFragment
import com.example.apkcocina.utils.base.TitleActionBar
import com.example.apkcocina.utils.extensions.collectFlow
import com.example.apkcocina.utils.extensions.loseFocusAfterAction
import com.example.apkcocina.utils.extensions.onTextChanged
import com.example.apkcocina.utils.states.ResetPassWordResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReestablecerContrasenaFragment : BaseFragment<FrgReestablecerContrasenaBinding>() {

    val profileViewModel: ProfileViewModel by viewModels()
    override lateinit var actionBar: APKCocinaActionBar

    override fun assingActionBar() {
        actionBar = TitleActionBar(getString(R.string.reestablecer_contrasena))
    }

    override fun initializeView() {
        with(binding){

            ilNewPassword.errorIconDrawable = null
            ilOldPassword.errorIconDrawable = null

            etCorreoReset.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            etCorreoReset.onTextChanged {
                profileViewModel.onFieldsChanged(it,"")
            }

            etOldPassword.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            btReset.setOnClickListener{
                profileViewModel.sendResetPasswordEmail(etCorreoReset.text.toString(),etOldPassword.text.toString(),etNewPassword.text.toString())
            }

            etNewPassword.loseFocusAfterAction(EditorInfo.IME_ACTION_DONE)
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
                    ResetPassWordResult.Updated ->{
                        Toast.makeText(requireContext(), "Contraseña cambiada con éxito", Toast.LENGTH_SHORT).show()
                    }
                    ResetPassWordResult.NetWorkProblem ->{
                        Toast.makeText(requireContext(), "problema de red al cambiar la contraseña", Toast.LENGTH_SHORT).show()
                    }
                    ResetPassWordResult.TooManyTrys ->{
                        Toast.makeText(requireContext(), "demasiados intentos al cambiar la contraseña", Toast.LENGTH_SHORT).show()
                    }
                    is ResetPassWordResult.GenericError ->{
                        Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

}