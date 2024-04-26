package ayds.songinfo.moredetails.data
import ayds.songinfo.moredetails.data.local.lastfm.LasfmLocalStorage
import ayds.songinfo.moredetails.domain.Article
import ayds.songinfo.moredetails.domain.ArticleRepository

class ArticleRepositoryImpl(
    val lastfmArticleService: ayds.songinfo.moredetails.data.external.lastfm.LastfmArticleService,
    val lastfmLocalStorage: LasfmLocalStorage
): ArticleRepository {

    override fun getArticle(artistName: String): Article?{
        var article = lastfmLocalStorage.getArticle(artistName)
        if (article == null) {
            article = lastfmArticleService.getArticle(artistName)
            if (article?.biography != null){
                lastfmLocalStorage.saveArticle(article)
            }
        }
        return article
    }
}