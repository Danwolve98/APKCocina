package com.example.apkcocina.features.profile.fragment

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import com.example.apkcocina.R
import com.example.apkcocina.databinding.FrgProfileBinding
import com.example.apkcocina.MainActivity
import com.example.apkcocina.features.profile.viewModel.ProfileViewModel
import com.example.apkcocina.utils.base.APKCocinaActionBar
import com.example.apkcocina.utils.base.BaseFragment
import com.example.apkcocina.utils.base.TitleActionBar
import com.example.apkcocina.utils.extensions.playAnimation
import com.example.apkcocina.utils.extensions.visibilityCheck
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FrgProfileBinding>() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    override lateinit var actionBar: APKCocinaActionBar

    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        isUserLogged()

        actionBar = TitleActionBar(getString(R.string.perfil)).apply { haveBack = false }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeView()
    }

    private fun isUserLogged() {
        if (firebaseAuth.currentUser == null) {
            navigate(R.id.action_perfil_fragment_to_loginFragment)
        } else {
            if (!firebaseAuth.currentUser!!.isEmailVerified) {
                Toast.makeText(requireContext(), "Se requiere validar la cuenta", Toast.LENGTH_SHORT).show()
                navigate(R.id.action_perfil_fragment_to_loginFragment)
            }
        }
    }

    private fun initializeView() {
        profileViewModel.loginResult.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { loginResult ->

            }
        }

        profileViewModel.updateResult.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { updateResult ->
                Toast.makeText(requireContext(), "Datos actualizados correctamente", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btDate.setOnClickListener {
            it.playAnimation(R.anim.click_animation)
            showDatePickerDialog()
        }

        binding.btEditProfile.setOnClickListener {
            it.playAnimation(R.anim.click_animation)
            val bt = it as MaterialButton
            if(bt.isChecked){
                activarEditables(true)
                bt.icon = ResourcesCompat.getDrawable(resources,R.drawable.ic_save_profile,null)
            }else{
                activarEditables(false)
                mostrarAlertDialog()
            }
        }
    }

    private fun mostrarAlertDialog() {
        var date : Calendar? = null
        if(!binding.tvBirthday.text.isNullOrEmpty()){
            date = Calendar.getInstance()
            date.time = SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH).parse(binding.tvBirthday.text.toString())!!
        }
        AlertDialog.Builder(requireContext())
            .setTitle("Guardar perfil")
            .setMessage("¿Seguro que desea guardar esta configuración?")
            .setPositiveButton("Aceptar"){ dialogInterface: DialogInterface, i: Int ->
                with(binding){
                    profileViewModel.updateUser(
                        etNombrePerfil.text.toString(),
                        etApellidosPerfil.text.toString(),
                        etNacionalidadPerfil.text.toString(),
                        date,
                        null)

                    dialogInterface.dismiss()
                    btEditProfile.icon = ResourcesCompat.getDrawable(resources,R.drawable.ic_edit_profile,null)
                }
            }
            .setNegativeButton("Cancelar"){ dialogInterface: DialogInterface, i: Int ->
                dialogInterface.dismiss()
                binding.btEditProfile.icon = ResourcesCompat.getDrawable(resources,R.drawable.ic_edit_profile,null)
            }.show()
    }

    private fun activarEditables(activar : Boolean) =
        with(binding){
            etNombrePerfil.isEnabled = activar
            etApellidosPerfil.isEnabled = activar
            etNacionalidadPerfil.isEnabled = activar
            btDate.visibilityCheck(activar)
        }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        var year = calendar.get(Calendar.YEAR)
        var month = calendar.get(Calendar.MONTH)
        var dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        with(binding.tvBirthday){
            if(!text.isNullOrEmpty()){
                val date = Calendar.getInstance().apply {
                    time = SimpleDateFormat("dd/MM/yyyy",Locale.FRENCH).parse(text.toString())!!
                }
                year = date.get(Calendar.YEAR)
                month = date.get(Calendar.MONTH)
                dayOfMonth = date.get(Calendar.DAY_OF_MONTH)
            }
        }

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            android.R.style.Theme_Holo_Dialog_MinWidth,
            { _, anio, mes, dia ->
                // Aquí puedes manejar la fecha seleccionada
                binding.tvBirthday.text = "$dia/$mes/$anio"
            },
            year,
            month,
            dayOfMonth
        )

        datePickerDialog.datePicker.maxDate = Calendar.getInstance().time.time

        datePickerDialog.show()
    }
}