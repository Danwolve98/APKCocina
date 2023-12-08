package com.example.apkcocina.features.queCocinoHoy.fragment

import android.annotation.SuppressLint
import android.content.Context.RECEIVER_EXPORTED
import android.content.Context.VIBRATOR_MANAGER_SERVICE
import android.content.Context.VIBRATOR_SERVICE
import android.os.Build
import android.os.CountDownTimer
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.MotionEvent
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.apkcocina.NavGraphDirections
import com.example.apkcocina.R
import com.example.apkcocina.databinding.FrgQueCocinoHoyBinding
import com.example.apkcocina.features.recetasOnline.viewModel.GetRecetasViewModel
import com.example.apkcocina.utils.base.APKCocinaActionBar
import com.example.apkcocina.utils.base.BaseFragment
import com.example.apkcocina.utils.base.TitleActionBar
import com.example.apkcocina.utils.model.Receta
import dagger.hilt.android.AndroidEntryPoint
import kotlin.random.Random

@AndroidEntryPoint
class QueCocinoHoyFragment : BaseFragment<FrgQueCocinoHoyBinding>() {

    override lateinit var actionBar: APKCocinaActionBar
    private val viewModel : GetRecetasViewModel by viewModels()

    override fun assingActionBar() {
        actionBar = TitleActionBar(getString(R.string.que_cocino_hoy))
    }

    override fun initializeObservers() {
        super.initializeObservers()
        viewModel.idRecetaRandom.observe(viewLifecycleOwner){event->
            event.getContentIfNotHandled()?.let { pair->
                navigate(NavGraphDirections.actionGlobalRecetaDetalle(idReceta = pair.first,name = pair.second, collection = Receta.RECETAS_USUARIOS))
            }
        }

        viewModel.idRecetaRandomeError.observe(viewLifecycleOwner){event->
            event.getContentIfNotHandled()?.let{error->
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initializeView() {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = requireContext().getSystemService(VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
                requireContext().getSystemService(VIBRATOR_SERVICE) as Vibrator
        }

        var countDown : CountDownTimer? = null
        val lottieAnim = binding.lottieHoldingAnimation.apply { setMinAndMaxProgress(0f,0.92f) }
        binding.ivHuella.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    val timeToLoad = Random.nextInt(4,8) * 1000
                    countDown = object : CountDownTimer(timeToLoad.toLong(),2200){
                        override fun onTick(p0: Long) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                vibrator.vibrate(VibrationEffect.createOneShot(1000,5))
                            }
                        }
                        override fun onFinish() {
                            viewModel.getRandomReceta()
                        }
                    }.start()
                    lottieAnim.animate().alpha(1f).setDuration(1000).start()
                    lottieAnim.playAnimation()
                    true
                }

                MotionEvent.ACTION_UP->{
                    countDown?.cancel()
                    vibrator.cancel()
                    lottieAnim.animate().alpha(0f).setDuration(1000).withEndAction {
                        lottieAnim.cancelAnimation()
                    }.start()
                    true
                }
                else -> false
            }
        }
    }
}