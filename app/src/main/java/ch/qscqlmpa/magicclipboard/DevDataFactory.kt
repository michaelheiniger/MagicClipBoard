package ch.qscqlmpa.magicclipboard

import ch.qscqlmpa.magicclipboard.clipboard.McbItem
import ch.qscqlmpa.magicclipboard.clipboard.McbItemId
import java.time.LocalDateTime
import java.util.*

val debugClipBoardItems = listOf(
    McbItem(
        id = McbItemId(UUID.fromString("9d0823b0-fbd4-4b05-a5fe-8d5060eef811")),
        value = "entry 1",
        creationDate = LocalDateTime.now().minusSeconds(10)
    ),
    McbItem(
        id = McbItemId(UUID.fromString("3d52bbc1-a5aa-4e71-b5fa-3c3b537a1943")),
        value = "entry 2 which is a bit longer",
        creationDate = LocalDateTime.now().minusMinutes(3)
    ),
    McbItem(
        id = McbItemId(UUID.fromString("838b3e00-5ffa-4aeb-973c-a31a59a1559e")),
        value = "entry 3 which is a much longeeeeeeeeeeeeeeeeeer entry\n bla bidi bla bla blaaaa didi dudu dada\n bla bidi bla bla blaaaa didi dudu dada\n bla bidi bla bla blaaaa didi dudu dada",
        creationDate = LocalDateTime.of(2021, 1, 12, 16, 15)
    ),
    McbItem(
        id = McbItemId(UUID.fromString("f2502a9d-4759-44ae-922a-c45db6803993")),
        value = "entry 4",
        creationDate = LocalDateTime.now().minusHours(2)
    ),
    McbItem(
        id = McbItemId(UUID.fromString("858f97ff-4d3e-4512-848a-1bfcdad95bb0")),
        value = "entry 5",
        creationDate = LocalDateTime.of(2021, 1, 11, 16, 15)
    ),
    McbItem(
        id = McbItemId(UUID.fromString("f0f1871f-83f1-432d-a802-6617d182e6d6")),
        value = "entry 6",
        creationDate = LocalDateTime.of(2021, 1, 12, 16, 15)
    ),
    McbItem(
        id = McbItemId(UUID.fromString("d906c0c4-b476-4c8a-ac5b-2ef6c4c2ba60")),
        value = "entry 7",
        creationDate = LocalDateTime.of(2021, 1, 12, 16, 15)
    ),
    McbItem(
        id = McbItemId(UUID.fromString("c3b2e14f-e713-475c-9e3c-1ca4b482fc43")),
        value = "entry 8",
        creationDate = LocalDateTime.of(2021, 1, 12, 16, 15)
    ),
    McbItem(
        id = McbItemId(UUID.fromString("4c2cc3e2-4278-41c7-a515-f19db4534d2d")),
        value = "entry 9",
        creationDate = LocalDateTime.of(2021, 1, 12, 16, 15)
    ),
    McbItem(
        id = McbItemId(UUID.fromString("d4d72ee9-38bf-4c7b-a71f-a3b1a3a5f3f2")),
        value = "entry 10",
        creationDate = LocalDateTime.of(2021, 1, 12, 16, 15)
    ),
    McbItem(
        id = McbItemId(UUID.fromString("fda7ca12-b85e-475a-b846-f4752b61e80e")),
        value = "entry 11",
        creationDate = LocalDateTime.now().minusSeconds(10)
    ),
    McbItem(
        id = McbItemId(UUID.fromString("869c15c5-6185-4790-9d57-3217229ef5a7")),
        value = "entry 12 which is a bit longer",
        creationDate = LocalDateTime.now().minusMinutes(3)
    ),
    McbItem(
        id = McbItemId(UUID.fromString("c786e4e4-99f9-4009-ba0b-7753b47e33dc")),
        value = "entry 13 which is a much longeeeeeeeeeeeeeeeeeer entry\n bla bidi bla bla blaaaa didi dudu dada",
        creationDate = LocalDateTime.of(2021, 1, 12, 16, 15)
    ),
    McbItem(
        id = McbItemId(UUID.fromString("47c9a1a9-96ae-4472-b1b3-222066aca318")),
        value = "entry 14",
        creationDate = LocalDateTime.now().minusHours(2)
    ),
    McbItem(
        id = McbItemId(UUID.fromString("a86ebfcf-94d7-4d36-9ce6-123dcfd2ef7a")),
        value = "entry 15",
        creationDate = LocalDateTime.of(2021, 1, 11, 16, 15)
    ),
    McbItem(
        id = McbItemId(UUID.fromString("0dd8b20d-b4fc-4c64-8250-87927fa2e133")),
        value = "entry 17",
        creationDate = LocalDateTime.of(2021, 1, 12, 16, 15)
    ),
    McbItem(
        id = McbItemId(UUID.fromString("2a1221e8-0a32-4f8e-9098-d000063bb8b7")),
        value = "entry 17",
        creationDate = LocalDateTime.of(2021, 1, 12, 16, 15)
    ),
    McbItem(
        id = McbItemId(UUID.fromString("3c1715a3-3e04-4ef5-86fc-25b80017a801")),
        value = "entry 18",
        creationDate = LocalDateTime.of(2021, 1, 12, 16, 15)
    ),
    McbItem(
        id = McbItemId(UUID.fromString("41fae70b-7c4d-49da-ac2f-179364f442f6")),
        value = "entry 19",
        creationDate = LocalDateTime.of(2021, 1, 12, 16, 15)
    ),
    McbItem(
        id = McbItemId(UUID.fromString("1bc0ffd7-7e8e-49cb-a0c8-cb32b8e8f43d")),
        value = "entry 20",
        creationDate = LocalDateTime.of(2021, 1, 12, 16, 15)
    )
)
