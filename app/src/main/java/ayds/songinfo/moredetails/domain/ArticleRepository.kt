package ayds.songinfo.moredetails.domain

interface ArticleRepository {
    fun getArticle(artistName: String): Article?
}