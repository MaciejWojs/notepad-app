package pl.maciejwojs.ar00k.bestnotepadevercreaated.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pl.maciejwojs.ar00k.bestnotepadevercreaated.NotesViewModel
import pl.maciejwojs.ar00k.bestnotepadevercreaated.db.Note

@Composable
fun NotesListPageORG(
    navController: NavController,
    viewModel: NotesViewModel,
) {
    // Observe the ViewModel's state
    val state = viewModel.state.collectAsState().value

    // Scaffold to handle top app bar, FAB, or other layouts if needed
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
    ) {
        // Page title
        Text(
            text = "Notes",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier =
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Display notes in a LazyColumn
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(state.notes) { note ->
                NoteItem(note = note)
            }
        }
    }
}

@Composable
fun NoteItemORG(note: Note) {
    Card(
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .wrapContentHeight(),
    ) {
        Column(
            modifier =
                Modifier
                    .padding(16.dp),
        ) {
            Text(
                text = note.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = note.content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Created: ${note.creationTime}",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray,
            )
        }
    }
}
