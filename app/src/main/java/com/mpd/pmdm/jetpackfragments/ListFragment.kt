package com.mpd.pmdm.jetpackfragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mpd.pmdm.jetpackfragments.databinding.FragmentListBinding

class ListFragment : Fragment() {

    var _binding: FragmentListBinding? = null
    val binding get() = _binding!!

    //Declaramos una variable para almacenar la implementación de la interfaz
    //StarSignListener declarada en MainActivity.kt
    private lateinit var starSignListener: StarSignListener

    //Con este onAttach comprobamos que la Actividad que contenga este fragmento
    //implemente el starSignListener, o de lo contrario se lanzará una excepción.
    //Así aseguramos poder notificar a la Actividad cuando se haga click en un signo
    override fun onAttach(context: Context) {
        super.onAttach(context)
        //Opcion 1
//        if(context is StarSignListener){
//            starSignListener = context
//        } else{
//          throw RuntimeException("Must implement StarSignListener")
//        }
        //Opcion 2.
        if (activity is StarSignListener) starSignListener = activity as StarSignListener
        else throw RuntimeException("Must implement StarSignListener")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val starSigns = listOf<View>(
            binding.aquarius,
            binding.pisces,
            binding.aries,
            binding.taurus,
            binding.gemini,
            binding.cancer,
            binding.leo,
            binding.virgo,
            binding.libra,
            binding.scorpio,
            binding.sagittarius,
            binding.capricorn,
        )

        //A cada TextView signo le asociamos al evento onClick una llamada al Listener starSignListener
        starSigns.forEach {
            it.setOnClickListener {
                starSignListener.onSelected(it.id)
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        //Liberamos la memoria del atributo _binding
        _binding = null
    }
}