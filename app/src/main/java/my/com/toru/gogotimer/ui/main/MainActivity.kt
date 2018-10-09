package my.com.toru.gogotimer.ui.main

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import my.com.toru.gogotimer.R
import my.com.toru.gogotimer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(){
    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    lateinit var mainBinding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainBinding.apply {
            mainViewModel = MainViewModel()
        }
    }

    override fun onResume() {
        super.onResume()
        mainBinding.mainViewModel?.onResume()
    }

    override fun onPause() {
        mainBinding.mainViewModel?.onPause()
        super.onPause()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.w(TAG, "onNewIntent")
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        Log.w(TAG, "onSaveInstanceState")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.w(TAG, "onRestoreInstanceState")
    }

    override fun onBackPressed() {
        if(supportFragmentManager.fragments.size > 0){
            supportFragmentManager.popBackStack()
        }
        else{
            super.onBackPressed()
        }
    }
}