package net.techandgraphics.hymn.di

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

  @Singleton
  @Provides
  fun providesFirebaseAnalytics(
    @ApplicationContext context: Context
  ): FirebaseAnalytics = FirebaseAnalytics.getInstance(context)
}
