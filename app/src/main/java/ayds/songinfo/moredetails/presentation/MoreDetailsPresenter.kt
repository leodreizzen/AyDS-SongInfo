package ayds.songinfo.moredetails.presentation
import ayds.observer.Observable
import ayds.observer.Subject
import ayds.songinfo.moredetails.domain.Article
import ayds.songinfo.moredetails.domain.ArticleRepository

interface MoreDetailsPresenter {
    val articleObservable: Observable<ArticleUIState>
    fun onOpen(artistName: String)
}
private const val NO_RESULTS_TEXT: String = "No results"

private const val NOT_FOUND = "Not found"

class MoreDetailsPresenterImpl(
   private val repository: ArticleRepository,
   private val articleDescriptionHelper: ArticleDescriptionHelper
): MoreDetailsPresenter{
    override val articleObservable = Subject<ArticleUIState>()

    override fun onOpen(artistName: String) {
        Thread {
            getAndShowArtistInfo(artistName)
        }.start()
    }

    private fun getAndShowArtistInfo(artistName: String) {
        val article = repository.getArticle(artistName)
        showData(article)
    }

    private fun showData(
        article: Article,
    ) {
        articleObservable.notify(articleToUiState(article))
    }

    private fun getText(article: Article.LastFMArticle): String{
        return (if (article.isLocallyStored) "[*]" else "") + (article.biography ?: "No Results")
    }

    private fun lastFMArticleToUiState(article: Article.LastFMArticle):ArticleUIState =
        ArticleUIState(
            articleDescriptionHelper.getDescription(article),
            article.articleUrl
        )

    private fun emptyArticleToUiState(article: Article.EmptyArticle):ArticleUIState =
        ArticleUIState(
            NOT_FOUND,
            null
        )

    private fun articleToUiState(article: Article) =
        if (article is Article.LastFMArticle)
            lastFMArticleToUiState(article)
        else
            emptyArticleToUiState(article as Article.EmptyArticle)

}