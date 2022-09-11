package com.wilde.caloriecounter2.data.journal

import androidx.lifecycle.LiveData
import androidx.room.*
import com.wilde.caloriecounter2.data.journal.entities.FullJournalEntry
import com.wilde.caloriecounter2.data.journal.entities.JournalEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface JournalDao {
    @Transaction
    @Query("SELECT * FROM journal_entries ORDER BY date DESC")
    fun getFullJournalEntries(): Flow<List<FullJournalEntry>>

    @Transaction
    @Query("SELECT * FROM journal_entries WHERE id = :id")
    fun getFullJournalEntry(id: Int): Flow<FullJournalEntry>

//    @Query("SELECT * FROM journal_entries")
//    fun getJournalEntries(): LiveData<List<JournalEntry>>

    /**
     * @param entries vararg of [JournalEntry] to be updated
     * @return A list of longs representing the id of each newly inserted [JournalEntry]
     */
    @Insert
    suspend fun insertJournalEntries(vararg entries: JournalEntry): List<Long>

    /**
     * @param entries vararg of [JournalEntry] to be updated
     * @return Number of entities updated
     */
    @Update
    suspend fun updateJournalEntries(vararg entries: JournalEntry): Int

    @Delete
    suspend fun deleteJournalEntries(vararg entries: JournalEntry)
}