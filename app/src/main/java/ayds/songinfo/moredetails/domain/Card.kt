package ayds.songinfo.moredetails.domain

data class Card(
    val artistName: String,
    val description: String?,
    val infoUrl: String,
    val source: CardSource,
    val sourceLogoUrl: String,
    val isLocallyStored: Boolean = false
)

enum class CardSource(val displayName: String){
    LASTFM("LastFM"),
    WIKIPEDIA("Wikipedia"),
    NEW_YORK_TIMES("New York Times")
}