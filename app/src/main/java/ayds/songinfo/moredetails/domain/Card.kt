package ayds.songinfo.moredetails.domain

data class Card(
    val artistName: String,
    val description: String,
    val infoUrl: String,
    val source: CardSource,
    val sourceLogoUrl: String,
    val isLocallyStored: Boolean = false
)

enum class CardSource{
    LASTFM,
    WIKIPEDIA,
    NEW_YORK_TIMES
}