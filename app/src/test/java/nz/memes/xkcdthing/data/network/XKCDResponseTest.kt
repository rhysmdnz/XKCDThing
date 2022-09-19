package nz.memes.xkcdthing.data.network

import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.time.LocalDate

class XKCDResponseTest {
    @Test
    fun toXKCDComic() {
        val response = XKCDResponse(
            4,
            2011,
            27,
            891,
            "https://imgs.xkcd.com/comics/movie_ages.png",
            "If you're 15 or younger, then just remember that it's nevertheless probably too late to be a child prodigy.", // ktlint-disable max-line-length
            "Movie Ages",
            "Movie Ages",
            "The 2011 Guide to Making People Feel Old\n-Using Movie Release Dates-\n[[A chart with 2 columns. First column is labeled 'Their Age,' and is numbered 1 through 35 & 'over 35.' The second column is labeled 'You Say' and is divided into four sub-columns. The first sub-column reads '\"Did you realize that...' from 1-35, and the third sub-column says 'Came Out' from 1-35.]\nAge 16: Snakes on a plane ... Half a decade ago?\"\n17-19: Revenge of the Sith ... More than half a decade ago?\"\n20: Finding Nemo ... Eight years ago?\"\n21-22: Shrek ... Ten years ago?\"\n23-25: The Matrix ... Not the last decade, but the one before \nthat\n?\"\n26: Toy Story\" ... Over fifteen years ago?\"\n27: The Lion King ... Seventeen years ago?\"\n28: Jurassic Park ... Eighteen years ago?\"\n29: Terminator 2 ... Twenty years ago?\"\n30-32: Home Alone ... More than twenty years ago?\"\n33-35\" The Little Mermaid ... Closer to the moon landing than the present day?\nOver 35: \"Hey, did you see this chart? You match your age to movie -- oh, right, sorry, it only goes up to 35. I guess it's not really aimed at older people.\"\n\n{{Title text: If you're 15 or younger, then just remember that it's nevertheless probably too late to be a child prodigy.}}" // ktlint-disable max-line-length
        )

        val comic = response.toXKCDComic()
        assertEquals(891, comic.num)
        assertEquals("https://imgs.xkcd.com/comics/movie_ages.png", comic.img)
        assertEquals(
            "If you're 15 or younger, then just remember that it's nevertheless probably too late to be a child prodigy.", // ktlint-disable max-line-length
            comic.alt
        )
        assertEquals("Movie Ages", comic.title)
        assertEquals("Movie Ages", comic.safeTitle)
        assertEquals(
            "The 2011 Guide to Making People Feel Old\n-Using Movie Release Dates-\n[[A chart with 2 columns. First column is labeled 'Their Age,' and is numbered 1 through 35 & 'over 35.' The second column is labeled 'You Say' and is divided into four sub-columns. The first sub-column reads '\"Did you realize that...' from 1-35, and the third sub-column says 'Came Out' from 1-35.]\nAge 16: Snakes on a plane ... Half a decade ago?\"\n17-19: Revenge of the Sith ... More than half a decade ago?\"\n20: Finding Nemo ... Eight years ago?\"\n21-22: Shrek ... Ten years ago?\"\n23-25: The Matrix ... Not the last decade, but the one before \nthat\n?\"\n26: Toy Story\" ... Over fifteen years ago?\"\n27: The Lion King ... Seventeen years ago?\"\n28: Jurassic Park ... Eighteen years ago?\"\n29: Terminator 2 ... Twenty years ago?\"\n30-32: Home Alone ... More than twenty years ago?\"\n33-35\" The Little Mermaid ... Closer to the moon landing than the present day?\nOver 35: \"Hey, did you see this chart? You match your age to movie -- oh, right, sorry, it only goes up to 35. I guess it's not really aimed at older people.\"\n\n{{Title text: If you're 15 or younger, then just remember that it's nevertheless probably too late to be a child prodigy.}}", // ktlint-disable max-line-length
            comic.transcript
        )
        assertEquals(LocalDate.of(2011, 4, 27), comic.releaseDate)
    }
}
