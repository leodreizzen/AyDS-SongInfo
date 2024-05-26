package ayds.songinfo.moredetails.presentation

import android.app.Activity
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import ayds.songinfo.R
import ayds.songinfo.moredetails.injector.MoreDetailsPresentationInjector
import ayds.songinfo.utils.UtilsInjector
import ayds.songinfo.utils.navigation.NavigationUtils
import com.squareup.picasso.Picasso


class MoreDetailsView : Activity() {
    private lateinit var articleTextViews: MutableList<TextView>
    private lateinit var openURLButtons : MutableList<View>
    private lateinit var imageViews: MutableList<ImageView>
    private lateinit var sourceTextViews: MutableList<TextView>
    private lateinit var presenter: MoreDetailsPresenter
    private var uiState = ArticleUIState()
    private val navigationUtils: NavigationUtils = UtilsInjector.navigationUtils


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initModule()
        initGUI()
        initObservers()
        notifyPresenter()
    }

    private fun initModule(){
        MoreDetailsPresentationInjector.init(this)
        presenter = MoreDetailsPresentationInjector.presenter
    }

    private fun initObservers(){
        presenter.articleObservable.subscribe{updateUI(it)}
    }

    private fun initGUI() {
        setContentView(R.layout.activity_other_info)
        articleTextViews = ArrayList()
        openURLButtons = ArrayList()
        imageViews = ArrayList()
        sourceTextViews = ArrayList()

        articleTextViews.add(findViewById(R.id.textPane1))
        openURLButtons.add(findViewById(R.id.openUrlButton1))
        imageViews.add(findViewById(R.id.imageView1))
        sourceTextViews.add(findViewById(R.id.sourceText1))
    }

    private fun notifyPresenter() {
        val artist = getArtistName()
        if (artist != null) {
            presenter.onOpen(artist)
        }
    }

    private fun getArtistName() = intent.getStringExtra(ARTIST_NAME_EXTRA)

    private fun updateUI(state: ArticleUIState){
        uiState = state
        runOnUiThread{
            updateText()
            updateSource()
            updateImage()
            updateButton()
        }
    }

    private fun updateSource(){
        sourceTextViews[0].text = uiState.source
    }

    private fun updateText(){
        articleTextViews[0].text = Html.fromHtml(uiState.text)
    }

    private fun updateImage(){
        Picasso.get().load(uiState.image).into(imageViews[0])
    }

    private fun updateButton(){
        if (uiState.articleLink != null){
            updateLink()
            showButton()
        }
        else
            hideButton()
    }

    private fun hideButton() {
        openURLButtons[0].visibility = View.GONE
    }

    private fun showButton() {
        openURLButtons[0].visibility = View.VISIBLE
    }

    private fun updateLink() {
        openURLButtons[0].setOnClickListener {
            openExternalLink(uiState.articleLink)
        }
    }

    private fun openExternalLink(url: String?) {
        if (url != null)
            navigationUtils.openExternalUrl(this, url)
    }

    companion object{
        const val ARTIST_NAME_EXTRA = "artistName"
    }
}