package my.com.toru.gogotimer.ui.main

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
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

    override fun onBackPressed() {
        if(supportFragmentManager.fragments.size > 0){
            supportFragmentManager.popBackStack()
        }
        else{
            super.onBackPressed()
        }
    }
}