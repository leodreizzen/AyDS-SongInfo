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
        article: Article?,
    ) {
        if (article != null) {
            urlObservable.notify(article.articleUrl)
        }
        textObservable.notify(getText(article))
    }

    private fun getText(article: Article?): String{
        if(article != null)
            return (if (article.isLocallyStored) "[*]" else "") + (article.biography ?: "No Results")
        else
            return ""
    }
}