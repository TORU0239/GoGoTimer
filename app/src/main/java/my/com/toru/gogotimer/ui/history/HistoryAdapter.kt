package my.com.toru.gogotimer.ui.history

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import my.com.toru.gogotimer.R
import my.com.toru.gogotimer.databinding.AdapterTimerBinding
import my.com.toru.gogotimer.model.TimerHistoryData
import java.util.*
import kotlin.collections.ArrayList

class HistoryAdapter(var list: ArrayList<TimerHistoryData>) : RecyclerView.Adapter<HistoryViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val bindingAdapter: AdapterTimerBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                                                                        R.layout.adapter_timer,
                                                                        parent,
                                                            false)
        return HistoryViewHolder(bindingAdapter)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bindData(list[position])
    }
}

class HistoryViewHolder(private val binding:AdapterTimerBinding):RecyclerView.ViewHolder(binding.root){
    fun bindData(item: TimerHistoryData){
        if(item.taskStartTimeStamp == 0L){
            binding.imgAlarm.setImageResource(R.drawable.ic_baseline_alarm_off_24px)
        }
        else{
            binding.imgAlarm.setImageResource(R.drawable.ic_baseline_alarm_on_24px)
        }

        binding.txtDescription.text = item.taskName
    }
}