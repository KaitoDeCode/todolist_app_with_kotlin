package com.example.belajar_kotlin1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.belajar_kotlin1.databinding.ActivityProfileBinding

class Profile : AppCompatActivity() {
    lateinit var binding: ActivityProfileBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding = ActivityProfileBinding.inflate(this.layoutInflater)
        setContentView(binding.root)
        navBtn()
    }

    fun navBtn(){
        binding.navList3.setOnClickListener {
            startActivity(Intent(this@Profile,Dashboard::class.java))
        }
        binding.navTambah3.setOnClickListener {
            startActivity(Intent(this@Profile,Tambah::class.java))
        }
        binding.navProfile3.setOnClickListener {
            startActivity(Intent(this@Profile,Profile::class.java))
        }
    }
}