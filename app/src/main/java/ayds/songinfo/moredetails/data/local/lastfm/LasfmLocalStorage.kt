package ayds.songinfo.moredetails.data.local.lastfm

import ayds.songinfo.moredetails.domain.Article.LastFMArticle

interface LasfmLocalStorage {
    fun getArticle(artistName: String): LastFMArticle?
    fun saveArticle(article: LastFMArticle)
}

internal class LastfmLocalStorageImpl(
    private val dataBase: ArticleDatabase
) : LasfmLocalStorage {

    override fun getArticle(artistName: String): LastFMArticle? {
        val article = dataBase.ArticleDao().getArticleByArtistName(artistName)
        return if (article != null) {
            LastFMArticle(article.artistName, article.biography, article.articleUrl, true)
        } else null
    }

    override fun saveArticle(article: LastFMArticle) {
        if (article.biography != null) {
            dataBase.ArticleDao().insertArticle(
                ArticleEntity(
                    article.artistName,
                    article.biography,
                    article.articleUrl
                )
            )
        }
    }
}