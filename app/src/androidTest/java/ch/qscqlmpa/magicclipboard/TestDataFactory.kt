package ch.qscqlmpa.magicclipboard

import ch.qscqlmpa.magicclipboard.clipboard.McbItem
import ch.qscqlmpa.magicclipboard.clipboard.McbItemId
import java.time.LocalDateTime
import java.util.UUID

val idItem1 = McbItemId(UUID.fromString("b96da1ee-2f09-4dcc-af07-530d6c5eb7e9"))
val idItem2 = McbItemId(UUID.fromString("49d02886-7a63-4ac3-8eef-42986987dce4"))
val idItem3 = McbItemId(UUID.fromString("6e507b74-ffed-48b9-bf10-fe7575653250"))
val idItem4 = McbItemId(UUID.fromString("530991a0-80cf-447f-b89e-de78fc3283c1"))
val idItem5 = McbItemId(UUID.fromString("00180cfb-8372-4342-ab9a-b0104f55cd0b"))
val idItem6 = McbItemId(UUID.fromString("c0b93b2c-a570-4ba2-bd6d-51b56593a8ff"))
val idItem7 = McbItemId(UUID.fromString("ccc1ec2c-e6cb-4896-b3c9-7fa018b553e8"))
val idItem8 = McbItemId(UUID.fromString("9f34f75d-7391-4acb-a705-0e5f83244061"))
val idItem9 = McbItemId(UUID.fromString("1b880f3f-bc2d-4ae2-ab20-25dc76f7f5ac"))
val idItem10 = McbItemId(UUID.fromString("bd8f92e1-fa8a-4145-9e16-f2d9a3ada2ad"))
val idItem11 = McbItemId(UUID.fromString("e6153811-81fb-4f87-9d6c-493609014a12"))
val idItem12 = McbItemId(UUID.fromString("898340ab-bbf5-4ec3-a012-163c802cff49"))
val idItem13 = McbItemId(UUID.fromString("8ddbf345-1aa6-40f1-9065-66832907a128"))
val idItem14 = McbItemId(UUID.fromString("6d55560e-8603-4756-b27a-b14f4a3d401e"))
val idItem15 = McbItemId(UUID.fromString("16db4c9d-7c2c-4531-836f-059a90e46f8f"))
val idItem16 = McbItemId(UUID.fromString("7808a93a-2c14-4d26-a0e5-88864283092b"))
val idItem17 = McbItemId(UUID.fromString("e414d91b-6a51-4925-9553-92336a3f8bf2"))
val idItem18 = McbItemId(UUID.fromString("2ee17dac-3c1b-4028-8aaa-ea980e75d3af"))
val idItem19 = McbItemId(UUID.fromString("3185ec97-aadf-4763-a408-7f4db2ce1a64"))
val idItem20 = McbItemId(UUID.fromString("453b0081-9b7a-4028-b9b1-6ccd8dd34755"))

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
