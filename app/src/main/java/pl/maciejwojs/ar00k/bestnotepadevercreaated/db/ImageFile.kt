package pl.maciejwojs.ar00k.bestnotepadevercreaated.db

import androidx.room.PrimaryKey

/**
 * Klasa reprezentująca plik obrazu.
 *
 * @property name Nazwa pliku obrazu.
 * @property byteArray Zawartość pliku obrazu w postaci tablicy bajtów.
 * @property imageID Unikalny identyfikator obrazu generowany automatycznie.
 */
data class ImageFile(
    val name: String,
    val byteArray: ByteArray,
    @PrimaryKey(autoGenerate = true) val imageID: Long = 0,
)
