package it.webilend.swdex.network


import retrofit2.http.GET
import retrofit2.http.Query
import io.reactivex.rxjava3.core.Observable
import it.webilend.swdex.model.*
import retrofit2.http.Path
import retrofit2.http.Url

/**
 * This interface is responsible for holding all APIs need to be invoked by the application.
 *
 * @author Andrea Caruso - 27th March 2021
 * @since 1.0.0
 */
interface SWRestService {
    @GET("people/")
    fun getCharacters(@Query("page") page:Int):Observable<SWAPIResponse<Character>>
    @GET("vehicles/")
    fun getVehicles():Observable<SWAPIResponse<Vehicle>>
    @GET("starships/")
    fun getStarships():Observable<SWAPIResponse<Starship>>

    @GET("people/{id}/")
    fun getCharacter(@Path("id") id:Int): Observable<Character>
    @GET("films/{id}/")
    fun getFilm(@Path("id") filmId:Int): Observable<Film>
    @GET("vehicles/{id}/")
    fun getVehicle(@Path("id") vehicleId:Int): Observable<Vehicle>
    @GET("starships/{id}/")
    fun getStarship(@Path("id") starshipId:Int): Observable<Starship>

    @GET
    fun getFilm(@Url url:String): Observable<Film>
    @GET
    fun getVehicle(@Url url:String): Observable<Vehicle>
}