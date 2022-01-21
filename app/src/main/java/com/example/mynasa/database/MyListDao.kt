package com.example.mynasa.database


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.example.ResultResponse

@Dao
interface MyListDao {
    @Insert
    suspend fun insertWatchList(myListTable: ResultResponse)

    @Query("DELETE FROM my_list WHERE date =:dateString")
    suspend fun removeWatchList(dateString: String)


    @Query("SELECT EXISTS (SELECT 1 FROM my_list WHERE date = :dateString)")
    fun exists(dateString: String): Boolean

    @Query("Select * from my_list")
    fun getAllData(): List<ResultResponse>


}