package ayds.songinfo.home.view

import ayds.songinfo.home.model.entities.Song
import ayds.songinfo.home.model.entities.Song.SpotifySong
import ayds.songinfo.utils.date.getMonthName
import ayds.songinfo.utils.date.isLeapYear


interface SongReleaseDateFactory {
    fun getReleaseDateResolver(song : SpotifySong): SongReleaseDateHelper
}

class SongReleaseDateFactoryImpl : SongReleaseDateFactory {
    override fun getReleaseDateResolver(song: SpotifySong): SongReleaseDateHelper {
        return when(song.releaseDatePrecision){
            "day" -> ReleaseDateDayResolver(song)
            "month" -> ReleaseDateMonthResolver(song)
            "year" -> ReleaseDateYearResolver(song)
            else -> ReleaseDateDefaultResolver(song)
        }
    }
}



interface SongReleaseDateHelper {
    val song: SpotifySong
    fun getReleaseDateText(): String
}




internal class ReleaseDateDayResolver(override val song: SpotifySong) : SongReleaseDateHelper {
    override fun getReleaseDateText(): String{
        val partes = song.releaseDate.split("-").map{it.toInt()}
        return "${partes[2]}/${partes[1]}/${partes[0]}"
    }
}

internal class ReleaseDateMonthResolver(override val song : SpotifySong) : SongReleaseDateHelper {
    override fun getReleaseDateText(): String{
        val partes = song.releaseDate.split("-").map{it.toInt()}
        return "${partes[1].getMonthName()}, ${partes[0]}"
    }
}


internal class ReleaseDateYearResolver(override val song : SpotifySong) : SongReleaseDateHelper {
    override fun getReleaseDateText(): String{
        val year = song.releaseDate.toInt()
        return "$year ${getLeapYearText(year)}"
    }

    private fun getLeapYearText(year: Int): String{
        return if (year.isLeapYear())
            "(leap year)"
        else
            "(not a leap year)"
    }
}

internal class ReleaseDateDefaultResolver(override val song : SpotifySong) : SongReleaseDateHelper {
    override fun getReleaseDateText(): String{
        return song.releaseDate
    }
}


