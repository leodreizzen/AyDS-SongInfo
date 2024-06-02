package ayds.songinfo.moredetails.injector

import android.content.Context
import androidx.room.Room.databaseBuilder
import ayds.songinfo.moredetails.data.CardRepositoryImpl
import ayds.songinfo.moredetails.data.local.lastfm.ArticleDatabase
import ayds.songinfo.moredetails.data.local.lastfm.CardLocalStorageImpl
import ayds.songinfo.moredetails.domain.CardRepository
import ayds.songinfo.utils.ErrorLoggerImpl
import ayds.artist.external.lastfm.injector.LastFMInjector
import ayds.artist.external.newyorktimes.injector.NYTimesInjector
import ayds.artist.external.wikipedia.injector.WikipediaInjector
import ayds.songinfo.moredetails.data.CardBroker
import ayds.songinfo.moredetails.data.CardBrokerImpl
import ayds.songinfo.moredetails.data.external.LastFMToBiographyProxyImpl
import ayds.songinfo.moredetails.data.external.NewYorkTimesToBiographyProxyImpl
import ayds.songinfo.moredetails.data.external.WikipediaToBiographyProxyImpl

object DataInjector {
    lateinit var repository: CardRepository
    fun initRepository(context: Context) {
        val database = initDatabase(context)
        val lastfmLocalStorage = CardLocalStorageImpl(database)
        val errorLogger = ErrorLoggerImpl()
        repository = CardRepositoryImpl(lastfmLocalStorage, errorLogger, initBroker())
    }


    private fun initBroker() : CardBroker{
        val lastFmProxy = LastFMToBiographyProxyImpl(LastFMInjector.lastFMArticleService)
        val wikipediaProxy = WikipediaToBiographyProxyImpl(WikipediaInjector.wikipediaTrackService)
        val nytProxy = NewYorkTimesToBiographyProxyImpl(NYTimesInjector.nyTimesService)
        return CardBrokerImpl(lastFmProxy, wikipediaProxy, nytProxy)
    }


    private fun initDatabase(context: Context) =
        databaseBuilder(context, ArticleDatabase::class.java, "database-name-thename").build()





}