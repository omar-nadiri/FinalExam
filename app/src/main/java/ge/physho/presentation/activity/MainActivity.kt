package ge.physho.presentation.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Path
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.AnticipateInterpolator
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import ge.physho.R
import ge.physho.databinding.ActivityMainBinding
import ge.physho.network.state.UiState

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var splashScreen: SplashScreen

//    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        splashScreen = installSplashScreen()
        setContentView(binding.root)

        customizeSplashScreen(splashScreen)

//        val content: View = findViewById(android.R.id.content)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            content.viewTreeObserver.addOnPreDrawListener(object :
//                ViewTreeObserver.OnPreDrawListener {
//                override fun onPreDraw(): Boolean =
//                    when {
//                        viewModel.mockDataLoading() -> {
//                            content.viewTreeObserver.removeOnPreDrawListener(this)
//                            true
//                        }
//                        else -> false
//                    }
//            })
//        }

    }

    private fun customizeSplashScreen(splashScreen: SplashScreen) {

        customizeSplashScreenExit(splashScreen)

    }


    private fun customizeSplashScreenExit(splashScreen: SplashScreen) {
        splashScreen.setOnExitAnimationListener { splashScreenViewProvider ->

            showSplashExitAnimator(splashScreenViewProvider.view) {
                splashScreenViewProvider.remove()
            }

            showSplashIconExitAnimator(splashScreenViewProvider.iconView) {
                splashScreenViewProvider.remove()
            }
        }
    }


    private fun showSplashExitAnimator(splashScreenView: View, onExit: () -> Unit = {}) {

        // Create your custom animation set.
        val alphaOut = ObjectAnimator.ofFloat(
            splashScreenView,
            View.ALPHA,
            1f,
            0f
        )

        // Slide up to center.
        val slideUp = ObjectAnimator.ofFloat(
            splashScreenView,
            View.TRANSLATION_Y,
            0f,
            // iconView.translationY,
            -(splashScreenView.height).toFloat()
        ).apply {
            addUpdateListener {
                Log.d(
                    "Splash",
                    "showSplashIconExitAnimator() translationY:${splashScreenView.translationY}"
                )
            }
        }

        // Slide down to center.
        val slideDown = ObjectAnimator.ofFloat(
            splashScreenView,
            View.TRANSLATION_Y,
            0f,
            // iconView.translationY,
            (splashScreenView.height).toFloat()
        ).apply {
            addUpdateListener {
                Log.d(
                    "Splash",
                    "showSplashIconExitAnimator() translationY:${splashScreenView.translationY}"
                )
            }
        }

        val scaleOut = ObjectAnimator.ofFloat(
            splashScreenView,
            View.SCALE_X,
            View.SCALE_Y,
            Path().apply {
                moveTo(1.0f, 1.0f)
                lineTo(0f, 0f)
            }
        )

        AnimatorSet().run {
            duration = resources.getInteger(R.integer.splash_exit_total_duration).toLong()
            interpolator = AnticipateInterpolator()
            Log.d("Splash", "showSplashExitAnimator() duration:$duration")

//             playTogether(alphaOut, slideUp)
//             playTogether(scaleOut)
             playTogether(alphaOut)
//             playTogether(scaleOut, slideUp, alphaOut)
//             playTogether(slideUp, alphaOut)
//            playTogether(slideDown, alphaOut)

            doOnEnd {
                Log.d("Splash", "showSplashExitAnimator() onEnd")
                Log.d("Splash", "showSplashExitAnimator() onEnd remove")
                onExit()
            }

            start()
        }
    }


    // Show exit animator for splash icon.
    private fun showSplashIconExitAnimator(iconView: View, onExit: () -> Unit = {}) {

        val alphaOut = ObjectAnimator.ofFloat(
            iconView,
            View.ALPHA,
            1f,
            0f
        )

        // Bird scale out animator.
        val scaleOut = ObjectAnimator.ofFloat(
            iconView,
            View.SCALE_X,
            View.SCALE_Y,
            Path().apply {
                moveTo(1.0f, 1.0f)
                lineTo(0.3f, 0.3f)
            }
        )

        // Bird slide up to center.
        val slideUp = ObjectAnimator.ofFloat(
            iconView,
            View.TRANSLATION_Y,
            0f,
            // iconView.translationY,
            -(iconView.height).toFloat() * 2.25f
        ).apply {
            addUpdateListener {
                Log.d(
                    "Splash",
                    "showSplashIconExitAnimator() translationY:${iconView.translationY}"
                )
            }
        }

        AnimatorSet().run {
            interpolator = AnticipateInterpolator()
            duration = resources.getInteger(R.integer.splash_exit_total_duration).toLong()
            Log.d("Splash", "showSplashIconExitAnimator() duration:$duration")

             playTogether(alphaOut, slideUp)
//            playTogether(alphaOut, scaleOut, slideUp)
//             playTogether(scaleOut, slideUp)
//             playTogether(slideUp)

            doOnEnd {
                Log.d("Splash", "showSplashIconExitAnimator() onEnd remove")
                onExit()
            }

            start()
        }
    }


}