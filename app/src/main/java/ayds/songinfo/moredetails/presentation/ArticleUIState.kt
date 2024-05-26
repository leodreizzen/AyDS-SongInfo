package ayds.songinfo.moredetails.presentation

data class ArticleUIState(
    val text: String = "",
    val articleLink: String? = null,
    val source: String = "",
    val image: String? = null
){
    companion object {
        const val NOT_FOUND = "No results"
    }
}