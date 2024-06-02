package ayds.songinfo.moredetails.data

import ayds.songinfo.moredetails.data.local.lastfm.CardLocalStorage
import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.utils.ErrorLogger
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test


class CardExternalRepositoryTest {
    private val lastfmLocalStorage: CardLocalStorage = mockk(relaxUnitFun = true)
    private val errorLogger: ErrorLogger = mockk(relaxUnitFun = true)
    private val broker: CardBroker = mockk()

    private val repositoryImpl = CardRepositoryImpl(lastfmLocalStorage, errorLogger, broker)

    @Test
    fun `if there are cards in local storage it should return them`(){
        val cardsMockk = listOf( mockk<Card>())
        every{lastfmLocalStorage.getCards("artist")} returns cardsMockk

        val res = repositoryImpl.getCards("artist")

        assertEquals(cardsMockk, res)
    }


    @Test
    fun `if article not in local storage it should search in broker, save them, and return them`(){
        val cardsMockk = listOf( mockk<Card>())

        every{lastfmLocalStorage.getCards("artist")} returns listOf()
        every{broker.getCards("artist")} returns cardsMockk

        val res = repositoryImpl.getCards("artist")

        verify { lastfmLocalStorage.saveCards(cardsMockk) }
        assertEquals(cardsMockk, res)
    }

    @Test
    fun `if article not found in any place, it should return empty list and not save anything`(){
        every{lastfmLocalStorage.getCards("artist")} returns listOf()
        every{broker.getCards("artist")} returns listOf()

        val res = repositoryImpl.getCards("artist")

        verify(inverse = true) { lastfmLocalStorage.saveCards(any()) }
        assertEquals(listOf<Card>(), res)
    }

    @Test
    fun `if there is an exception saving to local storage, it should log it and return the cards anyways`(){
        val cardsMockk = listOf(mockk<Card>{ every { description } returns "biography"})
        every{lastfmLocalStorage.getCards("artist")} returns listOf()
        val exception = Exception("MSG")
        every{lastfmLocalStorage.saveCards(any())} throws exception

        every{broker.getCards("artist")} returns cardsMockk

        val res = repositoryImpl.getCards("artist")
        verify{errorLogger.logError("Error saving to database", exception.message)}
        assertEquals(cardsMockk, res)
    }

}