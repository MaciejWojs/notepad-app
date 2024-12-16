package pl.maciejwojs.ar00k.bestnotepadevercreaated.record

import java.io.File

interface AudioRecorder {
    fun start(outputFile: File)

    fun stop()
}
