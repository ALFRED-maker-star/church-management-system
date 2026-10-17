package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.Member
import com.example.ui.theme.BurgundyPrimary
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.NavySecondary

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddEditMemberDialog(
    memberToEdit: Member?,
    availableDepartments: List<String>,
    onDismiss: () -> Unit,
    onSave: (Member) -> Unit
) {
    val isEditing = memberToEdit != null

    var firstName by remember { mutableStateOf(memberToEdit?.firstName ?: "") }
    var lastName by remember { mutableStateOf(memberToEdit?.lastName ?: "") }
    var phone by remember { mutableStateOf(memberToEdit?.phone ?: "") }
    var email by remember { mutableStateOf(memberToEdit?.email ?: "") }
    var address by remember { mutableStateOf(memberToEdit?.address ?: "") }
    var dateJoined by remember { mutableStateOf(memberToEdit?.dateJoined ?: "2026-10-17") }
    var householdName by remember { mutableStateOf(memberToEdit?.householdName ?: "") }
    var householdRole by remember { mutableStateOf(memberToEdit?.householdRole ?: "Head of Household") }
    var status by remember { mutableStateOf(memberToEdit?.status ?: "Active Member") }
    var gender by remember { mutableStateOf(memberToEdit?.gender ?: "Female") }
    var ageGroup by remember { mutableStateOf(memberToEdit?.ageGroup ?: "Adult") }
    var notes by remember { mutableStateOf(memberToEdit?.notes ?: "") }
    var selectedColor by remember { mutableStateOf(memberToEdit?.avatarColor ?: 0xFF7C2D42) }

    val selectedDepartments = remember {
        mutableStateListOf<String>().apply {
            if (memberToEdit != null) {
                addAll(memberToEdit.departmentList)
            }
        }
    }

    val colorOptions = listOf(
        0xFF7C2D42, // Burgundy
        0xFF1E3A5F, // Navy
        0xFFD4A017, // Gold
        0xFF3D6B58, // Forest Green
        0xFF8B263E, // Rose Wine
        0xFF58427C, // Royal Purple
        0xFFA04828  // Terracotta
    )

    val allMinistries = remember(availableDepartments) {
        val base = listOf(
            "Choir & Worship Arts",
            "Ushering & Protocol",
            "Youth & Young Adults (YAM)",
            "Media & Technical Production",
            "Hospitality & Welcoming",
            "Children's Church (Kingdom Kids)",
            "Intercessory Prayer Team",
            "Men of Valor",
            "Women of Grace"
        )
        (base + availableDepartments).distinct()
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(vertical = 20.dp)
                .testTag("add_edit_member_dialog"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (isEditing) "Edit Member Details" else "Add New Church Member",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = BurgundyPrimary
                        )
                        Text(
                            text = "Fill in member bio, family household, and ministry roles",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close dialog")
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Avatar Preview & Color Picker
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val initials = "${firstName.firstOrNull() ?: '?'}${lastName.firstOrNull() ?: '?'}"
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(Color(selectedColor)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = initials,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text("Avatar Badge Color", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            colorOptions.forEach { hex ->
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(Color(hex))
                                        .border(
                                            width = if (selectedColor == hex) 2.dp else 0.dp,
                                            color = if (selectedColor == hex) GoldAccent else Color.Transparent,
                                            shape = CircleShape
                                        )
                                        .clickable { selectedColor = hex }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Name Fields
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = firstName,
                        onValueChange = { firstName = it },
                        label = { Text("First Name *") },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("member_first_name_input"),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )
                    OutlinedTextField(
                        value = lastName,
                        onValueChange = { lastName = it },
                        label = { Text("Last Name *") },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("member_last_name_input"),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Contact Info
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone Number *") },
                    placeholder = { Text("(555) 000-0000") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("member_phone_input"),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email Address") },
                    placeholder = { Text("member@gmail.com") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Residential Address") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Date Joined & Membership Status
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = dateJoined,
                        onValueChange = { dateJoined = it },
                        label = { Text("Date Joined") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Status Selection
                Text("Membership Status", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Active Member", "Visitor", "Leadership").forEach { st ->
                        val isSelected = status == st
                        FilterChip(
                            selected = isSelected,
                            onClick = { status = st },
                            label = { Text(st, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BurgundyPrimary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Gender & Age Group
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Gender", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf("Female", "Male").forEach { g ->
                                FilterChip(
                                    selected = gender == g,
                                    onClick = { gender = g },
                                    label = { Text(g, fontSize = 11.sp) }
                                )
                            }
                        }
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Age Group", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            listOf("Youth", "Adult", "Senior").forEach { a ->
                                FilterChip(
                                    selected = ageGroup == a,
                                    onClick = { ageGroup = a },
                                    label = { Text(a, fontSize = 10.sp) }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Family & Household Link
                Text("Family & Household Link", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = householdName,
                        onValueChange = { householdName = it },
                        label = { Text("Household / Family Name") },
                        placeholder = { Text("e.g. Miller Household") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("Head of Household", "Spouse", "Child", "Individual").forEach { role ->
                        FilterChip(
                            selected = householdRole == role,
                            onClick = { householdRole = role },
                            label = { Text(role, fontSize = 10.sp) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Ministry / Department Selection
                Text("Department / Ministry Membership(s)", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    allMinistries.forEach { ministry ->
                        val isChecked = selectedDepartments.contains(ministry)
                        FilterChip(
                            selected = isChecked,
                            onClick = {
                                if (isChecked) selectedDepartments.remove(ministry)
                                else selectedDepartments.add(ministry)
                            },
                            label = { Text(ministry, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BurgundyPrimary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Notes
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Initial Pastoral Note / Prayer Request") },
                    placeholder = { Text("Spiritual background, special talents, pastoral care notes...") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3,
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(
                        onClick = {
                            if (firstName.isNotBlank() && lastName.isNotBlank()) {
                                val saved = Member(
                                    id = memberToEdit?.id ?: 0L,
                                    firstName = firstName.trim(),
                                    lastName = lastName.trim(),
                                    phone = phone.trim().ifBlank { "(555) 000-0000" },
                                    email = email.trim(),
                                    address = address.trim(),
                                    dateJoined = dateJoined.trim(),
                                    departments = selectedDepartments.joinToString(", "),
                                    householdName = householdName.trim(),
                                    householdRole = householdRole,
                                    status = status,
                                    avatarColor = selectedColor,
                                    gender = gender,
                                    ageGroup = ageGroup,
                                    notes = notes.trim()
                                )
                                onSave(saved)
                            }
                        },
                        enabled = firstName.isNotBlank() && lastName.isNotBlank(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BurgundyPrimary,
                            contentColor = Color.White
                        ),
                        modifier = Modifier.testTag("save_member_button")
                    ) {
                        Text(if (isEditing) "Save Changes" else "Add Member")
                    }
                }
            }
        }
    }
}
