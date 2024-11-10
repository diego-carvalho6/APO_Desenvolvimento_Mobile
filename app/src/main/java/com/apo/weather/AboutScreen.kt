package com.apo.weather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.apo.weather.ui.theme.WeatherTheme


class AboutScreen : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val cityName = intent?.getStringExtra("CITY_NAME") ?: "Curitiba, PR"

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherTheme {
                AboutData(cityName)
            }

        }
    }
}


@Composable
fun AboutData(cityName : String)
{
    val currentTabIndex = remember { mutableStateOf(2) }

    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        topBar =
        {Column() {
            NavigationTabs(currentTabIndex.value, cityName)
        }},
        content = { padding ->
            Column(modifier = Modifier.padding(padding)) {
                InfoCard()
            }})
}

@Composable
fun InfoCard() {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Nome: Diego de Sena Oliveira Carvalho")
            Spacer(modifier = Modifier.height(8.dp))
            Text("RA: 09048030")
            Spacer(modifier = Modifier.height(8.dp))
            Text("Curso: Sistemas de Informação")
            Spacer(modifier = Modifier.height(8.dp))
            Text("Campos: Fazenda Rio Grande ")
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
