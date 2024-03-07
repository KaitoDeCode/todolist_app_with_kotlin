package com.example.belajar_kotlin1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.belajar_kotlin1.databinding.ActivityRegisterBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class Register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var bind  =  ActivityRegisterBinding.inflate(this.layoutInflater)
        setContentView(bind.root)

        bind.loginBtn.setOnClickListener {
            startActivity(Intent(this@Register,MainActivity::class.java))
        }

        bind.registerBtn.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO){
                var API_URL = URL("http://10.0.2.2:5036/api/Auth/register")
                var conn = API_URL.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.setRequestProperty("Content-Type","application/json")

                var data = JSONObject().apply {
                    put("full_name", bind.inptFullname.text)
                    put("phone", bind.inptTelp.text)
                    put("email", bind.inptEmail.text)
                    put("password", bind.inpPassword.text)
                }

                conn.outputStream.write(data.toString().toByteArray())

                var statusCode = conn.responseCode

                if(statusCode == HttpURLConnection.HTTP_OK){
                    var result = conn.inputStream.bufferedReader().readText()
                    var user = JSONObject(result)

                    runOnUiThread{
                        var editor = getSharedPreferences("random", MODE_PRIVATE).edit()
                        editor.putString("user", result)
                        editor.putInt("user_id", user.getInt("ID"))
                        editor.apply()

                        setResult(RESULT_OK, Intent().apply{
                            putExtra("email","Email :" + bind.inptEmail.text.toString())
                            putExtra("pass", "Password : " + bind.inpPassword.text.toString())
                        })

                        Toast.makeText(this@Register, "Registrasi Berhasil", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@Register,MainActivity::class.java))
                    }

                }else{
                    var result = conn.errorStream.bufferedReader().readText()
                    var error = JSONObject(result)
                    Log.d("Error", "onCreate: ${error}")

                    runOnUiThread{
                        Toast.makeText(this@Register," Gagal Registrasi", Toast.LENGTH_SHORT).show()
                    }
                }
                
            }
        }

    }
}