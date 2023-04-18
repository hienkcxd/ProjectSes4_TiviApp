package com.example.projectses4_apptivi.ui

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.example.projectses4_apptivi.MainActivity
import com.example.projectses4_apptivi.R
import com.example.projectses4_apptivi.databinding.ActivityCreateDeviceBinding
import com.example.projectses4_apptivi.io.ManageSharePreference.JwtSharePreference
import com.example.projectses4_apptivi.io.response.AreaResponse
import com.example.projectses4_apptivi.io.response.GroupStoreResponse
import com.example.projectses4_apptivi.io.response.LoginResponse
import com.example.projectses4_apptivi.io.response.StoreResponse
import com.example.projectses4_apptivi.io.service.DeviceService
import com.example.projectses4_apptivi.io.service.LoginService
import com.example.projectses4_apptivi.model.DeviceModel
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.awaitResponse

class CreateDeviceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateDeviceBinding
    private var areaList : List<String> = listOf()
    private var storeList : List<String> = listOf()
    private var groupList : List<String> = listOf()
    private var areaSelected : String = ""
    private var storeSelected : String = ""
    private var groupSelected : String = ""
    private lateinit var autoCompleteTextViewArea : AutoCompleteTextView
    private lateinit var autoCompleteTextViewStore : AutoCompleteTextView
    private lateinit var autoCompleteTextViewGroup : AutoCompleteTextView
    private lateinit var sharedPreferences: JwtSharePreference
    private lateinit var gson: Gson
    private val deviceService: DeviceService by lazy {
        DeviceService.create(getJwt().toString())
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateDeviceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = JwtSharePreference(this)
        autoCompleteTextViewArea = binding.edtArea
        autoCompleteTextViewStore = binding.edtStore
        autoCompleteTextViewGroup = binding.edtGroup
        Log.i("jwt", "area list tra ve adapter ${areaList.toString()} ")

        //dropdown list for Area
        lifecycleScope.launch {
            getListArea()
            val adapterArea = ArrayAdapter<Any>(this@CreateDeviceActivity, android.R.layout.simple_dropdown_item_1line,areaList)
            autoCompleteTextViewArea.setAdapter(adapterArea)
        }
        autoCompleteTextViewArea.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, id ->
            areaSelected = adapterView.getItemAtPosition(position).toString()
            Toast.makeText(this, "item selected: $areaSelected", Toast.LENGTH_LONG).show()
        }

        //dropdown list for store
        lifecycleScope.launch {
            getListStore()
            val adapterStore = ArrayAdapter<Any>(this@CreateDeviceActivity, android.R.layout.simple_dropdown_item_1line,storeList)
            autoCompleteTextViewStore.setAdapter(adapterStore)
        }
        autoCompleteTextViewStore.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, id ->
            storeSelected = adapterView.getItemAtPosition(position).toString()
            Toast.makeText(this, "store selected: $storeSelected", Toast.LENGTH_LONG).show()
        }

        //dropdown list for group
        lifecycleScope.launch {
            getListGroupDevice()
            val adapterGroup = ArrayAdapter<Any>(this@CreateDeviceActivity, android.R.layout.simple_dropdown_item_1line,groupList)
            autoCompleteTextViewGroup.setAdapter(adapterGroup)
        }
        autoCompleteTextViewGroup.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, id ->
            groupSelected = adapterView.getItemAtPosition(position).toString()
            Toast.makeText(this, "group selected: $groupSelected", Toast.LENGTH_LONG).show()
        }
        binding.btnCancelDevice.setOnClickListener {
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
        binding.btnCreateDevice.setOnClickListener {
            val nameDevice = binding.edtDeviceName.text.toString()
            val userName = getUser()
            val hardwareId = Build.ID
            val deviceModel = DeviceModel(
                deviceID = hardwareId,
                deviceName = nameDevice,
                username = userName,
                storeName = storeSelected,
                groupName = groupSelected,
                area = areaSelected,
                fileStorage = null,
            )
            lifecycleScope.launch {
                saveDevice(deviceModel)
            }
        }
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        return super.onTouchEvent(event)
    }
    private fun clearJwt() {
        sharedPreferences.clearJwt()
    }
    private fun getJwt():String? {
        return sharedPreferences.getJwt()
    }
    private fun getUser():String? {
        return sharedPreferences.getUser()
    }
    private fun saveDeviceId(deviceId:String){
        sharedPreferences.saveDeviceId(deviceId)
    }
    private suspend fun saveDevice(deviceModel:DeviceModel){
        try {
            val saveDevice = deviceService.saveDevice(getJwt().toString(), deviceModel)
            val response = saveDevice.awaitResponse()
            if (response.isSuccessful) {
                val device : DeviceModel? = response.body()
                Log.i("jwt", "thanh cong save ${device.toString()} ")
                val intent = Intent(this, DashboardActivity::class.java)
                saveDeviceId(device?.deviceID.toString())
                startActivity(intent)
            } else {
                Log.i("jwt", "them device khong thanh cong")
                Toast.makeText(applicationContext, "can add device, sorry!!!", Toast.LENGTH_LONG).show()
            }
        }
        catch (e: Exception) {
            Log.e("jwt", e.message ?: "Unknown error")
        }
    }
    private suspend fun getListArea(){
        try {
            val callArea = deviceService.getArea(getJwt().toString())
            val response = callArea.awaitResponse()
            if (response.isSuccessful) {
                val areas: List<AreaResponse>? = response.body()
                Log.i("jwt", "jwt lay duoc ${areas.toString()} ")
                if (areas == null) {
                    Toast.makeText(applicationContext, "không có dữ liệu!!!", Toast.LENGTH_LONG).show()
                    Log.i("jwt", "area trống")
                    return
                } else {
                    areaList = areas.map { it.areaName }
                    Log.i("jwt", areaList.toString())
                }
            } else {
                Log.i("jwt", "khong lay duoc du lieu")
                Toast.makeText(applicationContext, "error 403!!!", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Log.e("jwt", e.message ?: "Unknown error")
        }
    }

    private suspend fun getListStore(){
        try {
            val callStore = deviceService.getStore(getJwt().toString())
            val response = callStore.awaitResponse()
            if (response.isSuccessful) {
                val storeResponse: List<StoreResponse>? = response.body()
                Log.i("jwt", "jwt lay duoc ${storeResponse.toString()} ")
                if (storeResponse == null) {
                    Toast.makeText(applicationContext, "không có dữ liệu!!!", Toast.LENGTH_LONG).show()
                    Log.i("jwt", "store trống")
                    return
                } else {
                    storeList = storeResponse.map { it.storeName }
                    Log.i("jwt", "danh sach store: ${storeList.toString()}")
                }
            } else {
                Log.i("jwt", "khong lay duoc du lieu")
                Toast.makeText(applicationContext, "error 403!!!", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Log.e("jwt", e.message ?: "Unknown error")
        }
    }

    private suspend fun getListGroupDevice(){
        try {
            val callGroup = deviceService.getGroupDevice(getJwt().toString())
            val response = callGroup.awaitResponse()
            if (response.isSuccessful) {
                val groupResponse: List<GroupStoreResponse>? = response.body()
                Log.i("jwt", "jwt lay duoc ${groupResponse.toString()} ")
                if (groupResponse == null) {
                    Toast.makeText(applicationContext, "không có dữ liệu!!!", Toast.LENGTH_LONG).show()
                    Log.i("jwt", "group trống")
                    return
                } else {
                    groupList = groupResponse.map { it.groupName }
                    Log.i("jwt", "danh sach group: ${groupList.toString()}")
                }
            } else {
                Log.i("jwt", "khong lay duoc du lieu")
                Toast.makeText(applicationContext, "error 403!!!", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Log.e("jwt", e.message ?: "Unknown error")
        }
    }
}

