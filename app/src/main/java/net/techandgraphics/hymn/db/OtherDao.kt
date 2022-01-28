package net.techandgraphics.hymn.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import net.techandgraphics.hymn.models.Other
 import net.techandgraphics.hymn.models.Search

@Dao
interface OtherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(resource: List<Other>)

    @Delete
    suspend fun delete(search: Search)

    @Query("SELECT * FROM other ORDER BY resourceId ")
    fun observeOther(): Flow<List<Other>>
}