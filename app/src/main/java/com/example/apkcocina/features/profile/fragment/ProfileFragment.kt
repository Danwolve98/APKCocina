package com.example.apkcocina.features.profile.fragment

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.apkcocina.R
import com.example.apkcocina.databinding.FrgProfileBinding
import com.example.apkcocina.features.home.activity.MainActivity
import com.example.apkcocina.features.profile.viewModel.ProfileViewModel
import com.example.apkcocina.utils.base.APKCocinaActionBar
import com.example.apkcocina.utils.base.BaseFragment
import com.example.apkcocina.utils.base.TitleActionBar
import com.example.apkcocina.utils.extensions.invisible
import com.example.apkcocina.utils.extensions.playAnimation
import com.example.apkcocina.utils.extensions.visibilityCheck
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.ktx.userProfileChangeRequest
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FrgProfileBinding>() {

    override lateinit var actionBar: APKCocinaActionBar
    private lateinit var mainActivity: MainActivity

    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = requireActivity() as MainActivity
        actionBar = TitleActionBar(getString(R.string.perfil)).apply { haveBack = false }
        (activity as MainActivity).configureActionBar(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeView()
    }

    private fun initializeView() {
        profileViewModel.loginResult.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { loginResult ->
                binding.fragmentContainerLogin.invisible()
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
            }else{
                activarEditables(false)
                mostrarAlertDialog()
            }
        }
    }

    //TODO ESTAS HACIENDO ESTO AQUIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII
    private fun mostrarAlertDialog() {
        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle("Guardar perfil")
            .setMessage("¿Seguro que desea guardar esta configuración?")
            .setPositiveButton("Aceptar"){ dialogInterface: DialogInterface, i: Int ->
                with(binding){
                    profileViewModel.updateUser(
                        etNombrePerfil.text.toString(),
                        etApellidosPerfil.text.toString(),
                        etNacionalidadPerfil.text.toString(),
                        (SimpleDateFormat("dd/MM/yyyy")).parse(tvBirthday.text.toString()),
                        null)
                    dialogInterface.dismiss()
                }
            }
            .setNegativeButton("Cancelar"){ dialogInterface: DialogInterface, i: Int ->
                dialogInterface.dismiss()
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
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            android.R.style.Theme_Holo_Dialog_MinWidth,
            { _, year, mounth, day ->
                // Aquí puedes manejar la fecha seleccionada
                binding.tvBirthday.text = "$day/$mounth/$year"
            },
            year,
            month,
            dayOfMonth
        )

        datePickerDialog.datePicker.maxDate = Calendar.getInstance().time.time

        datePickerDialog.show()
    }
}