package ayds.artist.external.lastfm.data

import java.io.IOException

interface LastfmService {
    fun getArticle(artistName: String): LastFMArticle
}

class LastFmServiceImpl(
    private val articleResolver: LastfmToArticleResolver,
    private val lastFMAPI: LastFMAPI
) : LastfmService {
    override fun getArticle(artistName: String): LastFMArticle {
        try {
            val callResponse = lastFMAPI.getArtistInfo(artistName).execute()
            return callResponse.body()?.let { articleResolver.getArticleFromExternalData(it, artistName) }
                ?: LastFMArticle.EmptyLastfmArticle
        } catch (e: IOException) {
            return LastFMArticle.EmptyLastfmArticle
        }
    }
}