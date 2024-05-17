package ayds.songinfo.moredetails.presentation

import ayds.songinfo.moredetails.domain.Article
import java.util.*
private const val NO_RESULTS = "No Results"
private const val WIDTH = 400
private const val FONT = "arial"
interface ArticleDescriptionHelper{
    fun getDescription(article: Article.LastFMArticle): String
}


internal class ArticleDescriptionHelperImpl: ArticleDescriptionHelper {
    override fun getDescription(article: Article.LastFMArticle): String{
        return textToHTML(getTextBiography(article), article.artistName)
    }
    private fun getTextBiography(article: Article.LastFMArticle): String {
        val prefix = if (article.isLocallyStored) "[*] " else ""
        val text = article.biography?.replace("\\n", "\n") ?: NO_RESULTS
        return "$prefix$text"
    }

    private fun textToHTML(text: String, term: String): String {
        val builder = StringBuilder()
        builder.append("<html><div width=$WIDTH>")
        builder.append("<font face=\"$FONT\">")
        val textWithBold = text
            .replace("'", " ")
            .replace("\n", "<br>")
            .replace(
                "(?i)$term".toRegex(),
                "<b>" + term.uppercase(Locale.getDefault()) + "</b>"
            )
        builder.append(textWithBold)
        builder.append("</font></div></html>")
        return builder.toString()
    }
}