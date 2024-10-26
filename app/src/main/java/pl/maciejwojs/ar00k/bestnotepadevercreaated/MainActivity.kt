package pl.maciejwojs.ar00k.bestnotepadevercreaated

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.maciejwojs.ar00k.bestnotepadevercreaated.ui.theme.BestNotepadEverCreatedTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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
        enableEdgeToEdge()
        setContent {
            BestNotepadEverCreatedTheme {
                Scaffold(modifier = Modifier.fillMaxSize(), floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            Toast.makeText(
                                applicationContext,
                                "Tu będzie możliwość dodania notatki",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        containerColor = MaterialTheme.colorScheme.onSecondary,
                        shape = CircleShape,
                        elevation = FloatingActionButtonDefaults.elevation(20.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                    }
                }) { innerPadding ->

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
                            generateIconButton {}
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
                                        Toast
                                            .makeText(
                                                applicationContext,
                                                "Clicked ${++searchCounter} ${if (searchCounter < 2) "time" else "times"}",
                                                Toast.LENGTH_LONG
                                            )
                                            .show()
                                    },
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                    Text(
                                        text = "Search",
                                        Modifier.padding(start = 5.dp)
//                                            .align(alignment = Alignment.CenterVertically)
                                    )

                                    generateIconButton(Icons.Default.Search, "Search menu") {}
                            }
                            generateIconButton(icon = Icons.Default.Settings, "Settings") {}
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        val currentDate = LocalDate.now()
                        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                        val formattedDate = currentDate.format(formatter)
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(
                                    rememberScrollState(1)

                                )

                        ) {
                            for (i in 0..20) {
                                var color: Color
                                if (i % 2 == 0) {
                                    color = MaterialTheme.colorScheme.primaryContainer
                                } else {
                                    color = MaterialTheme.colorScheme.secondaryContainer
                                }
                                val roundness = 10 // zaokraglenie musi być zsynchronizowane
                                GenerateNote(
                                    Modifier
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
                                        .fillMaxWidth(0.8f),
                                    weight = null,
                                    creationDate = formattedDate
                                )

                                Spacer(modifier = Modifier.height(50.dp))
                            }
                        }
                    }
                }
            }
        }
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

    /**
     * Wywoływana przed zniszczeniem aktywności.
     *
     * Wyświetla wiadomość Toast, aby wskazać, że aktywność jest w stanie onDestroy().
     *
     * @see ComponentActivity.onDestroy
     */
    override fun onDestroy() {
        super.onDestroy()
//        Toast.makeText(applicationContext, "onDestroy()", Toast.LENGTH_LONG).show()
    }

    /**
     * Wywoływana, gdy system zamierza wznowić wcześniejszą aktywność.
     *
     * Wyświetla wiadomość Toast, aby wskazać, że aktywność jest w stanie onPause().
     *
     * @see ComponentActivity.onPause
     */
    override fun onPause() {
        super.onPause()
//        Toast.makeText(applicationContext, "onPause()", Toast.LENGTH_LONG).show()
    }

    /**
     * Wywoływana po zatrzymaniu aktywności i ponownym uruchomieniu.
     *
     * Wyświetla wiadomość Toast, aby wskazać, że aktywność jest w stanie onRestart().
     *
     * @see ComponentActivity.onRestart
     */
    override fun onRestart() {
        super.onRestart()
//        Toast.makeText(applicationContext, "onRestart()", Toast.LENGTH_LONG).show()
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
fun Greeting(name: String, modifier: Modifier = Modifier, weight: FontWeight? = FontWeight(400)) {
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

@Composable
fun generateIconButton(
    icon: ImageVector = Icons.Default.Menu,
    message: String = "Menu",
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    IconButton(onClick, modifier) {
        Icon(icon, contentDescription = message)
    }
}

@Composable
fun GenerateNote(
    modifier: Modifier = Modifier,
    weight: FontWeight?,
    creationDate: String,
    modificationDate: String = ""
) {
    Card(
        Modifier.shadow(elevation = 20.dp, spotColor = MaterialTheme.colorScheme.onSurface),
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
                    modifier = Modifier.padding(bottom = 20.dp)
                )
                Text(
                    text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla id nisl eget.",
                    modifier = Modifier.fillMaxWidth()
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