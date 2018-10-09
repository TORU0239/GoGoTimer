package my.com.toru.gogotimer.ui.history


import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_history.*
import my.com.toru.gogotimer.R
import my.com.toru.gogotimer.app.GoGoTimerApp
import my.com.toru.gogotimer.databinding.ActivityHistoryBinding
import my.com.toru.gogotimer.model.TimerHistoryData
import java.util.*

class HistoryFragment : Fragment() {
    lateinit var historyDataBinding:ActivityHistoryBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?{
        historyDataBinding = DataBindingUtil.inflate(inflater, R.layout.activity_history, container, false)
        historyDataBinding.historyViewModel = HistoryViewModel()
        return historyDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView(){
        historyDataBinding.historyViewModel?.loadDataFromDao{
            activity?.runOnUiThread {
                progress_history.visibility = View.GONE
                if(it.isEmpty()){
                    with(txt_no_data){
                        ll_no_data_container.visibility = View.VISIBLE
                        animation = AnimationUtils.loadAnimation(context, R.anim.no_data_anim)
                        animation.start()
                    }
                }
                else{
                    val itemDecoration = DividerItemDecoration(GoGoTimerApp.applicationContext(), DividerItemDecoration.VERTICAL)
                    itemDecoration.setDrawable(GoGoTimerApp.applicationContext().getDrawable(R.drawable.shape_divider))
                    val controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_recyclerview_anim)

                    with(rcvTimerHistory){
                        layoutAnimation = controller
                        adapter = HistoryAdapter(it as ArrayList<TimerHistoryData>)
                        addItemDecoration(itemDecoration)
                        scheduleLayoutAnimation()
                    }
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = HistoryFragment()
    }
}