package pl.maciejwojs.ar00k.bestnotepadevercreaated.content

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.maciejwojs.ar00k.bestnotepadevercreaated.CreateNoteTitle
import pl.maciejwojs.ar00k.bestnotepadevercreaated.NotesDao
import pl.maciejwojs.ar00k.bestnotepadevercreaated.NotesViewModel
import pl.maciejwojs.ar00k.bestnotepadevercreaated.db.Note
import pl.maciejwojs.ar00k.bestnotepadevercreaated.settings.roundness

@Composable
fun GenerateNote2(
    modifier: Modifier = Modifier,
    viewModel: NotesViewModel,
    dao: NotesDao,
    note: Note,
    weight: FontWeight? = null,
) {
    val context = LocalContext.current
    val state = viewModel.state.collectAsState().value
    Card(
        Modifier
            .shadow(elevation = 20.dp, spotColor = MaterialTheme.colorScheme.onSurface)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    Toast
                        .makeText(context, "Tap", Toast.LENGTH_SHORT)
                        .show()
                }, onLongPress = {
                    Toast
                        .makeText(context, "Long Press", Toast.LENGTH_SHORT)
                        .show()
                    viewModel.viewModelScope.launch {
                        dao.deleteNote(note = note)
                    }
                })
            },
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(roundness.dp))
//                                        .background(color)
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(roundness.dp)
                )

                .padding(7.dp)
                .scale(0.9f)
                .height(250.dp)
                .fillMaxWidth(0.8f)
                .then(modifier)
        ) {
            //Note title and Content
            Column {
                CreateNoteTitle(
                    noteTitle = note.title,
                    weight = weight,
                    modifier = Modifier.padding(bottom = 20.dp)
                )
                Text(
                    text = if (note.content.split(" ").size > 25)
                        "${getFirst25Words(note.content)} ..." else note.content,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            //Note creation and modification date
            Column(
                modifier = Modifier.align(Alignment.BottomEnd)
            ) {

                Text(
                    text = note.modificationTime, //przyszłe pociagniecie z bazy
                    modifier = Modifier
                )
                Text(
                    text = note.creationTime, //przyszłe pociagniecie z bazy
                    modifier = Modifier
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(50.dp))
}


fun getFirst25Words(text: String): String {
    return text.split(" ")          // Split the text by spaces to get words
        .take(25)           // Take the first 100 words
        .joinToString(" ")    // Join the words back into a single string with spaces
}