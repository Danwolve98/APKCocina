package com.example.apkcocina.features.quecocinohoy

import android.annotation.SuppressLint
import android.content.Context.VIBRATOR_MANAGER_SERVICE
import android.content.Context.VIBRATOR_SERVICE
import android.os.Build
import android.os.CountDownTimer
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.MotionEvent
import android.widget.Toast
import com.example.apkcocina.R
import com.example.apkcocina.databinding.FrgQueCocinoHoyBinding
import com.example.apkcocina.utils.base.APKCocinaActionBar
import com.example.apkcocina.utils.base.BaseFragment
import com.example.apkcocina.utils.base.TitleActionBar
import kotlin.random.Random

class QueCocinoHoyFragment : BaseFragment<FrgQueCocinoHoyBinding>() {

    override lateinit var actionBar: APKCocinaActionBar

    override fun assingActionBar() {
        actionBar = TitleActionBar(getString(R.string.que_cocino_hoy))
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
                            Toast.makeText(requireContext(), "NAVEGAR", Toast.LENGTH_SHORT).show()
                            /*navigate(R.id.action_recetasBaseFragment_to_recetaDetalle)*/
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