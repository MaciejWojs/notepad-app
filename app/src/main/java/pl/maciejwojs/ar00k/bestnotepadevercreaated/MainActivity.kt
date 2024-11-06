package pl.maciejwojs.ar00k.bestnotepadevercreaated

import CreateNotePage
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.launch
import pl.maciejwojs.ar00k.bestnotepadevercreaated.db.Note
import pl.maciejwojs.ar00k.bestnotepadevercreaated.db.Tag
import pl.maciejwojs.ar00k.bestnotepadevercreaated.pages.EditNotePage
import pl.maciejwojs.ar00k.bestnotepadevercreaated.pages.HamburgerPage
import pl.maciejwojs.ar00k.bestnotepadevercreaated.pages.MainPage
import pl.maciejwojs.ar00k.bestnotepadevercreaated.pages.NotesListPage
import pl.maciejwojs.ar00k.bestnotepadevercreaated.pages.NotesWithTagPage
import pl.maciejwojs.ar00k.bestnotepadevercreaated.pages.SettingsPage
import pl.maciejwojs.ar00k.bestnotepadevercreaated.pages.TestPage
import pl.maciejwojs.ar00k.bestnotepadevercreaated.ui.theme.BestNotepadEverCreatedTheme

/**
 * The main activity of the application BestNotepadEverCreated.
 *
 * This activity extends [ComponentActivity] and serves as the primary entry point for the application.
 * It uses Jetpack Compose for the UI and displays Toast messages during lifecycle changes.
 */
class MainActivity : ComponentActivity() {

    /**
     * Called when the activity is first created.
     *
     * This method sets up the UI for the activity using Jetpack Compose and initializes database interactions
     * and ViewModels required by the application.
     *
     * @param savedInstanceState A bundle containing the previously saved state of the activity.
     * @see ComponentActivity.onCreate
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize DAO to interact with the database
        val dao: NotesDao = NotesDatabase.getInstance(this).dao

        enableEdgeToEdge()

        // Launch coroutine to set up default relations and tags in the database if not already present
        lifecycleScope.launch {
            if (dao.isAddingRelations() == 0) {
                dao.insertRelation()
            }
            if (dao.getTagsCount() == 0) {
                dao.insertTag(tag = Tag("Zakupy"))
                dao.insertTag(tag = Tag("Szkoła"))
            }
        }

        // Initialize ViewModels with a custom factory
        val notesViewModel by viewModels<NotesViewModel>(factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return NotesViewModel(dao) as T
                }
            }
        })
        val tagsViewModel by viewModels<TagsViewModel>(factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return TagsViewModel(dao) as T
                }
            }
        })
        val notesWithTagPageViewModel by viewModels<NotesTagsCrossRefViewModel>(factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return NotesTagsCrossRefViewModel(dao) as T
                }
            }
        })

        // Set up the Jetpack Compose UI
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "MainPage") {
                composable("MainPage") {
                    MainPage(navController,
                        viewModel = notesViewModel,
                        navigateToEditNotePage = { note ->
                            navController.currentBackStackEntry?.savedStateHandle?.set("note", note)
                            navController.navigate("EditNotePage")
                        })
                }
                composable("SettingsPage") {
                    SettingsPage(navController)
                }
                composable("NotesListPage") {
                    NotesListPage(navController, viewModel = notesViewModel)
                }
                composable("TestPage") {
                    TestPage(navController, viewModel = notesViewModel, dao = dao)
                }
                composable("HamburgerPage") {
                    HamburgerPage(
                        navController, tagsViewModel,
                        onCreate = { tagName ->
                            tagsViewModel.viewModelScope.launch {
                                dao.insertTag(
                                    Tag(tagName)
                                )
                            }
                        },
                        onDelete = { tag ->
                            tagsViewModel.viewModelScope.launch {
                                dao.deleteTag(tag)
                            }
                        },
                        onEdit = {tag, name->
                            tagsViewModel.viewModelScope.launch {
                                dao.updateTag(id=tag.tagID, name = name)
                            }
                        }
                    )
                }
                composable("CreateNotePage") {

                    CreateNotePage(
                        navigator = navController,
                        onCreate = { title, content ->
                            notesViewModel.viewModelScope.launch {
                                dao.insertNote(Note(title, content))
                            }
                        },
                        tags = tagsViewModel.state.value.tags,
                    )
                }
                composable(
                    "NotesWithTagPage/{tagID}", arguments = listOf(navArgument("tagID") {
                        type = NavType.IntType; nullable = false
                    })
                ) { backStackEntry ->
                    val tagID = backStackEntry.arguments?.getInt("tagID")
                        ?: error("Required argument 'tagID' is missing")
                    NotesWithTagPage(navigator = navController,
                        viewModel = notesWithTagPageViewModel,
                        tagID = tagID,
                        navigateToEditNotePage = { note ->
                            navController.currentBackStackEntry?.savedStateHandle?.set("note", note)
                            navController.navigate("EditNotePage")
                        })
                }
                composable("EditNotePage") {
                    val note =
                        navController.previousBackStackEntry?.savedStateHandle?.get<Note>("note")
                    if (note != null) {
                        LaunchedEffect(note.noteID) {
                            notesWithTagPageViewModel.loadTagsByNote(note.noteID)
                        }

                        val currentTags =
                            notesWithTagPageViewModel.state.value.tagsWithNote.flatMap { it.tags }

                        // Ensure that the tags are loaded before showing the EditNotePage
                        EditNotePage(
                            navigator = navController,
                            onEdit = { title, content ->
                                notesViewModel.viewModelScope.launch {
                                    dao.updateNote(note.noteID, title, content)
                                }
                            },
                            note = note,
                            tags = tagsViewModel.state.value.tags,
                            currentNoteTags = currentTags // This will now be populated correctly
                        )
                    }
                }
            }
        }
    }
}

/**
 * A composable function that displays the title of a note.
 *
 * This function displays the title text of a note with a specified font weight.
 *
 * @param noteTitle The title of the note to be displayed.
 * @param modifier [Modifier] applied to the composable layout. Default is an empty modifier.
 * @param weight [FontWeight] applied to the font style of the note title text. Default is FontWeight(900).
 */
@Composable
fun CreateNoteTitle(
    noteTitle: String, modifier: Modifier = Modifier, weight: FontWeight? = FontWeight(900)
) {
    Text(
        text = noteTitle, modifier = modifier, fontWeight = weight
    )
}
