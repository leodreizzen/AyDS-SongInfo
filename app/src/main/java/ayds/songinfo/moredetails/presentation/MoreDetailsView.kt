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
    private lateinit var openURLButtons: MutableList<View>
    private lateinit var imageViews: MutableList<ImageView>
    private lateinit var sourceTextViews: MutableList<TextView>
    private lateinit var presenter: MoreDetailsPresenter
    private val navigationUtils: NavigationUtils = UtilsInjector.navigationUtils


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initModule()
        initGUI()
        initObservers()
        notifyPresenter()
    }

    private fun initModule() {
        MoreDetailsPresentationInjector.init(this)
        presenter = MoreDetailsPresentationInjector.presenter
    }

    private fun initObservers() {
        presenter.articleObservable.subscribe { updateUI(it) }
    }

    private fun initGUI() {
        setContentView(R.layout.activity_other_info)
        articleTextViews = ArrayList()
        openURLButtons = ArrayList()
        imageViews = ArrayList()
        sourceTextViews = ArrayList()

        articleTextViews.add(findViewById(R.id.CardDescriptionTextPane1))
        openURLButtons.add(findViewById(R.id.openUrlButton1))
        imageViews.add(findViewById(R.id.SourceLogoImageView1))
        sourceTextViews.add(findViewById(R.id.sourceText1))

        articleTextViews.add(findViewById(R.id.CardDescriptionTextPane2))
        openURLButtons.add(findViewById(R.id.openUrlButton2))
        imageViews.add(findViewById(R.id.SourceLogoImageView2))
        sourceTextViews.add(findViewById(R.id.sourceText2))

        articleTextViews.add(findViewById(R.id.CardDescriptionTextPane3))
        openURLButtons.add(findViewById(R.id.openUrlButton3))
        imageViews.add(findViewById(R.id.SourceLogoImageView3))
        sourceTextViews.add(findViewById(R.id.sourceText3))
    }

    private fun notifyPresenter() {
        val artist = getArtistName()
        if (artist != null) {
            presenter.onOpen(artist)
        }
    }

    private fun getArtistName() = intent.getStringExtra(ARTIST_NAME_EXTRA)

    private fun updateUI(states: List<CardUIState>) {
        runOnUiThread {
            states.forEachIndexed { index, state ->
                updateUIState(state, index)
            }
        }
    }


    private fun updateUIState(state: CardUIState, index : Int){
        updateSource(state, index)
        updateText(state, index)
        updateImage(state, index)
        updateButton(state, index)

    }

    private fun updateSource(state: CardUIState, index: Int) {
        sourceTextViews[index].text = state.source
    }

    private fun updateText(state: CardUIState, index: Int) {

        articleTextViews[index].text = Html.fromHtml(state.text)
    }

    private fun updateImage(state: CardUIState, index: Int) {
        Picasso.get().load(state.image).into(imageViews[index])
    }

    private fun updateButton(state: CardUIState, index: Int) {
        if (state.articleLink != null) {
            updateLink(state, index)
            showButton(index)
        } else
            hideButton(index)
    }

    private fun hideButton(index: Int) {
        openURLButtons[index].visibility = View.GONE
    }

    private fun showButton(index : Int) {
        openURLButtons[index].visibility = View.VISIBLE
    }

    private fun updateLink(state: CardUIState, index: Int) {
        openURLButtons[index].setOnClickListener {
            openExternalLink(state.articleLink)
        }
    }

    private fun openExternalLink(url: String?) {
        if (url != null)
            navigationUtils.openExternalUrl(this, url)
    }

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }
}