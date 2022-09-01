package com.example.contentprovider

import android.net.Uri

object Contract {
    /**
     * The content URI needs the following structure:
     * * scheme (or content URIs, this is always content://)
     * * authority (this represents the domain)
     * * path (this represents the path to the page)
     * * ID (this is the name of the file/table, unique within the namespace)
     *
     */
    // Customarily, to make Authority unique, it's the package name extended with "provider".
    // Must match with the authority defined in Android Manifest.
    const val AUTHORITY = "com.android.example.wordcontentprovioder.provider"

    // The content path is an abstract semantic identifier of the data you are interested in.
    const val CONTENT_PATH = "words"

    // A content:// style URI to the authority for this table */
    val CONTENT_URI: Uri? = Uri.parse("content://$AUTHORITY/$CONTENT_PATH")
    const val ALL_ITEMS = -2
    const val WORD_ID = "id"

    // MIME types for this content provider.
    const val SINGLE_RECORD_MIME_TYPE = "vnd.android.cursor.item/vnd.com.example.provider.words"
    const val MULTIPLE_RECORDS_MIME_TYPE = "vnd.android.cursor.dir/vnd.com.example.provider.words"
}
