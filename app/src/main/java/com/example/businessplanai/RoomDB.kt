package com.example.businessplanai

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Entity
data class BusinessEnity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String
)

@Database(entities = [BusinessEnity::class], version = 5)
abstract class AppDatabase : RoomDatabase() {

    abstract fun businessDao(): BusinessDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance =
                    Room.databaseBuilder(context, AppDatabase::class.java, "app_database")
                        .fallbackToDestructiveMigration(false)
                        .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

@Dao
interface BusinessDao {
    @Query("SELECT * FROM BusinessEnity")
    fun getAll(): Flow<List<BusinessEnity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(stat: BusinessEnity): Long

    @Query("SELECT * FROM BusinessEnity WHERE id = :id")
    suspend fun getId(id: Int?): BusinessEnity?

    @Query("DELETE FROM BusinessEnity WHERE id = :id")
    suspend fun delete(id: Int)
}