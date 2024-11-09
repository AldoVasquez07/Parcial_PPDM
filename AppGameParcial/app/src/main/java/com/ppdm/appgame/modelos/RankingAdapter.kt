package com.ppdm.appgame.modelos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ppdm.appgame.R

class RankingAdapter(private val rankingList: List<Ranking>) : RecyclerView.Adapter<RankingAdapter.RankingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ranking, parent, false)
        return RankingViewHolder(view)
    }

    override fun onBindViewHolder(holder: RankingViewHolder, position: Int) {
        val rankingItem = rankingList[position]
        holder.usernameTextView.text = rankingItem.username
        holder.scoreTextView.text = rankingItem.score.toString()
    }

    override fun getItemCount(): Int {
        return rankingList.size
    }

    // ViewHolder para los ítems de ranking
    class RankingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val usernameTextView: TextView = view.findViewById(R.id.usernameTextView)
        val scoreTextView: TextView = view.findViewById(R.id.scoreTextView)
    }
}