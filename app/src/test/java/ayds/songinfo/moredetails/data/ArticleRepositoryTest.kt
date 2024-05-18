package ayds.songinfo.moredetails.data

import ayds.songinfo.moredetails.data.external.lastfm.LastfmArticleService
import ayds.songinfo.moredetails.data.local.lastfm.LasfmLocalStorage
import ayds.songinfo.moredetails.domain.Article
import ayds.songinfo.utils.ErrorLogger
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test


class ArticleRepositoryTest {
    private val lastfmArticleService: LastfmArticleService = mockk()
    private val lastfmLocalStorage: LasfmLocalStorage = mockk(relaxUnitFun = true)
    private val errorLogger: ErrorLogger = mockk(relaxUnitFun = true)

    private val repositoryImpl = ArticleRepositoryImpl(lastfmArticleService,lastfmLocalStorage, errorLogger)

    @Test
    fun `if article is in local storage it should return it`(){
        val articleMockk = mockk<Article.LastFMArticle>()
        every{lastfmLocalStorage.getArticle("artist")} returns articleMockk

        val res = repositoryImpl.getArticle("artist")

        assertEquals(articleMockk, res)
    }


    @Test
    fun `if article not in local storage it should search in articleService and return it`(){
        val articleMockk = mockk<Article.LastFMArticle>{ every { biography } returns null}
        every{lastfmLocalStorage.getArticle("artist")} returns null
        every{lastfmArticleService.getArticle("artist")} returns articleMockk

        val res = repositoryImpl.getArticle("artist")

        assertEquals(articleMockk, res)
    }

    @Test
    fun `if article from articleService has biography, it should save the article in local storage`(){
        val articleMockk = mockk<Article.LastFMArticle>{ every { biography } returns "biography"}
        every{lastfmLocalStorage.getArticle("artist")} returns null
        every{lastfmArticleService.getArticle("artist")} returns articleMockk

        repositoryImpl.getArticle("artist")

        verify { lastfmLocalStorage.saveArticle(articleMockk)}

    }

    @Test
    fun `if article from articleService does not have biography, it should not save the article in local storage`(){
        val articleMockk = mockk<Article.LastFMArticle>{ every { biography } returns null}
        every{lastfmLocalStorage.getArticle("artist")} returns null
        every{lastfmArticleService.getArticle("artist")} returns articleMockk

        repositoryImpl.getArticle("artist")

        verify(inverse = true) { lastfmLocalStorage.saveArticle(articleMockk)}

    }

    @Test
    fun `if there is an exception on article service, it should return EmptyArticle`(){
        every{lastfmLocalStorage.getArticle("artist")} returns null
        every{lastfmArticleService.getArticle("artist")} throws Exception()

        val res = repositoryImpl.getArticle("artist")

        assertEquals(Article.EmptyArticle, res)

    }

    @Test
    fun `if there is an exception saving to local storage, it should log it return the article anyways`(){
        val articleMockk = mockk<Article.LastFMArticle>{ every { biography } returns "biography"}
        every{lastfmLocalStorage.getArticle("artist")} returns null
        val exception = Exception("MSG")
        every{lastfmLocalStorage.saveArticle(any())} throws exception

        every{lastfmArticleService.getArticle("artist")} returns articleMockk

        val res = repositoryImpl.getArticle("artist")
        verify{errorLogger.logError("Error saving to database", exception.message)}
        assertEquals(articleMockk, res)
    }

    @Test
    fun `if article article not found in any place it should return EmptyArticle`(){
        every{lastfmArticleService.getArticle(any())} returns null
        every{lastfmLocalStorage.getArticle(any())} returns null

        val result = repositoryImpl.getArticle("artist")

        assertEquals(Article.EmptyArticle, result)

    }


}