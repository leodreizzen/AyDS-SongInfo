package ayds.songinfo.moredetails.presetation

import ayds.songinfo.moredetails.domain.Article
import ayds.songinfo.moredetails.domain.ArticleRepository
import ayds.songinfo.moredetails.presentation.ArticleDescriptionHelper
import ayds.songinfo.moredetails.presentation.ArticleUIState
import ayds.songinfo.moredetails.presentation.ArticleUIState.Companion.NOT_FOUND
import ayds.songinfo.moredetails.presentation.MoreDetailsPresenterImpl
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class MoreDetailsPresenterTest {
    private val repository: ArticleRepository = mockk()
    private val articleDescriptionHelper : ArticleDescriptionHelper = mockk(relaxUnitFun = true)
    private val moreDetailsPresenterImpl = MoreDetailsPresenterImpl(repository, articleDescriptionHelper)

    @Test
    fun `onOpen should get from repository`(){
        every { repository.getArticle(any())} returns Article.EmptyArticle
        moreDetailsPresenterImpl.onOpen("artistName")
        verify { repository.getArticle("artistName") }
    }

    @Test
    fun `if repository returns EmptyArticle, it should notify with the correct state`(){
        every { repository.getArticle(any())} returns Article.EmptyArticle
        val callback = mockk<(ArticleUIState) -> Unit>(relaxed = true)
        moreDetailsPresenterImpl.articleObservable.subscribe{callback(it)}
        moreDetailsPresenterImpl.onOpen("artistName")
        verify { callback(
            ArticleUIState(
                NOT_FOUND,
                null
            )
        ) }
    }

    @Test
    fun `if repository returns LastfmArticle, it should notify with the correct state`(){
        val articleMockk = mockk<Article.LastFMArticle> { every {articleUrl} returns "url"}
        val description = "description"
        every { articleDescriptionHelper.getDescription(articleMockk)} returns description
        every { repository.getArticle(any())} returns articleMockk
        val callback = mockk<(ArticleUIState) -> Unit>(relaxed = true)
        moreDetailsPresenterImpl.articleObservable.subscribe{callback(it)}
        moreDetailsPresenterImpl.onOpen("artistName")

        verify { callback(
            ArticleUIState(
                description,
                "url"
            )
        ) }
    }
}