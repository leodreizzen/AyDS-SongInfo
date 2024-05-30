package ayds.songinfo.moredetails.injector

import android.content.Context
import androidx.room.Room.databaseBuilder
import ayds.songinfo.moredetails.data.CardRepositoryImpl
import ayds.songinfo.moredetails.data.local.lastfm.ArticleDatabase
import ayds.songinfo.moredetails.data.local.lastfm.LastfmLocalStorageImpl
import ayds.songinfo.moredetails.domain.CardRepository
import ayds.songinfo.utils.ErrorLoggerImpl
import ayds.artist.external.lastfm.injector.LastFMInjector



object DataInjector {
    lateinit var repository: CardRepository
    fun initRepository(context: Context) {
        val database = initDatabase(context)

        val lastfmLocalStorage = LastfmLocalStorageImpl(database)
        val lastFMArticleService = LastFMInjector.initArticleService()

        val errorLogger = ErrorLoggerImpl()

        repository = CardRepositoryImpl(lastFMArticleService, lastfmLocalStorage, errorLogger)
    }

    private fun initDatabase(context: Context) =
        databaseBuilder(context, ArticleDatabase::class.java, "database-name-thename").build()





}