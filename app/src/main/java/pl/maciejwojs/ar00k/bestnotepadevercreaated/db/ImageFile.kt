package pl.maciejwojs.ar00k.bestnotepadevercreaated.db

import androidx.room.PrimaryKey

data class ImageFile(
    val name: String,
    val byteArray: ByteArray,
    @PrimaryKey(autoGenerate = true) val imageID: Long = 0,
)
