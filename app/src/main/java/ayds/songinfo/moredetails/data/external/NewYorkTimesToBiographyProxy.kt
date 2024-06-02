package ayds.songinfo.moredetails.data.external

import ayds.artist.external.newyorktimes.data.NYTimesArticle
import ayds.artist.external.newyorktimes.data.NYTimesService
import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.CardSource

interface NewYorkTimesToBiographyProxy {
    fun getCard(artistName: String): Card?
}

internal class NewYorkTimesToBiographyProxyImpl(private val service: NYTimesService) : NewYorkTimesToBiographyProxy {
    override fun getCard(artistName: String): Card?{
        val article = service.getArtistInfo(artistName)
        return articleToCard(article)
    }

    private fun articleToCard(article: NYTimesArticle): Card? {
        return when (article) {
            is NYTimesArticle.NYTimesArticleWithData -> return Card(
                artistName = article.name,
                description = article.info,
                infoUrl = article.url,
                source = CardSource.NEW_YORK_TIMES,
                sourceLogoUrl = article.sourceLogoUrl,
                isLocallyStored = false
            )
            else -> null
        }
    }
}