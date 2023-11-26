package com.example.app_starwars

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.app_starwars.ui.theme.App_StarWarsTheme
import com.google.android.material.search.SearchBar
import java.time.LocalDate

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App_StarWarsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var query by remember {
                        mutableStateOf("")
                    }

                    CharacterListScreen()
            }
        }
    }
}

data class Character(
    val name: String,
    val movies: List<String>,
    val roles: List<String>,
    val planet: String,
    val releaseDate: LocalDate
)

val characters = listOf(
    Character(
        name = "Luke Skywalker",
        movies = listOf("A New Hope", "The Empire Strikes Back", "Return of the Jedi"),
        roles = listOf("Jedi Knight", "Rebel Alliance Leader"),
        planet = "Tatooine",
        releaseDate = LocalDate.of(1977, 5, 25)
    ),
    Character(
        name = "Darth Vader",
        movies = listOf("A New Hope", "The Empire Strikes Back", "Return of the Jedi"),
        roles = listOf("Sith Lord", "Galactic Empire Leader"),
        planet = "Tatooine",
        releaseDate = LocalDate.of(1977, 5, 25)
    ),
    Character(
        name = "Princess Leia Organa",
        movies = listOf("A New Hope", "The Empire Strikes Back", "Return of the Jedi"),
        roles = listOf("Rebel Alliance Leader"),
        planet = "Alderaan",
        releaseDate = LocalDate.of(1977, 5, 25)
    ),
    Character(
        name = "Han Solo",
        movies = listOf("A New Hope", "The Empire Strikes Back", "Return of the Jedi"),
        roles = listOf("Smuggler", "Rebel Alliance Leader"),
        planet = "Corellia",
        releaseDate = LocalDate.of(1977, 5, 25)
    ),
    Character(
        name = "Chewbacca",
        movies = listOf("A New Hope", "The Empire Strikes Back", "Return of the Jedi"),
        roles = listOf("Wookiee"),
        planet = "Kashyyyk",
        releaseDate = LocalDate.of(1977, 5, 25)
    )
)
@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterListScreen() {
    var characters by remember { mutableStateOf(characters) }
    var searchText by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf("") }
    var isDeleteMode by remember { mutableStateOf(false) }
    var selectedToDelete = remember { mutableStateListOf<Character>() }
    val dialogState = remember { mutableStateOf(false) }
    val filteredCharacters2 = characters.filter { it.roles.contains(selectedRole) }
    var active by remember { mutableStateOf("") }
    var query by remember { mutableStateOf(false)}
    val filteredCharacters = characters.filter {
        it.roles.contains(selectedRole) && it.name.contains(searchText, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Button(
                onClick = {  }
            ) {
                Text("Crear")
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = {
                    if (isDeleteMode && selectedToDelete.isNotEmpty()) {
                        dialogState.value = true
                    } else {
                        isDeleteMode = !isDeleteMode
                        selectedToDelete.clear()
                    }
                }
            ) {
                Text("Eliminar")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Star wars Characters",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(bottom = 20.dp)
                .fillMaxWidth()
                .align(CenterHorizontally)

        )

        LazyColumn {
            items(characters) { character ->
                if (isDeleteMode) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = selectedToDelete.contains(character),
                            onCheckedChange = { isChecked ->
                                if (isChecked) {
                                    selectedToDelete.add(character)
                                } else {
                                    selectedToDelete.remove(character)
                                }
                            },
                            modifier = Modifier.padding(end = 16.dp)
                        )
                        CharacterItem(character = character) {
                            characters = characters.filter { it != character }
                        }
                    }
                } else {
                    CharacterItem(character = character) {
                        characters = characters.filter { it != character }
                    }
                }
            }
        }

        if (dialogState.value) {
            AlertDialog(
                onDismissRequest = { dialogState.value = false },
                title = { Text("Eliminar personajes") },
                text = { Text("¿Estás seguro de eliminar los personajes seleccionados?") },
                confirmButton = {
                    Button(
                        onClick = {
                            characters = characters.filter { !selectedToDelete.contains(it) }
                            selectedToDelete.clear()
                            isDeleteMode = false
                            dialogState.value = false
                        }
                    ) {
                        Text("Eliminar")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            dialogState.value = false
                        }
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}
}

    @DrawableRes
    fun getCharacterImage(name: String): Int {
        return when (name) {
            "Darth Vader" -> R.drawable.darth
            "Han Solo" -> R.drawable.han
            "Luke Skywalker" -> R.drawable.luke
            "Princess Leia Organa" -> R.drawable.leia
            "Chewbacca" -> R.drawable.chewee
            else -> R.drawable.leia
        }
    }

    @Composable
    fun CharacterItem(
        character: MainActivity.Character,
        onDelete: () -> Unit,

    ) {
        val cardColor = when {
            character.roles.contains("Jedi Knight") -> Color.Green
            character.roles.contains("Sith Lord") -> Color.Red
            character.roles.contains("Rebel Alliance Leader") -> Color.Blue
            character.roles.contains("Galactic Empire Leader") -> Color.Gray
            else -> Color.White
        }
        Card(

            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .background(cardColor)
            
            ) {

            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Image(
                    painter = painterResource(id = getCharacterImage(character.name)),
                    contentDescription = "",
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = "Name: ${character.name}",
                    style = MaterialTheme.typography.headlineMedium

                )
                Text(
                    text = "Role: ${character.roles}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Planet: ${character.planet}",
                    style = MaterialTheme.typography.bodySmall
                )

            }
        }
    }

