package ayds.songinfo.moredetails.presentation

data class ArticleUIState(
    val text: String = "",
    val articleLink: String? = null,
    val image: String = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
){
    companion object {
        val NOT_FOUND = "No results"
    }
}