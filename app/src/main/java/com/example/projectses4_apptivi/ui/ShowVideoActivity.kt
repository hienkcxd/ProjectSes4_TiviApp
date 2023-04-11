package com.example.projectses4_apptivi.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.projectses4_apptivi.R
import com.example.projectses4_apptivi.databinding.ActivityMainBinding
import com.example.projectses4_apptivi.databinding.ActivityShowVideoBinding

class ShowVideoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShowVideoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
