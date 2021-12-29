package ch.qscqlmpa.magicclipboard

import ch.qscqlmpa.magicclipboard.clipboard.McbItem
import ch.qscqlmpa.magicclipboard.clipboard.McbItemId
import java.time.LocalDateTime
import java.util.*

val idItem1 = McbItemId(UUID.fromString("9d0823b0-fbd4-4b05-a5fe-8d5060eef811"))
val idItem2 = McbItemId(UUID.fromString("3d52bbc1-a5aa-4e71-b5fa-3c3b537a1943"))
val idItem3 = McbItemId(UUID.fromString("838b3e00-5ffa-4aeb-973c-a31a59a1559e"))
val idItem4 = McbItemId(UUID.fromString("f2502a9d-4759-44ae-922a-c45db6803993"))
val idItem5 = McbItemId(UUID.fromString("858f97ff-4d3e-4512-848a-1bfcdad95bb0"))
val idItem6 = McbItemId(UUID.fromString("f0f1871f-83f1-432d-a802-6617d182e6d6"))
val idItem7 = McbItemId(UUID.fromString("d906c0c4-b476-4c8a-ac5b-2ef6c4c2ba60"))
val idItem8 = McbItemId(UUID.fromString("c3b2e14f-e713-475c-9e3c-1ca4b482fc43"))
val idItem9 = McbItemId(UUID.fromString("4c2cc3e2-4278-41c7-a515-f19db4534d2d"))
val idItem10 = McbItemId(UUID.fromString("d4d72ee9-38bf-4c7b-a71f-a3b1a3a5f3f2"))
val idItem11 = McbItemId(UUID.fromString("fda7ca12-b85e-475a-b846-f4752b61e80e"))
val idItem12 = McbItemId(UUID.fromString("869c15c5-6185-4790-9d57-3217229ef5a7"))
val idItem13 = McbItemId(UUID.fromString("c786e4e4-99f9-4009-ba0b-7753b47e33dc"))
val idItem14 = McbItemId(UUID.fromString("47c9a1a9-96ae-4472-b1b3-222066aca318"))
val idItem15 = McbItemId(UUID.fromString("a86ebfcf-94d7-4d36-9ce6-123dcfd2ef7a"))
val idItem16 = McbItemId(UUID.fromString("0dd8b20d-b4fc-4c64-8250-87927fa2e133"))
val idItem17 = McbItemId(UUID.fromString("2a1221e8-0a32-4f8e-9098-d000063bb8b7"))
val idItem18 = McbItemId(UUID.fromString("3c1715a3-3e04-4ef5-86fc-25b80017a801"))
val idItem19 = McbItemId(UUID.fromString("41fae70b-7c4d-49da-ac2f-179364f442f6"))
val idItem20 = McbItemId(UUID.fromString("1bc0ffd7-7e8e-49cb-a0c8-cb32b8e8f43d"))

val item1 = McbItem(
    id = idItem1,
    value = "[item 1]",
    creationDate = LocalDateTime.now().minusSeconds(10)
)
val item2 = McbItem(
    id = idItem2,
    value = "[item 2] which is a bit longer",
    creationDate = LocalDateTime.now().minusMinutes(3)
)
val item3 = McbItem(
    id = idItem3,
    value = """
        [item 3] which is a much longeeeeeeeeeeeeeeeeeer item
        bla bidi bla bla blaaaa didi dudu dada
        bla bidi bla bla blaaaa didi dudu dada
        bla bidi bla bla blaaaa didi dudu dada
    """.trimIndent(),
    creationDate = LocalDateTime.of(
        LocalDateTime.now().year,
        LocalDateTime.now().month,
        LocalDateTime.now().dayOfMonth,
        13,
        45,
        24
    )
)
val item4 = McbItem(
    id = idItem4,
    value = "[item 4]",
    creationDate = LocalDateTime.now().minusHours(2)
)
val item5 = McbItem(
    id = idItem5,
    value = "[item 5]",
    creationDate = LocalDateTime.of(
        1989,
        7,
        26,
        19,
        55,
        0
    )
)
val item6 = McbItem(
    id = idItem6,
    value = "[item 6]",
    creationDate = LocalDateTime.now().minusDays(6)
)
val item7 = McbItem(
    id = idItem7,
    value = "[item 7]",
    creationDate = LocalDateTime.now().minusDays(7)
)
val item8 = McbItem(
    id = idItem8,
    value = "[item 8]",
    creationDate = LocalDateTime.now().minusDays(8)
)
val item9 = McbItem(
    id = idItem9,
    value = "[item 9]",
    creationDate = LocalDateTime.now().minusDays(9)
)
val item10 = McbItem(
    id = idItem10,
    value = "[item 10]",
    creationDate = LocalDateTime.now().minusDays(10)
)
val item11 = McbItem(
    id = idItem11,
    value = "[item 11]",
    creationDate = LocalDateTime.now().minusDays(1)
)
val item12 = McbItem(
    id = idItem12,
    value = "[item 12] which is a bit longer",
    creationDate = LocalDateTime.now().minusDays(1)
)
val item13 = McbItem(
    id = idItem13,
    value = "[item 13] which is a much longeeeeeeeeeeeeeeeeeer item\n bla bidi bla bla blaaaa didi dudu dada",
    creationDate = LocalDateTime.now().minusDays(13)
)
val item14 = McbItem(
    id = idItem14,
    value = "[item 14]",
    creationDate = LocalDateTime.now().minusDays(1)
)
val item15 = McbItem(
    id = idItem15,
    value = "[item 15]",
    creationDate = LocalDateTime.now().minusDays(15)
)
val item16 = McbItem(
    id = idItem16,
    value = "[item 16]",
    creationDate = LocalDateTime.now().minusDays(16)
)
val item17 = McbItem(
    id = idItem17,
    value = "[item 17]",
    creationDate = LocalDateTime.now().minusDays(17)
)
val item18 = McbItem(
    id = idItem18,
    value = "[item 18]",
    creationDate = LocalDateTime.now().minusDays(18)
)
val item19 = McbItem(
    id = idItem19,
    value = "[item 19]",
    creationDate = LocalDateTime.now().minusDays(19)
)
val item20 = McbItem(
    id = idItem20,
    value = "[item 20]",
    creationDate = LocalDateTime.now().minusDays(20)
)

// Sorted on creationDate as in the app.
val clipBoardItems = listOf(
    item1,
    item2,
    item3,
    item4,
    item5,
    item6,
    item7,
    item8,
    item9,
    item10,
    item11,
    item12,
    item13,
    item14,
    item15,
    item16,
    item17,
    item18,
    item19,
    item20
)
