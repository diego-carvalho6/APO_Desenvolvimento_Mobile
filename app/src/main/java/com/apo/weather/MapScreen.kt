package com.apo.weather

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.apo.weather.ui.theme.WeatherTheme
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MapScreen : ComponentActivity() {
      private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLocation: GeoPoint? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cityName = intent?.getStringExtra("CITY_NAME") ?: "Curitiba, PR"

        Configuration.getInstance().userAgentValue = packageName

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setContent {
            WeatherTheme {
                MapScreenContent(cityName, currentLocation)
            }
        }
    }




data class CitySearchResult(
    val lat: String,
    val lon: String
)
@Preview(showBackground = true)
@Composable
fun PreviewMapScreen() {
    WeatherTheme {
        MapScreenContent(cityName = "Curitiba, PR", currentLocation = null)
    }
}
@Composable
fun MapScreenContent(cityName: String, currentLocation: GeoPoint?) {
    var cityCoordinates by remember { mutableStateOf(currentLocation) }

    LaunchedEffect(cityName) {
        cityCoordinates = getCityCoordinates(cityName)
    }

    cityCoordinates?.let {
        Scaffold(
            modifier = Modifier.statusBarsPadding(),
            topBar = {
                Column() {
                    NavigationTabs(1, cityName)
                }
            },
            content = { padding ->
                Column(modifier = Modifier.padding(padding)) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        MapViewComposable(cityCoordinates)
                    }
                }
            }
        )
    } ?: run {
             Scaffold(
            modifier = Modifier.statusBarsPadding(),
            topBar = {
                Column() {
                    NavigationTabs(1, cityName)
                }
            },
            content = { padding ->
                Column(modifier = Modifier.padding(padding)) {
                    Text("Carregando mapa...")
                }
            }
        )
    }
}
    private suspend fun getCityCoordinates(cityName: String): GeoPoint {
        val client = OkHttpClient()
        val url = "https://nominatim.openstreetmap.org/search?q=${cityName}&format=json&addressdetails=1"

        val request = Request.Builder()
            .url(url)
            .addHeader("User-Agent", "WeatherForecast/1.0 diego.sena.oliveira.carvalho@gmail.com")
            .build()

        return withContext(Dispatchers.IO) {
            try {
                val response: Response = client.newCall(request).execute()

                var lat : Double =  -25.4284
                var long : Double = -49.2733

                if (response.isSuccessful) {
                    val body = response.body?.string()
                    val result = Gson().fromJson(body, Array<CitySearchResult>::class.java)
                    if (result.isNotEmpty()) {
                        val firstResult = result[0]
                        lat = firstResult.lat.toDouble()
                        long = firstResult.lon.toDouble()
                        return@withContext GeoPoint(lat, long)
                    } else {
                        return@withContext GeoPoint(lat, long)
                    }
                } else {
                    return@withContext GeoPoint(lat, long)
                }
            } catch (e: Exception) {
                return@withContext GeoPoint(-25.4284, -49.2733)
            }
        }
    }
}
@Composable
fun MapViewComposable(currentLocation: GeoPoint?) {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            val mapView = MapView(context).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)
            }

            val mapController: IMapController = mapView.controller
            mapController.setZoom(10.0)

            val center = currentLocation ?: GeoPoint(-25.4284, -49.2733)
            mapController.setCenter(center)

            val marker = Marker(mapView)
            marker.position = center
            marker.title = "Localização Atual"
            mapView.overlays.add(marker)

            mapView.invalidate()

            mapView
        }
    )
}
