package nz.memes.xkcdthing.data

import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import nz.memes.xkcdthing.XKCDService
import nz.memes.xkcdthing.data.local.XKCDDao
import nz.memes.xkcdthing.data.network.XKCDResponse
import org.junit.Test
import java.time.LocalDate

class XKCDRepositoryTest {

    @Test
    fun getComicInCache(): Unit = runBlocking {
        val xkcdDao = mockk<XKCDDao>()
        val xkcdService = mockk<XKCDService>()

        val comic = XKCDComic(
            241,
            LocalDate.of(2007, 3, 28),
            "https://imgs.xkcd.com/comics/battle_room.png",
            "Bean actually sabotaged it just to give Dink the excuse to make that joke.",
            "Battle Room",
            "Battle Room",
            "[[A scene is depicted from the Battle Room of the novel Ender's Game by Orson Scott Card.  The men are floating in a room with random cubes.]]\\nDink: Sorry, Ender - seems like there were some system crashes.  The battle's gotta be cut short.\\nEnder: The lasers still work.\\nDink: Yeah, but the enemy's gate is down.\\n{{Title Text: Bean actually sabotaged it just to give Dink the excuse to make that joke.}}" // ktlint-disable max-line-length
        )

        coEvery { xkcdDao.getComic(1) } returns comic

        val xkcdRepository = XKCDRepository(xkcdDao, xkcdService)
        assertEquals(comic, xkcdRepository.getComic(1))
    }

    @Test
    fun getUncachedComicFromNetwork(): Unit = runBlocking {
        val xkcdDao = mockk<XKCDDao>()
        val xkcdService = mockk<XKCDService>()

        coEvery { xkcdDao.getComic(1) } returns null
        coEvery { xkcdService.getComic(1) } returns XKCDResponse(
            3,
            2008,
            17,
            397,
            "https://imgs.xkcd.com/comics/unscientific.png",
            "Last week, we busted the myth that electroweak gauge symmetry is broken by the Higgs mechanism.  We'll also examine the existence of God and whether true love exists.", // ktlint-disable max-line-length
            "Unscientific",
            "Unscientific",
            "TV: Can a ninja catch an arrow? On this episode, we'll find out!\nGuy: Mmm, science.\nGirl: Hey, Mythbusters is entertaining, but it's not science.\nZF: BRAAAIIIINNS ...\nGuy: Zombie Feynman!\nZF: You got a problem with Mythbusters?\nGirl: They fail at basic rigor!\nZF: \"Ideas are tested by experiment.\"  That is the _core_ of science.  Everything else is bookkeeping.\nZF: By teaching people to hold their beliefs up to experiment, Mythbusters is doing more to drag humanity out of the unscientific darkness than a thousand lessons in rigor. Show them some love.\nZF: Anyway, back to zombie stuff.  I hunger for BRAAAAAIIINNS!\nGuy: Try the physics lab next door.\nZF: I said _brains_.  All they've got are string theorists." // ktlint-disable max-line-length
        )
        coEvery { xkcdDao.insertComics(any()) } just Runs

        val xkcdRepository = XKCDRepository(xkcdDao, xkcdService)
        assertEquals(397, xkcdRepository.getComic(1).num)
    }
}
