package com.example.productviewerapp.utlis

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.productviewerapp.R
import kotlinx.coroutines.ExperimentalCoroutinesApi

object Helper {

    //No data found
    @Composable
    fun NoDataFound(searchItem: String) {
        Text(
            text = stringResource(R.string.no_result) + "\"$searchItem\"",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            style = TextStyle(
                color = Color.Gray, fontSize = 16.sp, fontWeight = FontWeight.Bold
            ),
            textAlign = TextAlign.Center
        )

    }

    //To show network status
    @Composable
    fun NetworkStatus(message: String) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painterResource(R.drawable.no_internet),
                contentDescription = stringResource(R.string.no_internet)
            )
            Text(
                text = message, style = TextStyle(
                    color = Color.Red, fontWeight = FontWeight.Normal
                ), modifier = Modifier.padding(16.dp), textAlign = TextAlign.Center
            )
        }

    }

    //To set header for page
    @Composable
    fun PageHeader(title: String) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(
                text = title, color = Color.Black,
                style = TextStyle(
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    lineHeight = 34.sp,
                    shadow = Shadow(
                        color = Color.Gray, offset = Offset(2f, 2f),
                        blurRadius = 4f
                    )
                )
            )

        }
    }

}


