package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.dao.AttendanceDao
import com.example.data.dao.DepartmentDao
import com.example.data.dao.EventDao
import com.example.data.dao.MemberDao
import com.example.data.dao.PastoralNoteDao
import com.example.data.model.AttendanceRecord
import com.example.data.model.ChurchEvent
import com.example.data.model.Department
import com.example.data.model.Member
import com.example.data.model.MemberAttendanceRecord
import com.example.data.model.PastoralNote
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        Member::class,
        Department::class,
        AttendanceRecord::class,
        MemberAttendanceRecord::class,
        ChurchEvent::class,
        PastoralNote::class
    ],
    version = 1,
    exportSchema = false
)
abstract class FaithConnectDatabase : RoomDatabase() {
    abstract fun memberDao(): MemberDao
    abstract fun departmentDao(): DepartmentDao
    abstract fun attendanceDao(): AttendanceDao
    abstract fun eventDao(): EventDao
    abstract fun pastoralNoteDao(): PastoralNoteDao

    companion object {
        @Volatile
        private var INSTANCE: FaithConnectDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): FaithConnectDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FaithConnectDatabase::class.java,
                    "faithconnect_database"
                )
                .addCallback(DatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        PrepopulateData.seedDatabase(database)
                    }
                }
            }
        }
    }
}
