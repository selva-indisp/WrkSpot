package com.indisp.country

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*
import java.lang.StringBuilder

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

@OptIn(ExperimentalCoroutinesApi::class)
class ExampleUnitTest {
    @OptIn(FlowPreview::class)
    @Test
    fun addition_isCorrect(): Unit = runBlocking {
        val repository = Repository()
        val searchQueryFlow = MutableStateFlow("")
        val filterFlow = MutableStateFlow("")
        val searchResultFlow = combine(searchQueryFlow, filterFlow) { query, filter ->
            println("Query $query, filter $filter")
        }
        launch {
            searchResultFlow.collectLatest {
                println("Result----------")
                println(it)
            }
        }
        launch {
            searchQueryFlow.update { "Hello" }
            delay(4000)
            searchQueryFlow.update { "World" }
        }
    }
}

sealed interface Filter {
    data class Search(val query: String): Filter
    data class Even(val even: Boolean): Filter
    data class Odd(val odd: Boolean): Filter
}

class Repository {

    fun search(filters: Set<Filter>) = flow {
        val filterOptions = StringBuilder()
        var query = ""
        filters.forEach {
            when (it) {
                is Filter.Even -> filterOptions.append("Even")
                is Filter.Odd -> filterOptions.append("Odd")
                is Filter.Search -> {
                    filterOptions.append("Search ${it.query}")
                    query = it.query
                }
            }
        }
        println("Filters -> $filterOptions")

        val result = buildList {
            repeat(5) {
                add("Query $query $it")
            }
        }
        delay(5000)
        emit(result)
    }

}