package com.example.projectses4_apptivi.ui

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.PopupWindow
import android.widget.TextView
import com.example.projectses4_apptivi.R
import com.example.projectses4_apptivi.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private var popup: PopupWindow? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val osVersion = Build.VERSION.RELEASE
        val popupView = LayoutInflater.from(this).inflate(R.layout.popup_detail_device, null)
        val btnClose = popupView.findViewById<ImageButton>(R.id.btnClosePopUp)
        popup = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        binding.btnSystem.setOnClickListener {
            popupView.findViewById<TextView>(R.id.tvDeviceName).text = osVersion
            popupView.findViewById<TextView>(R.id.tvStoreName).text = osVersion
            popupView.findViewById<TextView>(R.id.tvAndroidVersion).text = osVersion
            popup?.showAtLocation(popupView, Gravity.CENTER, 0, 0)
        }
        btnClose.setOnClickListener {
            popup?.dismiss()
        }
    }
}