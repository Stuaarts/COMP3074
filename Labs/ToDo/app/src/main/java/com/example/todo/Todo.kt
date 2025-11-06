package com.example.todo

data class Todo(
    val userId: Int,
    val id: Int,
    val title: String,
    val completed: Boolean
)
