package com.example.todo

import android.os.Handler
import android.os.Looper
import android.util.Log
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors

class TodoRepository {

    private val executor = Executors.newSingleThreadExecutor()
    private val mainHandler = Handler(Looper.getMainLooper())

    fun fetchTodos(callback: (Result<List<Todo>>) -> Unit) {
        executor.execute {
            try {
                val url = URL("https://jsonplaceholder.typicode.com/todos")
                val conn = (url.openConnection() as HttpURLConnection).apply {
                    requestMethod = "GET"
                    connectTimeout = 10_000
                    readTimeout = 10_000
                }

                val list: List<Todo> = conn.inputStream.use { stream ->
                    val text = BufferedReader(InputStreamReader(stream)).readText()
                    val arr = JSONArray(text)
                    buildList {
                        for (i in 0 until arr.length()) {
                            val o = arr.getJSONObject(i)
                            add(
                                Todo(
                                    userId = o.getInt("userId"),
                                    id = o.getInt("id"),
                                    title = o.getString("title"),
                                    completed = o.getBoolean("completed")
                                )
                            )
                        }
                    }
                }

                mainHandler.post { callback(Result.success(list)) }
            } catch (t: Throwable) {
                Log.e("TodoRepository", "Error fetching todos", t)
                mainHandler.post { callback(Result.failure(t)) }
            }
        }
    }
}
