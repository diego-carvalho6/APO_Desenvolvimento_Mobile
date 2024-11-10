@file:JvmName("WeatherScreenKt")

package com.apo.weather

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.apo.weather.ui.theme.WeatherTheme
import com.google.gson.Gson
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException


class MainActivity : ComponentActivity() {
    private val cityState = mutableStateOf("Curitiba, PR")

    private val qrScanLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val result = IntentIntegrator.parseActivityResult(result.resultCode, result.resultCode, data)
            if (result != null && result.contents != null) {
                val city = result.contents
                cityState.value = city
            } else {
                Log.e("QRScan", "QR Code não encontrado ou leitura cancelada")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val cityName = intent?.getStringExtra("CITY_NAME") ?: "Curitiba, PR"
        cityState.value = cityName
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherTheme {
                WeatherData()
            }
        }
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun WeatherData() {
        val currentTabIndex = remember { mutableStateOf(0) }

        Scaffold(
            modifier = Modifier.statusBarsPadding(),
            topBar = {
                Column {
                    NavigationTabs(currentTabIndex.value, cityState.value)
                }
            },
            content = { paddingValues ->
                Column(modifier = Modifier.padding(paddingValues)) {
                    Weather(this@MainActivity, cityState)
                }
            }
        )
    }

    @Composable
    fun Weather(context: ComponentActivity, cityState: MutableState<String>) {
        val weatherData = remember { mutableStateOf(Results.default()) }

        suspend fun updateWeatherData(city: String) {
            weatherData.value = fetchCity(city)
        }

        LaunchedEffect(cityState.value) {
            updateWeatherData(cityState.value)
        }

        WeatherTheme {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    "Previsão do Tempo para ${cityState.value}",
                    style = MaterialTheme.typography.titleMedium
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    TextField(
                        value = cityState.value,
                        onValueChange = { cityState.value = it },
                        label = { Text("Cidade") },
                        modifier = Modifier.weight(1f)
                    )

                }
                FloatingActionButton(
                    onClick = {
                        val intentIntegrator = IntentIntegrator(context)
                        intentIntegrator.setPrompt("Escaneie o QR Code")
                        intentIntegrator.setBeepEnabled(true)
                        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
                        intentIntegrator.setOrientationLocked(true)
                        qrScanLauncher.launch(intentIntegrator.createScanIntent())
                    },
                    modifier = Modifier.padding(16.dp).fillMaxWidth()
                ) {
                    Icon(Icons.Filled.Place, contentDescription = "Escanear QR Code")
                }
                LazyColumn {
                    itemsIndexed(weatherData.value.forecast) { index, forecast ->
                        Card(modifier = Modifier.padding(8.dp)) {
                            Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                                if (index == 0) {
                                    Text(text = "Hoje", style = MaterialTheme.typography.titleMedium)
                                } else {
                                    Text(text = "Data: ${forecast.date}")
                                }
                                Text(text = "Descrição: ${forecast.description}")
                                Text(text = "Temperatura: min ${forecast.min} - ${forecast.max}")
                            }
                        }
                    }
                }
            }
        }
    }
}

suspend fun fetchCity(city: String): Results {
    val apiKey = "49af764d"
    val url = "https://api.hgbrasil.com/weather?key=$apiKey&city_name=$city"

    val client = OkHttpClient()

    val request = Request.Builder()
        .url(url)
        .addHeader("User-Agent", "WeatherForecast/1.0 diego.sena.oliveira.carvalho@gmail.com")
        .build()

    return withContext(Dispatchers.IO) {
        try {
            val response: Response = client.newCall(request).execute()

            if (response.isSuccessful) {
                val body = response.body?.string()
                if (body != null) {
                    val weatherResponse = Gson().fromJson(body, WeatherResponse::class.java)
                    return@withContext weatherResponse.results
                } else {
                    return@withContext Results.default()
                }
            } else {
                return@withContext Results.default()
            }
        } catch (e: IOException) {
            Log.e("WeatherRequest", "Erro ao fazer requisição", e)
            return@withContext Results.default()
        }
    }
}

data class WeatherResponse(
    val by: String,
    val valid_key: Boolean,
    val results: Results,
    val execution_time: Double,
    val from_cache: Boolean
)

data class Results(
    val temp: Int,
    val date: String,
    val time: String,
    val condition_code: String,
    val description: String,
    val currently: String,
    val city: String,
    val img_id: String,
    val humidity: Int,
    val cloudiness: Double,
    val rain: Double,
    val wind_speedy: String,
    val wind_direction: Int,
    val wind_cardinal: String,
    val sunrise: String,
    val sunset: String,
    val moon_phase: String,
    val condition_slug: String,
    val city_name: String,
    val timezone: String,
    val forecast: List<Forecast>) {
    companion object {

        fun default(): Results {
            return Results(
                temp = 0,
                date = "",
                time = "",
                condition_code = "",
                description = "Informação indisponível",
                currently = "",
                city = "Cidade não encontrada",
                img_id = "",
                humidity = 0,
                cloudiness = 0.0,
                rain = 0.0,
                wind_speedy = "",
                wind_direction = 0,
                wind_cardinal = "",
                sunrise = "",
                sunset = "",
                moon_phase = "",
                condition_slug = "",
                city_name = "",
                timezone = "",
                forecast = emptyList()
            )
        }
    }
}


data class Forecast(
    val date: String,
    val weekday: String,
    val max: Int,
    val min: Int,
    val humidity: Int,
    val cloudiness: Double,
    val rain: Double,
    val rain_probability: Int,
    val wind_speedy: String,
    val sunrise: String,
    val sunset: String,
    val moon_phase: String,
    val description: String,
    val condition: String
)
