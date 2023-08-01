package com.example.apkcocina.features.profile.fragment

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import com.example.apkcocina.R
import com.example.apkcocina.databinding.LoginFragmentBinding
import com.example.apkcocina.features.home.activity.MainActivity
import com.example.apkcocina.utils.extensions.invisible
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.actionCodeSettings
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding : LoginFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainActivity : MainActivity

    @Inject
    lateinit var fireBaseAuth : FirebaseAuth
    private lateinit var authListener : AuthStateListener


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

        initializeView()
    }

    private fun initializeView() {
        binding.apply {
            btRegistrarse.setOnClickListener { registrar() }
            btIniciarSesion.setOnClickListener { login() }
        }
    }

    private fun login() {
        mainActivity.setLoading(true)
        val correo = binding.etCorreoLogin.text.toString()
        val contrasena = binding.etContrasenaLogin.text.toString()
        if (validEntryTexts(correo, contrasena)) {
            fireBaseAuth.signInWithEmailAndPassword(correo, contrasena)
                .addOnCompleteListener { login ->
                    if (login.isSuccessful && fireBaseAuth.currentUser?.isEmailVerified == true) {
                        mainActivity.setCurrentUser(fireBaseAuth.currentUser!!)
                        showAlert(
                            "Bienvenido",
                            "Bienvenido ${fireBaseAuth.currentUser!!.displayName}"
                        )
                        mainActivity.setLoading(false)
                        hideFragment()
                    } else if(fireBaseAuth.currentUser?.isEmailVerified == true){
                        showAlert(
                            "Error",
                            "Este usuario no esta verificado, te hemos enviado una verificación a tu correo"
                        )
                        enviarEmailVerificacion(fireBaseAuth.currentUser,correo)
                    } else{
                        mainActivity.setLoading(false)
                        showAlert<AuthResult>(login)
                    }
                }
        }
    }

    private fun registrar() {
        mainActivity.setLoading(true)
        val correo = binding.etCorreoLogin.text.toString()
        val contrasena = binding.etContrasenaLogin.text.toString()
        if (validEntryTexts(correo, contrasena)) {
            fireBaseAuth.createUserWithEmailAndPassword(correo, contrasena)
                .addOnCompleteListener { creacionTask ->
                    if (creacionTask.isSuccessful) {
                        val user = fireBaseAuth.currentUser!!
                        mainActivity.setCurrentUser(user)
                        enviarEmailVerificacion(user, correo)
                    } else {
                        mainActivity.setLoading(false)
                        Toast.makeText(
                            requireContext(),
                            creacionTask.exception.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                        showAlert<AuthResult>(creacionTask)
                    }
                }
        }
    }

    private fun enviarEmailVerificacion(user: FirebaseUser?, correo: String) {
        val actionCodeSettings = actionCodeSettings {
            url = "https://google.com"
            handleCodeInApp = true
            //setAndroidPackageName("com.example.apkcocina", true, "22")
        }

        user?.sendEmailVerification(actionCodeSettings)?.addOnCompleteListener { emailSent ->
            if (emailSent.isSuccessful) {
                showAlert(
                    getString(R.string.enviado),
                    "Se ha enviado un email de confirmación a la dirección: $correo"
                )
                authListener = AuthStateListener {
                    if (user.isEmailVerified) {
                        mainActivity.setLoading(false)
                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setDisplayName(getString(R.string.chef_curioso))
                            .build()
                        user.updateProfile(profileUpdates)
                        hideFragment()
                        showAlert(
                            getString(R.string.enviado),
                            "Email verificado con éxito, bienvenido ${user.displayName}"
                        )
                        mainActivity.setCurrentUser(fireBaseAuth.currentUser!!)
                        fireBaseAuth.removeAuthStateListener { authListener }
                    }else {
                        mainActivity.setLoading(false)
                        showAlert<Void>(emailSent)
                    }
                }

                fireBaseAuth.addAuthStateListener {authListener}
            }else{
                mainActivity.setLoading(false)
                showAlert<Void>(emailSent)
            }
        }
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
