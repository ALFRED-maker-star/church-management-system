package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.AttendanceRecord
import com.example.data.model.ChurchEvent
import com.example.data.model.Department
import com.example.data.model.Member
import com.example.data.model.MemberAttendanceRecord
import com.example.data.model.PastoralNote
import kotlinx.coroutines.flow.Flow

@Dao
interface MemberDao {
    @Query("SELECT * FROM members ORDER BY lastName ASC, firstName ASC")
    fun getAllMembers(): Flow<List<Member>>

    @Query("SELECT * FROM members WHERE id = :id LIMIT 1")
    fun getMemberById(id: Long): Flow<Member?>

    @Query("SELECT * FROM members WHERE id = :id LIMIT 1")
    suspend fun getMemberByIdDirect(id: Long): Member?

    @Query("SELECT * FROM members WHERE status = 'Active Member' OR status = 'Leadership'")
    fun getActiveMembers(): Flow<List<Member>>

    @Query("SELECT COUNT(*) FROM members")
    fun getMemberCount(): Flow<Int>

    @Query("SELECT * FROM members WHERE householdName = :householdName AND householdName != ''")
    fun getHouseholdMembers(householdName: String): Flow<List<Member>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMember(member: Member): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(members: List<Member>)

    @Update
    suspend fun updateMember(member: Member)

    @Delete
    suspend fun deleteMember(member: Member)
}

@Dao
interface DepartmentDao {
    @Query("SELECT * FROM departments ORDER BY name ASC")
    fun getAllDepartments(): Flow<List<Department>>

    @Query("SELECT * FROM departments WHERE id = :id LIMIT 1")
    fun getDepartmentById(id: Long): Flow<Department?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDepartment(department: Department): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(departments: List<Department>)

    @Update
    suspend fun updateDepartment(department: Department)

    @Delete
    suspend fun deleteDepartment(department: Department)
}

@Dao
interface AttendanceDao {
    @Query("SELECT * FROM attendance_records ORDER BY serviceDate DESC, id DESC")
    fun getAllAttendanceRecords(): Flow<List<AttendanceRecord>>

    @Query("SELECT * FROM attendance_records ORDER BY serviceDate DESC LIMIT 10")
    fun getRecentAttendance(): Flow<List<AttendanceRecord>>

    @Query("SELECT * FROM attendance_records WHERE serviceDate = :date AND serviceName = :serviceName LIMIT 1")
    suspend fun findRecord(date: String, serviceName: String): AttendanceRecord?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: AttendanceRecord): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllRecords(records: List<AttendanceRecord>)

    @Query("SELECT * FROM member_attendance WHERE attendanceRecordId = :recordId")
    fun getMemberAttendanceForRecord(recordId: Long): Flow<List<MemberAttendanceRecord>>

    @Query("SELECT * FROM member_attendance WHERE memberId = :memberId")
    fun getAttendanceForMember(memberId: Long): Flow<List<MemberAttendanceRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemberAttendance(attendance: List<MemberAttendanceRecord>)

    @Query("DELETE FROM member_attendance WHERE attendanceRecordId = :recordId")
    suspend fun clearMemberAttendanceForRecord(recordId: Long)
}

@Dao
interface EventDao {
    @Query("SELECT * FROM church_events ORDER BY date ASC, startTime ASC")
    fun getAllEvents(): Flow<List<ChurchEvent>>

    @Query("SELECT * FROM church_events WHERE date >= :currentDate ORDER BY date ASC, startTime ASC LIMIT 5")
    fun getUpcomingEvents(currentDate: String): Flow<List<ChurchEvent>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: ChurchEvent): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(events: List<ChurchEvent>)

    @Update
    suspend fun updateEvent(event: ChurchEvent)

    @Delete
    suspend fun deleteEvent(event: ChurchEvent)
}

@Dao
interface PastoralNoteDao {
    @Query("SELECT * FROM pastoral_notes WHERE memberId = :memberId ORDER BY date DESC, id DESC")
    fun getNotesForMember(memberId: Long): Flow<List<PastoralNote>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: PastoralNote): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(notes: List<PastoralNote>)

    @Delete
    suspend fun deleteNote(note: PastoralNote)
}
