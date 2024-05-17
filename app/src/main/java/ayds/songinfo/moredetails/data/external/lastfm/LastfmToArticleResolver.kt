package ayds.songinfo.moredetails.data.external.lastfm

import ayds.songinfo.moredetails.domain.Article.LastFMArticle
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.util.*

private const val URL = "url"
private const val CONTENT = "content"
private const val ARTIST = "artist"
private const val BIO = "bio"


interface LastfmToArticleResolver {
    fun getArticleFromExternalData(serviceData: String, artistName: String): LastFMArticle
}

internal class JsonToArticleResolver: LastfmToArticleResolver{

    override fun getArticleFromExternalData(
        serviceData: String,
        artistName: String
    ): LastFMArticle {
        val jobj = getJson(serviceData)
        val artist = getArtist(jobj)
        val text: String? = getBioText(artist)
        val url = getUrl(artist)
        return LastFMArticle(artistName, text, url, false)
    }

    private fun getJson(
        serviceData: String,
    ): JsonObject {
        val gson = Gson()
        return gson.fromJson(serviceData, JsonObject::class.java)
    }

    private fun getUrl(artist: JsonObject) = artist[URL].asString


    private fun getBioText(artist : JsonObject): String? {
        val bio = getBio(artist)
        val extract = getBioContent(bio)
        val text = if (extract != null) {
            getTextFromBioContent(extract)
        } else
            null
        return text
    }

    private fun getTextFromBioContent(
        extract: JsonElement
    ): String = extract.asString.replace("\\n", "\n")

    private fun getBioContent(bio: JsonObject): JsonElement? =
        bio[CONTENT]

    private fun getBio(artist: JsonObject): JsonObject =
        artist[BIO].getAsJsonObject()

    private fun getArtist(jobj: JsonObject): JsonObject =
        jobj[ARTIST].getAsJsonObject()
}