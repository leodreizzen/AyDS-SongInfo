package ayds.songinfo.moredetails.data
//import ayds.artist.external.lastfm.LastfmArticleService
import ayds.artist.external.lastfm.LastfmArticleService
import ayds.songinfo.moredetails.data.local.lastfm.LasfmLocalStorage
import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.Card.EmptyCard

import ayds.songinfo.moredetails.domain.CardRepository
import ayds.songinfo.moredetails.domain.CardSource
import ayds.songinfo.utils.ErrorLogger
import domain.ArticleExternal

private const val LASTFM_LOGO_URL =
    "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"

internal class CardRepositoryImpl(
    private val lastfmArticleService: LastfmArticleService,
    private val lastfmLocalStorage: LasfmLocalStorage,
    private val errorLogger: ErrorLogger
) : CardRepository {

    override fun getCard(artistName: String): Card {
        val cards = lastfmLocalStorage.getCards(artistName)
        var card: Card?
        if (cards.isNotEmpty()) {
            card = cards[0]
        } else {
            try {
                card = lastfmArticleService.getArticle(artistName)?.toCard()
                card?.let {
                    if (it.description != null) {
                        try {
                            lastfmLocalStorage.saveCard(it)
                        } catch (e: Exception) {
                            errorLogger.logError("Error saving to database", e.message ?: "")
                        }
                    }
                }
            } catch (e: Exception) {
                card = null
            }
        }
        return card ?: EmptyCard
    }

    private fun ArticleExternal.LastFMArticle.toCard() = Card.DataCard(
        artistName = artistName,
        description = biography,
        infoUrl = articleUrl,
        source = CardSource.LASTFM,
        sourceLogoUrl = LASTFM_LOGO_URL
    )
}