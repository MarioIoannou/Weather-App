package com.marioioannou.weatherapp.ui

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.marioioannou.weatherapp.R
import com.marioioannou.weatherapp.api.model.WeatherModel
import com.marioioannou.weatherapp.utils.Consts.Companion.BASE_URL
import com.marioioannou.weatherapp.databinding.ActivityMainBinding
import com.marioioannou.weatherapp.ui.repository.Repository
import com.marioioannou.weatherapp.ui.viewmodel.MainViewModel
import com.marioioannou.weatherapp.ui.viewmodel.MainViewModelFactory
import com.marioioannou.weatherapp.utils.ScreenState
import kotlinx.coroutines.*
import retrofit2.Response
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

private lateinit var binding: ActivityMainBinding
private lateinit var viewModel: MainViewModel
private var TAG = "MainActivity"

@DelicateCoroutinesApi
class MainActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        //Toast.makeText(applicationContext, BASE_URL, Toast.LENGTH_SHORT).show()
        viewModel = ViewModelProvider(this, viewModelFactory)[(MainViewModel::class.java)]
        viewModel.weatherResponse.observe(this, Observer { response ->
            processResponse(response)
        })
        about()
//            lifecycleScope.launch {
//                repeatOnLifecycle(Lifecycle.State.CREATED) {
//                    try {
//                        viewModel.getWeather(38.4624F, 23.5948F, "ba3a5c5eb56940101c62bf15b2352356")
//                    } catch (e: Exception) {
//                        Toast.makeText(applicationContext, "No internet", Toast.LENGTH_SHORT).show()
//                        binding.progressBar.isVisible = false
//                        binding.imgNoWifi.isVisible = true
//                        return@repeatOnLifecycle
//                    }
//                    if (response.isSuccessful) {
//                        binding.progressBar.isVisible = false
//                        binding.tvLocation.text = response.body()?.name.toString()
//                        binding.tvUpdated.text = "Updated at ${
//                            SimpleDateFormat("HH:mm", Locale.ENGLISH).format(
//                                Date(
//                                    (response.body()?.dt?.toLong())!! * 1000
//                                )
//                            )
//                        }"
//                        binding.tvStatus.text = "${
//                            response.body()?.weather?.firstOrNull()?.description.toString()
//                                .uppercase(Locale.getDefault())
//                        }°"
//                        binding.tvTemperature.text =
//                            "${response.body()?.main?.temp?.toInt().toString()}°C"
//                        binding.tvMinTemperature.text = "Min temperature: ${
//                            response.body()?.main?.temp_min?.toInt().toString()
//                        }°C"
//                        binding.tvMaxTemperature.text = "Max temperature: ${
//                            response.body()?.main?.temp_max?.toInt().toString()
//                        }°C"
//                        binding.tvHumidity.text =
//                            "${response.body()?.main?.humidity?.toString()}%"
//                        binding.tvPressure.text =
//                            "${response.body()?.main?.pressure?.toString()}hPa"
//                        binding.tvSunrise.text =
//                            SimpleDateFormat("HH:mm:ss", Locale.ENGLISH).format(
//                                Date(
//                                    (response.body()?.sys?.sunrise?.toLong())!! * 1000
//                                )
//                            )
//                        binding.tvSunset.text =
//                            SimpleDateFormat("HH:mm:ss", Locale.ENGLISH).format(
//                                Date(
//                                    (response.body()?.sys?.sunset?.toLong())!! * 1000
//                                )
//                            )
//                        binding.tvWind.text = "${response.body()?.wind?.speed?.toString()} km/h"
//                        binding.tvWindDegree.text = "${response.body()?.wind?.deg?.toString()}°"
//                    }
//                }
//            }

    }

    @SuppressLint("SetTextI18n")
    private fun processResponse(state: ScreenState<Response<WeatherModel>>?) {
        when (state) {
            is ScreenState.Loading -> {
                Log.e(TAG,"ScreenState.Loading")
                binding.progressBar.isVisible = true

            }
            is ScreenState.Success -> {
                Log.e(TAG,"ScreenState.Success")
                if (state.data != null) {
                    binding.apply {
                        binding.progressBar.isVisible = false
                        binding.tvLocation.text = state.data.body()?.name
                        binding.tvUpdated.text = "Updated at ${
                            SimpleDateFormat("HH:mm", Locale.ENGLISH).format(
                                Date(
                                    (state.data.body()?.dt?.toLong())!! * 1000
                                )
                            )
                        }"
                        binding.tvStatus.text = "${
                            state.data.body()?.weather?.firstOrNull()?.description.toString()
                                .uppercase(Locale.getDefault())
                        }°"
                        binding.tvTemperature.text =
                            "${state.data.body()?.main?.temp?.toInt().toString()}°C"
                        binding.tvMinTemperature.text = "Min temperature: ${
                            state.data.body()?.main?.temp_min?.toInt().toString()
                        }°C"
                        binding.tvMaxTemperature.text = "Max temperature: ${
                            state.data.body()?.main?.temp_max?.toInt().toString()
                        }°C"
                        binding.tvHumidity.text =
                            "${state.data.body()?.main?.humidity?.toString()}%"
                        binding.tvPressure.text =
                            "${state.data.body()?.main?.pressure?.toString()}hPa"
                        binding.tvSunrise.text =
                            SimpleDateFormat("HH:mm:ss", Locale.ENGLISH).format(
                                Date(
                                    (state.data.body()?.sys?.sunrise?.toLong())!! * 1000
                                )
                            )
                        binding.tvSunset.text =
                            SimpleDateFormat("HH:mm:ss", Locale.ENGLISH).format(
                                Date(
                                    (state.data.body()?.sys?.sunset?.toLong())!! * 1000
                                )
                            )
                        binding.tvWind.text = "${state.data.body()?.wind?.speed?.toString()} km/h"
                        binding.tvWindDegree.text = "${state.data.body()?.wind?.deg?.toString()}°"
                    }
                }
            }
            is ScreenState.Error -> {
                Log.e(TAG,"ScreenState.Error")
                binding.progressBar.isVisible = false
                //binding.imgNoWifi.isVisible = true
                notConnected()
            }
        }
    }

    private fun notConnected() {
        val dialogBuilder = AlertDialog.Builder(this, R.style.AlertDialogTheme)
        dialogBuilder
            .setMessage("Please check your connection and try again.")
            .setTitle("Connection")
            .setCancelable(false)
            .setNegativeButton("Exit", DialogInterface.OnClickListener { _, _ -> finish() })
            .setIcon(R.drawable.no_wifi_2)
            .show()
    }

    private fun about() {
        binding.btnAbout.setOnClickListener {
            Intent(this, AboutActivity::class.java).apply {
                startActivity(this)
            }
        }
    }
}