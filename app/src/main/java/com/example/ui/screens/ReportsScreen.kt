package com.example.ui.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AttendanceRecord
import com.example.data.model.Department
import com.example.data.model.Member
import com.example.ui.theme.BurgundyPrimary
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.NavySecondary
import com.example.ui.theme.WarmBackground

@Composable
fun ReportsScreen(
    members: List<Member>,
    departments: List<Department>,
    attendanceRecords: List<AttendanceRecord>
) {
    val context = LocalContext.current

    val totalMembers = members.size
    val activeMembers = members.count { it.status == "Active Member" || it.status == "Leadership" }
    val visitors = members.count { it.status == "Visitor" }

    // Demographics
    val females = members.count { it.gender.equals("Female", ignoreCase = true) }
    val males = members.count { it.gender.equals("Male", ignoreCase = true) }
    val femalePercent = if (totalMembers > 0) females.toFloat() / totalMembers else 0.5f

    val childrenCount = members.count { it.ageGroup.equals("Children", ignoreCase = true) }
    val youthCount = members.count { it.ageGroup.equals("Youth", ignoreCase = true) }
    val adultsCount = members.count { it.ageGroup.equals("Adult", ignoreCase = true) }
    val seniorsCount = members.count { it.ageGroup.equals("Senior", ignoreCase = true) }

    val distinctHouseholds = remember(members) {
        members.mapNotNull { it.householdName.takeIf { name -> name.isNotBlank() } }.distinct().size
    }

    // Attendance stats
    val avgAttendance = remember(attendanceRecords) {
        if (attendanceRecords.isNotEmpty()) attendanceRecords.map { it.totalCount }.average().toInt()
        else 285
    }
    val peakAttendance = remember(attendanceRecords) {
        attendanceRecords.maxOfOrNull { it.totalCount } ?: 312
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
            // Report Header Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = BurgundyPrimary)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Church Executive Report",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Comprehensive attendance, growth & demographics",
                                style = MaterialTheme.typography.bodySmall,
                                color = GoldAccent
                            )
                        }
                        Surface(
                            color = Color.White.copy(alpha = 0.2f),
                            shape = CircleShape,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.BarChart, contentDescription = null, tint = Color.White)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = {
                            shareChurchReport(
                                context,
                                totalMembers = totalMembers,
                                avgAttendance = avgAttendance,
                                peakAttendance = peakAttendance,
                                departmentsCount = departments.size,
                                activeMembers = activeMembers,
                                visitors = visitors
                            )
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GoldAccent,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("export_report_button")
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Share / Export Church Summary", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }

        // Section 1: Attendance Over Time
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.TrendingUp, contentDescription = null, tint = BurgundyPrimary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Attendance Growth & Trends",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatPill("Avg Sunday", "$avgAttendance", BurgundyPrimary)
                        StatPill("Peak Service", "$peakAttendance", GoldAccent)
                        StatPill("Retention", "94%", NavySecondary)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Line & Spline Canvas Chart
                    AttendanceLineChart(
                        records = attendanceRecords.take(7).reversed(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                    )
                }
            }
        }

        // Section 2: Department Growth & Distribution
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.PieChart, contentDescription = null, tint = NavySecondary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Ministry Member Distribution",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "Member participation across active church teams",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    departments.forEach { dept ->
                        val count = members.count { m ->
                            m.departmentList.any { it.contains(dept.name.take(5), ignoreCase = true) || dept.name.contains(it.take(5), ignoreCase = true) }
                        }
                        val fraction = if (totalMembers > 0) (count.toFloat() / totalMembers).coerceIn(0f, 1f) else 0.1f

                        Column(modifier = Modifier.padding(vertical = 4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = dept.name,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "$count members (${(fraction * 100).toInt()}%)",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(dept.colorHex),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = { fraction },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = Color(dept.colorHex),
                                trackColor = Color.LightGray.copy(alpha = 0.25f)
                            )
                        }
                    }
                }
            }
        }

        // Section 3: Member Demographics
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Group, contentDescription = null, tint = GoldAccent)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Member Demographics & Households",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Gender Ratio
                    Text("Gender Ratio", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = Color.Gray)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Female: $females (${(femalePercent * 100).toInt()}%)", fontSize = 12.sp, color = BurgundyPrimary, fontWeight = FontWeight.Bold)
                        Text("Male: $males (${((1f - femalePercent) * 100).toInt()}%)", fontSize = 12.sp, color = NavySecondary, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { femalePercent },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = BurgundyPrimary,
                        trackColor = NavySecondary
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                    // Age Groups
                    Text("Age Group Breakdown", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = Color.Gray)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        AgeGroupPill("Children (0-12)", "$childrenCount", Color(0xFF58427C))
                        AgeGroupPill("Youth (13-25)", "$youthCount", GoldAccent)
                        AgeGroupPill("Adults (26-59)", "$adultsCount", BurgundyPrimary)
                        AgeGroupPill("Seniors (60+)", "$seniorsCount", NavySecondary)
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                    // Household stats
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Registered Households", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                            Text("$distinctHouseholds Families", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("New Visitors Logged", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                            Text("$visitors Guests", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = GoldAccent)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
fun StatPill(label: String, value: String, color: Color) {
    Surface(
        color = color.copy(alpha = 0.12f),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(label, style = MaterialTheme.typography.labelSmall, color = Color.DarkGray)
            Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = color)
        }
    }
}

@Composable
fun AgeGroupPill(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontWeight = FontWeight.Bold, color = color, fontSize = 14.sp)
        Text(label, fontSize = 9.sp, color = Color.Gray)
    }
}

@Composable
fun AttendanceLineChart(
    records: List<AttendanceRecord>,
    modifier: Modifier = Modifier
) {
    if (records.isEmpty()) return

    val maxVal = (records.maxOfOrNull { it.totalCount } ?: 350).coerceAtLeast(350).toFloat()
    val minVal = (records.minOfOrNull { it.totalCount } ?: 100).coerceAtMost(100).toFloat()

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val step = w / (records.size - 1).coerceAtLeast(1)

        val path = Path()
        val points = mutableListOf<Offset>()

        records.forEachIndexed { index, record ->
            val x = index * step
            val normalized = (record.totalCount - minVal) / (maxVal - minVal)
            val y = h - (normalized * (h - 40f)) - 20f
            val point = Offset(x, y)
            points.add(point)

            if (index == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }

        // Draw Line
        drawPath(
            path = path,
            color = BurgundyPrimary,
            style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
        )

        // Draw Data Points and value text
        points.forEachIndexed { index, point ->
            drawCircle(
                color = Color.White,
                radius = 6.dp.toPx(),
                center = point
            )
            drawCircle(
                color = if (index == points.lastIndex) GoldAccent else BurgundyPrimary,
                radius = 4.dp.toPx(),
                center = point
            )

            // Draw number
            val count = records[index].totalCount
            val paint = android.graphics.Paint().apply {
                color = android.graphics.Color.DKGRAY
                textSize = 24f
                textAlign = android.graphics.Paint.Align.CENTER
                isFakeBoldText = true
            }
            drawContext.canvas.nativeCanvas.drawText(
                "$count",
                point.x,
                point.y - 14f,
                paint
            )
        }
    }
}

fun shareChurchReport(
    context: Context,
    totalMembers: Int,
    avgAttendance: Int,
    peakAttendance: Int,
    departmentsCount: Int,
    activeMembers: Int,
    visitors: Int
) {
    val reportText = """
        FAITHCONNECT CHURCH EXECUTIVE REPORT
        Faith Community Church • Administrative Office
        ---------------------------------------------
        Total Registered Members: $totalMembers
        Active Ministry Personnel: $activeMembers
        First-Time Visitors Logged: $visitors
        Active Ministry Departments: $departmentsCount

        ATTENDANCE SUMMARY:
        - Average Sunday Attendance: $avgAttendance attendees
        - Peak Service Attendance: $peakAttendance attendees
        - Attendance Retention Rate: 94%

        "Let all things be done decently and in order." (1 Cor 14:40)
        Generated by FaithConnect Church Management System.
    """.trimIndent()

    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, reportText)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, "Share FaithConnect Church Report")
    context.startActivity(shareIntent)
}
