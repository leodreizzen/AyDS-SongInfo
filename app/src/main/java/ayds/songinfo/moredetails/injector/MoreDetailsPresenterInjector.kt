package ayds.songinfo.moredetails.injector
import android.content.Context
import ayds.songinfo.moredetails.presentation.MoreDetailsPresenter
import ayds.songinfo.moredetails.presentation.MoreDetailsPresenterImpl

object MoreDetailsPresenterInjector {
    lateinit var presenter: MoreDetailsPresenter
    fun init(context: Context){
        ArticleRepositoryInjector.init(context)
        presenter = MoreDetailsPresenterImpl(ArticleRepositoryInjector.repository)
    }
}