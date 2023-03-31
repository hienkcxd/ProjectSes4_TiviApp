package com.example.projectses4_apptivi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.projectses4_apptivi.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}