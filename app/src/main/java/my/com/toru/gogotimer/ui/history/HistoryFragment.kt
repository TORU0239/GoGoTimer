package my.com.toru.gogotimer.ui.history


import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_history.*
import my.com.toru.gogotimer.R
import my.com.toru.gogotimer.app.GoGoTimerApp
import my.com.toru.gogotimer.database.AppDatabase
import my.com.toru.gogotimer.databinding.ActivityHistoryBinding
import my.com.toru.gogotimer.model.TimerHistoryData
import java.util.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class HistoryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?{
        val historyDataBinding:ActivityHistoryBinding = DataBindingUtil.inflate(inflater, R.layout.activity_history, container, false)
        historyDataBinding.historyViewModel = HistoryViewModel()
        return historyDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView(){
        val db = AppDatabase.getInstance(GoGoTimerApp.applicationContext())
        val totalDB = db?.timerHistoryDao()?.getAll()
        rcvTimerHistory.adapter = HistoryAdapter(totalDB as ArrayList<TimerHistoryData>)
        rcvTimerHistory.addItemDecoration(DividerItemDecoration(GoGoTimerApp.applicationContext(), DividerItemDecoration.VERTICAL))
    }

    companion object {
        @JvmStatic
        fun newInstance() = HistoryFragment()
    }
}