package ayds.songinfo.moredetails.presentation
import ayds.observer.Observable
import ayds.observer.Subject
import ayds.songinfo.moredetails.domain.Article
import ayds.songinfo.moredetails.domain.ArticleRepository

interface OtherInfoPresenter {
    val textObservable: Observable<String>
    val urlObservable: Observable<String>
    fun onOpen(artistName: String)
}
private const val NO_RESULTS_TEXT: String = "No results"

class OtherInfoPresenterImpl(
   private val repository: ArticleRepository
): OtherInfoPresenter{
    override val textObservable = Subject<String>()
    override val urlObservable = Subject<String>()

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
        when (article){
            is Article.LastFMArticle -> {
                showArticle(article)
            }
            Article.EmptyArticle -> {
                showNoResultsArticle()
            }
        }
    }

    private fun showNoResultsArticle() {
        urlObservable.notify("")
        textObservable.notify(NO_RESULTS_TEXT)
    }

    private fun showArticle(article: Article.LastFMArticle) {
        urlObservable.notify(article.articleUrl)
        textObservable.notify(getText(article))
    }

    private fun getText(article: Article.LastFMArticle): String{
        return (if (article.isLocallyStored) "[*]" else "") + (article.biography ?: "No Results")
    }
}