package ayds.songinfo.moredetails.domain

interface CardRepository {
    fun getCards(artistName: String): List<Card>
}