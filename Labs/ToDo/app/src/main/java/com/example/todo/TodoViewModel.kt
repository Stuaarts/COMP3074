package com.example.todo

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class TodoViewModel(private val repo: TodoRepository = TodoRepository()): ViewModel() {

    data class UiState(
        val loading:Boolean = false,
        val items: List<Todo> = emptyList(),
        val error:String? = null

    )

    var state by mutableStateOf(UiState(loading = true))
        private set


    init {
        refresh()
    }

    fun refresh() {
        state = UiState(loading = true)
        repo.fetchTodos { result ->
            state = result.fold(
                onFailure = { e ->
                    UiState(loading = false, items = emptyList(), error = e.message ?: e.toString())
                },
                onSuccess = { items ->
                    UiState(loading = false, items = items, error = null)
                }
            )
        }
    }
}
