package com.example.belajar_kotlin1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.belajar_kotlin1.databinding.ActivityTambahBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class Tambah : AppCompatActivity() {
    lateinit var binding: ActivityTambahBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahBinding.inflate(this.layoutInflater)
        setContentView(binding.root)
        navBtn()

        binding.btnCreateTodo.setOnClickListener {
            GlobalScope.launch (Dispatchers.IO){
                var API_URL = URL("http://10.0.2.2:5036/api/Todo")
                var conn = API_URL.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.setRequestProperty("Content-Type","application/json")

                var data = JSONObject().apply {
                    put("id",0)
                    put("title",binding.inpTitle.text)
                    put("isCompleted",false)
                }

                conn.outputStream.write(data.toString().toByteArray())
                var statusCode = conn.responseCode
                Log.d("Status-Code", "onCreate: ${statusCode}")
                if(statusCode == 200 || statusCode == 201){
                    var result = conn.inputStream.bufferedReader().readText()
                    var todo = JSONObject(result)

                    runOnUiThread{
                        Toast.makeText(this@Tambah, "Berhasil menambahkan todo", Toast.LENGTH_SHORT).show()
                    }

                }else{

                    var result = conn.errorStream.bufferedReader().readText()
                    var error = JSONObject(result)
                    Log.d("Error", "onCreate: ${error}")

                    runOnUiThread{
                        Toast.makeText(this@Tambah,"Gagal membuat todo", Toast.LENGTH_SHORT).show()
                    }

                }

            }
        }

    }

    fun navBtn(){
        binding.navList2.setOnClickListener {
            startActivity(Intent(this@Tambah,Dashboard::class.java))
        }
        binding.navTambah2.setOnClickListener {
            startActivity(Intent(this@Tambah,Tambah::class.java))
        }
        binding.navProfile2.setOnClickListener {
            startActivity(Intent(this@Tambah,Profile::class.java))
        }
    }


}