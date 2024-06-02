package ayds.artist.external.wikipedia.data

import retrofit2.Response
import java.io.IOException

internal class WikipediaTrackServiceImpl(
    private val wikipediaTrackAPI: WikipediaTrackAPI,
    private val wikipediaToInfoResolver: WikipediaToInfoResolver,
) : WikipediaTrackService {

    override fun getInfo(artistName: String): WikipediaArticle {
        try {
            val callResponse = getInfoFromService(artistName)
            return wikipediaToInfoResolver.getInfoFromExternalData(artistName, callResponse.body())
        } catch (e: IOException) {
            return WikipediaArticle.EmptyWikipediaArticle
        }

    }

    private fun getInfoFromService(artistName: String): Response<String> {
        return wikipediaTrackAPI.getArtistInfo(artistName)
            .execute()
    }
}