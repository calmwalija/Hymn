package net.techandgraphics.hymn.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import net.techandgraphics.hymn.domain.model.Search

@Dao
interface SearchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(search: List<Search>)

    @Delete
    suspend fun delete(search: Search)

    @Query("DELETE  FROM  search")
    suspend fun clear()

    @Query("SELECT * FROM search WHERE lang=:version ORDER BY id DESC LIMIT 15")
    fun observeSearch(version: String): Flow<List<Search>>
}