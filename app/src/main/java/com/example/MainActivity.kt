package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.data.database.FaithConnectDatabase
import com.example.data.repository.ChurchRepository
import com.example.ui.components.AppTopBar
import com.example.ui.components.FaithConnectDrawer
import com.example.ui.screens.AddEditMemberDialog
import com.example.ui.screens.AddEventDialog
import com.example.ui.screens.AttendanceScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.DepartmentRosterScreen
import com.example.ui.screens.DepartmentsScreen
import com.example.ui.screens.EventsCalendarScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.MemberProfileScreen
import com.example.ui.screens.MembersListScreen
import com.example.ui.screens.ReportsScreen
import com.example.ui.theme.FaithConnectTheme
import com.example.ui.viewmodel.ChurchViewModel
import com.example.ui.viewmodel.ChurchViewModelFactory
import com.example.ui.viewmodel.FaithScreen
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: ChurchViewModel by viewModels {
        val database = FaithConnectDatabase.getDatabase(applicationContext, lifecycleScope)
        val repository = ChurchRepository(database)
        ChurchViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            FaithConnectTheme {
                FaithConnectApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun FaithConnectApp(viewModel: ChurchViewModel) {
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val snackbarHostState = remember { SnackbarHostState() }

    // States from ViewModel
    val isLoggedIn by viewModel.isLoggedIn.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()

    val allMembers by viewModel.allMembers.collectAsStateWithLifecycle()
    val filteredMembers by viewModel.filteredMembers.collectAsStateWithLifecycle()
    val allDepartments by viewModel.allDepartments.collectAsStateWithLifecycle()
    val allAttendanceRecords by viewModel.allAttendanceRecords.collectAsStateWithLifecycle()
    val allEvents by viewModel.allEvents.collectAsStateWithLifecycle()

    val memberSearchQuery by viewModel.memberSearchQuery.collectAsStateWithLifecycle()
    val selectedDepartmentFilter by viewModel.selectedDepartmentFilter.collectAsStateWithLifecycle()
    val selectedStatusFilter by viewModel.selectedStatusFilter.collectAsStateWithLifecycle()
    val isMemberGridView by viewModel.isMemberGridView.collectAsStateWithLifecycle()

    val selectedMember by viewModel.selectedMember.collectAsStateWithLifecycle()
    val selectedMemberNotes by viewModel.selectedMemberNotes.collectAsStateWithLifecycle()
    val householdMembers by viewModel.householdMembers.collectAsStateWithLifecycle()
    val selectedDepartment by viewModel.selectedDepartment.collectAsStateWithLifecycle()

    val selectedAttendanceService by viewModel.selectedAttendanceService.collectAsStateWithLifecycle()
    val selectedAttendanceDate by viewModel.selectedAttendanceDate.collectAsStateWithLifecycle()
    val attendanceMode by viewModel.attendanceMode.collectAsStateWithLifecycle()
    val headcountMen by viewModel.headcountMen.collectAsStateWithLifecycle()
    val headcountWomen by viewModel.headcountWomen.collectAsStateWithLifecycle()
    val headcountChildren by viewModel.headcountChildren.collectAsStateWithLifecycle()
    val headcountVisitors by viewModel.headcountVisitors.collectAsStateWithLifecycle()
    val attendanceChecklist by viewModel.attendanceChecklist.collectAsStateWithLifecycle()
    val attendanceNotes by viewModel.attendanceNotes.collectAsStateWithLifecycle()

    val selectedCalendarDate by viewModel.selectedCalendarDate.collectAsStateWithLifecycle()
    val isAddEditMemberDialogOpen by viewModel.isAddEditMemberDialogOpen.collectAsStateWithLifecycle()
    val editingMember by viewModel.editingMember.collectAsStateWithLifecycle()
    val isAddEventDialogOpen by viewModel.isAddEventDialogOpen.collectAsStateWithLifecycle()
    val snackbarMessage by viewModel.uiSnackbarMessage.collectAsStateWithLifecycle()

    // Snackbar effect
    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSnackbar()
        }
    }

    // Android Hardware / Predictive Back Handler
    BackHandler(enabled = true) {
        if (drawerState.isOpen) {
            coroutineScope.launch { drawerState.close() }
        } else {
            val handled = viewModel.handleBackPress()
            if (!handled && currentScreen != FaithScreen.DASHBOARD && currentScreen != FaithScreen.LOGIN) {
                viewModel.navigateTo(FaithScreen.DASHBOARD)
            }
        }
    }

    // If unauthenticated or in Login screen, show Login
    if (!isLoggedIn || currentScreen == FaithScreen.LOGIN) {
        LoginScreen(
            onLoginSuccess = { user ->
                viewModel.login(user)
            }
        )
        return
    }

    // Main Authenticated Admin Experience with Sidebar Drawer
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            FaithConnectDrawer(
                currentScreen = currentScreen,
                currentUser = currentUser,
                memberCount = allMembers.size,
                departmentCount = allDepartments.size,
                onSelectScreen = { screen ->
                    viewModel.navigateTo(screen)
                    coroutineScope.launch { drawerState.close() }
                },
                onSwitchUser = { newUser ->
                    viewModel.login(newUser)
                },
                onLogout = {
                    coroutineScope.launch { drawerState.close() }
                    viewModel.logout()
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                AppTopBar(
                    currentScreen = currentScreen,
                    currentUser = currentUser,
                    onOpenDrawer = {
                        coroutineScope.launch { drawerState.open() }
                    },
                    onNavigateTo = { screen -> viewModel.navigateTo(screen) }
                )
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (currentScreen) {
                    FaithScreen.LOGIN -> {
                        LoginScreen(onLoginSuccess = { viewModel.login(it) })
                    }
                    FaithScreen.DASHBOARD -> {
                        DashboardScreen(
                            currentUser = currentUser,
                            members = allMembers,
                            departments = allDepartments,
                            attendanceRecords = allAttendanceRecords,
                            events = allEvents,
                            onNavigateTo = { screen -> viewModel.navigateTo(screen) },
                            onOpenAddMember = { viewModel.openAddMemberDialog(null) },
                            onOpenAddEvent = { viewModel.openAddEventDialog() }
                        )
                    }
                    FaithScreen.MEMBERS -> {
                        MembersListScreen(
                            members = filteredMembers,
                            searchQuery = memberSearchQuery,
                            onSearchQueryChange = { viewModel.setMemberSearchQuery(it) },
                            selectedDepartmentFilter = selectedDepartmentFilter,
                            onSelectDepartmentFilter = { viewModel.setDepartmentFilter(it) },
                            selectedStatusFilter = selectedStatusFilter,
                            onSelectStatusFilter = { viewModel.setStatusFilter(it) },
                            isGridView = isMemberGridView,
                            onToggleGridView = { viewModel.toggleMemberGridView() },
                            onMemberClick = { member -> viewModel.openMemberProfile(member) },
                            onOpenAddMember = { viewModel.openAddMemberDialog(null) }
                        )
                    }
                    FaithScreen.MEMBER_PROFILE -> {
                        MemberProfileScreen(
                            member = selectedMember,
                            pastoralNotes = selectedMemberNotes,
                            householdMembers = householdMembers,
                            onBack = { viewModel.handleBackPress() },
                            onEditMember = { member -> viewModel.openAddMemberDialog(member) },
                            onDeleteMember = { member -> viewModel.deleteMember(member) },
                            onSelectFamilyMember = { famMember -> viewModel.openMemberProfile(famMember) },
                            onAddNote = { type, content -> viewModel.savePastoralNote(type, content) }
                        )
                    }
                    FaithScreen.DEPARTMENTS -> {
                        DepartmentsScreen(
                            departments = allDepartments,
                            members = allMembers,
                            onSelectDepartment = { dept -> viewModel.openDepartmentRoster(dept) }
                        )
                    }
                    FaithScreen.DEPARTMENT_ROSTER -> {
                        DepartmentRosterScreen(
                            department = selectedDepartment,
                            allMembers = allMembers,
                            onBack = { viewModel.handleBackPress() },
                            onMemberClick = { member -> viewModel.openMemberProfile(member) }
                        )
                    }
                    FaithScreen.ATTENDANCE -> {
                        AttendanceScreen(
                            members = allMembers,
                            attendanceRecords = allAttendanceRecords,
                            selectedService = selectedAttendanceService,
                            onSelectService = { viewModel.setAttendanceService(it) },
                            selectedDate = selectedAttendanceDate,
                            onSelectDate = { viewModel.setAttendanceDate(it) },
                            entryMode = attendanceMode,
                            onSetEntryMode = { viewModel.setAttendanceMode(it) },
                            headcountMen = headcountMen,
                            headcountWomen = headcountWomen,
                            headcountChildren = headcountChildren,
                            headcountVisitors = headcountVisitors,
                            onUpdateHeadcount = { m, w, c, v -> viewModel.setHeadcount(m, w, c, v) },
                            attendanceChecklist = attendanceChecklist,
                            onToggleChecklistMember = { id, status -> viewModel.setMemberAttendanceStatus(id, status) },
                            onMarkAllPresent = { viewModel.markAllPresent() },
                            attendanceNotes = attendanceNotes,
                            onSaveAttendance = { viewModel.saveAttendance() }
                        )
                    }
                    FaithScreen.EVENTS -> {
                        EventsCalendarScreen(
                            events = allEvents,
                            selectedDate = selectedCalendarDate,
                            onSelectDate = { viewModel.setSelectedCalendarDate(it) },
                            onOpenAddEvent = { viewModel.openAddEventDialog() },
                            onDeleteEvent = { viewModel.deleteEvent(it) }
                        )
                    }
                    FaithScreen.REPORTS -> {
                        ReportsScreen(
                            members = allMembers,
                            departments = allDepartments,
                            attendanceRecords = allAttendanceRecords
                        )
                    }
                }
            }

            // Dialogs
            if (isAddEditMemberDialogOpen) {
                AddEditMemberDialog(
                    memberToEdit = editingMember,
                    availableDepartments = allDepartments.map { it.name },
                    onDismiss = { viewModel.closeAddMemberDialog() },
                    onSave = { member -> viewModel.saveMember(member) }
                )
            }

            if (isAddEventDialogOpen) {
                AddEventDialog(
                    initialDate = selectedCalendarDate,
                    onDismiss = { viewModel.closeAddEventDialog() },
                    onSave = { event -> viewModel.saveEvent(event) }
                )
            }
        }
    }
}
