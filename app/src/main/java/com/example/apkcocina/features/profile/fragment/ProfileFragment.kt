package com.example.apkcocina.features.profile.fragment

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.apkcocina.R
import com.example.apkcocina.databinding.ProfileFragmentBinding
import com.example.apkcocina.features.home.activity.MainActivity
import com.example.apkcocina.features.home.adapter.MenuItemsAdapter
import com.example.apkcocina.features.profile.viewModel.ProfileViewModel
import com.example.apkcocina.utils.base.APKCocinaActionBar
import com.example.apkcocina.utils.base.BaseFragment
import com.example.apkcocina.utils.base.PrincipalActionBar
import com.example.apkcocina.utils.base.TitleActionBar
import com.example.apkcocina.utils.extensions.invisible
import com.example.apkcocina.utils.extensions.visible
import com.google.apphosting.datastore.testing.DatastoreTestTrace.TimelineTestTrace
import com.google.firebase.auth.ktx.userProfileChangeRequest
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class ProfileFragment : BaseFragment<ProfileFragmentBinding>() {

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
                if (loginResult.user.displayName == null) {
                    loginResult.user.updateProfile(userProfileChangeRequest {
                        displayName = getString(R.string.chef_curioso)
                    })
                }
                mainActivity.setCurrentUser(loginResult.user)
                binding.fragmentContainerLogin.invisible()
            }
        }

        val calendiario = Calendar.getInstance().apply {
            this.set(2000, 1, 1)
        }

        binding.btDate.setOnClickListener {
            showDatePickerDialog()
        }

    }

    fun showDatePickerDialog() {
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