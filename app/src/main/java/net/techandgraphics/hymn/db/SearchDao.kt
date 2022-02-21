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

    @Query("DELETE  FROM  search  ")
    suspend fun clear()

    @Query("SELECT * FROM search ORDER BY id DESC LIMIT 15")
    fun observeSearch(): Flow<List<Search>>
}