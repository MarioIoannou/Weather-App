package com.marioioannou.weatherapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.marioioannou.weatherapp.databinding.ActivityAboutBinding

private lateinit var binding: ActivityAboutBinding


class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}