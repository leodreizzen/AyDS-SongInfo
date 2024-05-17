package ayds.songinfo.moredetails.injector
import android.content.Context
import androidx.room.Room.databaseBuilder
import ayds.songinfo.moredetails.data.external.lastfm.LastFMAPI
import ayds.songinfo.moredetails.data.ArticleRepositoryImpl
import ayds.songinfo.moredetails.data.external.lastfm.JsonToArticleResolver
import ayds.songinfo.moredetails.data.external.lastfm.LastFmArticleServiceImpl
import ayds.songinfo.moredetails.data.local.lastfm.ArticleDatabase
import ayds.songinfo.moredetails.data.local.lastfm.LastfmLocalStorageImpl
import ayds.songinfo.moredetails.domain.ArticleRepository
import ayds.songinfo.utils.ErrorLoggerImpl
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val AUDIO_SCROBBLER = "https://ws.audioscrobbler.com/2.0/"

object ArticleRepositoryInjector {
    lateinit var repository: ArticleRepository
    fun init(context: Context){
        val articleResolver = JsonToArticleResolver()
        val lastFMAPI = initFMAPI()
        val database = initDatabase(context)

        val lastFMArticleService =
            LastFmArticleServiceImpl(articleResolver, lastFMAPI)
        val lastfmLocalStorage = LastfmLocalStorageImpl(database)

        val errorLogger = ErrorLoggerImpl()

        repository = ArticleRepositoryImpl(lastFMArticleService, lastfmLocalStorage, errorLogger)
    }

    private fun initFMAPI(): LastFMAPI {
        val retrofit = Retrofit.Builder()
            .baseUrl(AUDIO_SCROBBLER)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        return retrofit.create(LastFMAPI::class.java)
    }

    private fun initDatabase(context: Context) = databaseBuilder(context, ArticleDatabase::class.java, "database-name-thename").build()

}