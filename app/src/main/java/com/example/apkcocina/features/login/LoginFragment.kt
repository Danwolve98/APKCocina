package com.example.apkcocina.features.login

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.apkcocina.databinding.LoginFragmentBinding
import com.google.firebase.auth.FirebaseAuth


class LoginFragment : Fragment() {

    private var _binding : LoginFragmentBinding? = null
    private val binding get() = _binding!!

    private val fireBaseUserAdmin by lazy { FirebaseAuth.getInstance() }


    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

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
                val correo = binding.etCorreoLogin.text.toString()
                val contrasena = binding.etContrasenaLogin.text.toString()
                Toast.makeText(requireContext(), "BOTON PULSADO", Toast.LENGTH_SHORT).show()
                if(validEntryTexts(correo,contrasena))
                fireBaseUserAdmin.createUserWithEmailAndPassword(correo,contrasena).addOnCompleteListener {
                    if(it.isSuccessful){
                        Toast.makeText(requireContext(), "Usuario registrado correctamente", Toast.LENGTH_SHORT).show()
                    } else{
                        showAlert()
                    }
                }
            }
        }
    }

    private fun validEntryTexts(correo : String,contrasena : String) = correo.isNotEmpty() && contrasena.isNotEmpty()


    private fun showAlert(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Error")
        builder.setMessage("Ha ocurrido un error")
        builder.setPositiveButton("Aceptar",null)
        val dialog = builder.create()
        dialog.show()
    }
}

