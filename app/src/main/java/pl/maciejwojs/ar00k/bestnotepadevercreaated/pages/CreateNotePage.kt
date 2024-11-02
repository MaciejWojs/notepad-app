package pl.maciejwojs.ar00k.bestnotepadevercreaated.pages

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pl.maciejwojs.ar00k.bestnotepadevercreaated.content.GenerateIconButton
import pl.maciejwojs.ar00k.bestnotepadevercreaated.content.GenerateNote
import pl.maciejwojs.ar00k.bestnotepadevercreaated.settings.roundness
import pl.maciejwojs.ar00k.bestnotepadevercreaated.ui.theme.BestNotepadEverCreatedTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun CreateNotePage(navigator: NavController) {
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
                    GenerateIconButton(
                        icon = Icons.AutoMirrored.Filled.ArrowBack,
                        "Back to main screen"
                    ) {
                        if (!navigator.popBackStack()) {
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
                                //TODO search Implementation
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
                        icon = Icons.Default.Settings, "Settings", transparent = true
                    ) {}
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
                    for (i in 0..1) {
                        GenerateNote(
                            Modifier
                                .clip(RoundedCornerShape(roundness.dp))
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
