package pl.maciejwojs.ar00k.bestnotepadevercreaated

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import pl.maciejwojs.ar00k.bestnotepadevercreaated.pages.CreateNotePage
import pl.maciejwojs.ar00k.bestnotepadevercreaated.pages.EditNotePage
import pl.maciejwojs.ar00k.bestnotepadevercreaated.pages.HamburgerPage
import pl.maciejwojs.ar00k.bestnotepadevercreaated.pages.MainPage
import pl.maciejwojs.ar00k.bestnotepadevercreaated.pages.NotesListPage
import pl.maciejwojs.ar00k.bestnotepadevercreaated.pages.NotesWithTagPage
import pl.maciejwojs.ar00k.bestnotepadevercreaated.pages.SettingsPage
import pl.maciejwojs.ar00k.bestnotepadevercreaated.pages.TestPage
import pl.maciejwojs.ar00k.bestnotepadevercreaated.ui.theme.BestNotepadEverCreatedTheme

/**
 * Główna aktywność aplikacji BestNotepadEverCreated.
 *
 * Ta aktywność rozszerza [ComponentActivity] i służy jako główny punkt wejścia do aplikacji.
 * Używa Jetpack Compose do wyświetlania interfejsu użytkownika i wyświetla wiadomości Toast w trakcie zmian cyklu życia.
 */
class MainActivity : ComponentActivity() {

    /**
     * Wywoływana, gdy aktywność jest po raz pierwszy tworzona.
     *
     * Ta metoda ustawia interfejs użytkownika dla aktywności przy użyciu Jetpack Compose.
     *
     * @param savedInstanceState Pakiet zawierający poprzednio zapisany stan aktywności.
     * @see ComponentActivity.onCreate
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dao: NotesDao = NotesDatabase.getInstance(this).dao
        enableEdgeToEdge()
        lifecycleScope.launch {
            if (dao.isAddingRelations() == 0) {
                dao.insertRelation()
            }
            if (dao.getTagsCount() == 0) {
                dao.insertTag(tag = Tag("Zakupy"))
                dao.insertTag(tag = Tag("Szkoła"))
            }
        }
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


        setContent {
            val bundle = Bundle()
            val state by notesViewModel.state.collectAsState()
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
                    HamburgerPage(navController, tagsViewModel)
                }
                composable("CreateNotePage") {
                    CreateNotePage(navigator = navController, onCreate = { title, content ->
                        // Insert note creation logic here, like saving the note in a database
                        notesViewModel.viewModelScope.launch {
                            dao.insertNote(Note(title, content))
                        }
                    })
                }


                composable(
                    "NotesWithTagPage/{tagID}", arguments = listOf(navArgument("tagID") {
                        type = NavType.IntType; nullable = false
                    })
                ) { backStackEntry ->
                    val tagID = backStackEntry.arguments?.getInt("tagID")
                        ?: error("Required argument 'tagID' is missing")

                    // Pass tagID to NotesWithTagPage
                    NotesWithTagPage(
                        navigator = navController,
                        viewModel = notesWithTagPageViewModel,
                        tagID = tagID,
                        navigateToEditNotePage = { note ->
                            navController.currentBackStackEntry?.savedStateHandle?.set("note", note)
                            navController.navigate("EditNotePage")
                        }
                    )
                }
                composable("EditNotePage") {
                    // Retrieve the Note object from the previous back stack entry's savedStateHandle
                    val note =
                        navController.previousBackStackEntry?.savedStateHandle?.get<Note>("note")

                    // Ensure the note is non-null before passing it to EditNotePage
                    if (note != null) {
                        EditNotePage(
                            navigator = navController,
                            onEdit = { title, content ->
                                notesViewModel.viewModelScope.launch {
                                    dao.updateNote(note.noteID, title, content)
                                }
                            }, note = note
                        )
                    }
                }
            }
        }
//            DestinationsNavHost(navGraph = RootNavGraph)
    }


    /**
     * Wywoływana, gdy aktywność staje się widoczna dla użytkownika.
     *
     * Wyświetla wiadomość Toast, aby wskazać, że aktywność jest w stanie onStart().
     *
     * @see ComponentActivity.onStart
     */
    override fun onStart() {
        super.onStart()
        Toast.makeText(applicationContext, "onStart()", Toast.LENGTH_LONG).show()
    }

}

/**
 * Funkcja kompozytowa, która wyświetla wiadomość powitalną.
 *
 * Ta funkcja wyświetla tekst powitalny z użyciem podanej nazwy.
 *
 * @param name Nazwa, która ma być wyświetlana w wiadomości powitalnej.
 * @param modifier [Modifier] stosowany do układu kompozytowego. Domyślnie jest to pusty modifier.
 */
@Composable
fun Greeting(
    name: String, modifier: Modifier = Modifier, weight: FontWeight? = FontWeight(400)
) {
    Text(
        text = "$name!", modifier = modifier, fontWeight = weight

    )
}

@Composable
fun CreateNoteTitle(
    noteTitle: String, modifier: Modifier = Modifier, weight: FontWeight? = FontWeight(900)
) {
    Text(
        text = noteTitle, modifier = modifier, fontWeight = weight

    )
}

/**
 * Funkcja podglądu dla kompozytu [Greeting].
 *
 * Wyświetla podgląd wiadomości powitalnej z "Android" jako nazwą.
 */
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BestNotepadEverCreatedTheme {
        Greeting("Android")
    }
}