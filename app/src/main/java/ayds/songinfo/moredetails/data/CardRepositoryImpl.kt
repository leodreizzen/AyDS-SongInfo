package ayds.songinfo.moredetails.data
import ayds.songinfo.moredetails.data.local.lastfm.CardLocalStorage
import ayds.songinfo.moredetails.domain.Card

import ayds.songinfo.moredetails.domain.CardRepository
import ayds.songinfo.utils.ErrorLogger

internal class CardRepositoryImpl(
    private val lastfmLocalStorage: CardLocalStorage,
    private val errorLogger: ErrorLogger,
    private val broker: CardBroker
) : CardRepository {

    override fun getCards(artistName: String): List<Card> {
        var cards = lastfmLocalStorage.getCards(artistName)
        if (cards.isEmpty()) {
            cards = broker.getCards(artistName)
            try {
                if (cards.isNotEmpty()) {
                    lastfmLocalStorage.saveCards(cards)
                }
            }catch (e: Exception) {
                errorLogger.logError("Error saving to database", e.message ?: "")
            }
        }
        return cards
    }

}