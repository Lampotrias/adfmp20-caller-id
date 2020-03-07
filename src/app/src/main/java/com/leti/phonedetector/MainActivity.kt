package com.leti.phonedetector

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val APP_PREFERENCES = "PHONEDETECTOR_PREFERENCES"
    private lateinit var sharedPreferences: SharedPreferences

    // Sample of input data
    // TODO get this from sqlite
    val phones = arrayOf(PhoneInfo("Max", "+79992295999", false),
                         PhoneInfo("Sber", "+79992295998", true),
                         PhoneInfo("Pizza", "+79992295997", false),
                         PhoneInfo("Citron", "+79992295996", false))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        createSharedPref()
        createRecycleView()

        // Samples TODO remove it later
        createSpamOverlay()
        createNotSpamOverlay()
    }

    private fun createRecycleView(){
        /**
         * Create RecycleView with list of phone call activity of user
         */
        val recyclerView = findViewById<View>(R.id.list_of_phones) as RecyclerView
        val adapter = DataAdapter(this, phones)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.itemAnimator = DefaultItemAnimator()
    }

    private fun createSpamOverlay(){
        val mIntent = Intent(this, OverlayActivity::class.java)
        mIntent.putExtra("number", phones[1].number)
        mIntent.putExtra("name", phones[1].name)
        mIntent.putExtra("is_spam", phones[1].isSpam)

        this.startActivity(mIntent)
    }

    private fun createNotSpamOverlay(){
        val mIntent = Intent(this, OverlayActivity::class.java)
        mIntent.putExtra("number", phones[0].number)
        mIntent.putExtra("name", phones[0].name)
        mIntent.putExtra("is_spam", phones[0].isSpam)

        this.startActivity(mIntent)
    }

    @SuppressLint("CommitPrefEdits")
    fun createSharedPref(){
        /**
         * Init Shared Preferences for save current options in Menu after close app
         * Options: is_show_spam, is_show_not_spam
         */
        sharedPreferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        /**
         * Fill items of menu with their values from  Shared Preferences or set default
         * @param menu: Menu of filter: Show spam, Show not spam
         * @return default value true
         */
        menuInflater.inflate(R.menu.menu_main, menu)

        menu.findItem(R.id.item_menu_option_is_show_spam).isChecked = sharedPreferences.getBoolean("is_show_spam", true)
        menu.findItem(R.id.item_menu_option_is_not_show_spam).isChecked = sharedPreferences.getBoolean("is_show_not_spam", true)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        /**
         * Detect touch event on item in menu list. Select action for every option
         * @param item element of list on which touch event is detected
         * @return default value true if ok
         */
        return when (item.itemId) {
            R.id.item_menu_option_is_show_spam -> {clickedOnShowSpam(item); true}
            R.id.item_menu_option_is_not_show_spam -> {clickedOnShowNotSpam(item); true}
            R.id.item_menu_option_setting -> {clickedOnSetting(); true}
            else -> false
        }
    }

    private fun clickedOnShowSpam(item : MenuItem){
        /**
         * Update state in Shared Pref and CheckBox state of 'Show Spam'
         * @param item element 'Show Spam'
         */
        updateMenuOptionState(item, "is_show_spam", !item.isChecked)

        // TODO Set filters here
        Toast.makeText(this, "Clicked on Show Spam. State: ${item.isChecked}", Toast.LENGTH_LONG).show()
    }

    private fun clickedOnShowNotSpam(item : MenuItem){
        /**
         * Update state in Shared Pref and CheckBox state of 'Show Not Spam'
         * @param item element 'Show Spam'
         */
        updateMenuOptionState(item, "is_show_not_spam", !item.isChecked)

        // TODO Set filters here
        Toast.makeText(this, "Clicked on Show Not Spam. State: ${item.isChecked}", Toast.LENGTH_LONG).show()
    }

    private fun clickedOnSetting(){
        /**
         * Redirect on Settings Activity
         */

        // TODO Create intent of Settings activity and start it
        Toast.makeText(this, "Clicked on Settings", Toast.LENGTH_LONG).show()
    }

    private fun updateMenuOptionState(item : MenuItem, key : String, state : Boolean){
        /**
         * Save state of element of list
         * @param item element of list. Example: 'Show spam'
         * @param key key in Shared Pref map. Example: 'is_show_not_spam'
         * @param state: boolean, clicked or not
         */
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, state)
        editor.apply()

        item.isChecked = state
    }
}