package com.example.projectses4_apptivi.ui

import android.app.ProgressDialog
import android.content.ContentValues.TAG
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
import com.example.projectses4_apptivi.R
import com.example.projectses4_apptivi.databinding.ActivityDashboardBinding
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
    //progress bar
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
            downloadVideoInList()
            progressDialog.dismiss()
        }

        binding.btnPlay.setOnClickListener {
            val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"video")
            val path = file.absolutePath
            val videoPath = "$path/video_kem_taco.mp4"
            Log.d(TAG, "File $path")
            videoView.start()
        }

    }
//    private fun listVideoOfDevice(fileList : List<String>):List<String>{
//        return fileList
//    }


    //cac function rieng
    //handle permission result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE -> {
                // Nếu người dùng cho phép truy cập bộ nhớ trong, tiến hành tải file về
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    downloadVideoInList()
                } else {
                    // Người dùng từ chối quyền truy cập bộ nhớ trong, hiển thị thông báo hoặc thực hiện hành động khác tùy vào yêu cầu của ứng dụng
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show()
                }
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }

    }
    private fun downloadVideoInList(){
        val storageRef = FirebaseStorage.getInstance().reference
        val fileList = listOf("video_kem_taco") // danh sách tên file cần tải
        val localDir = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) // thư mục lưu trữ tại thiết bị
        for (file in fileList) {
            val localFile = File(localDir, file)
            val videoRef = storageRef.child("Videos/$file")

            videoRef.getFile(localFile).addOnSuccessListener {
                // tải file thành công
                Log.d(TAG, "File $file downloaded successfully")
                Toast.makeText(this, "File $file downloaded successfully", Toast.LENGTH_LONG).show()
            }.addOnFailureListener {
                // tải file thất bại
                Log.d(TAG, "File $file downloaded failed")
                Toast.makeText(this, "File $file downloaded failed", Toast.LENGTH_LONG).show()
            }
        }

//        val fileList = listOf("video_kem_taco", "an_kieng_a007")
//        progressDialog.setMessage("Downloading $file")
//        progressDialog.show()
//        Log.d(TAG, "File $file downloaded successfully")
//        Toast.makeText(this, "File $file downloaded successfully", Toast.LENGTH_LONG).show()
    }
}