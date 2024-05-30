package ayds.songinfo.moredetails.data

import ayds.artist.external.lastfm.data.LastfmService
import ayds.songinfo.moredetails.data.local.lastfm.LasfmLocalStorage
import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.utils.ErrorLogger
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test


class CardExternalRepositoryTest {
    private val lastfmArticleService: LastfmService = mockk()
    private val lastfmLocalStorage: LasfmLocalStorage = mockk(relaxUnitFun = true)
    private val errorLogger: ErrorLogger = mockk(relaxUnitFun = true)

    private val repositoryImpl = CardRepositoryImpl(lastfmArticleService,lastfmLocalStorage, errorLogger)

    @Test
    fun `if article is in local storage it should return it`(){
        val cardMockk = mockk<Card.DataCard>()
        every{lastfmLocalStorage.getCards("artist")} returns cardMockk

        val res = repositoryImpl.getCard("artist")

        assertEquals(cardMockk, res)
    }


    @Test
    fun `if article not in local storage it should search in articleService and return it`(){
        val cardMockk = mockk<Card.DataCard>{ every { description } returns null}
        every{lastfmLocalStorage.getCards("artist")} returns null
        every{lastfmArticleService.getArticle("artist")} returns cardMockk

        val res = repositoryImpl.getCard("artist")

        assertEquals(cardMockk, res)
    }

    @Test
    fun `if article from articleService has biography, it should save the article in local storage`(){
        val cardMockk = mockk<Card.DataCard>{ every { description } returns "biography"}
        every{lastfmLocalStorage.getCards("artist")} returns null
        every{lastfmArticleService.getArticle("artist")} returns cardMockk

        repositoryImpl.getCard("artist")

        verify { lastfmLocalStorage.saveCard(cardMockk)}

    }

    @Test
    fun `if article from articleService does not have biography, it should not save the article in local storage`(){
        val cardMockk = mockk<Card.DataCard>{ every { description } returns null}
        every{lastfmLocalStorage.getCards("artist")} returns null
        every{lastfmArticleService.getArticle("artist")} returns cardMockk

        repositoryImpl.getCard("artist")

        verify(inverse = true) { lastfmLocalStorage.saveCard(cardMockk)}

    }

    @Test
    fun `if there is an exception on article service, it should return EmptyArticle`(){
        every{lastfmLocalStorage.getCards("artist")} returns null
        every{lastfmArticleService.getArticle("artist")} throws Exception()

        val res = repositoryImpl.getCard("artist")

        assertEquals(Card.EmptyCard, res)

    }

    @Test
    fun `if there is an exception saving to local storage, it should log it return the article anyways`(){
        val cardMockk = mockk<Card.DataCard>{ every { description } returns "biography"}
        every{lastfmLocalStorage.getCards("artist")} returns null
        val exception = Exception("MSG")
        every{lastfmLocalStorage.saveCard(any())} throws exception

        every{lastfmArticleService.getArticle("artist")} returns cardMockk

        val res = repositoryImpl.getCard("artist")
        verify{errorLogger.logError("Error saving to database", exception.message)}
        assertEquals(cardMockk, res)
    }

    @Test
    fun `if article article not found in any place it should return EmptyArticle`(){
        every{lastfmArticleService.getArticle(any())} returns null
        every{lastfmLocalStorage.getCards(any())} returns null

        val result = repositoryImpl.getCard("artist")

        assertEquals(Card.EmptyCard, result)

    }


}