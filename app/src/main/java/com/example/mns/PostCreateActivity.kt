package com.example.mns

import android.Manifest
import android.app.ActionBar
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File

class PostCreateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_create)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var imagebtn = findViewById<ImageButton>(R.id.userPhoto)
        var pinbtn = findViewById<ImageButton>(R.id.pin)
        var pinArray = arrayOf("실시간 상황", "프로모션", "나만의 관광지", "질문", "사진 핫스팟")
        var pincolor = arrayOf(R.drawable.pinred, R.drawable.pinblue,
                        R.drawable.pingreen, R.drawable.pin, R.drawable.pinyellow)

        // 핀 버튼 클릭했을 때 핀 색상 선택 가능
        pinbtn.setOnClickListener {
            var dlg = AlertDialog.Builder(this)
            dlg.setTitle("핀 카테고리 선택")
            dlg.setItems(pinArray){dialog, which->
                pinbtn.setImageResource(pincolor[which])
            }
            dlg.setIcon(R.drawable.pin)
            dlg.show()
        }

        // + 버튼 클릭했을 때 갤러리 열어 이미지 파일 들고오기
        imagebtn.setOnClickListener {
            var intent = Intent(Intent.ACTION_PICK)
            intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            android.R.id.home -> {
                finish()
                return true
            }
            // 우측 상단 아이콘 클릭시 (데이터베이스로 파일 전송하는 코드 필요)
            R.id.menu_edit -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    
    // 선택한 이미지를 비트맵화 한 뒤 이미지버튼을 변경
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            var imagebtn = findViewById<ImageButton>(R.id.userPhoto)

            var currentImageUrl: Uri? = data?.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, currentImageUrl)
            imagebtn.setPadding(0, 0, 0, 0)
            imagebtn.setImageBitmap(bitmap)
        }
    }

}




