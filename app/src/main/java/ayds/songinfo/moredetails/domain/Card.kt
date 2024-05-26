package ayds.songinfo.moredetails.domain

sealed class Card {
    data class DataCard(
        val artistName: String,
        val description: String?,
        val infoUrl: String,
        val source: CardSource,
        val sourceLogoUrl: String,
        val isLocallyStored: Boolean = false
    ): Card()
    data object EmptyCard: Card()
}

enum class CardSource(val displayName: String){
    LASTFM("LastFM"),
    WIKIPEDIA("Wikipedia"),
    NEW_YORK_TIMES("New York Times")
}