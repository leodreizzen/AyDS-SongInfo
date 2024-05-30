package ayds.artist.external.lastfm.data

import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.CardSource


interface LastFMToBiographyProxy {
    fun getCard(artistName: String): Card?
}

internal class LastFMToBiographyProxyImpl(private val service: LastfmService) : LastFMToBiographyProxy {
    override fun getCard(artistName: String): Card?{
        val article = service.getArticle(artistName)
        return articleToCard(article)
    }

    private fun articleToCard(article: LastFMArticle): Card? {
        return when (article) {
            is LastFMArticle.LastFMArticleWithData -> return Card(
                artistName = article.artistName,
                description = article.biography,
                infoUrl = article.articleUrl,
                source = CardSource.LASTFM,
                sourceLogoUrl = article.sourceLogoUrl,
                isLocallyStored = false
            )
            else -> null
        }
   }
}