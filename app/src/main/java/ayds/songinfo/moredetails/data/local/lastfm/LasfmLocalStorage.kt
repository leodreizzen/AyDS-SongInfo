package ayds.songinfo.moredetails.data.local.lastfm

import ayds.songinfo.moredetails.domain.Card.DataCard

interface LasfmLocalStorage {
    fun getCards(artistName: String): List<DataCard>
    fun saveCard(article: DataCard)
}

internal class LastfmLocalStorageImpl(
    private val dataBase: ArticleDatabase
) : LasfmLocalStorage {

    override fun getCards(artistName: String): List<DataCard> {
        val dbCards = dataBase.ArticleDao().getCardsByArtistName(artistName)
        val cards = mapToDomainCards(dbCards)
        return cards
    }

    private fun mapToDomainCards(dbCards: List<CardEntity>) =
        dbCards.map { DataCard(it.artistName, it.description, it.infoUrl, it.source, it.sourceLogoUrl, true) }

    override fun saveCard(article: DataCard) {
        if (article.description != null) {
            dataBase.ArticleDao().insertArticle(
                CardEntity(
                    article.artistName,
                    article.description,
                    article.infoUrl,
                    article.source,
                    article.sourceLogoUrl
                )
            )
        }
    }
}