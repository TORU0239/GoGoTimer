package my.com.toru.gogotimer

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView(){
        tlb_info.setOnClickListener {
            Toast.makeText(this@MainActivity, "Under Implementation", Toast.LENGTH_SHORT).show()

        }

        tlb_history.setOnClickListener {
            Toast.makeText(this@MainActivity, "Under Construction", Toast.LENGTH_SHORT).show()
        }

        btn_increase_time.setOnClickListener {

        }

        btn_decrease_time.setOnClickListener {

        }

        ed_task.apply{
            addTextChangedListener(object: TextWatcher {
                override fun afterTextChanged(e: Editable?) {
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            })
            setOnEditorActionListener { p0, p1, p2 -> true}
        }

        btn_trigger_timer.setOnClickListener {

        }
    }
}