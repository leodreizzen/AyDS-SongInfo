package ayds.songinfo.moredetails.presentation

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import ayds.songinfo.R
import ayds.songinfo.moredetails.injector.MoreDetailsPresenterInjector
import ayds.songinfo.moredetails.injector.MoreDetailsViewInjector
import com.squareup.picasso.Picasso

private const val LASTFM_IMAGE =
    "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"


class MoreDetailsView : Activity() {
    private lateinit var articleTextView: TextView
    private lateinit var openURLButton : View
    private lateinit var imageView: ImageView
    private lateinit var presenter: MoreDetailsPresenter
    private var uiState = MoreDetailsUIState()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initModule()
        initGUI()
        initObservers()
        notifyPresenter()
    }

    private fun initModule(){
        MoreDetailsViewInjector.init(this)
        presenter = MoreDetailsPresenterInjector.presenter
    }

    private fun initObservers(){
        presenter.textObservable.subscribe{ updateText(it) }
        presenter.urlObservable.subscribe{setUrlButtonLink(it)}
    }

    private fun initGUI() {
        setContentView(R.layout.activity_other_info)
        articleTextView = findViewById(R.id.textPane1)
        openURLButton = findViewById(R.id.openUrlButton1)
        imageView = findViewById<View>(R.id.imageView1) as ImageView
    }

    private fun notifyPresenter() {
        val artist = getArtistName()
        if (artist != null) {
            presenter.onOpen(artist)
        }
    }

    private fun getArtistName() = intent.getStringExtra("artistName")

    private fun updateText(text: String) {
        uiState = uiState.copy(
            image = LASTFM_IMAGE,
            text = text
        )
        updateTextAndImage()
    }

    private fun setUrlButtonLink(urlString: String) {
        uiState = uiState.copy(
            articleLink = urlString
        )
        updateLink()
    }

    private fun updateTextAndImage() {
        runOnUiThread {
            updateText()
            updateImage()
        }
    }

    private fun updateText(){
        articleTextView.text = Html.fromHtml(uiState.text)
    }

    private fun updateImage(){
        Picasso.get().load(uiState.image).into(imageView)
    }

    private fun updateLink(){
        openURLButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(uiState.articleLink))
            startActivity(intent)
        }
    }
    companion object{
        const val ARTIST_NAME_EXTRA = "artistName"
    }
}