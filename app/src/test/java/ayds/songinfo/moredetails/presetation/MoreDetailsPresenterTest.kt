package ayds.songinfo.moredetails.presetation

import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.CardRepository
import ayds.songinfo.moredetails.domain.CardSource
import ayds.songinfo.moredetails.presentation.CardDescriptionHelper
import ayds.songinfo.moredetails.presentation.CardUIState
import ayds.songinfo.moredetails.presentation.MoreDetailsPresenterImpl
import ayds.songinfo.moredetails.presentation.SourceNameResolver
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class MoreDetailsPresenterTest {
    private val repository: CardRepository = mockk()
    private val cardDescriptionHelper : CardDescriptionHelper = mockk(relaxUnitFun = true)
    private val sourceNameResolver: SourceNameResolver = mockk()
    private val moreDetailsPresenterImpl = MoreDetailsPresenterImpl(repository, cardDescriptionHelper, sourceNameResolver)

    @Test
    fun `onOpen should get from repository`(){
        every { repository.getCards(any())} returns listOf()
        moreDetailsPresenterImpl.onOpen("artistName")
        verify { repository.getCards("artistName") }
    }



    @Test
    fun `if repository returns a single card, it should notify with the correct state, filling with empties`(){
        val cards = listOf(Card(artistName = "artistName", description = "description", infoUrl = "url", source = CardSource.LASTFM, sourceLogoUrl = "sourceLogoUrl"))
        val description = "description"
        every { cardDescriptionHelper.getDescription(cards[0])} returns description
        every { repository.getCards(any())} returns cards
        every { sourceNameResolver.getSourceName(CardSource.LASTFM)} returns "last_fm"
        val callback = mockk<(List<CardUIState>) -> Unit>(relaxed = true)
        moreDetailsPresenterImpl.articleObservable.subscribe{callback(it)}
        moreDetailsPresenterImpl.onOpen("artistName")

        verify { callback(
            listOf( CardUIState(
                "description",
                "url",
                "Source: last_fm",
                "sourceLogoUrl"
            ), CardUIState(), CardUIState()
            )
        ) }
    }

    @Test
    fun `if repository returns three cards, it should notify with the correct state`(){
        val cards = listOf(Card(artistName = "artistName", description = "description1", infoUrl = "url1", source = CardSource.LASTFM, sourceLogoUrl = "sourceLogoUrl1"),
            Card(artistName = "artistName", description = "description2", infoUrl = "url2", source = CardSource.WIKIPEDIA, sourceLogoUrl = "sourceLogoUrl2"),
            Card(artistName = "artistName", description = "description3", infoUrl = "url3", source = CardSource.NEW_YORK_TIMES, sourceLogoUrl = "sourceLogoUrl3")
            )

        every { cardDescriptionHelper.getDescription(cards[0])} returns "description_1"
        every { cardDescriptionHelper.getDescription(cards[1])} returns "description_2"
        every { cardDescriptionHelper.getDescription(cards[2])} returns "description_3"

        every { repository.getCards("artistName")} returns cards
        every { sourceNameResolver.getSourceName(CardSource.LASTFM)} returns "last_fm"
        every { sourceNameResolver.getSourceName(CardSource.WIKIPEDIA)} returns "wikipedia"
        every { sourceNameResolver.getSourceName(CardSource.NEW_YORK_TIMES)} returns "new_york_times"

        val callback = mockk<(List<CardUIState>) -> Unit>(relaxed = true)
        moreDetailsPresenterImpl.articleObservable.subscribe{callback(it)}
        moreDetailsPresenterImpl.onOpen("artistName")

        verify { callback(
            listOf( CardUIState(
                "description_1",
                "url1",
                "Source: last_fm",
                "sourceLogoUrl1"
            ), CardUIState(
                "description_2",
                "url2",
                "Source: wikipedia",
                "sourceLogoUrl2"
            ), CardUIState(
                "description_3",
                "url3",
                "Source: new_york_times",
                "sourceLogoUrl3"
            )
            )
        ) }
    }


    @Test
    fun `if repository returns more than three cards, it should notify with the correct state, removing the last ones`(){
        val cards = listOf(Card(artistName = "artistName", description = "description1", infoUrl = "url1", source = CardSource.LASTFM, sourceLogoUrl = "sourceLogoUrl1"),
            Card(artistName = "artistName", description = "description2", infoUrl = "url2", source = CardSource.WIKIPEDIA, sourceLogoUrl = "sourceLogoUrl2"),
            Card(artistName = "artistName", description = "description3", infoUrl = "url3", source = CardSource.NEW_YORK_TIMES, sourceLogoUrl = "sourceLogoUrl3"),
            Card(artistName = "artistName", description = "description4", infoUrl = "url4", source = CardSource.NEW_YORK_TIMES, sourceLogoUrl = "sourceLogoUrl4"),
            Card(artistName = "artistName", description = "description5", infoUrl = "url5", source = CardSource.NEW_YORK_TIMES, sourceLogoUrl = "sourceLogoUrl5")
        )

        every { cardDescriptionHelper.getDescription(cards[0])} returns "description_1"
        every { cardDescriptionHelper.getDescription(cards[1])} returns "description_2"
        every { cardDescriptionHelper.getDescription(cards[2])} returns "description_3"
        every { cardDescriptionHelper.getDescription(cards[3])} returns "description_4"
        every { cardDescriptionHelper.getDescription(cards[4])} returns "description_5"


        every { repository.getCards("artistName")} returns cards
        every { sourceNameResolver.getSourceName(CardSource.LASTFM)} returns "last_fm"
        every { sourceNameResolver.getSourceName(CardSource.WIKIPEDIA)} returns "wikipedia"
        every { sourceNameResolver.getSourceName(CardSource.NEW_YORK_TIMES)} returns "new_york_times"

        val callback = mockk<(List<CardUIState>) -> Unit>(relaxed = true)
        moreDetailsPresenterImpl.articleObservable.subscribe{callback(it)}
        moreDetailsPresenterImpl.onOpen("artistName")

        verify { callback(
            listOf( CardUIState(
                "description_1",
                "url1",
                "Source: last_fm",
                "sourceLogoUrl1"
            ), CardUIState(
                "description_2",
                "url2",
                "Source: wikipedia",
                "sourceLogoUrl2"
            ), CardUIState(
                "description_3",
                "url3",
                "Source: new_york_times",
                "sourceLogoUrl3"
            )
            )
        ) }
    }
}