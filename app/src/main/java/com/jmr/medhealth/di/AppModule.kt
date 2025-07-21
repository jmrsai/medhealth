package com.jmr.medhealth.di

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.jmr.medhealth.data.local.AppDatabase
import com.jmr.medhealth.data.local.DiagnosisDao
import com.jmr.medhealth.data.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseAnalytics(@ApplicationContext context: Context): FirebaseAnalytics {
        return Firebase.analytics
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
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

    // ChatRepository now depends on RemoteConfigRepository, so we provide it here.
    @Provides
    @Singleton
    fun provideChatRepository(remoteConfig: RemoteConfigRepository): ChatRepository {
        return ChatRepository(remoteConfig)
    }

    // RemoteConfigRepository has an @Inject constructor, but providing it here is fine too.
    @Provides
    @Singleton
    fun provideRemoteConfigRepository(): RemoteConfigRepository {
        return RemoteConfigRepository()
    }

    @Provides
    @Singleton
    fun provideDocumentRepository(): DocumentRepository {
        // Assuming DocumentRepository has an @Inject constructor()
        return DocumentRepository()
    }
}