package ayds.songinfo.moredetails.presetation

import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.CardRepository
import ayds.songinfo.moredetails.presentation.ArticleDescriptionHelper
import ayds.songinfo.moredetails.presentation.ArticleUIState
import ayds.songinfo.moredetails.presentation.ArticleUIState.Companion.NOT_FOUND
import ayds.songinfo.moredetails.presentation.MoreDetailsPresenterImpl
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class MoreDetailsPresenterTest {
    private val repository: CardRepository = mockk()
    private val articleDescriptionHelper : ArticleDescriptionHelper = mockk(relaxUnitFun = true)
    private val moreDetailsPresenterImpl = MoreDetailsPresenterImpl(repository, articleDescriptionHelper)

    @Test
    fun `onOpen should get from repository`(){
        every { repository.getCard(any())} returns Card.EmptyCard
        moreDetailsPresenterImpl.onOpen("artistName")
        verify { repository.getCard("artistName") }
    }

    @Test
    fun `if repository returns EmptyArticle, it should notify with the correct state`(){
        every { repository.getCard(any())} returns Card.EmptyCard
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
        val cardMockk = mockk<Card.DataCard> { every {infoUrl} returns "url"}
        val description = "description"
        every { articleDescriptionHelper.getDescription(cardMockk)} returns description
        every { repository.getCard(any())} returns cardMockk
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