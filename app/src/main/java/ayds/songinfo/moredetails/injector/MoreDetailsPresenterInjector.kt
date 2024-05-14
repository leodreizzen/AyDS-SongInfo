package ayds.songinfo.moredetails.injector
import android.content.Context
import ayds.songinfo.moredetails.presentation.ArticleDescriptionHelper
import ayds.songinfo.moredetails.presentation.ArticleDescriptionHelperImpl
import ayds.songinfo.moredetails.presentation.MoreDetailsPresenter
import ayds.songinfo.moredetails.presentation.MoreDetailsPresenterImpl

object MoreDetailsPresenterInjector {
    lateinit var presenter: MoreDetailsPresenter
    fun init(context: Context){
        ArticleRepositoryInjector.init(context)
        val articleDescriptionHelper: ArticleDescriptionHelper = ArticleDescriptionHelperImpl()
        presenter = MoreDetailsPresenterImpl(ArticleRepositoryInjector.repository, articleDescriptionHelper)
    }
}