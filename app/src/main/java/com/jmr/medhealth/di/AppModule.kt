
package com.jmr.medhealth.di // Updated

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.jmr.medhealth.data.local.AppDatabase // Updated
import com.jmr.medhealth.data.local.DiagnosisDao // Updated
import com.jmr.medhealth.data.repository.AuthRepository // Updated
import com.jmr.medhealth.data.repository.AuthRepositoryImpl // Updated
import com.jmr.medhealth.data.repository.ChatRepository // <-- ADD THIS IMPORT
import com.jmr.medhealth.data.repository.DiagnosisRepository // Updated
import com.jmr.medhealth.data.repository.DiagnosisRepositoryImpl // Updated
import dagger.Module
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

import dagger.hilt.android.qualifiers.ApplicationContext



@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    // ... function bodies remain the same
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseAnalytics(@ApplicationContext context: Context): FirebaseAnalytics {
        return Firebase.analytics
    }
    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }
    @Provides
    @Singleton
    fun provideChatRepository(): ChatRepository {
        return ChatRepository()
    }
    @Provides
    @Singleton
    fun provideDiagnosisDao(database: AppDatabase): DiagnosisDao {
        return database.diagnosisDao()
    }

    @Provides
    @Singleton
    fun provideAuthRepository(auth: FirebaseAuth, firestore: FirebaseFirestore): AuthRepository {
        return AuthRepositoryImpl(auth, firestore)
    }

    @Provides
    @Singleton
    fun provideDiagnosisRepository(
        dao: DiagnosisDao,
        firestore: FirebaseFirestore,
        storage: FirebaseStorage,
        auth: FirebaseAuth
    ): DiagnosisRepository {
        return DiagnosisRepositoryImpl(dao, firestore, storage, auth)
    }
}