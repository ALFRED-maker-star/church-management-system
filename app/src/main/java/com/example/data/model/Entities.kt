package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "members")
data class Member(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val email: String,
    val address: String,
    val dateJoined: String,
    val departments: String, // Comma separated, e.g. "Choir, Hospitality"
    val householdName: String, // e.g. "Miller Household"
    val householdRole: String, // "Head of Household", "Spouse", "Child", "Individual"
    val status: String = "Active Member", // "Active Member", "Visitor", "Inactive", "Leadership"
    val avatarColor: Long = 0xFF7C2D42,
    val gender: String = "Female", // "Male", "Female"
    val ageGroup: String = "Adult", // "Children", "Youth", "Adult", "Senior"
    val notes: String = ""
) {
    val fullName: String
        get() = "$firstName $lastName"

    val initials: String
        get() = "${firstName.firstOrNull() ?: '?'}${lastName.firstOrNull() ?: '?'}"

    val departmentList: List<String>
        get() = if (departments.isBlank()) emptyList() else departments.split(",").map { it.trim() }.filter { it.isNotEmpty() }
}

@Entity(tableName = "departments")
data class Department(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val code: String,
    val leaderName: String,
    val meetingSchedule: String,
    val description: String,
    val colorHex: Long = 0xFF7C2D42,
    val iconName: String = "group"
)

@Entity(tableName = "attendance_records")
data class AttendanceRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val serviceName: String,
    val serviceDate: String,
    val entryMode: String = "HEADCOUNT", // "CHECKLIST" or "HEADCOUNT"
    val totalCount: Int,
    val menCount: Int = 0,
    val womenCount: Int = 0,
    val childrenCount: Int = 0,
    val visitorCount: Int = 0,
    val notes: String = ""
)

@Entity(tableName = "member_attendance")
data class MemberAttendanceRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val attendanceRecordId: Long,
    val memberId: Long,
    val status: String = "PRESENT" // "PRESENT", "ABSENT", "EXCUSED"
)

@Entity(tableName = "church_events")
data class ChurchEvent(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val category: String, // "Service", "Prayer", "Youth", "Outreach", "Leadership", "Fellowship"
    val date: String, // "YYYY-MM-DD"
    val startTime: String,
    val endTime: String,
    val location: String,
    val description: String
)

@Entity(tableName = "pastoral_notes")
data class PastoralNote(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val memberId: Long,
    val authorName: String,
    val noteType: String, // "Pastoral Visit", "Prayer Request", "Counseling", "General Follow-up"
    val content: String,
    val date: String
)

data class AdminUser(
    val id: String,
    val name: String,
    val title: String,
    val email: String,
    val role: String,
    val avatarInitials: String,
    val avatarColor: Long
)
