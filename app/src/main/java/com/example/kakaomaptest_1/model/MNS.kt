package com.example.kakaomaptest_1.model

import android.os.Parcelable
import android.provider.ContactsContract
import androidx.room.*
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "user_table")
data class User(
    @PrimaryKey val id: String,
    val password: String,
    @ColumnInfo(name = "nickname") val nickname: String,
    val photoUri: String
): Parcelable

@Parcelize
@Entity(tableName = "post_table")
data class Post(
    @PrimaryKey(autoGenerate = true) val key: Int,
    val userCreatorId: String,
    val title: String,
    val pinType: Int,
    val lati: Double,
    val longi: Double,
    val photoUri: String,
    val text: String,
    val date: Date
): Parcelable

@Parcelize
@Entity(tableName = "chat_table", primaryKeys = ["postId", "date"])
data class Chat(
    val postId: Int,
    val date: Date,
    val userId: String,
    val log: String
): Parcelable

