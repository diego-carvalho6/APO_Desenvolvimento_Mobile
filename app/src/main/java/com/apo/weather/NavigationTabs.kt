package com.apo.weather

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun NavigationTabs(currentTabIndex: Int, cityName: String) {
    val context = LocalContext.current

    TabRow(selectedTabIndex = currentTabIndex) {
        TabItem(context, currentTabIndex, 0, "Previsão", MainActivity::class.java, cityName)
        TabItem(context, currentTabIndex, 1, "Mapa", MapScreen::class.java, cityName)
        TabItem(context, currentTabIndex, 2, "Sobre", AboutScreen::class.java, cityName)
    }
}
@Composable
fun TabItem(
    context: Context,
    currentTabIndex: Int,
    tabIndex: Int,
    title: String,
    targetActivity: Class<*>,
    cityName: String
) {
    Tab(
        text = { Text(title) },
        selected = tabIndex == currentTabIndex,
        onClick = {
            if (tabIndex != currentTabIndex) {
                val intent = Intent(context, targetActivity).apply {
                    putExtra("CITY_NAME", cityName)
                }
                context.startActivity(intent)
            }
        }
    )
}


@Composable
fun Header() {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)) {
        Spacer(modifier = Modifier.height(8.dp))
    }
}