package ayds.songinfo.moredetails.data
import ayds.songinfo.moredetails.data.external.lastfm.LastfmArticleService
import ayds.songinfo.moredetails.data.local.lastfm.LasfmLocalStorage
import ayds.songinfo.moredetails.domain.Article
import ayds.songinfo.moredetails.domain.Article.EmptyArticle

import ayds.songinfo.moredetails.domain.ArticleRepository

class ArticleRepositoryImpl(
    val lastfmArticleService: LastfmArticleService,
    val lastfmLocalStorage: LasfmLocalStorage
): ArticleRepository {

    override fun getArticle(artistName: String): Article {
        var article = lastfmLocalStorage.getArticle(artistName)
        if (article == null) {
            try {
                article = lastfmArticleService.getArticle(artistName)
                if (article?.biography != null) {
                    lastfmLocalStorage.saveArticle(article)
                }
            } catch (e: Exception){
                article = null
            }
        }
        return article ?: EmptyArticle
    }
}