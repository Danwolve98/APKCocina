package com.example.apkcocina.features.profile.fragment

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.text.SpannableStringBuilder
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import com.example.apkcocina.R
import com.example.apkcocina.databinding.FrgProfileBinding
import com.example.apkcocina.features.profile.viewModel.ProfileViewModel
import com.example.apkcocina.utils.base.APKCocinaActionBar
import com.example.apkcocina.utils.base.BaseFragment
import com.example.apkcocina.utils.base.TitleActionBar
import com.example.apkcocina.utils.extensions.base64toByteArray
import com.example.apkcocina.utils.extensions.collectFlow
import com.example.apkcocina.utils.extensions.format
import com.example.apkcocina.utils.extensions.loadImage
import com.example.apkcocina.utils.extensions.notNull
import com.example.apkcocina.utils.extensions.playAnimation
import com.example.apkcocina.utils.extensions.timeToCalendar
import com.example.apkcocina.utils.extensions.toBase64
import com.example.apkcocina.utils.extensions.visibilityCheck
import com.example.apkcocina.utils.model.User
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

    private var photoUser : String? = null
    override fun assingActionBar() {
        actionBar = TitleActionBar(getString(R.string.perfil)).apply { haveBack = false }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        isUserLogged()
    }

    private fun isUserLogged() {
        if (firebaseAuth.currentUser == null) {
            navigate(R.id.action_perfil_fragment_to_loginFragment)
        } else {
            if (!firebaseAuth.currentUser!!.isEmailVerified) {
                Toast.makeText(requireContext(), getString(R.string.se_requiere_validar_la_cuenta), Toast.LENGTH_SHORT).show()
                navigate(R.id.action_perfil_fragment_to_loginFragment)
            }else
                profileViewModel.cargarUser()
        }
    }

    override fun initializeView() {
        with(binding){
            btDate.setOnClickListener {
                it.playAnimation(R.anim.click_animation)
                showDatePickerDialog()
            }

            photoUser.notNull { ivProfilePicture.loadImage(it.base64toByteArray())  }


            btEditProfile.setOnClickListener {
                it.playAnimation(R.anim.click_animation)
                val bt = it as MaterialButton
                if(bt.isChecked){
                    activarEditables(true)
                    bt.icon = ResourcesCompat.getDrawable(resources,R.drawable.ic_save_profile,null)
                }else{
                    mostrarAlertDialog()
                }
            }


            val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){
                ivProfilePicture.loadImage(it)
                photoUser = it?.toBase64(requireContext())
            }

            ivProfilePickPhoto.setOnClickListener{
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }

            btEditPassword.setOnClickListener {
                navigate(R.id.action_perfil_fragment_to_reestablecerContrasenaFragment)
            }
        }
    }

    private fun mostrarUsuario(user : User){
        with(binding){
            firebaseAuth.currentUser?.photoUrl.notNull(
                notNullAction = { ivProfilePicture.loadImage(firebaseAuth.currentUser!!.photoUrl)},
                nullAction = {ivProfilePicture.loadImage(user.foto)}
            )
            etNombrePerfil.text = SpannableStringBuilder(user.nombre)
            etApellidosPerfil.text = SpannableStringBuilder(user.apellidos)
            etNacionalidadPerfil.text = SpannableStringBuilder(user.nacionalidad)
            tvBirthday.text = user.cumpleanos?.timeToCalendar()?.format("dd/MM/yyyy") ?: getString(R.string.default_cumpleanos)
        }
    }

    override fun initializeObservers() {
        with(profileViewModel){
            collectFlow(profileViewState){
                mainActivity.setLoading(it.isLoading)
            }

            cargaUsuario.observe(viewLifecycleOwner){event->
                event.getContentIfNotHandled()?.let { user->
                    photoUser = user.foto
                    mostrarUsuario(user)
                }
            }

            updateResult.observe(viewLifecycleOwner) { event ->
                event.getContentIfNotHandled()?.let { updateResult ->
                    Toast.makeText(requireContext(), "Datos actualizados correctamente", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun mostrarAlertDialog() {
        var date : Calendar? = null
        if(!binding.tvBirthday.text.isNullOrEmpty()){
            date = Calendar.getInstance()
            date.time = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(binding.tvBirthday.text.toString())!!
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
                        photoUser)

                    dialogInterface.dismiss()
                    btEditProfile.icon = ResourcesCompat.getDrawable(resources,R.drawable.ic_edit_profile,null)
                    activarEditables(false)
                }
            }
            .setNegativeButton("Cancelar"){ dialogInterface: DialogInterface, i: Int ->
                dialogInterface.dismiss()
                binding.btEditProfile.icon = ResourcesCompat.getDrawable(resources,R.drawable.ic_edit_profile,null)
                activarEditables(false)
            }.show()
    }

    private fun activarEditables(activar : Boolean) =
        with(binding){
            etNombrePerfil.isEnabled = activar
            etApellidosPerfil.isEnabled = activar
            etNacionalidadPerfil.isEnabled = activar
            btDate.visibilityCheck(activar)
            ivProfilePickPhoto.visibilityCheck(activar)
            btEditPassword.visibilityCheck(!activar)
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