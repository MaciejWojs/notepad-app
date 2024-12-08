package pl.maciejwojs.ar00k.bestnotepadevercreaated.db.converters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream

// @ProvidedTypeConverter
class bitmapBytesArray {
    @TypeConverter
    fun fromByteArray(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    @TypeConverter
    fun toByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream)
        return stream.toByteArray()
    }
}
