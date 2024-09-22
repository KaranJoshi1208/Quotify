package com.karan.quotify.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface QuoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(array : Array<Quote>)

    @Query("SELECT * FROM quotes WHERE id = :id")
    suspend fun getQuoteById(id : Int) : Quote

    @Query("UPDATE quotes SET isFav = :isFav WHERE id = :id")
    suspend fun updateFav(id: Int, isFav : Int)

    @Query("SELECT * FROM quotes WHERE isFav = 1 ORDER BY id DESC")
    fun getFav() : LiveData<List<Quote>>
}