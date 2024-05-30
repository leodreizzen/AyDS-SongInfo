package ayds.songinfo.moredetails.data.local.lastfm

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import ayds.songinfo.moredetails.domain.CardSource

@Database(entities = [CardEntity::class], version = 2)
abstract class ArticleDatabase : RoomDatabase() {
    abstract fun ArticleDao(): ArticleDao
}

@Entity(primaryKeys = ["artistName", "source"])
data class CardEntity(
    val artistName: String,
    val description: String,
    val infoUrl: String,
    val source: CardSource,
    val sourceLogoUrl: String
)

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArticle(article: CardEntity)

    @Query("SELECT * FROM CardEntity WHERE artistName LIKE :artistName")
    fun getCardsByArtistName(artistName: String): List<CardEntity>

}