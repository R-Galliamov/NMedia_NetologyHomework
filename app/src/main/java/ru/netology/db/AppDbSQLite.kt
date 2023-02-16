package ru.netology.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ru.netology.dao.PostDaoSQLite
import ru.netology.dao.PostDaoSQLiteImpl

class AppDbSQLite private constructor(db: SQLiteDatabase) {
    val postDaoSQLite: PostDaoSQLite = PostDaoSQLiteImpl(db)

    companion object {
        @Volatile
        private var instance: AppDbSQLite? = null

        fun getInstance(context: Context): AppDbSQLite {
            return instance ?: synchronized(this) {
                instance ?: AppDbSQLite(
                    buildDatabase(context, arrayOf(PostDaoSQLiteImpl.DDL))
                ).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context, DDLs: Array<String>) = DbHelper(
            context, 1, "app.db", DDLs,
        ).writableDatabase
    }
}

class DbHelper(context: Context, dbVersion: Int, dbName: String, private val DDLs: Array<String>) :
    SQLiteOpenHelper(context, dbName, null, dbVersion) {
    override fun onCreate(db: SQLiteDatabase) {
        DDLs.forEach {
            db.execSQL(it)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        TODO("Not implemented")
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        TODO("Not implemented")
    }
}
