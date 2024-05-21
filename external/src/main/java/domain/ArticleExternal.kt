package domain

sealed class ArticleExternal {
    data class LastFMArticle(
        val artistName: String,
        val biography: String?,
        val articleUrl: String,
        val isLocallyStored: Boolean
    ): ArticleExternal()
    data object EmptyArticle: ArticleExternal()
}