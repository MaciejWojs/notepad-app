package pl.maciejwojs.ar00k.bestnotepadevercreaated.db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "audioFiles")
data class AudioFile(
//    val name: String,
//    val path: String,
    val byteArray: ByteArray,
    @PrimaryKey(autoGenerate = true) val audioID: Long = 0,
) : Parcelable
