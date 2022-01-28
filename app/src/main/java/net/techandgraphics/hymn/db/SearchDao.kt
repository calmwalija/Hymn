package net.techandgraphics.hymn.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import net.techandgraphics.hymn.models.Search

@Dao
interface SearchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(search: List<Search>)

    @Delete
    suspend fun delete(search: Search)

    @Query("SELECT * FROM search ORDER BY id DESC")
    fun observeSearch(): Flow<List<Search>>
}