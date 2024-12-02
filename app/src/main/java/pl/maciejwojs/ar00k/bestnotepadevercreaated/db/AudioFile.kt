package pl.maciejwojs.ar00k.bestnotepadevercreaated.db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

/**
 * Klasa reprezentująca plik audio.
 *
 * @property byteArray tablica bajtów reprezentująca zawartość pliku audio
 * @property audioID unikalny identyfikator pliku audio, generowany automatycznie
 */
@Parcelize
@Entity(tableName = "audioFiles")
data class AudioFile(
    val byteArray: ByteArray,
    @PrimaryKey(autoGenerate = true) val audioID: Long = 0,
) : Parcelable
