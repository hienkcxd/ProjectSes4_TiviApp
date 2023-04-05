package com.example.projectses4_apptivi.ui

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.StatFs
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.projectses4_apptivi.R
import com.example.projectses4_apptivi.databinding.ActivityDashboardBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.InputStream
import java.lang.String.format

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private var popup: PopupWindow? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //chức năng của nut xem system hệ thống
        val osVersion = Build.VERSION.RELEASE
        val path = Environment.getDataDirectory().path
        val stat = StatFs(path)
        val blockSize = stat.blockSizeLong
        val availableSize = stat.availableBlocksLong * blockSize/(1000*1024*1024)
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
            popupView.findViewById<TextView>(R.id.tvRomAvaiable).text = String.format("%.2f",availableSize.toFloat()).plus(" Gb")
            popup?.showAtLocation(popupView, Gravity.CENTER, 0, 0)
        }
        btnClose.setOnClickListener {
            popup?.dismiss()
        }

        //chức năng xóa các file trong bộ nhớ cache
        val downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(downloadDir, "ten_tep_tin")
        val builder = AlertDialog.Builder(this)
        binding.btnClearCache.setOnClickListener {
            builder.setTitle("Delete Videos")
            builder.setMessage("Do you want delete all videos?")
            builder.setPositiveButton("Accept") { dialog, which ->
                file.delete()
                Toast.makeText(this, "Video has been delete", Toast.LENGTH_SHORT).show()
            }
            builder.setNegativeButton("Decline") { dialog, which ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }

        //chức năng tải nội dung video
        val client = OkHttpClient()
        val url = "https://drive.google.com/uc?id=1a2YAn5wgYUZVUPEqUKfTMvw4TTiV2-1m&export=download"
        val request = Request.Builder()
            .url(url)
            .build()

    }
}