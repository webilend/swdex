package it.webilend.swdex

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.CollapsingToolbarLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import it.webilend.swdex.adapter.FilmRecyclerViewAdapter
import it.webilend.swdex.adapter.VehicleRecyclerViewAdapter
import it.webilend.swdex.model.Character
import it.webilend.swdex.model.Film
import it.webilend.swdex.model.Vehicle

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [ItemListActivity]
 * in two-pane mode (on tablets) or a [ItemDetailActivity]
 * on handsets.
 */
class ItemDetailFragment : Fragment() {
    private var character: Character? = null
    var vehicles:List<Vehicle> = mutableListOf()
    var films:List<Film> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                // Load the dummy content specified by the fragment
                // arguments. In a real-world scenario, use a Loader
                // to load content from a content provider.
                character = SWManager.characters.find { char -> char.id == it.getString(ARG_ITEM_ID) }
                activity?.findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout)?.title = character?.name
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.item_detail, container, false)
        rootView.findViewById<ProgressBar>(R.id.item_film_progressbar).visibility = View.VISIBLE
        rootView.findViewById<ProgressBar>(R.id.item_vehicle_progressbar).visibility = View.VISIBLE
        // Show the dummy content as text in a TextView.
        character?.let {
            rootView.findViewById<TextView>(R.id.item_sex).text = it.gender
            rootView.findViewById<TextView>(R.id.item_eyescolor).text = it.eyesColor
            rootView.findViewById<TextView>(R.id.item_haircolor).text = it.hairColor
            rootView.findViewById<TextView>(R.id.item_skincolor).text = it.skinColor
            rootView.findViewById<TextView>(R.id.item_height).text = it.height
            //rootView.findViewById<TextView>(R.id.item_sex).text = it.gender
            //rootView.findViewById<TextView>(R.id.item_sex).text = it.gender

            // Do films' WS calls
            val filmSource = arrayListOf<Observable<Film>>()
            it.films?.forEach { filmUrl ->
                filmSource.add(SWManager.swRestService.getFilm(filmUrl))
            }
            if(filmSource.isNotEmpty()) {
                rootView.findViewById<ProgressBar>(R.id.item_film_progressbar).visibility = View.VISIBLE
            }
            Observable.zip(filmSource) { args -> args.asList()}
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe { films ->
                    this.films = films as List<Film>
                    rootView.findViewById<RecyclerView>(R.id.item_films).adapter = FilmRecyclerViewAdapter(requireActivity(), this.films)
                    rootView.findViewById<RecyclerView>(R.id.item_films).layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
                    rootView.findViewById<ProgressBar>(R.id.item_film_progressbar).visibility = View.GONE
                }
            // Do vehicles' WS calls
            val vehicleSource = arrayListOf<Observable<Vehicle>>()
            it.vehicles?.forEach { vehicleUrl ->
                vehicleSource.add(SWManager.swRestService.getVehicle(vehicleUrl))
            }
            if(vehicleSource.isNotEmpty()) {
                rootView.findViewById<ProgressBar>(R.id.item_vehicle_progressbar).visibility = View.VISIBLE
            }
            Observable.zip(vehicleSource) { args -> args.asList() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe { vehicles ->
                this.vehicles = vehicles as List<Vehicle>
                rootView.findViewById<RecyclerView>(R.id.item_vehicles).adapter = VehicleRecyclerViewAdapter(requireActivity(), this.vehicles)
                rootView.findViewById<RecyclerView>(R.id.item_vehicles).layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
                rootView.findViewById<ProgressBar>(R.id.item_vehicle_progressbar).visibility = View.GONE
            }
        }
        return rootView
    }


    companion object {
        const val ARG_ITEM_ID = "item_id"
    }
}