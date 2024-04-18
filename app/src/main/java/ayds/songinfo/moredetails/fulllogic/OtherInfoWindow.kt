package ayds.songinfo.moredetails.fulllogic

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.room.Room.databaseBuilder
import ayds.songinfo.R
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.util.Locale


private const val AUDIO_SCROBBLER = "https://ws.audioscrobbler.com/2.0/"

private const val LASTFM_IMAGE =
    "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"

private const val URL = "url"

private const val CONTENT = "content"

private const val ARTIST = "artist"

private const val BIO = "bio"

class OtherInfoWindow : Activity() {
    private var textPane1: TextView? = null
    private var lastFMAPI : LastFMAPI? = null

    //private JPanel imagePanel;
    // private JLabel posterImageLabel;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        textPane1 = findViewById(R.id.textPane1)
        val artistName = intent.getStringExtra("artistName")
        if (artistName != null)
            open(artistName)
    }

    private fun getArtistInfo(artistName: String) {
        Log.e("TAG", "artistName $artistName")
        Thread {
            val article = getArticle(artistName)
            showData(article)
        }.start()
    }

    private fun getArticle(artistName: String): Article?{
        var article = getArticleFromDB(artistName)
        if (article == null) {
            article = getArticleFromAPI(artistName)
            if (article?.biography != null){
                    saveArticle(article)
            }
        }
        return article
    }

    private fun showData(
        article: Article?,
    ) {
        if (article != null) {
            setUrlButtonLink(article.articleUrl)
        }
        showText(getText(article))
    }

    private fun getText(article: Article?): String{
        if(article != null)
            return (if (article.isLocallyStored) "[*]" else "") + (article.biography ?: "No Results")
        else
            return ""
    }

    private fun showText(text: String) {
        runOnUiThread {
            Log.e("TAG", "Get Image from $LASTFM_IMAGE")
            Picasso.get().load(LASTFM_IMAGE).into(findViewById<View>(R.id.imageView1) as ImageView)
            textPane1!!.text = Html.fromHtml(text)
        }
    }

    private fun getArticleFromDB(artistName: String?): Article? {
        val article = dataBase!!.ArticleDao().getArticleByArtistName(artistName!!)
        return if (article != null) {
            Article(article.artistName, article.biography, article.articleUrl, true)
        } else null
    }

    private fun saveArticle(article: Article) {
        if (article.biography != null) {
            Thread {
                dataBase!!.ArticleDao().insertArticle(
                    ArticleEntity(
                        article.artistName,
                        article.biography,
                        article.articleUrl
                    )
                )
            }.start()
        }
    }

    private fun getArticleFromAPI(
        artistName: String
    ): Article? {
        try {
            val callResponse = lastFMAPI!!.getArtistInfo(artistName).execute()
            Log.e("TAG", "JSON " + callResponse.body())
            return getArticleFromResponse(callResponse, artistName)
        } catch (e: IOException) {
            Log.e("TAG", "Error $e")
            e.printStackTrace()
            return null
        }
    }

    private fun getArticleFromResponse(
        callResponse: Response<String>,
        artistName: String
    ): Article {
        val jobj = getJson(callResponse)
        val artist = getArtist(jobj)
        val text: String? = getBioText(artist, artistName)
        val url = getUrl(artist)
        return Article(artistName, text, url, false)
    }

    private fun getJson(
        callResponse: Response<String>
    ): JsonObject {
        val gson = Gson()
        return gson.fromJson(callResponse.body(), JsonObject::class.java)
    }

    private fun getUrl(artist: JsonObject) = artist[URL].toString()

    private fun getBioText(artist : JsonObject, artistName: String): String? {
        val bio = getBio(artist)
        val extract = getBioContent(bio)
        val text = if (extract != null) {
            getTextFromBioContent(extract, artistName)
        } else
            null
        return text
    }

    private fun getTextFromBioContent(
        extract: JsonElement,
        artistName: String
    ): String {
        var text = extract.asString.replace("\\n", "\n")
        text = textToHtml(text, artistName)
        return text
    }

    private fun getBioContent(bio: JsonObject): JsonElement? =
        bio[CONTENT]

    private fun getBio(artist: JsonObject): JsonObject =
        artist[BIO].getAsJsonObject()

    private fun getArtist(jobj: JsonObject): JsonObject =
        jobj[ARTIST].getAsJsonObject()


    private fun setUrlButtonLink(urlString: String) {
        findViewById<View>(R.id.openUrlButton1).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(urlString))
            startActivity(intent)
        }
    }

    private fun initFMAPI() {
        val retrofit = Retrofit.Builder()
            .baseUrl(AUDIO_SCROBBLER)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        lastFMAPI = retrofit.create(LastFMAPI::class.java)
    }

    private var dataBase: ArticleDatabase? = null
    private fun open(artist: String) {
        dataBase =
            databaseBuilder(this, ArticleDatabase::class.java, "database-name-thename").build()
        initFMAPI()
        getArtistInfo(artist)
    }

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
        fun textToHtml(text: String, term: String?): String {
            val builder = StringBuilder()
            builder.append("<html><div width=400>")
            builder.append("<font face=\"arial\">")
            val textWithBold = text
                .replace("'", " ")
                .replace("\n", "<br>")
                .replace(
                    "(?i)$term".toRegex(),
                    "<b>" + term!!.uppercase(Locale.getDefault()) + "</b>"
                )
            builder.append(textWithBold)
            builder.append("</font></div></html>")
            return builder.toString()
        }
    }

    internal data class Article(
        val artistName: String,
        val biography: String?,
        val articleUrl: String,
        val isLocallyStored: Boolean
    ) {

    }

}