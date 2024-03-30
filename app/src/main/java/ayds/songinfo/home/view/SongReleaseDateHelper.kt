package ayds.songinfo.home.view

import ayds.songinfo.home.model.entities.Song.SpotifySong
import ayds.songinfo.utils.date.getMonthName
import ayds.songinfo.utils.date.isLeapYear


interface SongReleaseDateHelper {
    fun getReleaseDateText(song: SpotifySong): String
}

internal class SongReleaseDateHelperImpl(): SongReleaseDateHelper{
    override fun getReleaseDateText(song: SpotifySong): String {
        return when(song.releaseDatePrecision){
            "day" -> getDayReleaseDateText(song.releaseDate)
            "month" -> getMonthReleaseDateText(song.releaseDate)
            "year" -> getYearReleaseDateText(song.releaseDate)
            else -> "Invalid release date"
        }
    }

    private fun getDayReleaseDateText(releaseDate: String): String{
        val partes = releaseDate.split("-").map{it.toInt()}
        return "${partes[2]}/${partes[1]}/${partes[0]}"
    }


    private fun getMonthReleaseDateText(releaseDate: String): String {
        val partes = releaseDate.split("-").map{it.toInt()}
        return "${partes[1].getMonthName()}, ${partes[0]}"
    }

    private fun getYearReleaseDateText(releaseDate: String): String {
        val year = releaseDate.toInt()
        return "$year ${getLeapYearText(year)}"
    }

    private fun getLeapYearText(year: Int): String{
        return if (year.isLeapYear())
            "(leap year)"
        else
            "(not a leap year)"
    }

}


