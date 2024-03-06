package com.example.belajar_kotlin1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.belajar_kotlin1.databinding.ActivityRegisterBinding

class Register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var bind  =  ActivityRegisterBinding.inflate(this.layoutInflater)
        setContentView(bind.root)

        bind.loginBtn.setOnClickListener {
            startActivity(Intent(this@Register,MainActivity::class.java))
        }

    }
}