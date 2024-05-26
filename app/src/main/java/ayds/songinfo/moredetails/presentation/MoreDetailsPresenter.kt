package ayds.songinfo.moredetails.presentation
import ayds.observer.Observable
import ayds.observer.Subject
import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.CardRepository

interface MoreDetailsPresenter {
    val articleObservable: Observable<ArticleUIState>
    fun onOpen(artistName: String)
}

internal class MoreDetailsPresenterImpl(
    private val repository: CardRepository,
    private val articleDescriptionHelper: ArticleDescriptionHelper
): MoreDetailsPresenter{
    override val articleObservable = Subject<ArticleUIState>()

    override fun onOpen(artistName: String) {
        Thread {
            getAndNotifyArticle(artistName)
        }.start()
    }

    private fun getAndNotifyArticle(artistName: String) {
        val article = repository.getCard(artistName)
        notifyArticleChange(article)
    }

    private fun notifyArticleChange(
        card: Card,
    ) {
        articleObservable.notify(articleToUiState(card))
    }


    private fun lastFMArticleToUiState(card: Card.DataCard):ArticleUIState =
        ArticleUIState(
            articleDescriptionHelper.getDescription(card),
            card.infoUrl,
            getSourceText(card),
            card.sourceLogoUrl
        )

    private fun getSourceText(card: Card.DataCard) = "Source: ${card.source.displayName}"

    private fun emptyArticleToUiState():ArticleUIState =
        ArticleUIState(
            ArticleUIState.NOT_FOUND,
            null
        )

    private fun articleToUiState(card: Card) =
        if (card is Card.DataCard)
            lastFMArticleToUiState(card)
        else
            emptyArticleToUiState()

}