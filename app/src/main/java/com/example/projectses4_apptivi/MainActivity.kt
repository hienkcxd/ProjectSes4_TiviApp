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
import com.example.projectses4_apptivi.databinding.ActivityMainBinding
import com.example.projectses4_apptivi.io.LoginRequest
import com.example.projectses4_apptivi.io.LoginService
import com.example.projectses4_apptivi.io.ManageSharePreference.JwtSharePreference
import com.example.projectses4_apptivi.io.response.LoginResponse
import com.example.projectses4_apptivi.ui.DashboardActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: JwtSharePreference
    private val loginService:LoginService by lazy {
        LoginService.create()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = JwtSharePreference(this)
        binding.btnSubmit.setOnClickListener{
            formLogin()
        }

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        return super.onTouchEvent(event)
    }

    private fun gotoDashboard() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun saveJwt(jwt: String) {
        sharedPreferences.saveJwt(jwt)
    }

    private fun getJwt(): String? {
        return sharedPreferences.getJwt()
    }

    private fun clearJwt() {
        sharedPreferences.clearJwt()
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
                        Log.i("jwt", "jwt hien tai: ${getJwt().toString()}")
                        gotoDashboard()
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
}