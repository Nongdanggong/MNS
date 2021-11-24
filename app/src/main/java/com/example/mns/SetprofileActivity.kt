package com.example.mns

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class SetprofileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setprofile)

        /********* 화면 상단 툴바 *********/
        setSupportActionBar(findViewById(R.id.toolbar))
        // 툴바 기본 타이틀 제거
        supportActionBar?.setDisplayShowTitleEnabled(false)
        // 뒤로가기 버튼 활성화
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var image_profile = findViewById<ImageButton>(R.id.account)
        var text_name = findViewById<EditText>(R.id.name_text)
        var name = text_name.text.toString()
        var btn_dup = findViewById<Button>(R.id.btn_dup)

        // 프로필 이미지 세팅
        image_profile.setOnClickListener {
            var intent = Intent(Intent.ACTION_PICK)
            intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        // 중복확인
        btn_dup.setOnClickListener {


        }




    }

    /********** 툴바의 메뉴들 기능 **********/
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            // 뒤로가기 버튼을 클릭(선택)한 경우
            android.R.id.home -> {
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
            var image_profile = findViewById<ImageButton>(R.id.account)

            var currentImageUrl: Uri? = data?.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, currentImageUrl)
            image_profile.setPadding(0, 0, 0, 0)
            image_profile.setImageBitmap(bitmap)
        }
    }


}