package it.webilend.swdex.network


import it.webilend.swdex.model.Character
import retrofit2.http.GET
import retrofit2.http.Query
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Path

/**
 * This interface is responsible for holding all APIs need to be invoked by the application.
 *
 * @author Andrea Caruso - 27th March 2021
 * @since 1.0.0
 */
interface SWRestService {
    /**
     * Returns Star Wars characters list.
     *
     * @return {@link Observable<Array<Character>>}
     */
    @GET("people/")
    fun getCharacters():Observable<Array<Character>>
    @GET("people/{id}/")
    fun getCharacter(@Path("id") id:Int): Observable<Character>
}