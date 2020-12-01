package cn.edu.food_blog

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri

class FoodContentProvider : ContentProvider() {
    private val foodTable = 0
    private val foodItem = 1
    private val authority = "cn.edu.food_blog.provider"
    private lateinit var db:SQLiteDatabase

    private val uriMatcher:UriMatcher
    init {
        uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        uriMatcher.addURI(authority,"food",0)
        uriMatcher.addURI(authority,"food/*",1)
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

    override fun getType(uri: Uri): String? {
        return when(uriMatcher.match(uri)){
            foodTable -> "vnd.android.cursor.dir/vnd.$authority.food"
            foodItem -> "vnd.android.cursor.item/vnd.$authority.food"
            else -> null
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun onCreate(): Boolean {
        context?.let {
            val openSqLiteHelper = MyOpenSqLiteHelper(it,1)
            db = openSqLiteHelper.readableDatabase
            return true
        }
        return false
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        return when(uriMatcher.match(uri)){
            foodTable -> db.query(TABLE_NAME,null,null,null,null,null,null)
            foodItem -> {
                val title = uri.pathSegments[1]
                db.query(TABLE_NAME,null,"title = ?", arrayOf(title),null,null,null)
            }
            else -> null
        }
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return 0
    }
}
