package ayds.songinfo.moredetails.presetation

import ayds.songinfo.moredetails.domain.CardSource
import ayds.songinfo.moredetails.presentation.SourceNameResolverImpl
import org.junit.Assert.assertEquals
import org.junit.Test


class SourceNameResolverTest {
    private val resolverImpl = SourceNameResolverImpl()

    @Test
    fun `SourceNameResolverImpl get correct source name for LastFM`() {
        assertEquals("LastFM", resolverImpl.getSourceName(CardSource.LASTFM))
    }
    @Test
    fun `SourceNameResolverImpl get correct source name for Wikipedia`() {
        assertEquals("Wikipedia", resolverImpl.getSourceName(CardSource.WIKIPEDIA))
    }
    @Test
    fun `SourceNameResolverImpl get correct source name for New York Times`() {
        assertEquals("New York Times", resolverImpl.getSourceName(CardSource.NEW_YORK_TIMES))
    }
}