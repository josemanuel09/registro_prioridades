package edu.ucne.registro_prioridades.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.ucne.registro_prioridades.data.local.database.PrioridadDb
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
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




}