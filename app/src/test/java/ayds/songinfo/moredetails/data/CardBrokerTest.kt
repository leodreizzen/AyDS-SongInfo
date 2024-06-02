package ayds.songinfo.moredetails.data

import ayds.songinfo.moredetails.data.external.LastFMToBiographyProxy
import ayds.songinfo.moredetails.data.external.NewYorkTimesToBiographyProxy
import ayds.songinfo.moredetails.data.external.WikipediaToBiographyProxy
import ayds.songinfo.moredetails.domain.Card
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class CardBrokerTest {
    private val lastFMProxy = mockk<LastFMToBiographyProxy>()
    private val wikipediaProxy = mockk<WikipediaToBiographyProxy>()
    private val nyTimesProxy = mockk<NewYorkTimesToBiographyProxy>()
    private val brokerImpl = CardBrokerImpl(lastFMProxy, wikipediaProxy, nyTimesProxy)

    @Test
    fun `if lastFM card is null it should not add it to result`(){
        every { lastFMProxy.getCard("artist") } returns null
        val card1 = mockk<Card>()
        val card2 = mockk<Card>()
        every { wikipediaProxy.getCard("artist") } returns card1
        every { nyTimesProxy.getCard("artist") } returns card2
        val res = brokerImpl.getCards("artist")
        assertEquals(listOf(card1, card2), res)
    }


    @Test
    fun `if wikipedia card is null it should not add it to result`(){
        val card1 = mockk<Card>()
        val card2 = mockk<Card>()
        every { lastFMProxy.getCard("artist") } returns card1
        every { wikipediaProxy.getCard("artist") } returns null
        every { nyTimesProxy.getCard("artist") } returns card2
        val res = brokerImpl.getCards("artist")
        assertEquals(listOf(card1, card2), res)
    }


    @Test
    fun `if nyt is null it should not add it to result`(){
        val card1 = mockk<Card>()
        val card2 = mockk<Card>()
        every { lastFMProxy.getCard("artist") } returns card1
        every { wikipediaProxy.getCard("artist") } returns card2
        every { nyTimesProxy.getCard("artist") } returns null
        val res = brokerImpl.getCards("artist")
        assertEquals(listOf(card1, card2), res)
    }

    @Test
    fun `if all proxies return non null it should return them all`(){
        val card1 = mockk<Card>()
        val card2 = mockk<Card>()
        val card3 = mockk<Card>()

        every { lastFMProxy.getCard("artist") } returns card1
        every { wikipediaProxy.getCard("artist") } returns card2
        every { nyTimesProxy.getCard("artist") } returns card3
        val res = brokerImpl.getCards("artist")

        assertEquals(listOf(card1, card2, card3), res)
    }

}