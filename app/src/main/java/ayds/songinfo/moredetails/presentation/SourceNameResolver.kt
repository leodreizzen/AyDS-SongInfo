package ayds.songinfo.moredetails.presentation

import ayds.songinfo.moredetails.domain.CardSource

interface SourceNameResolver {
    fun getSourceName(source: CardSource): String
}

internal class SourceNameResolverImpl : SourceNameResolver {
    override fun getSourceName(source: CardSource): String {
        return when (source) {
            CardSource.LASTFM -> "LastFM"
            CardSource.WIKIPEDIA -> "Wikipedia"
            CardSource.NEW_YORK_TIMES -> "New York Times"
        }
    }
}