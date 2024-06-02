package ayds.songinfo.moredetails.injector
import android.content.Context
import ayds.songinfo.moredetails.presentation.*
import ayds.songinfo.moredetails.presentation.CardDescriptionHelperImpl
import ayds.songinfo.moredetails.presentation.MoreDetailsPresenterImpl

object MoreDetailsPresentationInjector {
    lateinit var presenter: MoreDetailsPresenter
    fun init(context: Context){
        DataInjector.initRepository(context)
        val cardDescriptionHelper: CardDescriptionHelper = CardDescriptionHelperImpl()
        val sourceNameResolver: SourceNameResolver = SourceNameResolverImpl()
        presenter = MoreDetailsPresenterImpl(DataInjector.repository, cardDescriptionHelper, sourceNameResolver)
    }
}