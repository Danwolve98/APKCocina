package com.example.apkcocina.features.profile.fragment

import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.apkcocina.NavGraphDirections
import com.example.apkcocina.R
import com.example.apkcocina.databinding.FrgRegisterBinding
import com.example.apkcocina.features.profile.viewModel.ProfileViewModel
import com.example.apkcocina.utils.base.APKCocinaActionBar
import com.example.apkcocina.utils.base.BaseFragment
import com.example.apkcocina.utils.base.TitleActionBar
import com.example.apkcocina.utils.extensions.loseFocusAfterAction
import com.example.apkcocina.utils.extensions.onTextChanged
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FrgRegisterBinding>() {

    override lateinit var actionBar: APKCocinaActionBar
    private val profileViewModel: ProfileViewModel by viewModels()

    override fun assingActionBar() {
        actionBar = TitleActionBar(getString(R.string.register))
    }

    override fun initializeView() {
        with(binding){
            btRegistrarse.setOnClickListener{registrar()}

            etCorreoRegister.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            etCorreoRegister.onTextChanged { onFieldChanged() }

            etContrasenaRegister.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            etContrasenaRegister.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
            etContrasenaRegister.onTextChanged { onFieldChanged() }

            etConfContrasenaRegister.loseFocusAfterAction(EditorInfo.IME_ACTION_DONE)
            etConfContrasenaRegister.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }

            etConfContrasenaRegister.onTextChanged {
                if(it != etContrasenaRegister.text.toString())
                    ilConfContrasenaRegister.error = getString(R.string.las_contrasena_deben_coincidir)
                else
                    ilConfContrasenaRegister.error = null
            }

            ilContrasenaRegister.errorIconDrawable = null
            ilConfContrasenaRegister.errorIconDrawable = null
        }
    }

    override fun initializeObservers() {
        lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                profileViewModel.profileViewState.collect { profileViewState ->
                    mainActivity.setLoading(profileViewState.isLoading)
                    binding.ilCorreoRegister.error =
                        if(profileViewState.isValidEmail)null else getString(R.string.email_no_es_valido)
                    binding.ilContrasenaRegister.error =
                        if(profileViewState.isValidPassword.first)null else profileViewState.isValidPassword.second
                }
            }
        }

        with(profileViewModel){

            registerResult.observe(viewLifecycleOwner){ event ->
                event.getContentIfNotHandled()?.let{registerResult->

                }
            }

            registerError.observe(viewLifecycleOwner){ event ->
                event.getContentIfNotHandled()?.let{registerError->
                    Toast.makeText(requireContext(),registerError, Toast.LENGTH_SHORT).show()
                }
            }

            verifiedEmail.observe(viewLifecycleOwner){ event ->
                event.getContentIfNotHandled()?.let{verified->
                    if(verified){
                        Toast.makeText(requireContext(),getString(R.string.verificacion_con_exito),Toast.LENGTH_SHORT).show()
                        navigate(NavGraphDirections.actionGlobalPerfilFragment())
                    }else{
                        Toast.makeText(requireContext(),getString(R.string.hubo_un_problema_al_verificar_la_cuenta),Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun onFieldChanged(hasFocus: Boolean = false) {
        if (!hasFocus) {
            profileViewModel.onFieldsChanged(
                email = binding.etCorreoRegister.text.toString(),
                password = binding.etContrasenaRegister.text.toString()
            )
        }
    }

    private fun registrar() {
        val correo = binding.etCorreoRegister.text.toString()
        val contrasena = binding.etContrasenaRegister.text.toString()
        val confContrasena = binding.etConfContrasenaRegister.text.toString()

        if(contrasena == confContrasena){
            profileViewModel.register(correo,contrasena)
        }
    }
}