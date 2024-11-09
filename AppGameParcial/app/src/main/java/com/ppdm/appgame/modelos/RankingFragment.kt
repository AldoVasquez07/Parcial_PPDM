package com.ppdm.appgame.modelos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ppdm.appgame.R

class RankingFragment : Fragment() {

    private lateinit var rankingRecyclerView: RecyclerView
    private lateinit var rankingAdapter: RankingAdapter

    // Lista de ejemplo de jugadores y puntuaciones
    private val rankingList = RankingHelper.rankings

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ranking, container, false)

        // Configurar el RecyclerView
        rankingRecyclerView = view.findViewById(R.id.rankingRecyclerView)
        rankingRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        rankingAdapter = RankingAdapter(rankingList)
        rankingRecyclerView.adapter = rankingAdapter

        return view
    }
}

// Data class para representar un ítem en el ranking


// Adapter para manejar el RecyclerView

