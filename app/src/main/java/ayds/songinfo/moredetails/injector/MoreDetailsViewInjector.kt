package ayds.songinfo.moredetails.injector
import ayds.songinfo.moredetails.presentation.MoreDetailsView

object MoreDetailsViewInjector {
    fun init(moreDetailsView: MoreDetailsView){
        MoreDetailsPresenterInjector.init(moreDetailsView)
    }
}