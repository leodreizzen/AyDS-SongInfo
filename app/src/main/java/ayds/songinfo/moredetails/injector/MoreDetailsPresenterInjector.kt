package ayds.songinfo.moredetails.injector
import android.content.Context
import ayds.songinfo.moredetails.presentation.OtherInfoPresenter
import ayds.songinfo.moredetails.presentation.OtherInfoPresenterImpl

object MoreDetailsPresenterInjector {
    lateinit var presenter: OtherInfoPresenter
    fun init(context: Context){
        ArticleRepositoryInjector.init(context)
        presenter = OtherInfoPresenterImpl(ArticleRepositoryInjector.repository)
    }
}