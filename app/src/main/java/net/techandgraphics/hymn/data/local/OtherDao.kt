package net.techandgraphics.hymn.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import net.techandgraphics.hymn.domain.model.Other

@Dao
interface OtherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(resource: List<Other>)

    @Query("DELETE  FROM  other ")
    suspend fun clear()

    @Query("SELECT * FROM other ORDER BY resourceId ")
    fun observeOther(): Flow<List<Other>>
}