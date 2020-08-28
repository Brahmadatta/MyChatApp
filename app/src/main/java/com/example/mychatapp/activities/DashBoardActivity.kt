package com.example.mychatapp.activities

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.mychatapp.R
import com.example.mychatapp.adapters.SectionPagerAdapter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_dash_board.*

class DashBoardActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {

        var sectionPagerAdapter : SectionPagerAdapter ?= null

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)

        supportActionBar!!.title = "Dashboard"

        sectionPagerAdapter = SectionPagerAdapter(supportFragmentManager)
        dashboard_viewPager.adapter = sectionPagerAdapter
        mainTabs.setupWithViewPager(dashboard_viewPager)
        mainTabs.setTabTextColors(Color.WHITE,Color.GREEN)


        if (intent.extras != null)
        {
            var username = intent.extras!!.get("name")
            Toast.makeText(this,username.toString(),Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)

        menuInflater.inflate(R.menu.menu,menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)


        if (item != null)
        {
            if (item.itemId == R.id.logoutId)
            {
                //Logout
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }

            if (item.itemId == R.id.settingsId)
            {
                //Settings
                startActivity(Intent(this,SettingsActivity::class.java))
            }
        }


        return true
    }
}