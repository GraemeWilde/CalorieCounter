package com.wilde.caloriecounter2.data.journal

import androidx.lifecycle.LiveData
import com.wilde.caloriecounter2.data.journal.entities.FullJournalEntry
import com.wilde.caloriecounter2.data.journal.entities.JournalEntry
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JournalRepository @Inject constructor(
    private val journalDao: JournalDao
) {
    fun getFullJournalEntries(): LiveData<List<FullJournalEntry>> {
        return journalDao.getFullJournalEntries()
    }

    fun insertJournalEntry(journalEntry: JournalEntry): Long {
        return journalDao.insertEntries(journalEntry)[0]
    }
}