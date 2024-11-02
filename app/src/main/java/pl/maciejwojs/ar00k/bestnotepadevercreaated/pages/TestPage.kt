package pl.maciejwojs.ar00k.bestnotepadevercreaated.pages

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
import pl.maciejwojs.ar00k.bestnotepadevercreaated.NotesViewModel
import pl.maciejwojs.ar00k.bestnotepadevercreaated.content.GenerateIconButton
import pl.maciejwojs.ar00k.bestnotepadevercreaated.content.GenerateNote2
import pl.maciejwojs.ar00k.bestnotepadevercreaated.ui.theme.BestNotepadEverCreatedTheme

@Composable
fun TestPage(navigator: NavController,  viewModel: NotesViewModel, dao: NotesDao) {
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
                    var searchCounter: Int = 0
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

                Toast.makeText(context, "Liczba notatek: ${state.notes.size}", Toast.LENGTH_SHORT).show()

                // List of Notes
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
//                    .verticalScroll(rememberScrollState())
                ) {
//                item {
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .wrapContentHeight()
//                            .padding(vertical = 25.dp),
//                        horizontalArrangement = Arrangement.Center,
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Text(
//                            "\uD83C\uDF3F  Plants in Cosmetics",
//                        )
//                    }
//                }
//                        log()
                    items(state.notes) { singleNote ->
                        GenerateNote2(
                            dao = dao,
                            viewModel = viewModel,
                            note = singleNote
                        )
                    }
//                for (note in notes) {
//                    GenerateNote2(
//                        Modifier
//                            .clip(RoundedCornerShape(roundness.dp))
////                                        .background(color)
//                            .border(
//                                width = 2.dp,
//                                color = MaterialTheme.colorScheme.outline,
//                                shape = RoundedCornerShape(roundness.dp)
//                            )
//
//                            .padding(7.dp)
//                            .scale(0.9f)
//                            .height(250.dp)
//                            .fillMaxWidth(0.8f),
//                        title = note.title ?: "", // Default to empty if null
//                        content = note.content ?: "", // Default to empty if null
//                        creationDate = note.creationTime
//                    )
//                    Spacer(modifier = Modifier.height(50.dp))
//                }
                }
            }
        }
    }
}
