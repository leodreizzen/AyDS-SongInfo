package ayds.songinfo.moredetails.presetation

import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.presentation.ArticleDescriptionHelperImpl
import ayds.songinfo.moredetails.presentation.ArticleUIState
import org.junit.Assert.assertEquals
import org.junit.Test

private const val TEST_BIOGRAPHY = "line 1\\nline '2'\nline 3"
/*
    Should replace "\n" or \n with <br>
    Should replace ' with space
 */
private const val TEST_BIOGRAPHY_REPLACED = "line 1<br>line  2 <br>line 3"
private const val HEAD = "<html><div width=400><font face=\"arial\">"
private const val TAIL = "</font></div></html>"

class CardExternalDescriptionHelperTest {
    private val articleDescriptionHelperImpl = ArticleDescriptionHelperImpl()

    @Test
    fun `locally stored with biography`(){
        val card = Card.DataCard(infoUrl = "url", description = TEST_BIOGRAPHY, isLocallyStored = true, artistName = "artist")

        val res = articleDescriptionHelperImpl.getDescription(card)

        assertEquals(
            "$HEAD[*] $TEST_BIOGRAPHY_REPLACED$TAIL",
            res
        )
    }

    @Test
    fun `locally stored without biography`(){
        val card = Card.DataCard(infoUrl = "url", description = null, isLocallyStored = true, artistName = "artist")

        val res = articleDescriptionHelperImpl.getDescription(card)

        assertEquals(
            "$HEAD[*] ${ArticleUIState.NOT_FOUND}$TAIL",
            res
        )
    }

    @Test
    fun `not locally stored with biography`(){
        val card = Card.DataCard(infoUrl = "url", description = TEST_BIOGRAPHY, isLocallyStored = false, artistName = "artist")

        val res = articleDescriptionHelperImpl.getDescription(card)

        assertEquals(
            "$HEAD$TEST_BIOGRAPHY_REPLACED$TAIL",
            res
        )
    }

    @Test
    fun `not locally stored without biography`(){
        val card = Card.DataCard(infoUrl = "url", description = null, isLocallyStored = false, artistName = "artist")

        val res = articleDescriptionHelperImpl.getDescription(card)

        assertEquals(
            "$HEAD${ArticleUIState.NOT_FOUND}$TAIL",
            res
        )
    }

}