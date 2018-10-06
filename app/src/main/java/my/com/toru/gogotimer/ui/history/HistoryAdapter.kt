package my.com.toru.gogotimer.ui.history

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import my.com.toru.gogotimer.R
import my.com.toru.gogotimer.database.dao.TimerHistoryData
import java.util.*

class HistoryAdapter(var list: LinkedList<TimerHistoryData>) : RecyclerView.Adapter<HistoryViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.adapter_timer, parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bindData(list[position])
    }
}

class HistoryViewHolder(view:View):RecyclerView.ViewHolder(view){
    fun bindData(item:TimerHistoryData){

    }
}