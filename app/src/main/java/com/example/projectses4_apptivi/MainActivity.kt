package com.example.projectses4_apptivi

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.projectses4_apptivi.databinding.ActivityMainBinding
import com.example.projectses4_apptivi.io.service.LoginService
import com.example.projectses4_apptivi.io.service.LoginRequest
import com.example.projectses4_apptivi.io.ManageSharePreference.JwtSharePreference
import com.example.projectses4_apptivi.io.response.GroupStoreResponse
import com.example.projectses4_apptivi.io.response.LoginResponse
import com.example.projectses4_apptivi.io.service.DeviceService
import com.example.projectses4_apptivi.model.DeviceModel
import com.example.projectses4_apptivi.ui.CreateDeviceActivity
import com.example.projectses4_apptivi.ui.DashboardActivity
import com.example.projectses4_apptivi.ui.ShowVideoActivity
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.awaitResponse

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: JwtSharePreference
    private var deviceList : List<String> = listOf()
    private var checkDevice : Boolean = true
    private val loginService:LoginService by lazy {
        LoginService.create()
    }
    private val deviceService: DeviceService by lazy {
        DeviceService.create(getJwt().toString())
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = JwtSharePreference(this)
        lifecycleScope.launch {
            getListDevice()
            checkDevice = deviceList.contains(sharedPreferences.getDeviceId().toString())
            Log.i("jwt", "tinh trang check device co chua nhau khong: ${checkDevice.toString()}")
            goToFirst()
        }
        binding.btnSubmit.setOnClickListener{
            formLogin()
        }

    }
    private fun goToFirst(){
        if (isLoggedIn()&&checkDevice) {
            Log.i("jwt", "tinh trang check device: ${checkDevice.toString()}")
            val intent = Intent(this, ShowVideoActivity::class.java)
            startActivity(intent)
            finish()
        }
        else if(isLoggedIn()&&!checkDevice){
            Log.i("jwt", "tinh trang check device: ${checkDevice.toString()}")
            val intent = Intent(this, CreateDeviceActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        return super.onTouchEvent(event)
    }

    private fun saveJwt(jwt: String) {
        sharedPreferences.saveJwt(jwt)
    }

    private fun getJwt(): String? {
        return sharedPreferences.getJwt()
    }
    private fun saveUser(user: String) {
        sharedPreferences.saveUser(user)
    }

    private fun clearJwt() {
        sharedPreferences.clearJwt()
    }

    fun isLoggedIn(): Boolean {
        val sharedPreferences = getSharedPreferences("jwt_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.contains("jwt") //kiểm tra xem đã lưu jwt_token trong SharedPreferences chưa
    }

    private fun formLogin(){
        val usernameLogin = findViewById<EditText>(R.id.edtUsername).text.toString()
        val passwordLogin = findViewById<EditText>(R.id.edtPassword).text.toString()
        Log.i("jwt", usernameLogin)
        Log.i("jwt", passwordLogin)
        val loginRequest = LoginRequest(usernameLogin, passwordLogin)
        val call = loginService.login(usernameLogin, passwordLogin)
        call.enqueue(object: Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if(response.isSuccessful){
                    val loginResponse = response.body()
                    if(loginResponse == null){
                        Toast.makeText(applicationContext, "Login Error!!!", Toast.LENGTH_LONG).show()
                        return
                    }
                    else{
                        saveJwt(loginResponse.access_token)
                        saveUser(usernameLogin)
                        Log.i("jwt", "jwt hien tai: ${getJwt().toString()}")
                        gotoNext()
                    }
                }else{
                    Toast.makeText(applicationContext, "invalid username or password!!!", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(applicationContext, "login failed 123!!!", Toast.LENGTH_LONG).show()
            }

        })
    }
    private fun gotoNext() {
            val intent = Intent(this, CreateDeviceActivity::class.java)
            startActivity(intent)
            finish()
    }
    private suspend fun getListDevice(){
        try {
            val callGroup = deviceService.getDeviceList(getJwt().toString())
            val response = callGroup.awaitResponse()
            if (response.isSuccessful) {
                val deviceResponse: List<DeviceModel>? = response.body()
                Log.i("jwt", "jwt lay duoc ${deviceResponse.toString()} ")
                if (deviceResponse == null) {
                    Toast.makeText(applicationContext, "không có dữ liệu!!!", Toast.LENGTH_LONG).show()
                    Log.i("jwt", "group trống")
                    return
                } else {
                    deviceList = deviceResponse.map { it.deviceID.toString() }
                    Log.i("jwt", "danh sach group: ${deviceList.toString()}")
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