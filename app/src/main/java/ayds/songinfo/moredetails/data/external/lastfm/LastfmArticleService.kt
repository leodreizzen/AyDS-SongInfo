package ayds.songinfo.moredetails.data.external.lastfm
import ayds.songinfo.moredetails.domain.Article.LastFMArticle

interface LastfmArticleService {
    fun getArticle(artistName: String): LastFMArticle?
}

class LastFmArticleServiceImpl(
    private val articleResolver: LastfmToArticleResolver,
    private val lastFMAPI: LastFMAPI
) : LastfmArticleService {
    override fun getArticle(artistName: String): LastFMArticle? {
        val callResponse = lastFMAPI.getArtistInfo(artistName).execute()
        return callResponse.body()?.let { articleResolver.getArticleFromExternalData(it, artistName) }
    }
}