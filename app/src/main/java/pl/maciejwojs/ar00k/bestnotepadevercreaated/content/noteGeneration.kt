package pl.maciejwojs.ar00k.bestnotepadevercreaated.content.generation

import android.widget.Toast
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import pl.maciejwojs.ar00k.bestnotepadevercreaated.CreateNoteTitle

@Composable
fun GenerateNote(
    modifier: Modifier = Modifier,
    weight: FontWeight?,
    creationDate: String,
    modificationDate: String = ""
) {
    val context = LocalContext.current
    var clicked = false
    Card(
        Modifier
            .shadow(elevation = 20.dp, spotColor = MaterialTheme.colorScheme.onSurface)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        Toast
                            .makeText(context, "Tap", Toast.LENGTH_SHORT)
                            .show()
                    },
                    onLongPress = {
                        Toast
                            .makeText(context, "Long Press", Toast.LENGTH_SHORT)
                            .show()
                    }
                )
            },
    ) {
        Box(
            modifier = modifier
        )

        {
            //Note title and Content
            Column {
                CreateNoteTitle(
                    noteTitle = "Witaj android",
                    weight = weight,
                    modifier = Modifier
                        .padding(bottom = 20.dp)
                )
                Text(
                    text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla id nisl eget.",
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Column(modifier = Modifier.align(Alignment.BottomStart))
            {
                Text(
                    text = "#" // future database tag
                )
            }
            //Note creation and modification date
            Column(
                modifier = Modifier.align(Alignment.BottomEnd)
            ) {

                Text(
                    text = modificationDate, //przyszłe pociagniecie z bazy
                    modifier = Modifier
                )
                Text(
                    text = creationDate, //przyszłe pociagniecie z bazy
                    modifier = Modifier
                )
            }
        }
    }
}