package injector

import ayds.artist.external.lastfm.JsonToArticleResolver
import ayds.artist.external.lastfm.LastFMAPI
import ayds.artist.external.lastfm.LastFmArticleServiceImpl
import ayds.artist.external.lastfm.LastfmArticleService
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val AUDIO_SCROBBLER = "https://ws.audioscrobbler.com/2.0/"

object ExternalInjector {

    fun initArticleService(): LastfmArticleService {
        val articleResolver = JsonToArticleResolver()
        val lastFMAPI = initFMAPI()
        val lastFMArticleService = LastFmArticleServiceImpl(articleResolver, lastFMAPI)
        return lastFMArticleService
    }

    private fun initFMAPI(): LastFMAPI {
        val retrofit = Retrofit.Builder()
            .baseUrl(AUDIO_SCROBBLER)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        return retrofit.create(LastFMAPI::class.java)
    }


}