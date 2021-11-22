package com.example.mns

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


// Activity() -> AppCompatActivity()를 해줘야 툴바 사용 가능
class PostReadActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_read)

        /********* 화면 상단 툴바 *********/
        setSupportActionBar(findViewById(R.id.toolbar))
        // 툴바 기본 타이틀 제거
        supportActionBar?.setDisplayShowTitleEnabled(false)
        // 뒤로가기 버튼 활성화
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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