package com.example.apkcocina.features.settings.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import com.example.apkcocina.BuildConfig
import com.example.apkcocina.R
import com.example.apkcocina.databinding.FrgAjustesBinding
import com.example.apkcocina.utils.base.APKCocinaActionBar
import com.example.apkcocina.utils.base.BaseFragment
import com.example.apkcocina.utils.base.TitleActionBar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FrgAjustesBinding>() {

    override lateinit var actionBar: APKCocinaActionBar
    @Inject
    lateinit var authFirebaseAuth: FirebaseAuth

    companion object{
        const val AYUDA_INFO = 1
        const val INFORMACION_INFO = 2
    }
    override fun onAttach(context: Context) {
        actionBar = TitleActionBar(getString(R.string.ajustes)).apply { haveBack=false }
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeView()
    }

    private fun initializeView() {
        with(binding){
            btAyuda.setOnClickListener { navigate(SettingsFragmentDirections.actionSettingsFragmentToInformacionFragment(AYUDA_INFO)) }
            btCompartir.setOnClickListener { compartirAPK() }
            btInformacion.setOnClickListener { navigate(SettingsFragmentDirections.actionSettingsFragmentToInformacionFragment(INFORMACION_INFO)) }
            btIdioma.setOnClickListener { navigate(R.id.action_settings_fragment_to_idiomaFragment) }
            btIniciarSesion()
        }
    }

    private fun FrgAjustesBinding.btIniciarSesion(): Any? =
        if (authFirebaseAuth.currentUser != null) {
            //INICIAR SESION
        } else {
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.cerrar_sesion))
                .setMessage(getString(R.string.seguro_desea_cerrar_sesion))
                .setPositiveButton(
                    getString(R.string.aceptar)
                ) { dia, _ ->
                    btIniciarSesion.icon = AppCompatResources.getDrawable(
                        requireContext(),
                        R.drawable.ic_cerrar_sesion
                    )
                    btIniciarSesion.setOnClickListener {
                        authFirebaseAuth.signOut()
                        btIniciarSesion.icon = AppCompatResources.getDrawable(
                            requireContext(),
                            R.drawable.ic_iniciar_sesion
                        )
                       //INICAR SESION
                    }
                    dia.dismiss()
                }
                .setNegativeButton(getString(R.string.cancelar)) { dia, _ ->
                    dia.dismiss()
                }
        }

    private fun compartirAPK() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mira_esta_increible_apk_de_cocina))
        shareIntent.putExtra(Intent.EXTRA_TEXT, "${getString(R.string.te_recomiendo_esta_apk)}\n\nhttps://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}")

        startActivity(Intent.createChooser(shareIntent, "Compartir con"))
    }


}