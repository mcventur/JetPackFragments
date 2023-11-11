package com.mpd.pmdm.jetpackfragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.ListFragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.mpd.pmdm.jetpackfragments.databinding.FragmentListBinding

class ListFragment : Fragment() {

    var _binding: FragmentListBinding? = null
    val binding get() = _binding!!

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

        //Recordad que "it" aquí representa el único parámetro de la lambda: https://kotlinlang.org/docs/lambdas.html#it-implicit-name-of-a-single-parameter
        starSigns.forEach {
            //Creamos un Bundle() para el paso de datos, y le añadimos el id del signo pulsado
            val fragmentBundle = Bundle()
            fragmentBundle.putInt(STAR_SIGN_ID, it.id)

            //Al hacer click en un signo (it)
            it.setOnClickListener {
                val action  = ListFragmentDirections.actionListFragmentToDetailFragment(starSignId = it.id)
                findNavController().navigate(action)
            }

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        //Liberamos la memoria del atributo _binding
        _binding = null
    }
}