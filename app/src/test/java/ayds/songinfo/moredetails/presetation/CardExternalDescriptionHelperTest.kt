package ayds.songinfo.moredetails.presetation

import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.CardSource
import ayds.songinfo.moredetails.presentation.CardDescriptionHelperImpl
import org.junit.Assert.assertEquals
import org.junit.Test

private const val TEST_DESCRIPTION = "line 1\\nline '2'\nline 3"
/*
    Should replace "\n" or \n with <br>
    Should replace ' with space
 */
private const val TEST_DESCRIPTION_REPLACED = "line 1<br>line  2 <br>line 3"
private const val HEAD = "<html><div width=400><font face=\"arial\">"
private const val TAIL = "</font></div></html>"

class CardExternalDescriptionHelperTest {
    private val articleDescriptionHelperImpl = CardDescriptionHelperImpl()

    @Test
    fun `locally stored with description`(){
        val card = Card(infoUrl = "url", description = TEST_DESCRIPTION, isLocallyStored = true, artistName = "artist", source = CardSource.LASTFM, sourceLogoUrl = "sourceLogoURl")

        val res = articleDescriptionHelperImpl.getDescription(card)

        assertEquals(
            "$HEAD[*] $TEST_DESCRIPTION_REPLACED$TAIL",
            res
        )
    }


    @Test
    fun `not locally stored with description`(){
        val card = Card(infoUrl = "url", description = TEST_DESCRIPTION, isLocallyStored = false, artistName = "artist", source = CardSource.LASTFM, sourceLogoUrl = "sourceLogoURl")

        val res = articleDescriptionHelperImpl.getDescription(card)

        assertEquals(
            "$HEAD$TEST_DESCRIPTION_REPLACED$TAIL",
            res
        )
    }



}