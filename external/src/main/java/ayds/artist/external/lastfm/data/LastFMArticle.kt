package ayds.artist.external.lastfm.data
private const val AUDIO_SCROBBLER_IMAGE_URL = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"

sealed class LastFMArticle {
    data class LastFMArticleWithData(
        val artistName: String,
        val biography: String,
        val articleUrl: String,
        val sourceLogoUrl: String = AUDIO_SCROBBLER_IMAGE_URL
    ): LastFMArticle()
    object EmptyLastfmArticle : LastFMArticle()
}