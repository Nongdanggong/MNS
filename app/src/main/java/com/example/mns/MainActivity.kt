package com.example.mns

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)
        
        // 툴바에 메뉴 달아주는 코드 작성해야함 !!

        var btnCreatePost = findViewById<Button>(R.id.btnCreatePost)
        btnCreatePost.setOnClickListener {
            var intent = Intent(applicationContext,PostCreateActivity::class.java)
            startActivity(intent)
        }
        var btnReadPost = findViewById<Button>(R.id.btnReadPost)
        btnReadPost.setOnClickListener {
            var intent = Intent(applicationContext,PostReadActivity::class.java)
            startActivity(intent)
        }
    }
}