package it.webilend.swdex.network


import it.webilend.swdex.model.Character
import retrofit2.http.GET
import retrofit2.http.Query
import io.reactivex.rxjava3.core.Observable
import it.webilend.swdex.model.Film
import it.webilend.swdex.model.SWAPIResponse
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
    fun getCharacters():Observable<SWAPIResponse<Character>>
    @GET("people/{id}/")
    fun getCharacter(@Path("id") id:Int): Observable<Character>
    @GET("/films/{id}/")
    fun getFilm(@Path("id") filmId:Int): Observable<Film>
}