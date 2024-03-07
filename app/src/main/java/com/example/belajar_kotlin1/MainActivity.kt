package com.example.belajar_kotlin1

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import com.example.belajar_kotlin1.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ActivityMainBinding.inflate(this.layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener{
            GlobalScope.launch(Dispatchers.IO){
                var API_URL = URL("http://10.0.2.2:5036/api/Auth/login")
                var conn = API_URL.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.setRequestProperty("Content-Type","application/json")

                var data = JSONObject().apply {
                    put("email", binding.inptEmail.text)
                    put("password", binding.inpPassword.text)
                }
                Log.d("data", "onCreate: ${data.toString()}")

                conn.outputStream.write(data.toString().toByteArray())

                var statusCode = conn.responseCode
                Log.d("Status-Code", "onCreate: ${statusCode}")

                if(statusCode == 200 || statusCode == 201){
                    var result = conn.inputStream.bufferedReader().readText()
                    var user = JSONObject(result)

                    // Cara menyimpan data ke local storage
                    runOnUiThread{
                        var editor = getSharedPreferences("random", MODE_PRIVATE).edit()
                        editor.putString("user", result)
                        editor.putInt("user_id", user.getInt("id"))
                        editor.apply()

                        setResult(RESULT_OK, Intent().apply{
                            putExtra("nama","Nama :" + binding.inptEmail.text.toString())
                            putExtra("pass", "Password : " + binding.inptEmail.text.toString())
                        })

                        Toast.makeText(this@MainActivity, "Selamat datang",Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@MainActivity,Dashboard::class.java))
                    }


                }else{
                    var result = conn.errorStream.bufferedReader().readText()
                    var error = JSONObject(result)
                    Log.d("Error", "onCreate: ${error}")

                    runOnUiThread{
                     Toast.makeText(this@MainActivity," Gagal Authentikasi", Toast.LENGTH_SHORT).show()
                    }

                }

            }
        }

        binding.btnRegister.setOnClickListener{
            startActivity(Intent(this@MainActivity,Register::class.java))
        }

    }


}