package fun.superpets.mobile.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MuseumDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertObjects(objects: List<MuseumObject>)

    @Query("SELECT * FROM MuseumObject ORDER BY objectID")
    fun getObjects(): Flow<List<MuseumObject>>

    @Query("SELECT * FROM MuseumObject WHERE objectID = :objectId")
    fun getObjectById(objectId: Int): Flow<MuseumObject?>
} 