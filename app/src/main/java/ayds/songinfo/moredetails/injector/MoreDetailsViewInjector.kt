package ayds.songinfo.moredetails.injector
import ayds.songinfo.moredetails.presentation.MoreDetails

object MoreDetailsViewInjector {
    fun init(moreDetails: MoreDetails){
        MoreDetailsPresenterInjector.init(moreDetails)
    }
}