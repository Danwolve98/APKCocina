package com.example.apkcocina.features.profile.fragment

import android.app.AlertDialog
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.viewModels
import com.example.apkcocina.NavGraphDirections
import com.example.apkcocina.R
import com.example.apkcocina.databinding.FrgLoginBinding
import com.example.apkcocina.features.profile.viewModel.ProfileViewModel
import com.example.apkcocina.utils.base.APKCocinaActionBar
import com.example.apkcocina.utils.base.BaseFragment
import com.example.apkcocina.utils.base.TitleActionBar
import com.example.apkcocina.utils.extensions.collectFlow
import com.example.apkcocina.utils.extensions.loseFocusAfterAction
import com.example.apkcocina.utils.extensions.onTextChanged
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment() : BaseFragment<FrgLoginBinding>() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    override lateinit var actionBar: APKCocinaActionBar

    private val profileViewModel: ProfileViewModel by viewModels()

    override fun assingActionBar() {
        actionBar = TitleActionBar(getString(R.string.iniciar_sesion)).apply { haveBack = false }
    }

    override fun initializeView() {
        with(binding){
            btIniciarSesion.setOnClickListener { login() }
            btRegistrarse.setOnClickListener { navigate(R.id.action_loginFragment_to_registerFragment) }
            //FIXME IMPLEMENTAR
            tvSiHaOlvidadoSuContrasena.setOnClickListener {}

            ilContrasenaLogin.errorIconDrawable = null

            etCorreoLogin.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            etCorreoLogin.onTextChanged { onFieldChanged() }

            etContrasenaLogin.loseFocusAfterAction(EditorInfo.IME_ACTION_DONE)
            etContrasenaLogin.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
            etContrasenaLogin.onTextChanged { onFieldChanged() }
        }
    }

    private fun onFieldChanged(hasFocus: Boolean = false) {
        if (!hasFocus) {
            profileViewModel.onFieldsChanged(
                email = binding.etCorreoLogin.text.toString(),
                password = binding.etContrasenaLogin.text.toString()
            )
        }
    }

    override fun initializeObservers() {
        collectFlow(profileViewModel.profileViewState){profileViewState->
            mainActivity.setLoading(profileViewState.isLoading)
            binding.ilCorreoLogin.error =
                if(profileViewState.isValidEmail)null else getString(R.string.email_no_es_valido)
            binding.ilContrasenaLogin.error =
                if(profileViewState.isValidPassword.first)null else profileViewState.isValidPassword.second

            binding.btIniciarSesion.isEnabled =
                profileViewState.isValidEmail &&
                        profileViewState.isValidPassword.first &&
                        !binding.etCorreoLogin.text.isNullOrEmpty() &&
                        !binding.etContrasenaLogin.text.isNullOrEmpty()
        }

        with(profileViewModel){
            loginResult.observe(viewLifecycleOwner){ event ->
                event.getContentIfNotHandled()?.let{loginResult->
                    navigate(NavGraphDirections.actionGlobalPerfilFragment())
                }
            }

            loginError.observe(viewLifecycleOwner){ event ->
                event.getContentIfNotHandled()?.let{loginError->
                    Toast.makeText(requireContext(),loginError,Toast.LENGTH_SHORT).show()
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
    private fun login() {
        val correo = binding.etCorreoLogin.text.toString()
        val contrasena = binding.etContrasenaLogin.text.toString()
        profileViewModel.login(correo,contrasena)
    }
}

