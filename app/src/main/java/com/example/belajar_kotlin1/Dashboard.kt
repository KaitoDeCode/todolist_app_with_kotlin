package com.example.belajar_kotlin1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.belajar_kotlin1.databinding.ActivityDashboardBinding
import com.example.belajar_kotlin1.databinding.TodoViewBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL

class Dashboard : AppCompatActivity() {
lateinit var binding: ActivityDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding = ActivityDashboardBinding.inflate(this.layoutInflater)
        setContentView(binding.root)
        getData("http://10.0.2.2:5036/api/Todo","GET")
    }

    fun getData(url: String,method: String, ){

        GlobalScope.launch(Dispatchers.IO){
            var todoStr = URL(url).openStream().bufferedReader().readText()
            var todoList = JSONArray(todoStr)

            runOnUiThread{
                class CustomVh(val binding: TodoViewBinding) : RecyclerView.ViewHolder(binding.root)

                var adapter = object : RecyclerView.Adapter<CustomVh>(){
                    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomVh {
                        var binding = TodoViewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                        return CustomVh(binding)
                    }

                    override fun getItemCount(): Int = todoList.length()

                    override fun onBindViewHolder(
                        holder: CustomVh,
                        position: Int,
                    ) {
                        val todo = todoList.getJSONObject(position)
                        holder.binding.nomor.text = (position+1).toString()
                        holder.binding.title.text = todo.getString("title")
                       //super.onBindViewHolder(holder, position)
                    }

                }


                binding.todoList.adapter = adapter
                binding.todoList.layoutManager = LinearLayoutManager(this@Dashboard)

            }

        }
    }
}