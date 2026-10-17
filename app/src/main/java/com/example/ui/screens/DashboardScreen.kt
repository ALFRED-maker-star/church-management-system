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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.FactCheck
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AdminUser
import com.example.data.model.AttendanceRecord
import com.example.data.model.ChurchEvent
import com.example.data.model.Department
import com.example.data.model.Member
import com.example.ui.theme.BurgundyPrimary
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.NavySecondary
import com.example.ui.theme.WarmBackground
import com.example.ui.viewmodel.FaithScreen

@Composable
fun DashboardScreen(
    currentUser: AdminUser?,
    members: List<Member>,
    departments: List<Department>,
    attendanceRecords: List<AttendanceRecord>,
    events: List<ChurchEvent>,
    onNavigateTo: (FaithScreen) -> Unit,
    onOpenAddMember: () -> Unit,
    onOpenAddEvent: () -> Unit
) {
    val totalMembersCount = members.size
    val activeDepartmentsCount = departments.size
    val latestAttendance = attendanceRecords.firstOrNull()?.totalCount ?: 312
    val upcomingEventsCount = events.size

    val recentSundays = remember(attendanceRecords) {
        if (attendanceRecords.isNotEmpty()) {
            attendanceRecords.take(5).reversed()
        } else {
            listOf(
                AttendanceRecord(0, "Sunday", "Sep 13", "HEADCOUNT", 290),
                AttendanceRecord(0, "Sunday", "Sep 20", "HEADCOUNT", 275),
                AttendanceRecord(0, "Sunday", "Sep 27", "HEADCOUNT", 286),
                AttendanceRecord(0, "Sunday", "Oct 04", "HEADCOUNT", 295),
                AttendanceRecord(0, "Sunday", "Oct 11", "HEADCOUNT", 312)
            )
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            // Welcome Header Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = BurgundyPrimary
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = GoldAccent,
                                shape = RoundedCornerShape(4.dp),
                                modifier = Modifier.size(6.dp, 16.dp)
                            ) {}
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "PASTORAL OVERVIEW",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = GoldAccent,
                                letterSpacing = 1.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Grace and peace, ${currentUser?.name ?: "Pastor"}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${currentUser?.title ?: "Ministry Lead"} • Faith Community Church",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }

                    Surface(
                        color = Color.White.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "OCT",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = GoldAccent
                            )
                            Text(
                                text = "2026",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        // 4 Stat Cards in 2x2 Grid
        item {
            Text(
                text = "Key Church Metrics",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    DashboardStatCard(
                        modifier = Modifier.weight(1f),
                        title = "Total Members",
                        value = "$totalMembersCount",
                        subtitle = "+3 joined recently",
                        icon = Icons.Default.Group,
                        accentColor = BurgundyPrimary,
                        onClick = { onNavigateTo(FaithScreen.MEMBERS) }
                    )
                    DashboardStatCard(
                        modifier = Modifier.weight(1f),
                        title = "Active Ministries",
                        value = "$activeDepartmentsCount",
                        subtitle = "Choir, Youth & 7 more",
                        icon = Icons.Default.AccountTree,
                        accentColor = NavySecondary,
                        onClick = { onNavigateTo(FaithScreen.DEPARTMENTS) }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    DashboardStatCard(
                        modifier = Modifier.weight(1f),
                        title = "Latest Attendance",
                        value = "$latestAttendance",
                        subtitle = "Sunday Celebration",
                        icon = Icons.Default.FactCheck,
                        accentColor = GoldAccent,
                        onClick = { onNavigateTo(FaithScreen.ATTENDANCE) }
                    )
                    DashboardStatCard(
                        modifier = Modifier.weight(1f),
                        title = "Upcoming Events",
                        value = "$upcomingEventsCount",
                        subtitle = "Services & Fellowships",
                        icon = Icons.Default.Event,
                        accentColor = Color(0xFF2E7D32),
                        onClick = { onNavigateTo(FaithScreen.EVENTS) }
                    )
                }
            }
        }

        // Attendance Trend Mini-Chart
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("attendance_trend_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.TrendingUp,
                                contentDescription = null,
                                tint = BurgundyPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Sunday Attendance Trend",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Last 5 services attendance growth",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.Gray
                                )
                            }
                        }

                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.clickable { onNavigateTo(FaithScreen.ATTENDANCE) }
                        ) {
                            Text(
                                text = "Full Roster",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = BurgundyPrimary,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Custom Canvas Bar Chart
                    AttendanceBarChart(
                        records = recentSundays,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                    )
                }
            }
        }

        // Quick Action Shortcuts
        item {
            Text(
                text = "Quick Administrative Actions",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                QuickActionButton(
                    label = "Record Attendance",
                    icon = Icons.Default.FactCheck,
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigateTo(FaithScreen.ATTENDANCE) }
                )
                QuickActionButton(
                    label = "+ Add Member",
                    icon = Icons.Default.PersonAdd,
                    modifier = Modifier.weight(1f),
                    onClick = onOpenAddMember
                )
                QuickActionButton(
                    label = "+ New Event",
                    icon = Icons.Default.CalendarMonth,
                    modifier = Modifier.weight(1f),
                    onClick = onOpenAddEvent
                )
            }
        }

        // Recent Church Activity & Announcements Feed
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Recent Pastoral Activity",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    ActivityItem(
                        title = "Sunday Attendance Recorded",
                        description = "312 attendees logged for Sunday Celebration service",
                        timestamp = "Sunday, Oct 11",
                        iconColor = BurgundyPrimary
                    )
                    ActivityItem(
                        title = "2 New Visitors Registered",
                        description = "Emmanuel Eze & Chloe Bennett added to welcoming list",
                        timestamp = "Sunday, Oct 04",
                        iconColor = GoldAccent
                    )
                    ActivityItem(
                        title = "Pastoral Visit Recorded",
                        description = "Elder Joshua Vance recorded follow-up note for the congregation",
                        timestamp = "Thursday, Oct 08",
                        iconColor = NavySecondary
                    )
                    ActivityItem(
                        title = "Kingdom Marriage Seminar Scheduled",
                        description = "Workshop date set for Oct 28 in the Banquet Hall",
                        timestamp = "Wednesday, Oct 07",
                        iconColor = Color(0xFF2E7D32)
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun DashboardStatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .testTag("stat_card_${title.lowercase().replace(" ", "_")}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = accentColor.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.size(38.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = accentColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = Color.LightGray,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray,
                maxLines = 1
            )
        }
    }
}

@Composable
fun AttendanceBarChart(
    records: List<AttendanceRecord>,
    modifier: Modifier = Modifier
) {
    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    val maxCount = (records.maxOfOrNull { it.totalCount } ?: 350).coerceAtLeast(350).toFloat()

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val canvasWidth = size.width
                val canvasHeight = size.height
                val count = records.size
                if (count == 0) return@Canvas

                val barWidth = (canvasWidth / count) * 0.55f
                val step = canvasWidth / count

                records.forEachIndexed { index, record ->
                    val x = index * step + (step - barWidth) / 2f
                    val barHeight = (record.totalCount / maxCount) * (canvasHeight - 35f)
                    val y = canvasHeight - barHeight

                    val isHighlighted = index == records.lastIndex
                    val isSelected = selectedIndex == index

                    val barColor = when {
                        isSelected -> GoldAccent
                        isHighlighted -> BurgundyPrimary
                        else -> NavySecondary.copy(alpha = 0.75f)
                    }

                    // Background track
                    drawRoundRect(
                        color = Color.LightGray.copy(alpha = 0.2f),
                        topLeft = Offset(x, 0f),
                        size = Size(barWidth, canvasHeight),
                        cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
                    )

                    // Value bar
                    drawRoundRect(
                        color = barColor,
                        topLeft = Offset(x, y),
                        size = Size(barWidth, barHeight),
                        cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
                    )

                    // Draw attendance number text above bar
                    val paint = android.graphics.Paint().apply {
                        color = if (isHighlighted) BurgundyPrimary.toArgb() else Color.DarkGray.toArgb()
                        textSize = 28f
                        textAlign = android.graphics.Paint.Align.CENTER
                        isFakeBoldText = true
                    }
                    drawContext.canvas.nativeCanvas.drawText(
                        "${record.totalCount}",
                        x + barWidth / 2,
                        (y - 8f).coerceAtLeast(24f),
                        paint
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Dates Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            records.forEachIndexed { index, record ->
                val label = record.serviceDate.takeLast(5).replace("-", "/")
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 10.sp,
                    color = if (index == records.lastIndex) BurgundyPrimary else Color.Gray,
                    fontWeight = if (index == records.lastIndex) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

@Composable
fun QuickActionButton(
    label: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                color = BurgundyPrimary.copy(alpha = 0.1f),
                shape = CircleShape,
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = BurgundyPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )
        }
    }
}

@Composable
fun ActivityItem(
    title: String,
    description: String,
    timestamp: String,
    iconColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.Top
    ) {
        Surface(
            color = iconColor.copy(alpha = 0.12f),
            shape = CircleShape,
            modifier = Modifier
                .size(32.dp)
                .padding(top = 2.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = timestamp,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray,
                    fontSize = 10.sp
                )
            }
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
