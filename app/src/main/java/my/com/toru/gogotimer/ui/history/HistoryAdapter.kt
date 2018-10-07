package my.com.toru.gogotimer.ui.history

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import my.com.toru.gogotimer.BR
import my.com.toru.gogotimer.R
import my.com.toru.gogotimer.databinding.AdapterTimerBinding
import my.com.toru.gogotimer.model.TimerHistoryData

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
         holder.binding.apply {
             setVariable(BR.historyModel, list[position])
             executePendingBindings()
         }
    }
}

class HistoryViewHolder(val binding:AdapterTimerBinding):RecyclerView.ViewHolder(binding.root)