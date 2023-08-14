package com.example.apkcocina.features.profile.fragment

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.apkcocina.R
import com.example.apkcocina.databinding.LoginFragmentBinding
import com.example.apkcocina.features.home.activity.MainActivity
import com.example.apkcocina.features.profile.viewModel.ProfileViewModel
import com.example.apkcocina.utils.extensions.invisible
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding : LoginFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainActivity : MainActivity
    private var frameLayout : FragmentContainerView? = null

    @Inject
    lateinit var fireBaseAuth : FirebaseAuth
    private lateinit var authListener : AuthStateListener

    private val profileViewModel: ProfileViewModel by viewModels()
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = requireActivity() as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LoginFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        frameLayout = requireActivity().findViewById(R.id.fragment_container_login)
        initializeView()
    }

    private fun initializeView() {
        binding.apply {
            btRegistrarse.setOnClickListener { registrar() }
            btIniciarSesion.setOnClickListener { login() }
        }

        initialiceListeners()
    }


    private fun initialiceListeners() {
        lifecycleScope.launch(Dispatchers.IO) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                profileViewModel.profileViewState.collect { profileViewState ->
                    withContext(Dispatchers.Main){ mainActivity.setLoading(profileViewState.isLoading) }
                }
            }
        }

        profileViewModel.loginResult.observe(viewLifecycleOwner){ event ->
            event.getContentIfNotHandled()?.let{loginResult->
                mainActivity.setCurrentUser(loginResult.user)
                frameLayout?.invisible()
            }
        }

        profileViewModel.loginError.observe(viewLifecycleOwner){ event ->
            event.getContentIfNotHandled()?.let{loginError->
                Toast.makeText(requireContext(),loginError,Toast.LENGTH_SHORT).show()
            }
        }

        profileViewModel.registerResult.observe(viewLifecycleOwner){ event ->
            event.getContentIfNotHandled()?.let{registerResult->
                mainActivity.setCurrentUser(registerResult.user)
            }
        }

        profileViewModel.verifiedEmail.observe(viewLifecycleOwner){ event ->
            event.getContentIfNotHandled()?.let{verified->
                if(verified){
                    Toast.makeText(requireContext(),getString(R.string.verificacion_con_exito),Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(requireContext(),"Hubo un problema al verificar la cuenta",Toast.LENGTH_SHORT).show()
                }
            }
        }

        profileViewModel.registerError.observe(viewLifecycleOwner){ event ->
            event.getContentIfNotHandled()?.let{registerError->
                Toast.makeText(requireContext(),registerError,Toast.LENGTH_SHORT).show()
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

    private fun registrar() {
        val correo = binding.etCorreoLogin.text.toString()
        val contrasena = binding.etContrasenaLogin.text.toString()
        profileViewModel.register(correo,contrasena)
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

    private fun hideFragment(){
       mainActivity.findViewById<FragmentContainerView>(R.id.fragment_container_login).invisible()
    }
}

