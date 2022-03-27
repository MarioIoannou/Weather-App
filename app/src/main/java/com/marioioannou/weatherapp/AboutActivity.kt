package com.marioioannou.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.marioioannou.weatherapp.databinding.ActivityAboutBinding
import com.marioioannou.weatherapp.databinding.ActivityMainBinding

private lateinit var binding: ActivityAboutBinding


class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}