package com.example.mns

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)

        var btn_Createpost = findViewById<ImageButton>(R.id.btn_createpin)
        btn_Createpost.setOnClickListener {
            var intent = Intent(applicationContext,PostCreateActivity::class.java)
            startActivity(intent)
        }
        var btnReadPost = findViewById<ImageButton>(R.id.map)
        btnReadPost.setOnClickListener {
            var intent = Intent(applicationContext,PostReadActivity::class.java)
            startActivity(intent)
        }




    }

    /********** 툴바 우측의 메뉴 **********/
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        // 툴바 옵션 제거 (글 등록 옵션)
        var item : MenuItem
        item = menu!!.findItem(R.id.menu_edit)
        item.setVisible(false)
        return super.onCreateOptionsMenu(menu)
    }


}