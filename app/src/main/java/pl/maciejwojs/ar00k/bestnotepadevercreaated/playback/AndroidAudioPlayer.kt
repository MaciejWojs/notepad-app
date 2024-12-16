package pl.maciejwojs.ar00k.bestnotepadevercreaated.playback

import android.content.Context
import android.media.MediaPlayer
import androidx.core.net.toUri
import java.io.File

class AndroidAudioPlayer(
    private val context: Context,
) : AudioPlayer {
    private var player: MediaPlayer? = null

    override fun play(file: File) {
        MediaPlayer.create(context, file.toUri()).apply {
            player = this
            start()
        }
    }

    override fun stop() {
        player?.apply {
            stop()
            release()
        }
    }
}
