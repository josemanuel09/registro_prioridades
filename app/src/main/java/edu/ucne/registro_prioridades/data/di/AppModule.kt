package edu.ucne.registro_prioridades.data.di

import android.content.Context
import androidx.room.Room
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.ucne.registro_prioridades.data.local.database.PrioridadDb
import edu.ucne.registro_prioridades.data.remote.ClienteApi
import edu.ucne.registro_prioridades.data.remote.PrioridadApi
import edu.ucne.registro_prioridades.data.remote.SistemasApi
import edu.ucne.registro_prioridades.data.remote.TicketApi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    const val BASE_URL = "https://ticketssis-api-hvbmerheaqf4ckdz.eastus2-01.azurewebsites.net/"
    @Provides
    @Singleton
    fun ProvidedTicketDb(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            PrioridadDb::class.java,
            "TicketDb"
        ).fallbackToDestructiveMigration().build()

    @Provides
    fun provideTicketDao(db: PrioridadDb) = db.ticketDao()

    @Provides
    fun providePrioridadDao(db: PrioridadDb) = db.prioridadDao()



    @Singleton
    @Provides
    fun providesMoshi(): Moshi =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .add(DateAdapter())
            .build()

    @Provides
    @Singleton
    fun providesPrioridadesApi(moshi: Moshi): PrioridadApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(PrioridadApi::class.java)
    }

    @Provides
    @Singleton
    fun providesSistemasApi(moshi: Moshi): SistemasApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(SistemasApi::class.java)
    }
    @Provides
    @Singleton
    fun providesTicketApi(moshi: Moshi): TicketApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(TicketApi::class.java)
    }
    @Provides
    @Singleton
    fun providesClienteApi(moshi: Moshi): ClienteApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(ClienteApi::class.java)
    }





}