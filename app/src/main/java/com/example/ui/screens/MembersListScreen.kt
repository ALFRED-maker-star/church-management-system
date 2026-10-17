package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Member
import com.example.ui.theme.BurgundyPrimary
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.NavySecondary
import com.example.ui.theme.WarmBackground

@Composable
fun MembersListScreen(
    members: List<Member>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedDepartmentFilter: String,
    onSelectDepartmentFilter: (String) -> Unit,
    selectedStatusFilter: String,
    onSelectStatusFilter: (String) -> Unit,
    isGridView: Boolean,
    onToggleGridView: () -> Unit,
    onMemberClick: (Member) -> Unit,
    onOpenAddMember: () -> Unit
) {
    val context = LocalContext.current

    val departmentFilters = listOf(
        "All", "Choir", "Ushering", "Youth", "Media", "Hospitality",
        "Children", "Prayer", "Men of Valor", "Women of Grace"
    )

    val statusFilters = listOf("All", "Active Member", "Visitor", "Leadership")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(6.dp))

            // Search Bar & Grid/List Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("member_search_input"),
                    placeholder = { Text("Search by name, phone, ministry...") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = BurgundyPrimary
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { onSearchQueryChange("") }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear search", tint = Color.Gray)
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BurgundyPrimary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 1.dp
                ) {
                    IconButton(
                        onClick = onToggleGridView,
                        modifier = Modifier.testTag("toggle_grid_view_button")
                    ) {
                        Icon(
                            imageVector = if (isGridView) Icons.Default.ViewList else Icons.Default.GridView,
                            contentDescription = "Toggle Grid or List View",
                            tint = BurgundyPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Department Filters
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(departmentFilters) { dept ->
                    val isSelected = selectedDepartmentFilter == dept
                    FilterChip(
                        selected = isSelected,
                        onClick = { onSelectDepartmentFilter(dept) },
                        label = { Text(dept, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = BurgundyPrimary,
                            selectedLabelColor = Color.White
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                }
            }

            // Status Filter Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Status:",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold
                )
                statusFilters.forEach { status ->
                    val isSelected = selectedStatusFilter == status
                    Surface(
                        color = if (isSelected) BurgundyPrimary.copy(alpha = 0.15f) else Color.Transparent,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onSelectStatusFilter(status) }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = status,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isSelected) BurgundyPrimary else Color.DarkGray,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            Text(
                text = "Showing ${members.size} members",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            // Directory Grid or List
            if (members.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "No members match the criteria",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Try clearing filters or search query",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = if (isGridView) GridCells.Fixed(2) else GridCells.Fixed(1),
                    contentPadding = PaddingValues(bottom = 88.dp, top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(members, key = { it.id }) { member ->
                        MemberCard(
                            member = member,
                            isGrid = isGridView,
                            onClick = { onMemberClick(member) },
                            onCall = { dialPhone(context, member.phone) },
                            onSms = { sendSms(context, member.phone) }
                        )
                    }
                }
            }
        }

        // Floating Action Button: + Add Member
        FloatingActionButton(
            onClick = onOpenAddMember,
            containerColor = BurgundyPrimary,
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .testTag("add_member_fab")
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Member",
                    tint = GoldAccent
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Add Member",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun MemberCard(
    member: Member,
    isGrid: Boolean,
    onClick: () -> Unit,
    onCall: () -> Unit,
    onSms: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .testTag("member_card_${member.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        if (isGrid) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Member Initials Avatar with Status Badge
                Box(contentAlignment = Alignment.BottomEnd) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(Color(member.avatarColor)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = member.initials,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                    val statusDotColor = when (member.status) {
                        "Active Member" -> Color(0xFF2E7D32)
                        "Visitor" -> GoldAccent
                        "Leadership" -> BurgundyPrimary
                        else -> Color.Gray
                    }
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(statusDotColor)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = member.fullName,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // Status or Household tag
                Surface(
                    color = when (member.status) {
                        "Visitor" -> GoldAccent.copy(alpha = 0.15f)
                        "Leadership" -> BurgundyPrimary.copy(alpha = 0.12f)
                        else -> NavySecondary.copy(alpha = 0.08f)
                    },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
                ) {
                    Text(
                        text = member.status,
                        style = MaterialTheme.typography.labelSmall,
                        color = when (member.status) {
                            "Visitor" -> Color(0xFF8B6400)
                            "Leadership" -> BurgundyPrimary
                            else -> NavySecondary
                        },
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                // Department Tag
                val mainDept = member.departmentList.firstOrNull() ?: "General Congregation"
                Text(
                    text = mainDept,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.DarkGray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 11.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Phone
                Text(
                    text = member.phone,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray,
                    fontSize = 10.sp,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Quick Call & SMS action icons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Surface(
                        color = BurgundyPrimary.copy(alpha = 0.1f),
                        shape = CircleShape,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .clickable { onCall() }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Phone,
                                contentDescription = "Call ${member.fullName}",
                                tint = BurgundyPrimary,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Surface(
                        color = NavySecondary.copy(alpha = 0.1f),
                        shape = CircleShape,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .clickable { onSms() }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Message,
                                contentDescription = "Message ${member.fullName}",
                                tint = NavySecondary,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }
        } else {
            // List Row Mode
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color(member.avatarColor)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = member.initials,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = member.fullName,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            color = when (member.status) {
                                "Visitor" -> GoldAccent.copy(alpha = 0.15f)
                                "Leadership" -> BurgundyPrimary.copy(alpha = 0.12f)
                                else -> NavySecondary.copy(alpha = 0.08f)
                            },
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = member.status,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = when (member.status) {
                                    "Visitor" -> Color(0xFF8B6400)
                                    "Leadership" -> BurgundyPrimary
                                    else -> NavySecondary
                                },
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Text(
                        text = member.departments.ifBlank { "General Member" },
                        style = MaterialTheme.typography.bodySmall,
                        color = BurgundyPrimary,
                        fontSize = 11.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = "${member.phone} • ${member.householdName.ifBlank { "Individual" }}",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray,
                        fontSize = 11.sp
                    )
                }

                Row {
                    IconButton(onClick = onCall, modifier = Modifier.size(36.dp)) {
                        Icon(Icons.Default.Phone, contentDescription = "Call", tint = BurgundyPrimary, modifier = Modifier.size(18.dp))
                    }
                    IconButton(onClick = onSms, modifier = Modifier.size(36.dp)) {
                        Icon(Icons.Default.Message, contentDescription = "Message", tint = NavySecondary, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    }
}

fun dialPhone(context: Context, phone: String) {
    try {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${phone.replace(" ", "")}"))
        context.startActivity(intent)
    } catch (_: Exception) {}
}

fun sendSms(context: Context, phone: String) {
    try {
        val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:${phone.replace(" ", "")}"))
        context.startActivity(intent)
    } catch (_: Exception) {}
}
