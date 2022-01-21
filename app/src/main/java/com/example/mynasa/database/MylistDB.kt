package com.example.mynasa.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.example.ResultResponse

@Database(entities = [ResultResponse::class], version = 1)
abstract class MylistDB :RoomDatabase() {

    abstract fun getMyListDao() : MyListDao

    companion object {
        private var myListDB: MylistDB? = null
        fun getMyListDatabase(context: Context): MylistDB? {
            if (myListDB == null){
                myListDB = Room.databaseBuilder(context, MylistDB::class.java, "my_list_db")
                    .build()
            }
            return myListDB
        }
    }
}