package com.example.mns

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
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

        var imgbtn_heart = findViewById<ImageButton>(R.id.imgbtn_heart)
        var imgbtn_scrap = findViewById<ImageButton>(R.id.imgbtn_scrap)
        var imgbtn_send = findViewById<ImageButton>(R.id.bottom_comment_sendBtn)

        // 핀 카테고리에 따라 resource 변경 코드 필요
        var pin = findViewById<ImageView>(R.id.pin)

        // 버튼 클릭시 버튼 이미지 변환 조절
        var i = true

        // 좋아요 정보 저장하는 코드 필요
        imgbtn_heart.setOnClickListener {
            i = if(i){
                imgbtn_heart.setImageResource(R.drawable.heart_r)
                false
            } else {
                imgbtn_heart.setImageResource(R.drawable.heart_b)
                true
            }
        }

        // 스크랩 정보 저장하는 코드 필요
        imgbtn_scrap.setOnClickListener {
            i = if(i){
                imgbtn_scrap.setImageResource(R.drawable.bookmark_g)
                false
            } else {
                imgbtn_scrap.setImageResource(R.drawable.bookmark_b)
                true
            }
        }

        // 버튼 클릭시 데이터베이스에 댓글 내용 저장 뒤 화면 새로고침
        imgbtn_send.setOnClickListener {


        }

    }

//    /********** 툴바 우측의 메뉴 **********/
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.menu_toolbar, menu)
//        // 툴바 옵션 제거 (글 등록 옵션) ---> 그냥 onCreateOptionsMenu 안하면 됨......
//        var item : MenuItem
//        item = menu!!.findItem(R.id.menu_edit)
//        item.setVisible(false)
//        return super.onCreateOptionsMenu(menu)
//    }

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