package com.wilde.caloriecounter2.data.journal

import androidx.lifecycle.LiveData
import com.wilde.caloriecounter2.data.journal.entities.FullJournalEntry
import com.wilde.caloriecounter2.data.journal.entities.JournalEntry
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JournalRepository @Inject constructor(
    private val journalDao: JournalDao
) {
    fun getFullJournalEntries(): Flow<List<FullJournalEntry>> {
        return journalDao.getFullJournalEntries()
    }

    suspend fun insertJournalEntry(journalEntry: JournalEntry): Long {
        return journalDao.insertJournalEntries(journalEntry)[0]
    }

    suspend fun updateJournalEntry(journalEntry: JournalEntry): Int {
        return journalDao.updateJournalEntries(journalEntry)
    }

    suspend fun deleteJournalEntry(journalEntry: JournalEntry) {
        journalDao.deleteJournalEntries(journalEntry)
    }
}