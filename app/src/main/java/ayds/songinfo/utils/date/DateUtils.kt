package ayds.songinfo.utils.date

fun Int.getMonthName() = when(this){
        1 -> "January"
        2 -> "February"
        3 -> "March"
        4 -> "April"
        5 -> "May"
        6 -> "June"
        7 -> "July"
        8 -> "August"
        9 -> "September"
        10 -> "October"
        11 -> "November"
        12 -> "December"
        else -> "Invalid month"
    }
fun Int.isLeapYear() = ((this % 4 == 0 && this % 100 != 0) || (this % 400 == 0))
