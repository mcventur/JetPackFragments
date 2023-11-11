package com.mpd.pmdm.jetpackfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.mpd.pmdm.jetpackfragments.databinding.FragmentDetailBinding

/**
 * A simple [Fragment] subclass.
 * Use the [DetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailFragment : Fragment() {
    var _binding: FragmentDetailBinding? = null
    val binding get() = _binding!!

    val args: DetailFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    /*
    Aquí recuperamos el valor de la clave STAR_SIGN_ID que se ha añadido como argumento
    al instanciar el fragmento (ver la función newInstance del companion object)
    y se pasa a setStarSignData.
    Se tiene que hacer en onViewCreated porque en setStarSignData se accede a las vistas
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setStarSignData(args.starSignId)
    }

    /**
     * Cumplimenta la IU con los datos del signo
     */
    fun setStarSignData(starSignId: Int) {
        when (starSignId) {
            R.id.aquarius -> {
                binding.starSign.text = getString(R.string.aquarius)
                binding.symbol.text = getString(R.string.symbol, "Water Carrier")
                binding.dateRange.text = getString(
                    R.string.date_range,
                    "January 20 - February 18"
                )
            }

            R.id.pisces -> {
                binding.starSign.text = getString(R.string.pisces)
                binding.symbol.text = getString(R.string.symbol, "Fish")
                binding.dateRange.text = getString(R.string.date_range, "February 19 - March 20")
            }

            R.id.aries -> {
                binding.starSign.text = getString(R.string.aries)
                binding.symbol.text = getString(R.string.symbol, "Ram")
                binding.dateRange.text = getString(R.string.date_range, "March 21 - April 19")
            }

            R.id.taurus -> {
                binding.starSign.text = getString(R.string.taurus)
                binding.symbol.text = getString(R.string.symbol, "Bull")
                binding.dateRange.text = getString(R.string.date_range, "April 20 - May 20")
            }

            R.id.gemini -> {
                binding.starSign.text = getString(R.string.gemini)
                binding.symbol.text = getString(R.string.symbol, "Twins")
                binding.dateRange.text = getString(R.string.date_range, "May 21 - June 20")
            }

            R.id.cancer -> {
                binding.starSign.text = getString(R.string.cancer)
                binding.symbol.text = getString(R.string.symbol, "Crab")
                binding.dateRange.text = getString(R.string.date_range, "June 21 - July 22")
            }

            R.id.leo -> {
                binding.starSign.text = getString(R.string.leo)
                binding.symbol.text = getString(R.string.symbol, "Lion")
                binding.dateRange.text = getString(R.string.date_range, "July 23 - August 22")
            }

            R.id.virgo -> {
                binding.starSign.text = getString(R.string.virgo)
                binding.symbol.text = getString(R.string.symbol, "Virgin")
                binding.dateRange.text = getString(R.string.date_range, "August 23 - September 22")
            }

            R.id.libra -> {
                binding.starSign.text = getString(R.string.libra)
                binding.symbol.text = getString(R.string.symbol, "Scales")
                binding.dateRange.text = getString(R.string.date_range, "September 23 - October 22")
            }

            R.id.scorpio -> {
                binding.starSign.text = getString(R.string.scorpio)
                binding.symbol.text = getString(R.string.symbol, "Scorpion")
                binding.dateRange.text = getString(R.string.date_range, "October 23 - November 21")
            }

            R.id.sagittarius -> {
                binding.starSign.text = getString(R.string.sagittarius)
                binding.symbol.text = getString(R.string.symbol, "Archer")
                binding.dateRange.text = getString(R.string.date_range, "November 22 - December 21")
            }

            R.id.capricorn -> {
                binding.starSign.text = getString(R.string.capricorn)
                binding.symbol.text = getString(R.string.symbol, "Mountain Goat")
                binding.dateRange.text = getString(R.string.date_range, "December 22 - January 19")
            }

            else -> {
                Toast.makeText(context, getString(R.string.unknown_star_sign), Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    companion object {
        private const val STAR_SIGN_ID = "STAR_SIGN_ID"

        /*
        Función pública y estática, que
        toma como entrada una starSignId, crea una nueva instancia del
        DetailFragment, y añade el starSignId en un nuevo Bundle
        Se hace igual que con intent.extras, con pares clave/valor

        Es un static factory method: un método estático de la clase que nos genera
        instancias de la misma, como alternativa al uso de constructores. Es un constrcutor
        más flexible, que hace lo que nos dé la gana. En este caso, añadir directamente el argumento al Bundle
         */
        @JvmStatic
        fun newInstance(starSignId: Int) =
            DetailFragment().apply {
                arguments = Bundle().apply {
                    //Guardamos el id del signo del zodiaco
                    putInt(STAR_SIGN_ID, starSignId)
                }
            }
    }
}