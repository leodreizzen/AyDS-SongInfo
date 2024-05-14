package ayds.songinfo.moredetails.domain

sealed class Article {
    data class LastFMArticle(
        val artistName: String,
        val biography: String?,
        val articleUrl: String,
        val isLocallyStored: Boolean
    ): Article()
    data object EmptyArticle: Article()
}