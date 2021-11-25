package ch.qscqlmpa.magicclipboard

import ch.qscqlmpa.magicclipboard.clipboard.McbItem
import ch.qscqlmpa.magicclipboard.clipboard.McbItemId
import java.time.LocalDateTime
import java.util.*

val debugClipBoardItems = listOf(
    McbItem(
        id = McbItemId(UUID.randomUUID()),
        value = "entry 1",
        creationDate = LocalDateTime.now().minusSeconds(10)
    ),
    McbItem(
        id = McbItemId(UUID.randomUUID()),
        value = "entry 2 which is a bit longer",
        creationDate = LocalDateTime.now().minusMinutes(3)
    ),
    McbItem(
        id = McbItemId(UUID.randomUUID()),
        value = "entry 3 which is a much longeeeeeeeeeeeeeeeeeer entry\n bla bidi bla bla blaaaa didi dudu dada\n bla bidi bla bla blaaaa didi dudu dada\n bla bidi bla bla blaaaa didi dudu dada",
        creationDate = LocalDateTime.of(2021, 1, 12, 16, 15)
    ),
    McbItem(
        id = McbItemId(UUID.randomUUID()),
        value = "entry 4",
        creationDate = LocalDateTime.now().minusHours(2)
    ),
    McbItem(
        id = McbItemId(UUID.randomUUID()),
        value = "entry 5",
        creationDate = LocalDateTime.of(2021, 1, 11, 16, 15)
    ),
    McbItem(
        id = McbItemId(UUID.randomUUID()),
        value = "entry 6",
        creationDate = LocalDateTime.of(2021, 1, 12, 16, 15)
    ),
    McbItem(
        id = McbItemId(UUID.randomUUID()),
        value = "entry 7",
        creationDate = LocalDateTime.of(2021, 1, 12, 16, 15)
    ),
    McbItem(
        id = McbItemId(UUID.randomUUID()),
        value = "entry 8",
        creationDate = LocalDateTime.of(2021, 1, 12, 16, 15)
    ),
    McbItem(
        id = McbItemId(UUID.randomUUID()),
        value = "entry 9",
        creationDate = LocalDateTime.of(2021, 1, 12, 16, 15)
    ),
    McbItem(
        id = McbItemId(UUID.randomUUID()),
        value = "entry 10",
        creationDate = LocalDateTime.of(2021, 1, 12, 16, 15)
    ),
    McbItem(
        id = McbItemId(UUID.randomUUID()),
        value = "entry 11",
        creationDate = LocalDateTime.now().minusSeconds(10)
    ),
    McbItem(
        id = McbItemId(UUID.randomUUID()),
        value = "entry 12 which is a bit longer",
        creationDate = LocalDateTime.now().minusMinutes(3)
    ),
    McbItem(
        id = McbItemId(UUID.randomUUID()),
        value = "entry 13 which is a much longeeeeeeeeeeeeeeeeeer entry\n bla bidi bla bla blaaaa didi dudu dada",
        creationDate = LocalDateTime.of(2021, 1, 12, 16, 15)
    ),
    McbItem(
        id = McbItemId(UUID.randomUUID()),
        value = "entry 14",
        creationDate = LocalDateTime.now().minusHours(2)
    ),
    McbItem(
        id = McbItemId(UUID.randomUUID()),
        value = "entry 15",
        creationDate = LocalDateTime.of(2021, 1, 11, 16, 15)
    ),
    McbItem(
        id = McbItemId(UUID.randomUUID()),
        value = "entry 16",
        creationDate = LocalDateTime.of(2021, 1, 12, 16, 15)
    ),
    McbItem(
        id = McbItemId(UUID.randomUUID()),
        value = "entry 17",
        creationDate = LocalDateTime.of(2021, 1, 12, 16, 15)
    ),
    McbItem(
        id = McbItemId(UUID.randomUUID()),
        value = "entry 18",
        creationDate = LocalDateTime.of(2021, 1, 12, 16, 15)
    ),
    McbItem(
        id = McbItemId(UUID.randomUUID()),
        value = "entry 19",
        creationDate = LocalDateTime.of(2021, 1, 12, 16, 15)
    ),
    McbItem(
        id = McbItemId(UUID.randomUUID()),
        value = "entry 20",
        creationDate = LocalDateTime.of(2021, 1, 12, 16, 15)
    )
)
