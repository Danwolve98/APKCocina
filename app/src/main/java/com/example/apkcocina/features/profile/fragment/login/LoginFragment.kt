package com.example.apkcocina.features.profile.fragment.login

import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainer
import androidx.fragment.app.FragmentContainerView
import com.example.apkcocina.R
import com.example.apkcocina.databinding.LoginFragmentBinding
import com.example.apkcocina.features.home.activity.MainActivity
import com.example.apkcocina.utils.extensions.invisible
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await


class LoginFragment : Fragment() {

    private var _binding : LoginFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainActivity : MainActivity

    private val fireBaseUserAdmin by lazy { FirebaseAuth.getInstance() }


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
            btRegistrarse.setOnClickListener {
                mainActivity.setLoading(true)
                val correo = binding.etCorreoLogin.text.toString()
                val contrasena = binding.etContrasenaLogin.text.toString()
                if(validEntryTexts(correo,contrasena)){
                    fireBaseUserAdmin.createUserWithEmailAndPassword(correo,contrasena).addOnCompleteListener { creacionTask ->
                        if(creacionTask.isSuccessful){
                            val user = fireBaseUserAdmin.currentUser!!
                            mainActivity.setCurrentUser(user)
                            user.sendEmailVerification().addOnCompleteListener {verificacion ->
                                if(verificacion.isSuccessful){
                                    mainActivity.setLoading(false)

                                    val profileUpdates = UserProfileChangeRequest.Builder()
                                        .setDisplayName(getString(R.string.chef_curioso))
                                        .build()

                                    user.updateProfile(profileUpdates)
                                    showAlert(getString(R.string.enviado),"Se ha enviado un email de confirmación a la dirección: $correo")
                                    hideFragment()
                                } else{
                                    mainActivity.setLoading(false)
                                    showAlert<Void>(verificacion)
                                }
                            }
                        } else{
                            mainActivity.setLoading(false)
                            Toast.makeText(requireContext(), creacionTask.exception.toString(), Toast.LENGTH_SHORT).show()
                            showAlert<AuthResult>(creacionTask)
                        }
                    }
                }
            }

            btIniciarSesion.setOnClickListener {
                mainActivity.setLoading(true)
                val correo = binding.etCorreoLogin.text.toString()
                val contrasena = binding.etContrasenaLogin.text.toString()
                if(validEntryTexts(correo,contrasena)){
                    fireBaseUserAdmin.signInWithEmailAndPassword(correo,contrasena).addOnCompleteListener {login->
                        if(login.isSuccessful){
                            mainActivity.setCurrentUser(fireBaseUserAdmin.currentUser!!)
                            showAlert("Bienvenido","Bienvenido ${fireBaseUserAdmin.currentUser!!.displayName}")
                            mainActivity.setLoading(false)
                            hideFragment()
                        }else{
                            mainActivity.setLoading(false)
                            showAlert<AuthResult>(login)
                        }
                    }
                }
            }


        }
    }

    private fun validEntryTexts(correo : String,contrasena : String) = correo.isNotEmpty() && contrasena.isNotEmpty()

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

