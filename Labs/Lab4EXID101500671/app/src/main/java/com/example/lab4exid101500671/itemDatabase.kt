package com.example.lab4exid101500671

import android.content.ClipData
import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [Item::class], version = 1)
abstract class ItemDatabase : RoomDatabase(){

    abstract fun itemDao(): ItemDao

    companion object{
        @Volatile
        private var INSTANCE: ItemDatabase? = null

        fun getDatabase(context: android.content.Context): ItemDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = androidx.room.Room.databaseBuilder(
                    context.applicationContext,
                    ItemDatabase::class.java,
                    "item_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

}
