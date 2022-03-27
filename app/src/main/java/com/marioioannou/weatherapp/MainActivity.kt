package com.marioioannou.weatherapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.compose.ui.text.toUpperCase
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.marioioannou.weatherapp.api.Consts
import com.marioioannou.weatherapp.api.Repository
import com.marioioannou.weatherapp.api.WeatherApi
import com.marioioannou.weatherapp.api.model.WeatherModel
import com.marioioannou.weatherapp.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

private lateinit var binding: ActivityMainBinding
private lateinit var viewModel: MainViewModel

@DelicateCoroutinesApi
class MainActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        about()
        viewModel = ViewModelProvider(this, viewModelFactory)[(MainViewModel::class.java)]
        viewModel.getWeather(38.4624F, 23.5948F, "ba3a5c5eb56940101c62bf15b2352356")
        viewModel.weatherResponse.observe(this) { response ->
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    if (response.isSuccessful) {
                        withContext(Dispatchers.Main) {
                            binding.tvLocation.text = response.body()?.name.toString()
                            binding.tvUpdated.text = "Updated at ${
                                SimpleDateFormat("HH:mm", Locale.ENGLISH).format(
                                    Date(
                                        (response.body()?.dt?.toLong())!! * 1000
                                    )
                                )
                            }"
                            binding.tvStatus.text = "${
                                response.body()?.weather?.firstOrNull()?.description.toString()
                                    .uppercase(Locale.getDefault())
                            }°"
                            binding.tvTemperature.text =
                                "${response.body()?.main?.temp?.toInt().toString()}°C"
                            binding.tvMinTemperature.text = "Min temperature: ${
                                response.body()?.main?.temp_min?.toInt().toString()
                            }°C"
                            binding.tvMaxTemperature.text = "Max temperature: ${
                                response.body()?.main?.temp_max?.toInt().toString()
                            }°C"
                            binding.tvHumidity.text =
                                "${response.body()?.main?.humidity?.toString()}%"
                            binding.tvPressure.text =
                                "${response.body()?.main?.pressure?.toString()}hPa"
                            binding.tvSunrise.text =
                                SimpleDateFormat("HH:mm:ss", Locale.ENGLISH).format(
                                    Date(
                                        (response.body()?.sys?.sunrise?.toLong())!! * 1000
                                    )
                                )
                            binding.tvSunset.text =
                                SimpleDateFormat("HH:mm:ss", Locale.ENGLISH).format(
                                    Date(
                                        (response.body()?.sys?.sunset?.toLong())!! * 1000
                                    )
                                )

                            binding.tvWind.text = "${response.body()?.wind?.speed?.toString()} km/h"
                            binding.tvWindDegree.text = "${response.body()?.wind?.deg?.toString()}°"
                        }
//                    } else {
//                        Toast.makeText(applicationContext, "No internet", Toast.LENGTH_SHORT).show()
//                        binding.imgNoWifi.isVisible = true

                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(applicationContext, "No internet", Toast.LENGTH_SHORT).show()
                        binding.imgNoWifi.isVisible = true
                    }
                }
            }
        }
    }

    fun about(){
        binding.btnAbout.setOnClickListener {
            Intent(this, AboutActivity::class.java).apply {
                startActivity(this)
            }
        }
    }
}
//    @DelicateCoroutinesApi
//    private fun weatherTask() {
//        val api = Retrofit.Builder()
//            .baseUrl(Consts.BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(WeatherApi::class.java)
//        GlobalScope.launch(Dispatchers.IO) {
//            try {
//                val response = api.getWeather(38.4624F,23.5948F,"ba3a5c5eb56940101c62bf15b2352356").awaitResponse()
//                if (response.isSuccessful) {
//                    val data = response.body()!!
//                    withContext(Dispatchers.Main) {
//                        binding.progressBar.isVisible = true
//                        binding.tvLocation.text = data.name
//                    }
//                }
//            } catch (e: Exception){
//                withContext(Dispatchers.Main) {
//                    Toast.makeText(applicationContext, "No internet", Toast.LENGTH_SHORT).show()
//                    binding.imgNoWifi.isVisible = true
//                }
//            }
//        }
//    }

//    override fun onStart() {
//        val manager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        val activeNetwork = manager.activeNetworkInfo
//        if (null == activeNetwork) {
//            val dialogBuilder = AlertDialog.Builder(this)
//            dialogBuilder.setMessage("Please check your internet connection")
//                .setCancelable(false)
//                .setPositiveButton("Retry", DialogInterface.OnClickListener { dialog, id ->
//                    recreate()
//                })
//                .setNegativeButton("Exit", DialogInterface.OnClickListener { dialog, id ->
//                    finish()
//                })
//            val alert = dialogBuilder.create()
//            alert.setTitle("Weathery")
//            alert.setIcon(R.drawable.no_wifi)
//            alert.show()
//            super.onStart()
//        }
//    }
