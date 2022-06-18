package com.wilde.caloriecounter2.viewmodels

import androidx.lifecycle.ViewModel
import com.wilde.caloriecounter2.data.journal.entities.JournalEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class JournalEntryTransferViewModel @Inject internal constructor() : ViewModel() {
    private var transferJournalEntry: JournalEntry? = null

    val transferEntry: JournalEntry? get() = transferJournalEntry

    fun setTransferEntry(journalEntry: JournalEntry) {
        transferJournalEntry = journalEntry
    }
}