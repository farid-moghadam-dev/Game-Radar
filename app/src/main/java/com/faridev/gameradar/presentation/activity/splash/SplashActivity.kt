package com.faridev.gameradar.presentation.activity.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.faridev.gameradar.R
import com.faridev.gameradar.core.util.configFullScreenActivity
import com.faridev.gameradar.core.util.disableOnBackPressed
import com.faridev.gameradar.core.util.keepScreenOn
import com.faridev.gameradar.presentation.activity.main.MainActivity
import com.faridev.gameradar.presentation.common.components.AnimatedText
import com.faridev.gameradar.presentation.common.theme.Fonts
import com.faridev.gameradar.presentation.common.theme.GameRadarTheme
import kotlinx.coroutines.delay

@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        keepScreenOn()
        disableOnBackPressed()
        configFullScreenActivity()
        enableEdgeToEdge()

        setContent {
            GameRadarTheme {
                LaunchedEffect(Unit) {
                    delay(3000)
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    finish()
                }
                SplashContent(getString(R.string.app_name))
            }
        }
    }
}

@Composable
private fun SplashContent(appName: String) {
    val lottieComposition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.splash_lottie))
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LottieAnimation(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(bottom = 50.dp),
                composition = lottieComposition,
                iterations = 1
            )

            AnimatedText(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(top = 300.dp),
                text = appName,
                textStyle = TextStyle(
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 40.sp,
                    fontFamily = Fonts.Borg
                )
            )
        }
    }
}