package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.Member
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("FaithConnect", appName)
    }

    @Test
    fun `verify member model initials and department list`() {
        val member = Member(
            firstName = "Grace",
            lastName = "Adebayo",
            phone = "(555) 345-6789",
            email = "grace.a@gmail.com",
            address = "1204 Cedar Ridge Way",
            dateJoined = "2020-03-22",
            departments = "Choir & Worship Arts, Hospitality",
            householdName = "Adebayo Family",
            householdRole = "Head of Household",
            status = "Active Member"
        )
        assertEquals("Grace Adebayo", member.fullName)
        assertEquals("GA", member.initials)
        assertEquals(2, member.departmentList.size)
        assertTrue(member.departmentList.contains("Choir & Worship Arts"))
    }
}
