package pl.maciejwojs.ar00k.bestnotepadevercreaated.pages

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pl.maciejwojs.ar00k.bestnotepadevercreaated.NotesDao
import pl.maciejwojs.ar00k.bestnotepadevercreaated.NotesEvent
import pl.maciejwojs.ar00k.bestnotepadevercreaated.NotesViewModel
import pl.maciejwojs.ar00k.bestnotepadevercreaated.content.GenerateIconButton
import pl.maciejwojs.ar00k.bestnotepadevercreaated.content.GenerateNote2
import pl.maciejwojs.ar00k.bestnotepadevercreaated.ui.theme.BestNotepadEverCreatedTheme

@Composable
fun TestPage(navigator: NavController, viewModel: NotesViewModel, dao: NotesDao) {
    val context = LocalContext.current
    val state = viewModel.state.collectAsState().value
//    val noteList = state.notes
//        ?: emptyList() // Ensuring a non-null list
    BestNotepadEverCreatedTheme {

        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

            Column(modifier = Modifier.padding(innerPadding)) {
                Row(
                    Modifier
                        .height(50.dp)
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.onSecondary)
                        .padding(horizontal = 8.dp), //1 Add some padding to the row
                    horizontalArrangement = Arrangement.SpaceBetween // Arrange items in row
                ) {
//                            Row(
//                                Modifier
//                                    .fillMaxWidth()
//                            ) {
                    GenerateIconButton(
                        icon = Icons.AutoMirrored.Filled.ArrowBack, "Back to main screen"
                    ) {
                        if (!navigator.popBackStack()) {
                            // Przejdź do głównego ekranu lub innego widoku początkowego
                            navigator.navigate("MainPage")
                        }

                    }
                    Row(
                        Modifier
                            .clip(RoundedCornerShape(25.dp))
                            .align(Alignment.CenterVertically)
                            .fillMaxHeight(0.55f)
                            .fillMaxWidth(0.75f)
//                                    .background(Color.Red)
                            .border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.outline,
                                shape = RoundedCornerShape(25.dp)
                            )
                            .clickable {
//                                Toast
//                                    .makeText(
//                                        applicationContext,
//                                        "Clicked ${++searchCounter} ${if (searchCounter < 2) "time" else "times"}",
//                                        Toast.LENGTH_LONG
//                                    )
//                                    .show()
                            },
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = "Search", Modifier.padding(start = 5.dp)
//                                            .align(alignment = Alignment.CenterVertically)
                        )

                        GenerateIconButton(Icons.Default.Search, "Search menu") {}
                    }

                    GenerateIconButton(
                        icon = Icons.Filled.Settings,
                        "Settings",
                        transparent = true,
                        isEnabled = false,
                    ) {}
                }

                Spacer(modifier = Modifier.height(20.dp))
                // List of Notes
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(state.notes, key = { it.noteID }) { singleNote ->
                        GenerateNote2(note = singleNote, onDelete = {
                            Log.d("TestPage", "Deleting note: ${singleNote.title}")
                            viewModel.onEvent(NotesEvent.DeleteNote(singleNote))
                            Toast.makeText(
                                context, "${singleNote.title} deleted", Toast.LENGTH_SHORT
                            ).show()

                            // Log the current state after deletion
                            Log.d(
                                "TestPage",
                                "Current notes after deletion: ${state.notes.map { it.title }}"
                            )
                        })
                    }
                }
            }
        }
    }
}
