package ayds.songinfo.moredetails.domain

interface CardRepository {
    fun getCard(artistName: String): List<Card>
}