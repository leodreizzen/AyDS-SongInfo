package ayds.artist.external.lastfm
import domain.ArticleExternal

interface LastfmArticleService {
    fun getArticle(artistName: String): ArticleExternal.LastFMArticle?
}

 class LastFmArticleServiceImpl(
    private val articleResolver: LastfmToArticleResolver,
    private val lastFMAPI: LastFMAPI
) : LastfmArticleService {
    override fun getArticle(artistName: String): ArticleExternal.LastFMArticle? {
        val callResponse = lastFMAPI.getArtistInfo(artistName).execute()
        return callResponse.body()?.let { articleResolver.getArticleFromExternalData(it, artistName) }
    }
}