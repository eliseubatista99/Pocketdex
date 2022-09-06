package com.eliseubatista.pocketdex.network

import com.eliseubatista.pocketdex.network.items.ItemCategoriesApiData
import com.eliseubatista.pocketdex.network.items.ItemData
import com.eliseubatista.pocketdex.network.regions.LocationApiData
import com.eliseubatista.pocketdex.network.regions.LocationAreaApiData
import com.eliseubatista.pocketdex.network.regions.RegionApiData
import com.eliseubatista.pocketdex.network.pokemons.EvolutionChainData
import com.eliseubatista.pocketdex.network.pokemons.PokemonData
import com.eliseubatista.pocketdex.network.pokemons.SpeciesData
import com.eliseubatista.pocketdex.network.pokemons.TypeData
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "https://pokeapi.co/api/v2/"

//Build the moshi adapter
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


//Build the retrofit service
private fun retrofit(): Retrofit {
    val okHttpClientBuilder = OkHttpClient.Builder()

    //Create http client logging
    okHttpClientBuilder.addInterceptor(
        HttpLoggingInterceptor().setLevel(
            HttpLoggingInterceptor.Level.BASIC
        )
    )

    //Build retrofit with moshi adapter and client logging
    return Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .client(okHttpClientBuilder.build())
        .build()
}

interface PokeApiService {

    //TYPE ------------------------------------------------------------------------------

    @GET("type")
    suspend fun getTypes(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): GlobalSearchData

    @GET("type/{typeName}/")
    suspend fun getTypeByName(@Path("typeName") name: String): TypeData

    //POKEMON ------------------------------------------------------------------------------
    @GET("pokemon")
    suspend fun getPokemons(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): GlobalSearchData

    @GET("pokemon/{pokemonName}/")
    suspend fun getPokemonByName(@Path("pokemonName") name: String): PokemonData

    @GET("pokemon-species/{speciesName}/")
    suspend fun getSpeciesByName(@Path("speciesName") name: String): SpeciesData

    @GET("evolution-chain/{chainID}/")
    suspend fun getEvolutionChainById(@Path("chainID") id: String): EvolutionChainData

    //ITEM CATEGORY ------------------------------------------------------------------------------

    @GET("item-category")
    suspend fun getItemCategories(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): GlobalSearchData

    @GET("item-category/{itemCategoryName}/")
    suspend fun getItemCategoryByName(@Path("itemCategoryName") name: String): ItemCategoriesApiData

    //ITEM ------------------------------------------------------------------------------

    @GET("item")
    suspend fun getItems(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): GlobalSearchData

    @GET("item/{itemName}/")
    suspend fun getItemByName(@Path("itemName") name: String): ItemData

    //REGION ------------------------------------------------------------------------------

    @GET("region")
    suspend fun getRegions(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): GlobalSearchData

    @GET("region/{regionName}/")
    suspend fun getRegionByName(@Path("regionName") name: String): RegionApiData

    //LOCATION ------------------------------------------------------------------------------
    @GET("location")
    suspend fun getLocations(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): GlobalSearchData

    @GET("location/{locationName}/")
    suspend fun getLocationByName(@Path("locationName") name: String): LocationApiData

    @GET("location-area/{areaName}/")
    suspend fun getLocationAreaByName(@Path("areaName") name: String): LocationAreaApiData

}

object PokeApi {
    val retrofitService: PokeApiService by lazy { retrofit().create(PokeApiService::class.java) }
}