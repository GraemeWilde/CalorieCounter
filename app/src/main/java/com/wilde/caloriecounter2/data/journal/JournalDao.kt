package com.wilde.caloriecounter2.data.journal

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.wilde.caloriecounter2.data.journal.entities.FullJournalEntry
import com.wilde.caloriecounter2.data.journal.entities.JournalEntry

@Dao
interface JournalDao {
    @Transaction
    @Query("SELECT * FROM journal_entries ORDER BY date DESC")
    fun getFullJournalEntries(): LiveData<List<FullJournalEntry>>

    @Transaction
    @Query("SELECT * FROM journal_entries WHERE id = :id")
    fun getFullJournalEntry(id: Int): LiveData<FullJournalEntry>

//    @Query("SELECT * FROM journal_entries")
//    fun getJournalEntries(): LiveData<List<JournalEntry>>

    @Insert
    fun insertEntries(vararg entries: JournalEntry): List<Long>
}