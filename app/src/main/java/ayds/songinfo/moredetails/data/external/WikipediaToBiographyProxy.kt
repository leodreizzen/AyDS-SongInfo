package ayds.songinfo.moredetails.data.external
import ayds.artist.external.wikipedia.data.WikipediaArticle
import ayds.artist.external.wikipedia.data.WikipediaTrackService
import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.CardSource


interface WikipediaToBiographyProxy {
    fun getCard(artistName: String): Card?
}

internal class WikipediaToBiographyProxyImpl(private val service: WikipediaTrackService) : WikipediaToBiographyProxy {

    override fun getCard(artistName: String): Card?{
        val article = service.getInfo(artistName)
        return articleToCard(article)
    }

    private fun articleToCard(article: WikipediaArticle): Card? {
        return when (article) {
            is WikipediaArticle.WikipediaArticleWithData -> return Card(
                artistName = article.name,
                description = article.description,
                infoUrl = article.wikipediaURL,
                source = CardSource.WIKIPEDIA,
                sourceLogoUrl = article.wikipediaLogoURL,
                isLocallyStored = false
            )
            else -> null
        }
    }
}