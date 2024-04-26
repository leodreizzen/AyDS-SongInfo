package ayds.songinfo.moredetails.domain

data class Article(
    val artistName: String,
    val biography: String?,
    val articleUrl: String,
    val isLocallyStored: Boolean
)