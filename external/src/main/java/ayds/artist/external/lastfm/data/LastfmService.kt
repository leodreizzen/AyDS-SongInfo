package ayds.artist.external.lastfm.data

interface LastfmService {
    fun getArticle(artistName: String): LastFMArticle
}

 class LastFmServiceImpl(
     private val articleResolver: LastfmToArticleResolver,
     private val lastFMAPI: LastFMAPI
) : LastfmService {
    override fun getArticle(artistName: String): LastFMArticle {
        val callResponse = lastFMAPI.getArtistInfo(artistName).execute()
        return callResponse.body()?.let { articleResolver.getArticleFromExternalData(it, artistName) } ?: LastFMArticle.EmptyLastfmArticle
    }
}