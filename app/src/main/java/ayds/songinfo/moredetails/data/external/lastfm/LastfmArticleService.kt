package ayds.songinfo.moredetails.data.external.lastfm
import ayds.songinfo.moredetails.domain.Article

interface LastfmArticleService {
    fun getArticle(artistName: String): Article?
}

class LastFmArticleServiceImpl(
    private val articleResolver: LastfmToArticleResolver,
    private val lastFMAPI: LastFMAPI
) : LastfmArticleService {
    override fun getArticle(artistName: String): Article? {
        val callResponse = lastFMAPI.getArtistInfo(artistName).execute()
        return callResponse.body()?.let { articleResolver.getArticleFromExternalData(it, artistName) }
    }
}