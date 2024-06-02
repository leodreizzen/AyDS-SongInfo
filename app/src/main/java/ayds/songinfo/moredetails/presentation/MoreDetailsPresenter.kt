package ayds.songinfo.moredetails.presentation
import ayds.observer.Observable
import ayds.observer.Subject
import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.CardRepository

interface MoreDetailsPresenter {
    val articleObservable: Observable<List<CardUIState>>
    fun onOpen(artistName: String)
}

private const val CARD_NUMBER = 3

internal class MoreDetailsPresenterImpl(
    private val repository: CardRepository,
    private val cardDescriptionHelper: CardDescriptionHelper,
    private val sourceNameResolver: SourceNameResolver
): MoreDetailsPresenter{
    override val articleObservable = Subject<List<CardUIState>>()

    override fun onOpen(artistName: String) {
        Thread {
            getAndNotifyCards(artistName)
        }.start()
    }

    private fun getAndNotifyCards(artistName: String) {
        val cards = repository.getCards(artistName)
        notifyCardsChange(cards)
    }

    private fun notifyCardsChange(
        cards: List<Card>,
    ) {
        val states = cardsToStates(cards)
        removeExtraStates(states)
        fillEmptyStates(states)
        articleObservable.notify(states)
    }
    private fun fillEmptyStates(states: MutableList<CardUIState>) {
        for (i in states.size until CARD_NUMBER) {
            states.add(CardUIState())
        }
    }
    private fun removeExtraStates(states: MutableList<CardUIState>) {
        while (states.size > CARD_NUMBER) {
            states.removeAt(states.size - 1)
        }
    }

    private fun cardsToStates(cards: List<Card>): MutableList<CardUIState> {
        return cards.map { cardToUiState(it) }.toMutableList()
    }

    private fun getSourceText(card: Card) = "Source: ${sourceNameResolver.getSourceName(card.source)}"

    private fun cardToUiState(card: Card) =
        CardUIState(
            cardDescriptionHelper.getDescription(card),
            card.infoUrl,
            getSourceText(card),
            card.sourceLogoUrl
        )
}