package ayds.songinfo.moredetails.injector
import android.content.Context
import ayds.songinfo.moredetails.presentation.*
import ayds.songinfo.moredetails.presentation.ArticleDescriptionHelperImpl
import ayds.songinfo.moredetails.presentation.MoreDetailsPresenterImpl

object MoreDetailsPresentationInjector {
    lateinit var presenter: MoreDetailsPresenter
    fun init(context: Context){
        DataInjector.initRepository(context)
        val articleDescriptionHelper: ArticleDescriptionHelper = ArticleDescriptionHelperImpl()
        presenter = MoreDetailsPresenterImpl(DataInjector.repository, articleDescriptionHelper)
    }
}