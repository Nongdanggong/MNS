package com.example.kakaomaptest_1.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.kakaomaptest_1.model.Chat
import com.example.kakaomaptest_1.model.Converters
import com.example.kakaomaptest_1.model.Post
import com.example.kakaomaptest_1.model.User
import com.example.kakaomaptest_1.model.Scrap

@Database(entities = [User::class, Post::class, Chat::class, Scrap::class],
    version = 1,
    exportSchema = false)
@TypeConverters(Converters::class)

abstract class MNSDatabase: RoomDatabase() {

    abstract fun userDao(): MNSDao

    companion object{
        @Volatile
        private var INSTANCE: MNSDatabase? = null

        fun getDatabase(context: Context): MNSDatabase{
            val tempInstance = INSTANCE
            if(tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MNSDatabase::class.java,
                    "DB_MNS"
                ).allowMainThreadQueries().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}