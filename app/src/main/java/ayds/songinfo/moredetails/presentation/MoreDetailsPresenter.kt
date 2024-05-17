package ayds.songinfo.moredetails.presentation
import ayds.observer.Observable
import ayds.observer.Subject
import ayds.songinfo.moredetails.domain.Article
import ayds.songinfo.moredetails.domain.ArticleRepository

interface MoreDetailsPresenter {
    val articleObservable: Observable<ArticleUIState>
    fun onOpen(artistName: String)
}

internal class MoreDetailsPresenterImpl(
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


    private fun lastFMArticleToUiState(article: Article.LastFMArticle):ArticleUIState =
        ArticleUIState(
            articleDescriptionHelper.getDescription(article),
            article.articleUrl
        )

    private fun emptyArticleToUiState():ArticleUIState =
        ArticleUIState(
            ArticleUIState.NOT_FOUND,
            null
        )

    private fun articleToUiState(article: Article) =
        if (article is Article.LastFMArticle)
            lastFMArticleToUiState(article)
        else
            emptyArticleToUiState()

}