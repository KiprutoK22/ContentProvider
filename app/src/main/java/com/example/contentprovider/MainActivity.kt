package com.example.contentprovider


import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {
    private var mTextView: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mTextView = findViewById(R.id.textview)
    }

    /**
     * onClick method for the UI buttons.
     *
     * IMPORTANT: We can do this query two ways.
     * 1. Use a URL with a # at the end and all other arguments null.
     * 2. Use the CONTENT_URI URL and specify selection criteria.
     *
     * @param view
     */
    fun onClickDisplayEntries(view: View) {

        // URI That identifies the content provider and the table.
        val queryUri: String = Contract.CONTENT_URI.toString()
        val projection = arrayOf(Contract.CONTENT_PATH) // Only get words.
        val selectionClause: String?
        val selectionArgs: Array<String>?

        // The order in which to sort the results.
        val sortOrder: String? =
            null // For this example, we accept the order returned by the response.
        when (view.id) {
            R.id.button_display_all -> {
                selectionClause = null
                selectionArgs = null
            }
            R.id.button_display_first -> {
                selectionClause = Contract.WORD_ID + " = ?"
                selectionArgs = arrayOf("0")
            }
            else -> {
                selectionClause = null
                selectionArgs = null
            }
        }

        val cursor: Cursor? = contentResolver.query(
            Uri.parse(queryUri), projection, selectionClause,
            selectionArgs, sortOrder
        )

        // If we got data back, display it, otherwise report the error.
        // See WordList app and database chapter for more on cursors.
        if (cursor != null) {
            if (cursor.count > 0) {
                cursor.moveToFirst()
                val columnIndex = cursor.getColumnIndex(projection[0])
                do {
                    val word = cursor.getString(columnIndex)
                    mTextView!!.append("$word\n")
                } while (cursor.moveToNext())
            } else {
                Log.d(TAG, "onClickDisplayEntries")
                mTextView!!.append("No data returned.\n")
            }
            cursor.close()
        } else {
            Log.d(TAG, "onClickDisplayEntries")
            mTextView!!.append("Cursor is null.\n"
            )
        }
    }
}