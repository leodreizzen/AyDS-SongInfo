package ayds.songinfo.moredetails.data
import ayds.songinfo.moredetails.data.external.lastfm.LastfmArticleService
import ayds.songinfo.moredetails.data.local.lastfm.LasfmLocalStorage
import ayds.songinfo.moredetails.domain.Article
import ayds.songinfo.moredetails.domain.Article.EmptyArticle

import ayds.songinfo.moredetails.domain.ArticleRepository
import ayds.songinfo.utils.ErrorLogger

internal class ArticleRepositoryImpl(
    private val lastfmArticleService: LastfmArticleService,
    private val lastfmLocalStorage: LasfmLocalStorage,
    private val errorLogger: ErrorLogger
): ArticleRepository {

    override fun getArticle(artistName: String): Article {
        var article = lastfmLocalStorage.getArticle(artistName)
        if (article == null) {
            try {
                article = lastfmArticleService.getArticle(artistName)
                if (article?.biography != null) {
                    try{
                        lastfmLocalStorage.saveArticle(article)
                    } catch (e: Exception){
                        errorLogger.logError("Error saving to database", e.message?:"")
                    }
                }
            } catch (e: Exception){
                article = null
            }
        }
        return article ?: EmptyArticle
    }
}