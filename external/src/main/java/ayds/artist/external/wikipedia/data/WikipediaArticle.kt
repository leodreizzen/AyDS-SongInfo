package ayds.artist.external.wikipedia.data

sealed class WikipediaArticle {
    data class WikipediaArticleWithData(
        var name: String,
        var description: String,
        var wikipediaURL: String,
        var wikipediaLogoURL: String,
    ) :WikipediaArticle()

    object EmptyWikipediaArticle : WikipediaArticle()
}
