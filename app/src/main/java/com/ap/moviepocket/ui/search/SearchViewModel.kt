package com.ap.moviepocket.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor() : ViewModel() {

    val text: StateFlow<String> = flowOf("Home Fragment")
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), "Home Fragment")
}