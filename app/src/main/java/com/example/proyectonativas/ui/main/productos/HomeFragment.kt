package com.example.proyectonativas.ui.main.productos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectonativas.R

class HomeFragment : Fragment() {

    private val listaProducts= listOf(
        Product("Proteina", 256000.0, R.drawable.proteina1),
        Product("Guantes", 40000.0, R.drawable.guantes1),
        Product("Trembolona", 400000.0, R.drawable.trembolona1),
        Product("bandas de poder", 28000.0, R.drawable.bandas1),
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home,container,false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_poductos)
        recyclerView.layoutManager= GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = ProductAdapter(listaProducts)

        return view
    }
}