package com.example.mns

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        /********* 화면 상단 툴바 *********/
        setSupportActionBar(findViewById(R.id.toolbar))
        // 툴바 기본 타이틀 제거
        supportActionBar?.setDisplayShowTitleEnabled(false)
        // 뒤로가기 버튼 활성화
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


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

}