package com.example.apkcocina.features.profile.fragment

import android.app.AlertDialog
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.ActionBar.NavigationMode
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavGraph
import androidx.navigation.Navigation
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
import kotlinx.coroutines.launch
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
            tvOlvidasteTuContrasena.setOnClickListener { navigate(R.id.action_loginFragment_to_reestablecerContrasenaFragment) }

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

    private fun updateView(et: AppCompatEditText, valid: Boolean) {
        if(!valid)
            et.setTextColor(requireContext().getColor(R.color.red))
        else
            et.setTextColor(requireContext().getColor(R.color.green_base))
    }

    private fun login() {
        val correo = binding.etCorreoLogin.text.toString()
        val contrasena = binding.etContrasenaLogin.text.toString()
        profileViewModel.login(correo,contrasena)
    }

    private fun validEntryTexts(correo : String, contrasena : String) = correo.isNotEmpty() && contrasena.isNotEmpty()

    private fun <T>showAlert(task : Task<T>){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Error")
        builder.setMessage(task.exception.toString())
        builder.setPositiveButton("Aceptar",null)
        val dialog = builder.create()
        dialog.show()
    }

    private fun showAlert(title : String,texto : String){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
        builder.setMessage(texto)
        builder.setPositiveButton("Aceptar",null)
        val dialog = builder.create()
        dialog.show()
    }
}

