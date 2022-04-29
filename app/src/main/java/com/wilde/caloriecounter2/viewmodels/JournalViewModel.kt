package com.wilde.caloriecounter2.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wilde.caloriecounter2.data.journal.JournalRepository
import com.wilde.caloriecounter2.data.journal.entities.FullJournalEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class JournalViewModel @Inject internal constructor(
    private val journalRepository: JournalRepository
): ViewModel() {

    val entries: LiveData<List<FullJournalEntry>> = journalRepository.getFullJournalEntries()
}