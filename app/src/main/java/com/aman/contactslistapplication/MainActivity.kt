package com.aman.contactslistapplication

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.ListView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.cursoradapter.widget.CursorAdapter
import androidx.loader.content.CursorLoader
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader

private val FROM_COLUMNS: Array<String> = arrayOf(
    if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)) {
        ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
    } else {
        ContactsContract.Contacts.DISPLAY_NAME
    }
)
/*
 * Defines an array that contains resource ids for the layout views
 * that get the Cursor column contents. The id is pre-defined in
 * the Android framework, so it is prefaced with "android.R.id"
 */
private val TO_IDS: IntArray = intArrayOf(android.R.id.text1)

class MainActivity : AppCompatActivity(),
    LoaderManager.LoaderCallbacks<Cursor>{
    lateinit var cursor: Cursor
    lateinit var listView: ListView
    private  val TAG = "MainActivity"
    lateinit var cursorAdapter: CursorAdapter
    var getContactsPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()    ) {

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listView = findViewById(R.id.listView)
        cursorAdapter = SimpleCursorAdapter(
            this,
            R.layout.contacts_list_item,
            null,
            FROM_COLUMNS, TO_IDS,
            0
        )
        listView.adapter = cursorAdapter

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            getContactsPermission.launch(Manifest.permission.READ_CONTACTS)
        }else{
            getSupportLoaderManager().initLoader(0, null, this);

        }
    }

    override fun onLoadFinished(loader: Loader<Cursor>, cursor: Cursor) {
        // Put the result Cursor in the adapter for the ListView
        cursorAdapter?.swapCursor(cursor)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return CursorLoader(
            this,
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            null
        )
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {

    }

}
