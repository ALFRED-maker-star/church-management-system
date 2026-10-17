package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AttendanceRecord
import com.example.data.model.Member
import com.example.ui.theme.BurgundyPrimary
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.NavySecondary
import com.example.ui.theme.WarmBackground

@Composable
fun AttendanceScreen(
    members: List<Member>,
    attendanceRecords: List<AttendanceRecord>,
    selectedService: String,
    onSelectService: (String) -> Unit,
    selectedDate: String,
    onSelectDate: (String) -> Unit,
    entryMode: String,
    onSetEntryMode: (String) -> Unit,
    headcountMen: Int,
    headcountWomen: Int,
    headcountChildren: Int,
    headcountVisitors: Int,
    onUpdateHeadcount: (men: Int, women: Int, children: Int, visitors: Int) -> Unit,
    attendanceChecklist: Map<Long, String>,
    onToggleChecklistMember: (memberId: Long, status: String) -> Unit,
    onMarkAllPresent: () -> Unit,
    attendanceNotes: String,
    onSaveAttendance: () -> Unit
) {
    val serviceOptions = listOf(
        "Sunday Celebration (10:30 AM)",
        "Sunday 1st Service (8:00 AM)",
        "Midweek Bible Study (Wed 7:00 PM)",
        "Friday Prayer & Vigil (8:00 PM)"
    )

    var showServiceDropdown by remember { mutableStateOf(false) }
    var checklistSearch by remember { mutableStateOf("") }

    val totalHeadcount = headcountMen + headcountWomen + headcountChildren + headcountVisitors
    val totalChecklistPresent = members.count { attendanceChecklist[it.id] == "PRESENT" }

    val filteredChecklistMembers = remember(members, checklistSearch) {
        if (checklistSearch.isBlank()) members
        else members.filter { it.fullName.contains(checklistSearch, ignoreCase = true) }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            // Service and Date Selector Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Service & Date Selection",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    // Service Dropdown
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = selectedService,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Church Service / Program") },
                            trailingIcon = {
                                IconButton(onClick = { showServiceDropdown = true }) {
                                    Icon(Icons.Default.CalendarToday, contentDescription = "Select Service", tint = BurgundyPrimary)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showServiceDropdown = true },
                            shape = RoundedCornerShape(12.dp)
                        )

                        DropdownMenu(
                            expanded = showServiceDropdown,
                            onDismissRequest = { showServiceDropdown = false }
                        ) {
                            serviceOptions.forEach { service ->
                                DropdownMenuItem(
                                    text = { Text(service, fontWeight = FontWeight.Medium) },
                                    onClick = {
                                        onSelectService(service)
                                        showServiceDropdown = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Date Input & Mode Selector
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = selectedDate,
                            onValueChange = onSelectDate,
                            label = { Text("Service Date (YYYY-MM-DD)") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Mode Toggle: Checklist Mode vs Headcount Mode
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = entryMode == "CHECKLIST",
                            onClick = { onSetEntryMode("CHECKLIST") },
                            label = { Text("Checklist Mode (By Name)", fontWeight = FontWeight.SemiBold) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BurgundyPrimary,
                                selectedLabelColor = Color.White
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("attendance_mode_checklist")
                        )
                        FilterChip(
                            selected = entryMode == "HEADCOUNT",
                            onClick = { onSetEntryMode("HEADCOUNT") },
                            label = { Text("Headcount Mode (Demographics)", fontWeight = FontWeight.SemiBold) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BurgundyPrimary,
                                selectedLabelColor = Color.White
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("attendance_mode_headcount")
                        )
                    }
                }
            }
        }

        // Mode Specific Content
        if (entryMode == "HEADCOUNT") {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Total Headcount Recorded",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = BurgundyPrimary
                                )
                                Text(
                                    text = "Calculated from demographic breakdown",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.Gray
                                )
                            }
                            Surface(
                                color = BurgundyPrimary,
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "$totalHeadcount Attendees",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        HeadcountCounterRow(
                            label = "Men (Adults & Seniors)",
                            count = headcountMen,
                            accentColor = NavySecondary,
                            onIncrement = { onUpdateHeadcount(headcountMen + 5, headcountWomen, headcountChildren, headcountVisitors) },
                            onDecrement = { onUpdateHeadcount((headcountMen - 5).coerceAtLeast(0), headcountWomen, headcountChildren, headcountVisitors) }
                        )

                        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

                        HeadcountCounterRow(
                            label = "Women (Adults & Seniors)",
                            count = headcountWomen,
                            accentColor = BurgundyPrimary,
                            onIncrement = { onUpdateHeadcount(headcountMen, headcountWomen + 5, headcountChildren, headcountVisitors) },
                            onDecrement = { onUpdateHeadcount(headcountMen, (headcountWomen - 5).coerceAtLeast(0), headcountChildren, headcountVisitors) }
                        )

                        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

                        HeadcountCounterRow(
                            label = "Children & Teens (Kingdom Kids & Youth)",
                            count = headcountChildren,
                            accentColor = Color(0xFF58427C),
                            onIncrement = { onUpdateHeadcount(headcountMen, headcountWomen, headcountChildren + 2, headcountVisitors) },
                            onDecrement = { onUpdateHeadcount(headcountMen, headcountWomen, (headcountChildren - 2).coerceAtLeast(0), headcountVisitors) }
                        )

                        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

                        HeadcountCounterRow(
                            label = "First-time Visitors & Guests",
                            count = headcountVisitors,
                            accentColor = GoldAccent,
                            onIncrement = { onUpdateHeadcount(headcountMen, headcountWomen, headcountChildren, headcountVisitors + 1) },
                            onDecrement = { onUpdateHeadcount(headcountMen, headcountWomen, headcountChildren, (headcountVisitors - 1).coerceAtLeast(0)) }
                        )
                    }
                }
            }
        } else {
            // Checklist Mode
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Member Roll Call ($totalChecklistPresent Present)",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = BurgundyPrimary
                                )
                                Text(
                                    text = "Tap status to mark attendance",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.Gray
                                )
                            }
                            Button(
                                onClick = onMarkAllPresent,
                                colors = ButtonDefaults.buttonColors(containerColor = GoldAccent),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.testTag("mark_all_present_button")
                            ) {
                                Icon(Icons.Default.DoneAll, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.Black)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Mark All Present", fontSize = 11.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = checklistSearch,
                            onValueChange = { checklistSearch = it },
                            placeholder = { Text("Filter roster members...") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp)
                        )
                    }
                }
            }

            items(filteredChecklistMembers, key = { it.id }) { member ->
                val currentStatus = attendanceChecklist[member.id] ?: "UNMARKED"
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (currentStatus == "PRESENT") MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                        else MaterialTheme.colorScheme.surface
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color(member.avatarColor)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(member.initials, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(member.fullName, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                                Text(member.departments.ifBlank { "Congregation" }, fontSize = 11.sp, color = Color.Gray, maxLines = 1)
                            }
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            AttendanceStatusChip(
                                label = "P",
                                fullLabel = "Present",
                                isSelected = currentStatus == "PRESENT",
                                selectedColor = Color(0xFF2E7D32),
                                onClick = { onToggleChecklistMember(member.id, "PRESENT") }
                            )
                            AttendanceStatusChip(
                                label = "E",
                                fullLabel = "Excused",
                                isSelected = currentStatus == "EXCUSED",
                                selectedColor = GoldAccent,
                                onClick = { onToggleChecklistMember(member.id, "EXCUSED") }
                            )
                            AttendanceStatusChip(
                                label = "A",
                                fullLabel = "Absent",
                                isSelected = currentStatus == "ABSENT",
                                selectedColor = Color.Red,
                                onClick = { onToggleChecklistMember(member.id, "ABSENT") }
                            )
                        }
                    }
                }
            }
        }

        // Save Attendance Button
        item {
            Button(
                onClick = onSaveAttendance,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("save_attendance_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BurgundyPrimary,
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = null,
                    tint = GoldAccent,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Save Service Attendance",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Historical Attendance Trend Chart
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.TrendingUp, contentDescription = null, tint = BurgundyPrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Historical Attendance Records",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    if (attendanceRecords.isEmpty()) {
                        Text("No historical attendance saved yet.", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
                    } else {
                        AttendanceBarChart(
                            records = attendanceRecords.take(6).reversed(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun HeadcountCounterRow(
    label: String,
    count: Int,
    accentColor: Color,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(label, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
            Text("Category Headcount", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = CircleShape,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .clickable { onDecrement() }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Remove, contentDescription = "Decrement", modifier = Modifier.size(16.dp))
                }
            }

            Text(
                text = "$count",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = accentColor,
                modifier = Modifier.padding(horizontal = 14.dp)
            )

            Surface(
                color = accentColor,
                shape = CircleShape,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .clickable { onIncrement() }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Add, contentDescription = "Increment", tint = Color.White, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

@Composable
fun AttendanceStatusChip(
    label: String,
    fullLabel: String,
    isSelected: Boolean,
    selectedColor: Color,
    onClick: () -> Unit
) {
    Surface(
        color = if (isSelected) selectedColor else MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = label,
                color = if (isSelected) Color.White else Color.DarkGray,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
        }
    }
}
