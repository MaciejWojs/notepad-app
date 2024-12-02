/**
 * @file HamburgerPage.kt
 * @brief Plik odpowiadający za zarządzanie tagami i nawigację do odpowiednich stron
 *
 * W tym pliku znajduje się implementacja strony, na której użytkownik może zarządzać tagami.
 * Umożliwia tworzenie nowych tagów, edytowanie istniejących, usuwanie ich oraz przejście do listy notatek przypisanych do tagów.
 */
package pl.maciejwojs.ar00k.bestnotepadevercreaated.pages

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pl.maciejwojs.ar00k.bestnotepadevercreaated.TagsEvent
import pl.maciejwojs.ar00k.bestnotepadevercreaated.TagsViewModel
import pl.maciejwojs.ar00k.bestnotepadevercreaated.content.GenerateIconButton
import pl.maciejwojs.ar00k.bestnotepadevercreaated.db.Tag
import pl.maciejwojs.ar00k.bestnotepadevercreaated.ui.theme.BestNotepadEverCreatedTheme

/**
 * Strona zarządzania tagami.
 *
 * @param navigator Kontroler nawigacji.
 * @param viewModel Model widoku tagów.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HamburgerPage(
    navigator: NavController,
    viewModel: TagsViewModel,
//    onCreate: (String) -> Unit,
//    onDelete: (Tag) -> Unit,
//    onEdit: (Tag, String) -> Unit,
//    onTag: (TagsEvent) -> Unit,
) {
    val state = viewModel.state.collectAsState().value
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var tagName by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    var selectedTag by remember { mutableStateOf<Tag?>(null) }

    var showEditDialog by remember { mutableStateOf(false) }
    var editTagName by remember { mutableStateOf("") }

    BestNotepadEverCreatedTheme {
        Scaffold(modifier = Modifier.fillMaxSize(), floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showDialog = true
//                        navigator.navigate("CreateTagPage")
                },
                containerColor = MaterialTheme.colorScheme.onSecondary,
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(20.dp),
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add tag")
            }
        }) { innerPadding ->

            if (showBottomSheet && selectedTag != null) {
                ModalBottomSheet(
                    onDismissRequest = { showBottomSheet = false },
                    sheetState = sheetState,
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally, // Center all rows horizontally
                    ) {
                        Button(onClick = {
//                            onDelete(selectedTag!!)
                            viewModel.onEvent(TagsEvent.DeleteTag(selectedTag!!))
                            selectedTag = null
                            showBottomSheet = false
                        }) {
                            Icon(Icons.Default.Delete, "Delete tag")
                            Text(text = "Delete tag")
                        }
                        Spacer(Modifier.height(20.dp))
                        Button(onClick = {
//                            onEdit(selectedTag!!, "")
                            showEditDialog = true
                            showBottomSheet = false
                        }) {
                            Icon(Icons.Default.Edit, "Delete tag")
                            Text(text = "Edit tag")
                        }
                    }
                }
            }

            if (showDialog) {
                AlertDialog(title = {
                    Text(text = "Create tag")
                }, text = {
                    TextField(
                        value = tagName,
                        onValueChange = { tagName = it },
                        label = { Text("Tag Name") },
                        placeholder = { Text("Enter your tag name here") },
                        singleLine = true,
                        isError = tagName.isEmpty(),
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                    )
                }, onDismissRequest = {
//                    onDismissRequest()
                    tagName = ""
                    showDialog = false
                }, confirmButton = {
                    TextButton(onClick = {
//                        onCreate(tagName)
                        if (tagName.isNotEmpty()) {
                            viewModel.onEvent(TagsEvent.SaveTag(Tag(name = tagName)))
                            showDialog = false
                        } else {
                            Toast.makeText(context, "Tag name cannot be empty", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }) {
                        Text("Confirm")
                    }
                }, dismissButton = {
                    TextButton(onClick = {
                        showDialog = false
                        tagName = ""
                    }) {
                        Text("Dismiss")
                    }
                })
            }

            if (showEditDialog) {
                editTagName = selectedTag!!.name
                AlertDialog(
                    title = {
                        Text(text = "Edit tag")
                    },
                    text = {
                        TextField(
                            value = editTagName,
                            onValueChange = { editTagName = it },
                            label = { Text("Tag Name") },
                            placeholder = { Text("Enter your tag name here") },
                            isError = editTagName.isEmpty(),
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                        )
                    },
                    onDismissRequest = {
                        editTagName = ""
                        showEditDialog = false
                    },
                    confirmButton = {
                        TextButton(onClick = {
//                            onEdit(selectedTag!!, editTagName)
                            if (editTagName.isNotEmpty()) {
                                viewModel.onEvent(TagsEvent.UpdateTag(selectedTag!!.copy(name = editTagName)))
                                tagName = editTagName
                                Log.d("TAG", tagName)
                                selectedTag = null
                                editTagName = ""
                                showEditDialog = false
                            } else {
                                Toast.makeText(
                                    context,
                                    "Tag name cannot be empty",
                                    Toast.LENGTH_SHORT,
                                )
                                    .show()
                            }
                        }) {
                            Text("Confirm")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            showEditDialog = false
                            editTagName = ""
                        }) {
                            Text("Dismiss")
                        }
                    },
                )
            }

            Column(modifier = Modifier.padding(innerPadding)) {
                Row(
                    Modifier
                        .height(50.dp)
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.onSecondary)
                        .padding(horizontal = 8.dp), // 1 Add some padding to the row
                    horizontalArrangement = Arrangement.SpaceBetween, // Arrange items in row
                ) {
                    GenerateIconButton(
                        icon = Icons.AutoMirrored.Filled.ArrowBack,
                        "Back to main screen",
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
                                shape = RoundedCornerShape(25.dp),
                            )
                            .clickable {
                                // TODO search Implementation
                            },
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Search",
                            Modifier.padding(start = 5.dp),
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

                Box(
                    Modifier
                        .padding(horizontal = 50.dp)
                        .fillMaxWidth()
                        .clickable { }
                        .pointerInput(Unit) {
                            detectTapGestures(onTap = {
                                navigator.navigate("TrashPage")
                            })
                        },
                    contentAlignment = Alignment.Center,
                ) {
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically, // Wyrównanie elementów w wierszu
//                        horizontalArrangement = Arrangement.Center,
//                    ) {
//                        Icon(Icons.Default.Delete, contentDescription = "Trash")
//                        Text(modifier = Modifier.padding(vertical = 10.dp), text = "Kosz")
                    Text(modifier = Modifier.padding(vertical = 10.dp), text = "Kosz")
                }
                Spacer(modifier = Modifier.height(25.dp))

                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    items(state.tags, key = { it.tagID }) { tag ->
                        Box(
                            Modifier
                                .padding(horizontal = 50.dp)
                                .fillMaxWidth()
                                .clickable { }
                                .pointerInput(Unit) {
                                    detectTapGestures(onTap = {
                                        navigator.navigate("NotesWithTagPage/${tag.tagID}")
                                    }, onLongPress = {
//                                            onDelete()
                                        showBottomSheet = true
                                        selectedTag = tag
                                    })
                                },
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(modifier = Modifier.padding(vertical = 10.dp), text = tag.name)
                        }
                        Spacer(modifier = Modifier.height(25.dp))
                    }
                }
            }
        }
    }
}
