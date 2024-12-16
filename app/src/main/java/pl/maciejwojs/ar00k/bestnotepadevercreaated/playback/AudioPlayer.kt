package pl.maciejwojs.ar00k.bestnotepadevercreaated.playback

import java.io.File

interface AudioPlayer {
    fun play(file: File)

    fun stop()
}
