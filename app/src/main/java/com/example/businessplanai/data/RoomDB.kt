package com.example.businessplanai.data

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Entity
data class BusinessEnity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String
)

@Database(entities = [BusinessEnity::class], exportSchema = false, version = 6)
abstract class AppDatabase : RoomDatabase() {

    abstract fun businessDao(): BusinessDao
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