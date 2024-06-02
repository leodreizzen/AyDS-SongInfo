package ayds.artist.external.lastfm.injector

import ayds.artist.external.lastfm.data.*
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
private const val AUDIO_SCROBBLER = "https://ws.audioscrobbler.com/2.0/"


object LastFMInjector {
    val lastFMArticleService: LastfmService = initArticleService()

    private fun initArticleService(): LastfmService {
        val articleResolver = JsonToArticleResolver()
        val lastFMAPI = initFMAPI()
        val lastFMArticleService = LastFmServiceImpl(articleResolver, lastFMAPI)
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