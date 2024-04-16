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
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.util.Locale


private const val AUDIO_SCROBBLER = "https://ws.audioscrobbler.com/2.0/"

private const val LASTFM_IMAGE =
    "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"

class OtherInfoWindow : Activity() {
    private var textPane1: TextView? = null

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

    fun getArtistInfo(artistName: String) {
        val lastFMAPI = initFMAPI()
        Log.e("TAG", "artistName $artistName")
        Thread {
            var article = getArticleFromDB(artistName)
            if (article == null) {
                article = getArticleFromAPI(lastFMAPI, artistName)
                if (article != null) {
                    if (article.biography != null)
                        saveArticle(article)
                }
            }
            showData(article)
        }.start()
    }

    private fun showData(
        article: Article?,
    ) {
        var text = ""
        if (article != null) {
            text = (if (article.isLocallyStored) "[*]" else "") + (article.biography ?: "No Results")
            setUrlButtonLink(article.articleUrl)
        }
        val imageUrl = LASTFM_IMAGE
        Log.e("TAG", "Get Image from $imageUrl")
        showText(imageUrl, text)
    }

    private fun showText(imageUrl: String, text: String) {
        runOnUiThread {
            Picasso.get().load(imageUrl).into(findViewById<View>(R.id.imageView1) as ImageView)
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
        lastFMAPI: LastFMAPI,
        artistName: String
    ): Article? {
        try {
            val callResponse = lastFMAPI.getArtistInfo(artistName).execute()
            Log.e("TAG", "JSON " + callResponse.body())
            val gson = Gson()
            val jobj = gson.fromJson(callResponse.body(), JsonObject::class.java)
            val artist = jobj["artist"].getAsJsonObject()
            val bio = artist["bio"].getAsJsonObject()
            val extract = bio["content"]
            var text: String?
            if (extract != null) {
                text = extract.asString.replace("\\n", "\n")
                text = textToHtml(text, artistName)
            } else
                text = null
            val url = artist["url"].toString()
            return Article(artistName, text, url, false)
        } catch (e: IOException) {
            Log.e("TAG", "Error $e")
            e.printStackTrace()
            return null
        }
    }


    private fun setUrlButtonLink(urlString: String) {
        findViewById<View>(R.id.openUrlButton1).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(urlString))
            startActivity(intent)
        }
    }

    private fun initFMAPI(): LastFMAPI {
        val retrofit = Retrofit.Builder()
            .baseUrl(AUDIO_SCROBBLER)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        val lastFMAPI = retrofit.create(LastFMAPI::class.java)
        return lastFMAPI
    }

    private var dataBase: ArticleDatabase? = null
    private fun open(artist: String) {
        dataBase =
            databaseBuilder(this, ArticleDatabase::class.java, "database-name-thename").build()
        Thread {
            dataBase!!.ArticleDao().insertArticle(ArticleEntity("test", "sarasa", ""))
            Log.e("TAG", "" + dataBase!!.ArticleDao().getArticleByArtistName("test"))
            Log.e("TAG", "" + dataBase!!.ArticleDao().getArticleByArtistName("nada"))
        }.start()
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