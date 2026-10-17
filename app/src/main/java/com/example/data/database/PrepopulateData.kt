package com.example.data.database

import com.example.data.model.AttendanceRecord
import com.example.data.model.ChurchEvent
import com.example.data.model.Department
import com.example.data.model.Member
import com.example.data.model.PastoralNote

object PrepopulateData {
    suspend fun seedDatabase(database: FaithConnectDatabase) {
        val memberDao = database.memberDao()
        val departmentDao = database.departmentDao()
        val attendanceDao = database.attendanceDao()
        val eventDao = database.eventDao()
        val noteDao = database.pastoralNoteDao()

        // 1. Seed Departments
        val departments = listOf(
            Department(
                name = "Choir & Worship Arts",
                code = "CHOIR",
                leaderName = "Sister Grace Adebayo",
                meetingSchedule = "Thursdays 6:30 PM & Saturdays 10:00 AM",
                description = "Leading the congregation into joyful praise and reverence through sacred music and vocal ministry.",
                colorHex = 0xFF7C2D42,
                iconName = "music_note"
            ),
            Department(
                name = "Ushering & Protocol",
                code = "USHER",
                leaderName = "Deacon Samuel O'Connor",
                meetingSchedule = "1st Saturday 9:00 AM",
                description = "Ensuring order, hospitality at sanctuary entrances, escorting attendees, and offering collection ministry.",
                colorHex = 0xFF1E3A5F,
                iconName = "verified_user"
            ),
            Department(
                name = "Youth & Young Adults (YAM)",
                code = "YOUTH",
                leaderName = "Pastor Sarah Jenkins",
                meetingSchedule = "Fridays 7:00 PM (Youth Hall)",
                description = "Equipping teens and collegiate adults to stand firm in faith, fellowship, and campus outreach.",
                colorHex = 0xFFD4A017,
                iconName = "bolt"
            ),
            Department(
                name = "Media & Technical Production",
                code = "MEDIA",
                leaderName = "Brother David Kim",
                meetingSchedule = "Sundays 7:15 AM & Saturdays 4:00 PM",
                description = "Sound engineering, livestream broadcast, church app administration, lighting, and photography.",
                colorHex = 0xFF3D6B58,
                iconName = "videocam"
            ),
            Department(
                name = "Hospitality & Welcoming",
                code = "HOSP",
                leaderName = "Sister Martha Williams",
                meetingSchedule = "Sundays post-service & 2nd Saturday",
                description = "Welcoming visitors, organizing post-service refreshments, and following up on new guest cards.",
                colorHex = 0xFFA04828,
                iconName = "volunteer_activism"
            ),
            Department(
                name = "Children's Church (Kingdom Kids)",
                code = "KIDS",
                leaderName = "Sister Rebecca Taylor",
                meetingSchedule = "Sundays 9:30 AM & 3rd Saturday planning",
                description = "Nurturing infants to 11-year-olds with bible lessons, interactive crafts, and scripture memorization.",
                colorHex = 0xFF58427C,
                iconName = "child_care"
            ),
            Department(
                name = "Intercessory Prayer Team",
                code = "PRAYER",
                leaderName = "Elder Joshua Vance",
                meetingSchedule = "Tuesdays 6:00 AM & Saturdays 6:00 PM",
                description = "Holding vigil for the pastoral team, church families, sick brethren, community revival, and world needs.",
                colorHex = 0xFF8B263E,
                iconName = "favorite"
            ),
            Department(
                name = "Men's Fellowship (Men of Valor)",
                code = "MEN",
                leaderName = "Deacon Mark Thompson",
                meetingSchedule = "1st Saturday Breakfast 8:00 AM",
                description = "Discipling fathers and men in godly leadership, family stewardship, and church facility service.",
                colorHex = 0xFF2A4D69,
                iconName = "shield"
            ),
            Department(
                name = "Women of Grace",
                code = "WOMEN",
                leaderName = "Pastor Dr. Elizabeth Miller",
                meetingSchedule = "2nd Saturday Fellowship 10:00 AM",
                description = "Encouraging mothers and sisters through prayer circles, mentorship, and maternal support drives.",
                colorHex = 0xFF9E3D60,
                iconName = "spa"
            )
        )
        departmentDao.insertAll(departments)

        // 2. Seed Members
        val members = listOf(
            Member(
                firstName = "David",
                lastName = "Miller",
                phone = "(555) 234-5678",
                email = "pastor.david@faithconnect.org",
                address = "742 Evergreen Terrace, Springfield",
                dateJoined = "2018-01-14",
                departments = "Men of Valor, Leadership",
                householdName = "Miller Household",
                householdRole = "Head of Household",
                status = "Leadership",
                avatarColor = 0xFF7C2D42,
                gender = "Male",
                ageGroup = "Adult",
                notes = "Senior Pastor. Leads Sunday preachings and marriage council."
            ),
            Member(
                firstName = "Elizabeth",
                lastName = "Miller",
                phone = "(555) 234-5679",
                email = "elizabeth.m@faithconnect.org",
                address = "742 Evergreen Terrace, Springfield",
                dateJoined = "2018-01-14",
                departments = "Women of Grace, Choir & Worship Arts",
                householdName = "Miller Household",
                householdRole = "Spouse",
                status = "Leadership",
                avatarColor = 0xFF9E3D60,
                gender = "Female",
                ageGroup = "Adult",
                notes = "First Lady and Women's ministry overseer."
            ),
            Member(
                firstName = "Grace",
                lastName = "Adebayo",
                phone = "(555) 345-6789",
                email = "grace.adebayo@gmail.com",
                address = "1204 Cedar Ridge Way, Apt 3B",
                dateJoined = "2020-03-22",
                departments = "Choir & Worship Arts, Hospitality & Welcoming",
                householdName = "Adebayo Family",
                householdRole = "Head of Household",
                status = "Active Member",
                avatarColor = 0xFFD4A017,
                gender = "Female",
                ageGroup = "Adult",
                notes = "Choir Director. Leads Sunday morning worship set."
            ),
            Member(
                firstName = "Samuel",
                lastName = "O'Connor",
                phone = "(555) 456-7890",
                email = "samuel.oconnor@outlook.com",
                address = "89 Maplewood Blvd",
                dateJoined = "2019-06-10",
                departments = "Ushering & Protocol, Men of Valor",
                householdName = "O'Connor Family",
                householdRole = "Head of Household",
                status = "Active Member",
                avatarColor = 0xFF1E3A5F,
                gender = "Male",
                ageGroup = "Adult",
                notes = "Head Usher. Coordinates sanctuary seating and offering plates."
            ),
            Member(
                firstName = "Sarah",
                lastName = "Jenkins",
                phone = "(555) 567-8901",
                email = "sarah.j@faithconnect.org",
                address = "310 University Crescent",
                dateJoined = "2021-08-15",
                departments = "Youth & Young Adults (YAM), Media & Technical Production",
                householdName = "Jenkins Household",
                householdRole = "Individual",
                status = "Leadership",
                avatarColor = 0xFF7C2D42,
                gender = "Female",
                ageGroup = "Adult",
                notes = "Youth Pastor. Organizes Friday night teen devotionals."
            ),
            Member(
                firstName = "David",
                lastName = "Kim",
                phone = "(555) 678-9012",
                email = "david.kim@techfaith.io",
                address = "455 Silicon Glen Dr",
                dateJoined = "2022-02-18",
                departments = "Media & Technical Production",
                householdName = "Kim Family",
                householdRole = "Head of Household",
                status = "Active Member",
                avatarColor = 0xFF3D6B58,
                gender = "Male",
                ageGroup = "Adult",
                notes = "Sound board and audiovisual director."
            ),
            Member(
                firstName = "Rebecca",
                lastName = "Taylor",
                phone = "(555) 789-0123",
                email = "rebecca.taylor@edu.org",
                address = "210 Meadow Lane",
                dateJoined = "2021-04-05",
                departments = "Children's Church (Kingdom Kids)",
                householdName = "Taylor Household",
                householdRole = "Head of Household",
                status = "Active Member",
                avatarColor = 0xFF58427C,
                gender = "Female",
                ageGroup = "Adult",
                notes = "Kingdom Kids director and Sunday school teacher."
            ),
            Member(
                firstName = "Joshua",
                lastName = "Vance",
                phone = "(555) 890-1234",
                email = "elder.vance@churchmail.com",
                address = "55 St. Augustine Way",
                dateJoined = "2017-09-10",
                departments = "Intercessory Prayer Team, Men of Valor",
                householdName = "Vance Household",
                householdRole = "Head of Household",
                status = "Leadership",
                avatarColor = 0xFF8B263E,
                gender = "Male",
                ageGroup = "Senior",
                notes = "Church Board Trustee & Prayer coordinator."
            ),
            Member(
                firstName = "Martha",
                lastName = "Williams",
                phone = "(555) 901-2345",
                email = "martha.w@gmail.com",
                address = "88 Whispering Pines Ct",
                dateJoined = "2019-11-03",
                departments = "Hospitality & Welcoming, Women of Grace",
                householdName = "Williams Family",
                householdRole = "Spouse",
                status = "Active Member",
                avatarColor = 0xFFA04828,
                gender = "Female",
                ageGroup = "Senior",
                notes = "Coordinates Sunday welcome coffee and fellowship hour."
            ),
            Member(
                firstName = "Mark",
                lastName = "Thompson",
                phone = "(555) 012-3456",
                email = "mark.thompson@builders.com",
                address = "1430 Pinecrest Ave",
                dateJoined = "2020-01-20",
                departments = "Men of Valor, Ushering & Protocol",
                householdName = "Thompson Household",
                householdRole = "Head of Household",
                status = "Active Member",
                avatarColor = 0xFF2A4D69,
                gender = "Male",
                ageGroup = "Adult",
                notes = "Deacon in charge of facility upkeep and Men's breakfast."
            ),
            Member(
                firstName = "Caleb",
                lastName = "O'Connor",
                phone = "(555) 456-7891",
                email = "caleb.oc@campus.edu",
                address = "89 Maplewood Blvd",
                dateJoined = "2021-09-01",
                departments = "Youth & Young Adults (YAM), Media & Technical Production",
                householdName = "O'Connor Family",
                householdRole = "Child",
                status = "Active Member",
                avatarColor = 0xFFD4A017,
                gender = "Male",
                ageGroup = "Youth",
                notes = "Plays acoustic guitar in Youth Band and operates camera 2."
            ),
            Member(
                firstName = "Hannah",
                lastName = "Taylor",
                phone = "(555) 789-0124",
                email = "hannah.t@school.org",
                address = "210 Meadow Lane",
                dateJoined = "2021-04-05",
                departments = "Children's Church (Kingdom Kids)",
                householdName = "Taylor Household",
                householdRole = "Child",
                status = "Active Member",
                avatarColor = 0xFF9E3D60,
                gender = "Female",
                ageGroup = "Children",
                notes = "Attends Kingdom Kids Level 3 (Scripture Memory Champion)."
            ),
            Member(
                firstName = "Emmanuel",
                lastName = "Eze",
                phone = "(555) 123-9876",
                email = "emmanuel.eze@globalnet.org",
                address = "602 Grandview Ave",
                dateJoined = "2026-09-28",
                departments = "Hospitality & Welcoming",
                householdName = "Eze Family",
                householdRole = "Head of Household",
                status = "Visitor",
                avatarColor = 0xFF7C2D42,
                gender = "Male",
                ageGroup = "Adult",
                notes = "First-time visitor in September. Expressed interest in joining bible study."
            ),
            Member(
                firstName = "Chloe",
                lastName = "Bennett",
                phone = "(555) 654-3210",
                email = "chloe.bennett@designstudio.com",
                address = "904 Skyline Tower, Apt 14",
                dateJoined = "2026-10-04",
                departments = "Choir & Worship Arts",
                householdName = "Bennett Household",
                householdRole = "Individual",
                status = "Visitor",
                avatarColor = 0xFFD4A017,
                gender = "Female",
                ageGroup = "Youth",
                notes = "Visited during Night of Worship. Plays violin; invited to Choir practice."
            ),
            Member(
                firstName = "Robert",
                lastName = "Vance",
                phone = "(555) 890-5678",
                email = "robert.vance@faithconnect.org",
                address = "55 St. Augustine Way",
                dateJoined = "2017-09-10",
                departments = "Leadership, Men of Valor",
                householdName = "Vance Household",
                householdRole = "Spouse",
                status = "Leadership",
                avatarColor = 0xFF1E3A5F,
                gender = "Male",
                ageGroup = "Senior",
                notes = "Church Administrator and Board Treasurer."
            ),
            Member(
                firstName = "Abigail",
                lastName = "Miller",
                phone = "(555) 234-5680",
                email = "abigail.m@grace.org",
                address = "742 Evergreen Terrace, Springfield",
                dateJoined = "2019-02-10",
                departments = "Youth & Young Adults (YAM)",
                householdName = "Miller Household",
                householdRole = "Child",
                status = "Active Member",
                avatarColor = 0xFFD4A017,
                gender = "Female",
                ageGroup = "Youth",
                notes = "Youth choir vocalist and volunteer in toddler nursery."
            )
        )
        memberDao.insertAll(members)

        // 3. Seed Attendance Records (Last 6 weeks of Sunday services + Midweek)
        val attendances = listOf(
            AttendanceRecord(
                serviceName = "Sunday Celebration (10:30 AM)",
                serviceDate = "2026-10-11",
                entryMode = "HEADCOUNT",
                totalCount = 312,
                menCount = 98,
                womenCount = 134,
                childrenCount = 56,
                visitorCount = 24,
                notes = "Sermon: 'Walking by Faith'. Wonderful atmosphere of worship."
            ),
            AttendanceRecord(
                serviceName = "Sunday 1st Service (8:00 AM)",
                serviceDate = "2026-10-11",
                entryMode = "HEADCOUNT",
                totalCount = 148,
                menCount = 52,
                womenCount = 70,
                childrenCount = 16,
                visitorCount = 10,
                notes = "Early communion service."
            ),
            AttendanceRecord(
                serviceName = "Midweek Bible Study (Wed 7:00 PM)",
                serviceDate = "2026-10-07",
                entryMode = "HEADCOUNT",
                totalCount = 124,
                menCount = 44,
                womenCount = 68,
                childrenCount = 8,
                visitorCount = 4,
                notes = "Study on Book of Colossians Chapter 2."
            ),
            AttendanceRecord(
                serviceName = "Sunday Celebration (10:30 AM)",
                serviceDate = "2026-10-04",
                entryMode = "HEADCOUNT",
                totalCount = 295,
                menCount = 92,
                womenCount = 128,
                childrenCount = 54,
                visitorCount = 21,
                notes = "Special Thanksgiving Sunday and child dedication service."
            ),
            AttendanceRecord(
                serviceName = "Sunday 1st Service (8:00 AM)",
                serviceDate = "2026-10-04",
                entryMode = "HEADCOUNT",
                totalCount = 140,
                menCount = 48,
                womenCount = 69,
                childrenCount = 15,
                visitorCount = 8,
                notes = "Early service."
            ),
            AttendanceRecord(
                serviceName = "Sunday Celebration (10:30 AM)",
                serviceDate = "2026-09-27",
                entryMode = "HEADCOUNT",
                totalCount = 286,
                menCount = 88,
                womenCount = 126,
                childrenCount = 52,
                visitorCount = 20,
                notes = "Guest speaker Rev. Timothy Barnes."
            ),
            AttendanceRecord(
                serviceName = "Sunday Celebration (10:30 AM)",
                serviceDate = "2026-09-20",
                entryMode = "HEADCOUNT",
                totalCount = 275,
                menCount = 85,
                womenCount = 120,
                childrenCount = 50,
                visitorCount = 20,
                notes = "Youth Sunday takeover."
            ),
            AttendanceRecord(
                serviceName = "Sunday Celebration (10:30 AM)",
                serviceDate = "2026-09-13",
                entryMode = "HEADCOUNT",
                totalCount = 290,
                menCount = 90,
                womenCount = 130,
                childrenCount = 52,
                visitorCount = 18,
                notes = "Back to Sunday school kickoff."
            )
        )
        attendanceDao.insertAllRecords(attendances)

        // 4. Seed Church Events
        val events = listOf(
            ChurchEvent(
                title = "Sunday Celebration & Holy Communion",
                category = "Service",
                date = "2026-10-18",
                startTime = "10:30 AM",
                endTime = "12:30 PM",
                location = "Main Sanctuary",
                description = "Join us for inspiring worship, Holy Communion, and a life-transforming sermon from Pastor David."
            ),
            ChurchEvent(
                title = "Sunday Early Service",
                category = "Service",
                date = "2026-10-18",
                startTime = "08:00 AM",
                endTime = "09:30 AM",
                location = "Main Sanctuary",
                description = "Traditional liturgical morning service with quiet meditation and hymn singing."
            ),
            ChurchEvent(
                title = "Midweek Faith & Word Expository",
                category = "Service",
                date = "2026-10-21",
                startTime = "07:00 PM",
                endTime = "08:30 PM",
                location = "Fellowship Hall & Online",
                description = "Deep dive Bible study exploring the Epistle of Paul to the Ephesians."
            ),
            ChurchEvent(
                title = "Night of Praise & Youth Encounter",
                category = "Youth",
                date = "2026-10-23",
                startTime = "06:30 PM",
                endTime = "09:00 PM",
                location = "Youth Amphitheater",
                description = "High energy praise, acoustic worship sets, personal testimonies, and pizza fellowship."
            ),
            ChurchEvent(
                title = "Community Food Pantry Outreach",
                category = "Outreach",
                date = "2026-10-24",
                startTime = "09:00 AM",
                endTime = "01:00 PM",
                location = "Church Community Center",
                description = "Distributing fresh produce and non-perishable groceries to 200 local neighborhood families."
            ),
            ChurchEvent(
                title = "Kingdom Marriage & Family Seminar",
                category = "Fellowship",
                date = "2026-10-28",
                startTime = "06:00 PM",
                endTime = "08:30 PM",
                location = "Banquet Hall",
                description = "Interactive workshop for married and engaged couples hosted by Pastor David & Elizabeth Miller."
            ),
            ChurchEvent(
                title = "Monthly Leadership & Ministry Council",
                category = "Leadership",
                date = "2026-11-01",
                startTime = "02:00 PM",
                endTime = "04:00 PM",
                location = "Conference Room A",
                description = "Reviewing Q4 ministry objectives, department budgets, and end-of-year church calendar."
            )
        )
        eventDao.insertAll(events)

        // 5. Seed Pastoral Notes
        val notes = listOf(
            PastoralNote(
                memberId = 1,
                authorName = "Elder Vance",
                noteType = "Pastoral Visit",
                content = "Pastoral check-in before preaching tour. Prayed for physical renewal and wisdom for the congregation.",
                date = "2026-10-08"
            ),
            PastoralNote(
                memberId = 3,
                authorName = "Pastor David Miller",
                noteType = "General Follow-up",
                content = "Met with Sister Grace regarding the upcoming Christmas choral cantata rehearsals. Budget approved.",
                date = "2026-10-12"
            ),
            PastoralNote(
                memberId = 13,
                authorName = "Pastor Sarah Jenkins",
                noteType = "General Follow-up",
                content = "Welcomed Brother Emmanuel after his first service visit. He expressed desire to join the new believers class.",
                date = "2026-10-02"
            ),
            PastoralNote(
                memberId = 14,
                authorName = "Sister Grace Adebayo",
                noteType = "Prayer Request",
                content = "Sister Chloe requested prayer for her graduate school thesis presentation next week.",
                date = "2026-10-06"
            )
        )
        noteDao.insertAll(notes)
    }
}
