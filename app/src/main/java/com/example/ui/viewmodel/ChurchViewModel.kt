package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.model.AdminUser
import com.example.data.model.AttendanceRecord
import com.example.data.model.ChurchEvent
import com.example.data.model.Department
import com.example.data.model.Member
import com.example.data.model.MemberAttendanceRecord
import com.example.data.model.PastoralNote
import com.example.data.repository.ChurchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class FaithScreen(val title: String) {
    LOGIN("Admin & Pastoral Login"),
    DASHBOARD("Pastoral Dashboard"),
    MEMBERS("Member Directory"),
    MEMBER_PROFILE("Member Profile"),
    DEPARTMENTS("Ministries & Departments"),
    DEPARTMENT_ROSTER("Ministry Roster"),
    ATTENDANCE("Attendance Tracker"),
    EVENTS("Events Calendar"),
    REPORTS("Church Analytics & Reports")
}

val DemoAdminUsers = listOf(
    AdminUser(
        id = "pastor_david",
        name = "Dr. David Miller",
        title = "Senior Pastor",
        email = "pastor.david@faithconnect.org",
        role = "Senior Pastoral Oversight",
        avatarInitials = "DM",
        avatarColor = 0xFF7C2D42
    ),
    AdminUser(
        id = "pastor_sarah",
        name = "Sarah Jenkins",
        title = "Youth Pastor",
        email = "sarah.j@faithconnect.org",
        role = "Youth & Young Adults Director",
        avatarInitials = "SJ",
        avatarColor = 0xFFD4A017
    ),
    AdminUser(
        id = "admin_vance",
        name = "Robert Vance",
        title = "Church Administrator",
        email = "admin.vance@faithconnect.org",
        role = "Administrative Operations & Finance",
        avatarInitials = "RV",
        avatarColor = 0xFF1E3A5F
    ),
    AdminUser(
        id = "dir_grace",
        name = "Grace Adebayo",
        title = "Worship Director",
        email = "grace.a@faithconnect.org",
        role = "Choir & Sacred Arts Lead",
        avatarInitials = "GA",
        avatarColor = 0xFF3D6B58
    )
)

class ChurchViewModel(private val repository: ChurchRepository) : ViewModel() {

    // Auth & Navigation
    private val _currentUser = MutableStateFlow<AdminUser?>(DemoAdminUsers[0])
    val currentUser: StateFlow<AdminUser?> = _currentUser.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(true)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _currentScreen = MutableStateFlow(FaithScreen.DASHBOARD)
    val currentScreen: StateFlow<FaithScreen> = _currentScreen.asStateFlow()

    // Navigation history stack for back button support
    private val navigationStack = mutableListOf<FaithScreen>()

    // Core Data from Repository
    val allMembers = repository.allMembers.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val allDepartments = repository.allDepartments.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val allAttendanceRecords = repository.allAttendanceRecords.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val allEvents = repository.allEvents.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    // Member Directory Filters & Search
    private val _memberSearchQuery = MutableStateFlow("")
    val memberSearchQuery = _memberSearchQuery.asStateFlow()

    private val _selectedDepartmentFilter = MutableStateFlow("All")
    val selectedDepartmentFilter = _selectedDepartmentFilter.asStateFlow()

    private val _selectedStatusFilter = MutableStateFlow("All")
    val selectedStatusFilter = _selectedStatusFilter.asStateFlow()

    private val _isMemberGridView = MutableStateFlow(true)
    val isMemberGridView = _isMemberGridView.asStateFlow()

    // Filtered Members Flow
    val filteredMembers: StateFlow<List<Member>> = combine(
        allMembers,
        _memberSearchQuery,
        _selectedDepartmentFilter,
        _selectedStatusFilter
    ) { members, query, dept, status ->
        members.filter { member ->
            val matchesQuery = query.isBlank() ||
                member.fullName.contains(query, ignoreCase = true) ||
                member.phone.contains(query, ignoreCase = true) ||
                member.email.contains(query, ignoreCase = true) ||
                member.departments.contains(query, ignoreCase = true)

            val matchesDept = dept == "All" || member.departmentList.any { it.contains(dept, ignoreCase = true) }
            val matchesStatus = status == "All" || member.status.equals(status, ignoreCase = true)

            matchesQuery && matchesDept && matchesStatus
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Selected Member for Profile
    private val _selectedMember = MutableStateFlow<Member?>(null)
    val selectedMember = _selectedMember.asStateFlow()

    private val _selectedMemberNotes = MutableStateFlow<List<PastoralNote>>(emptyList())
    val selectedMemberNotes = _selectedMemberNotes.asStateFlow()

    private val _householdMembers = MutableStateFlow<List<Member>>(emptyList())
    val householdMembers = _householdMembers.asStateFlow()

    // Selected Department for Roster
    private val _selectedDepartment = MutableStateFlow<Department?>(null)
    val selectedDepartment = _selectedDepartment.asStateFlow()

    // Attendance Tracker State
    private val _selectedAttendanceService = MutableStateFlow("Sunday Celebration (10:30 AM)")
    val selectedAttendanceService = _selectedAttendanceService.asStateFlow()

    private val _selectedAttendanceDate = MutableStateFlow("2026-10-18")
    val selectedAttendanceDate = _selectedAttendanceDate.asStateFlow()

    private val _attendanceMode = MutableStateFlow("CHECKLIST") // "CHECKLIST" or "HEADCOUNT"
    val attendanceMode = _attendanceMode.asStateFlow()

    private val _headcountMen = MutableStateFlow(95)
    val headcountMen = _headcountMen.asStateFlow()

    private val _headcountWomen = MutableStateFlow(130)
    val headcountWomen = _headcountWomen.asStateFlow()

    private val _headcountChildren = MutableStateFlow(55)
    val headcountChildren = _headcountChildren.asStateFlow()

    private val _headcountVisitors = MutableStateFlow(22)
    val headcountVisitors = _headcountVisitors.asStateFlow()

    private val _attendanceChecklist = MutableStateFlow<Map<Long, String>>(emptyMap())
    val attendanceChecklist = _attendanceChecklist.asStateFlow()

    private val _attendanceNotes = MutableStateFlow("")
    val attendanceNotes = _attendanceNotes.asStateFlow()

    // Calendar selected date
    private val _selectedCalendarDate = MutableStateFlow("2026-10-18")
    val selectedCalendarDate = _selectedCalendarDate.asStateFlow()

    // Dialog & Sheet states
    private val _isAddEditMemberDialogOpen = MutableStateFlow(false)
    val isAddEditMemberDialogOpen = _isAddEditMemberDialogOpen.asStateFlow()

    private val _editingMember = MutableStateFlow<Member?>(null)
    val editingMember = _editingMember.asStateFlow()

    private val _isAddEventDialogOpen = MutableStateFlow(false)
    val isAddEventDialogOpen = _isAddEventDialogOpen.asStateFlow()

    private val _isAddNoteDialogOpen = MutableStateFlow(false)
    val isAddNoteDialogOpen = _isAddNoteDialogOpen.asStateFlow()

    private val _uiSnackbarMessage = MutableStateFlow<String?>(null)
    val uiSnackbarMessage = _uiSnackbarMessage.asStateFlow()

    // Navigation Actions
    fun navigateTo(screen: FaithScreen) {
        if (_currentScreen.value != screen) {
            navigationStack.add(_currentScreen.value)
            _currentScreen.value = screen
        }
    }

    fun handleBackPress(): Boolean {
        if (navigationStack.isNotEmpty()) {
            _currentScreen.value = navigationStack.removeAt(navigationStack.size - 1)
            return true
        } else if (_currentScreen.value != FaithScreen.DASHBOARD && _currentScreen.value != FaithScreen.LOGIN) {
            _currentScreen.value = FaithScreen.DASHBOARD
            return true
        }
        return false
    }

    fun login(user: AdminUser) {
        _currentUser.value = user
        _isLoggedIn.value = true
        _currentScreen.value = FaithScreen.DASHBOARD
        showSnackbar("Welcome, ${user.title} ${user.name}")
    }

    fun logout() {
        _isLoggedIn.value = false
        _currentScreen.value = FaithScreen.LOGIN
        navigationStack.clear()
        showSnackbar("Logged out successfully")
    }

    fun setMemberSearchQuery(query: String) {
        _memberSearchQuery.value = query
    }

    fun setDepartmentFilter(dept: String) {
        _selectedDepartmentFilter.value = dept
    }

    fun setStatusFilter(status: String) {
        _selectedStatusFilter.value = status
    }

    fun toggleMemberGridView() {
        _isMemberGridView.value = !_isMemberGridView.value
    }

    fun openMemberProfile(member: Member) {
        _selectedMember.value = member
        viewModelScope.launch {
            repository.getNotesForMember(member.id).collect { notes ->
                _selectedMemberNotes.value = notes
            }
        }
        if (member.householdName.isNotBlank()) {
            viewModelScope.launch {
                repository.getHouseholdMembers(member.householdName).collect { family ->
                    _householdMembers.value = family
                }
            }
        } else {
            _householdMembers.value = emptyList()
        }
        navigateTo(FaithScreen.MEMBER_PROFILE)
    }

    fun openDepartmentRoster(department: Department) {
        _selectedDepartment.value = department
        navigateTo(FaithScreen.DEPARTMENT_ROSTER)
    }

    fun openAddMemberDialog(memberToEdit: Member? = null) {
        _editingMember.value = memberToEdit
        _isAddEditMemberDialogOpen.value = true
    }

    fun closeAddMemberDialog() {
        _isAddEditMemberDialogOpen.value = false
        _editingMember.value = null
    }

    fun saveMember(member: Member) {
        viewModelScope.launch {
            if (member.id == 0L) {
                repository.insertMember(member)
                showSnackbar("Member ${member.fullName} added successfully")
            } else {
                repository.updateMember(member)
                if (_selectedMember.value?.id == member.id) {
                    _selectedMember.value = member
                }
                showSnackbar("Member ${member.fullName} updated")
            }
            closeAddMemberDialog()
        }
    }

    fun deleteMember(member: Member) {
        viewModelScope.launch {
            repository.deleteMember(member)
            showSnackbar("Member ${member.fullName} removed")
            if (_selectedMember.value?.id == member.id) {
                _selectedMember.value = null
                handleBackPress()
            }
        }
    }

    fun setAttendanceService(service: String) {
        _selectedAttendanceService.value = service
    }

    fun setAttendanceDate(date: String) {
        _selectedAttendanceDate.value = date
    }

    fun setAttendanceMode(mode: String) {
        _attendanceMode.value = mode
    }

    fun setHeadcount(men: Int, women: Int, children: Int, visitors: Int) {
        _headcountMen.value = men.coerceAtLeast(0)
        _headcountWomen.value = women.coerceAtLeast(0)
        _headcountChildren.value = children.coerceAtLeast(0)
        _headcountVisitors.value = visitors.coerceAtLeast(0)
    }

    fun setMemberAttendanceStatus(memberId: Long, status: String) {
        val current = _attendanceChecklist.value.toMutableMap()
        current[memberId] = status
        _attendanceChecklist.value = current
    }

    fun markAllPresent() {
        val current = mutableMapOf<Long, String>()
        allMembers.value.forEach { member ->
            current[member.id] = "PRESENT"
        }
        _attendanceChecklist.value = current
        showSnackbar("Marked all members present")
    }

    fun saveAttendance() {
        viewModelScope.launch {
            val total = if (_attendanceMode.value == "HEADCOUNT") {
                _headcountMen.value + _headcountWomen.value + _headcountChildren.value + _headcountVisitors.value
            } else {
                _attendanceChecklist.value.count { it.value == "PRESENT" }
            }

            val checklistRecords = _attendanceChecklist.value.map { (memId, status) ->
                MemberAttendanceRecord(
                    attendanceRecordId = 0L,
                    memberId = memId,
                    status = status
                )
            }

            val record = AttendanceRecord(
                serviceName = _selectedAttendanceService.value,
                serviceDate = _selectedAttendanceDate.value,
                entryMode = _attendanceMode.value,
                totalCount = total,
                menCount = _headcountMen.value,
                womenCount = _headcountWomen.value,
                childrenCount = _headcountChildren.value,
                visitorCount = _headcountVisitors.value,
                notes = _attendanceNotes.value
            )

            repository.saveAttendanceRecord(record, checklistRecords)
            showSnackbar("Attendance saved: $total total attendees")
        }
    }

    fun setSelectedCalendarDate(date: String) {
        _selectedCalendarDate.value = date
    }

    fun openAddEventDialog() {
        _isAddEventDialogOpen.value = true
    }

    fun closeAddEventDialog() {
        _isAddEventDialogOpen.value = false
    }

    fun saveEvent(event: ChurchEvent) {
        viewModelScope.launch {
            repository.insertEvent(event)
            showSnackbar("Event '${event.title}' scheduled")
            closeAddEventDialog()
        }
    }

    fun deleteEvent(event: ChurchEvent) {
        viewModelScope.launch {
            repository.deleteEvent(event)
            showSnackbar("Event removed")
        }
    }

    fun openAddNoteDialog() {
        _isAddNoteDialogOpen.value = true
    }

    fun closeAddNoteDialog() {
        _isAddNoteDialogOpen.value = false
    }

    fun savePastoralNote(type: String, content: String) {
        val member = _selectedMember.value ?: return
        val author = _currentUser.value?.let { "${it.title} ${it.name}" } ?: "Pastoral Staff"
        val note = PastoralNote(
            memberId = member.id,
            authorName = author,
            noteType = type,
            content = content,
            date = "2026-10-17"
        )
        viewModelScope.launch {
            repository.insertPastoralNote(note)
            showSnackbar("Pastoral note recorded")
            closeAddNoteDialog()
        }
    }

    fun showSnackbar(message: String) {
        _uiSnackbarMessage.value = message
    }

    fun clearSnackbar() {
        _uiSnackbarMessage.value = null
    }
}

class ChurchViewModelFactory(private val repository: ChurchRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChurchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChurchViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
