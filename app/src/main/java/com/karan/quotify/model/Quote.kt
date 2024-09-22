package com.karan.quotify.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "quotes")
data class Quote(
    val text: String,
    val author: String?,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var isFav: Int = 0
) : Serializable