package com.example.data.repository

import com.example.data.database.FaithConnectDatabase
import com.example.data.model.AttendanceRecord
import com.example.data.model.ChurchEvent
import com.example.data.model.Department
import com.example.data.model.Member
import com.example.data.model.MemberAttendanceRecord
import com.example.data.model.PastoralNote
import kotlinx.coroutines.flow.Flow

class ChurchRepository(private val database: FaithConnectDatabase) {
    private val memberDao = database.memberDao()
    private val departmentDao = database.departmentDao()
    private val attendanceDao = database.attendanceDao()
    private val eventDao = database.eventDao()
    private val noteDao = database.pastoralNoteDao()

    // Member flows
    val allMembers: Flow<List<Member>> = memberDao.getAllMembers()
    val activeMembers: Flow<List<Member>> = memberDao.getActiveMembers()
    val memberCount: Flow<Int> = memberDao.getMemberCount()

    fun getMemberById(id: Long): Flow<Member?> = memberDao.getMemberById(id)
    suspend fun getMemberByIdDirect(id: Long): Member? = memberDao.getMemberByIdDirect(id)
    fun getHouseholdMembers(householdName: String): Flow<List<Member>> = memberDao.getHouseholdMembers(householdName)

    suspend fun insertMember(member: Member): Long = memberDao.insertMember(member)
    suspend fun updateMember(member: Member) = memberDao.updateMember(member)
    suspend fun deleteMember(member: Member) = memberDao.deleteMember(member)

    // Department flows
    val allDepartments: Flow<List<Department>> = departmentDao.getAllDepartments()
    fun getDepartmentById(id: Long): Flow<Department?> = departmentDao.getDepartmentById(id)
    suspend fun insertDepartment(department: Department): Long = departmentDao.insertDepartment(department)
    suspend fun updateDepartment(department: Department) = departmentDao.updateDepartment(department)
    suspend fun deleteDepartment(department: Department) = departmentDao.deleteDepartment(department)

    // Attendance flows
    val allAttendanceRecords: Flow<List<AttendanceRecord>> = attendanceDao.getAllAttendanceRecords()
    val recentAttendance: Flow<List<AttendanceRecord>> = attendanceDao.getRecentAttendance()

    fun getMemberAttendance(recordId: Long): Flow<List<MemberAttendanceRecord>> =
        attendanceDao.getMemberAttendanceForRecord(recordId)

    fun getAttendanceForMember(memberId: Long): Flow<List<MemberAttendanceRecord>> =
        attendanceDao.getAttendanceForMember(memberId)

    suspend fun saveAttendanceRecord(
        record: AttendanceRecord,
        memberChecklist: List<MemberAttendanceRecord> = emptyList()
    ): Long {
        val recordId = attendanceDao.insertRecord(record)
        if (memberChecklist.isNotEmpty()) {
            attendanceDao.clearMemberAttendanceForRecord(recordId)
            val mapped = memberChecklist.map { it.copy(attendanceRecordId = recordId) }
            attendanceDao.insertMemberAttendance(mapped)
        }
        return recordId
    }

    // Events flows
    val allEvents: Flow<List<ChurchEvent>> = eventDao.getAllEvents()
    fun getUpcomingEvents(currentDate: String): Flow<List<ChurchEvent>> = eventDao.getUpcomingEvents(currentDate)
    suspend fun insertEvent(event: ChurchEvent): Long = eventDao.insertEvent(event)
    suspend fun updateEvent(event: ChurchEvent) = eventDao.updateEvent(event)
    suspend fun deleteEvent(event: ChurchEvent) = eventDao.deleteEvent(event)

    // Pastoral Notes
    fun getNotesForMember(memberId: Long): Flow<List<PastoralNote>> = noteDao.getNotesForMember(memberId)
    suspend fun insertPastoralNote(note: PastoralNote): Long = noteDao.insertNote(note)
    suspend fun deletePastoralNote(note: PastoralNote) = noteDao.deleteNote(note)
}
