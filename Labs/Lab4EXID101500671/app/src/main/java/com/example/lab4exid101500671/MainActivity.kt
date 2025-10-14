package com.example.lab4exid101500671

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab4exid101500671.ui.theme.Lab4EXID101500671Theme
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import androidx.compose.runtime.collectAsState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab4EXID101500671Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        ItemForm()
                        ItemList()
                    }
                }
            }
        }
    }
}

@Composable
fun ItemForm() {
    var name by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf(0) }
    val db = ItemDatabase.getDatabase(LocalContext.current)
    val scope = rememberCoroutineScope()

    Column {
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Item name") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = quantity.toString(),
            onValueChange = { newVal ->
                val v = newVal.filter { it.isDigit() }
                quantity = if (v.isNotEmpty()) v.toInt() else 0
            },
            label = { Text("Quantity") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                scope.launch {
                    if (name.isNotEmpty()) {
                        db.itemDao().insert(Item(name = name, quantity = quantity))
                        name = ""
                        quantity = 0
                    }
                }
            }
        ) { Text("Add Item") }
    }
}

@Composable
fun ItemRow(item: Item) {
    val db = ItemDatabase.getDatabase(LocalContext.current)
    val scope = rememberCoroutineScope()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                scope.launch {
                    db.itemDao().delete(item)
                }
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text(text = item.name)
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = item.quantity.toString())
    }
}

class ItemViewModel(private val db: ItemDatabase) : ViewModel() {
    val allItems: StateFlow<List<Item>> = db.itemDao().getAllItems()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}

@Composable
fun ItemList(viewModel: ItemViewModel = ItemViewModel(ItemDatabase.getDatabase(LocalContext.current))) {
    val items by viewModel.allItems.collectAsState()

    LazyColumn {
        items(items) { item ->
            ItemRow(item)
        }
    }
}