package com.jmr.medhealth.ui.articles

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun ArticleSearchScreen(/* viewModel: ArticleSearchViewModel */) {
    var searchQuery by remember { mutableStateOf("") }
    // val articles by viewModel.articles.collectAsState()

    Column(Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search PubMed articles...") },
            modifier = Modifier.fillMaxWidth()
        )
        // Button(onClick = { viewModel.search(searchQuery) }) { Text("Search") }

        LazyColumn(modifier = Modifier.weight(1f)) {
            // items(articles) { article ->
            //     ArticleItem(article)
            // }
        }
    }
}