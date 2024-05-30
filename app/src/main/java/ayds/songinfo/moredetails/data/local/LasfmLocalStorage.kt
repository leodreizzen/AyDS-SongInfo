package ayds.songinfo.moredetails.data.local.lastfm

import ayds.songinfo.moredetails.domain.Card

interface LasfmLocalStorage {
    fun getCards(artistName: String): List<Card>
    fun saveCards(cards: List<Card>)
}

internal class LastfmLocalStorageImpl(
    private val dataBase: ArticleDatabase
) : LasfmLocalStorage {

    override fun getCards(artistName: String): List<Card> {
        val dbCards = dataBase.ArticleDao().getCardsByArtistName(artistName)
        val cards = mapToDomainCards(dbCards)
        return cards
    }

    private fun mapToDomainCards(dbCards: List<CardEntity>) =
        dbCards.map { Card(it.artistName, it.description, it.infoUrl, it.source, it.sourceLogoUrl, true) }

    override fun saveCards(cards: List<Card>) {
        cards.forEach{
            saveCard(it)
        }
    }

    private fun saveCard(card: Card) {
        if (card.description != null) {
            dataBase.ArticleDao().insertArticle(
                CardEntity(
                    card.artistName,
                    card.description,
                    card.infoUrl,
                    card.source,
                    card.sourceLogoUrl
                )
            )
        }
    }
}