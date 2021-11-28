package com.example.kakaomaptest_1.model

import android.os.Parcelable
import android.provider.ContactsContract
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "user_table")
data class User(
    @PrimaryKey val id: String,
    val password: String,
    val nickname: String,
    val profileImg: String
): Parcelable

@Parcelize
@Entity(tableName = "post_table")
data class Post(
    @PrimaryKey(autoGenerate = true) val key: Int,
    val userCreatorId: String,
    // 마커 타입
    val markerType: Int,
    // post 좌표
    val lati: Double,
    val longi: Double,
    // 작성자 정보
    val profileImg: String,
    val nickname: String,
    // post 정보
    val title: String,
    val photoUri: String,
    val text: String,
    val date: Date
): Parcelable

@Parcelize
@Entity(tableName = "chat_table", primaryKeys = ["postId", "date"])
// ?? primaryKeys를 각 Chat의 Id로 안주고 위와 같이 준 이유는 뭐지?
data class Chat(
    val postId: Int,
    val date: Date,
    // 댓글 작성자 정보
    val profileImg: String,
    val nickname: String,
    val userId: String,
    val log: String
): Parcelable

