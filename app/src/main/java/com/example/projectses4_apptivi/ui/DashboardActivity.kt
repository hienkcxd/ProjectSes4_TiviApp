package com.example.projectses4_apptivi.ui

import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.StatFs
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.projectses4_apptivi.MainActivity
import com.example.projectses4_apptivi.R
import com.example.projectses4_apptivi.databinding.ActivityDashboardBinding
import com.example.projectses4_apptivi.io.ManageSharePreference.JwtSharePreference
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.InputStream
import java.lang.String.format

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private var popup: PopupWindow? = null
    private lateinit var videofile : List<String>
    private var videoUri : Uri?=null
    private val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1
    private lateinit var videoView:VideoView
    private lateinit var sharedPreferences: JwtSharePreference
    //progress bar
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = JwtSharePreference(this)
        //progress bar
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("Please Wait...")
        progressDialog.setCanceledOnTouchOutside(false)

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
        videofile = listOf("video_kem_taco", "video_1681181648756")
        //nut refresh
        binding.btnRefresh.setOnClickListener {
            val intent = Intent(this, ShowVideoActivity::class.java)
            startActivity(intent)
        }

        binding.btnPlay.setOnClickListener {

        }
        binding.btnLogout.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Bạn có muốn ở lại hay rời đi?")
                .setCancelable(false)
                .setPositiveButton("Cancel") { dialog, id ->
                    // Khi chọn ở lại
                    dialog.dismiss()
                }
                .setNegativeButton("Accept") { dialog, id ->
                    clearJwt()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    dialog.dismiss()
                    finish()
                }
            val alert = builder.create()
            alert.show()
        }
    }
    private fun clearJwt(){
        sharedPreferences.clearJwt()
    }
}