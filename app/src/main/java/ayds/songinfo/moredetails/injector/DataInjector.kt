package ayds.songinfo.moredetails.injector

import android.content.Context
import androidx.room.Room.databaseBuilder
import ayds.artist.external.lastfm.LastFMAPI
import ayds.songinfo.moredetails.data.ArticleRepositoryImpl
import ayds.artist.external.lastfm.LastFmArticleServiceImpl
import ayds.songinfo.moredetails.data.local.lastfm.ArticleDatabase
import ayds.songinfo.moredetails.data.local.lastfm.LastfmLocalStorageImpl
import ayds.songinfo.moredetails.domain.ArticleRepository
import ayds.songinfo.utils.ErrorLoggerImpl
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import injector.ExternalInjector



object DataInjector {
    lateinit var repository: ArticleRepository
    fun initRepository(context: Context) {
        val database = initDatabase(context)

        val lastfmLocalStorage = LastfmLocalStorageImpl(database)
        val lastFMArticleService = ExternalInjector.initArticleService()

        val errorLogger = ErrorLoggerImpl()

        repository = ArticleRepositoryImpl(lastFMArticleService, lastfmLocalStorage, errorLogger)
    }

    private fun initDatabase(context: Context) =
        databaseBuilder(context, ArticleDatabase::class.java, "database-name-thename").build()





}