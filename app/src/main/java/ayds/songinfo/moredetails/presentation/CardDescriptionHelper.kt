package ayds.songinfo.moredetails.presentation

import ayds.songinfo.moredetails.domain.Card
import java.util.*
private const val WIDTH = 400
private const val FONT = "arial"
private const val LOCALLY_STORED_PREFIX = "[*] "

interface CardDescriptionHelper{
    fun getDescription(card: Card): String
}

internal class CardDescriptionHelperImpl: CardDescriptionHelper {
    override fun getDescription(card: Card): String{
        return textToHTML(getTextBiography(card), card.artistName)
    }

    private fun getTextBiography(card: Card): String {
        val prefix = if (card.isLocallyStored) LOCALLY_STORED_PREFIX else ""
        val text = card.description.replace("\\n", "\n")
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