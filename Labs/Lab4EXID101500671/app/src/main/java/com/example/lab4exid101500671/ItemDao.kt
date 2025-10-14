package com.example.lab4exid101500671

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    @Insert
    suspend fun insert(item: Item)

    @Delete
    suspend fun delete(item: Item)

    @Query("SELECT * FROM item")
    fun getAllItems(): Flow<List<Item>>

    @Query("SELECT * FROM item WHERE id = :itemId")
    fun getItemById(itemId: Int): Item

    @Query("SELECT * FROM item WHERE name LIKE :itemName")
    fun getItemByName(itemName: String): Flow<List<Item>>
}
