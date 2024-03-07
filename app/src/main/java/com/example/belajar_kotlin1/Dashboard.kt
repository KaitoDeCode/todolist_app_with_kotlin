package com.example.belajar_kotlin1

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.belajar_kotlin1.databinding.ActivityDashboardBinding
import com.example.belajar_kotlin1.databinding.TodoViewBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class Dashboard : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding

    private var updateLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            getData("http://10.0.2.2:5036/api/Todo/not-completed", "GET")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getData("http://10.0.2.2:5036/api/Todo/not-completed", "GET")
        navBtn()
    }

    private fun navBtn() {
        binding.navList.setOnClickListener {
            startActivity(Intent(this@Dashboard, Dashboard::class.java))
        }
        binding.navTambah.setOnClickListener {
            startActivity(Intent(this@Dashboard, Tambah::class.java))
        }
        binding.navProfile.setOnClickListener {
            startActivity(Intent(this@Dashboard, Profile::class.java))
        }
    }

    private fun getData(url: String, method: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val todoStr = URL(url).openStream().bufferedReader().readText()
            val todoList = JSONArray(todoStr)

            runOnUiThread {
                class CustomVh(val binding: TodoViewBinding) : RecyclerView.ViewHolder(binding.root)

                val adapter = object : RecyclerView.Adapter<CustomVh>() {
                    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomVh {
                        val binding = TodoViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                        return CustomVh(binding)
                    }

                    override fun getItemCount(): Int = todoList.length()

                    override fun onBindViewHolder(holder: CustomVh, position: Int) {
                        val todo = todoList.getJSONObject(position)
                        holder.binding.nomor.text = (position + 1).toString()
                        holder.binding.title.text = todo.getString("title")
                        holder.binding.btnSubmit.setOnClickListener {
                          GlobalScope.launch (Dispatchers.IO){
                              val id = todo.getString("id")
                              val API_URL = URL("http://10.0.2.2:5036/api/Todo/$id")
                              val conn = API_URL.openConnection() as HttpURLConnection

                              conn.requestMethod = "PUT"

                              val data = JSONObject().apply {
                                  put("id", id)
                                  put("title", todo.getString("title"))
                                  put("isCompleted", true)
                              }
                              conn.setRequestProperty("Content-Type","application/json")

                              conn.outputStream.write(data.toString().toByteArray())

                              val statusCode = conn.responseCode
                              Log.d("status-code", "onBindViewHolder: $statusCode")
                              if (statusCode == 200 || statusCode == 201) {
                                  updateLauncher.launch(Intent(this@Dashboard, Dashboard::class.java))
                                  runOnUiThread{
                                  Toast.makeText(this@Dashboard, "Berhasil menyelesaikan todo", Toast.LENGTH_SHORT).show()

                                  }
                              } else {

                                  var result = conn.errorStream.bufferedReader().readText()
                                  var error = JSONObject(result)
                                  Log.d("Error", "onCreate: ${error}")


                                  runOnUiThread{
                                  Toast.makeText(this@Dashboard, "Gagal menyelesaikan todo", Toast.LENGTH_SHORT).show()

                                  }
                              }
                          }
                        }
                    }
                }

                binding.todoList.adapter = adapter
                binding.todoList.layoutManager = LinearLayoutManager(this@Dashboard)
            }
        }
    }
}