package com.example.contentprovider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.util.Log
import androidx.annotation.Nullable


private const val TAG = "MiniContentProvider"

private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

class MiniContentProvider : ContentProvider() {
    private lateinit var mData: Array<String>
    override fun onCreate(): Boolean {
        // Set up the URI scheme for this content provider.
        initializeUriMatching()
        val context = context
        mData = context!!.resources.getStringArray(R.array.words)
        return true
    }

    /**
     * Defines the accepted Uri schemes for this content provider.
     * Calls addURI()for all of the content URI patterns that the provide should recognize.
     */
    private fun initializeUriMatching() {
        // Matches a URI that references one word in the list by its index.
        // The # symbol matches a string of numeric characters of any length.
        // For this sample, this references the first, second, etc. words in the list.
        sUriMatcher.addURI(Contract.AUTHORITY, Contract.CONTENT_PATH + "/#", 1)

        // Matches a URI that is just the authority + the path, triggering the return of all words.
        sUriMatcher.addURI(Contract.AUTHORITY, Contract.CONTENT_PATH, 0)
    }

    /**
     * Matches the URI, converts it to a query, executes the query, and returns the result.
     *
     * @param uri The complete URI queried. This cannot be null.
     * @param projection Indicates which columns/attributes you want to access.
     * @param selection Indicates which rows/records of the objects you want to access
     * @param selectionArgs The binding parameters to the previous selection argument.
     * @param sortOrder Whether to sort, and if so, whether ascending or descending.
     * @return a Cursor of any kind with the response data inside.
     */
    @Nullable
    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor {
        var id: Int
        when (sUriMatcher.match(uri)) {
            0 -> {
                // Matches URI to get all of the entries.
                id = Contract.ALL_ITEMS

                if (selection != null) {
                    id = selectionArgs!![0].toInt()
                }
            }
            1 ->
                id = uri.lastPathSegment!!.toInt()
            UriMatcher.NO_MATCH -> {
                // You should do some error handling here.
                Log.d(TAG, "NO MATCH FOR THIS URI IN SCHEME.")
                id = -1
            }
            else -> {
                // You should do some error handling here.
                Log.d(TAG, "INVALID URI - URI NOT RECOGNIZED.")
                id = -1
            }
        }
        Log.d(TAG, "query: $id")
        return populateCursor(id)
    }

    private fun populateCursor(id: Int): Cursor {
        // The query() method must return a cursor.
        // If you are not using data storage that returns a cursor,
        val cursor = MatrixCursor(arrayOf(Contract.CONTENT_PATH))

        // If there is a valid query, execute it and add the result to the cursor.
        if (id == Contract.ALL_ITEMS) {
            for (i in mData.indices) {
                val word = mData[i]
                cursor.addRow(arrayOf<Any>(word))
            }
        } else if (id >= 0) {

            val word = mData[id]

            cursor.addRow(arrayOf<Any>(word))
        }
        return cursor
    }

    @Nullable
    override fun getType(uri: Uri): String? {
        return when (sUriMatcher.match(uri)) {
            0 -> Contract.MULTIPLE_RECORDS_MIME_TYPE
            1 -> Contract.SINGLE_RECORD_MIME_TYPE
            else ->
                null
        }
    }

    @Nullable
    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        Log.e(TAG, "Not implemented: insert uri: $uri")
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        Log.e(TAG, "Not implemented: delete uri: $uri")
        return 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        Log.e(TAG, "Not implemented: update uri: $uri")
        return 0
    }
}
