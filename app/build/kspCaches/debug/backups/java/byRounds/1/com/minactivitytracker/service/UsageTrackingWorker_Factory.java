package com.minactivitytracker.service;

import android.content.Context;
import androidx.work.WorkerParameters;
import com.minactivitytracker.repository.ActivityRepository;
import com.minactivitytracker.repository.SettingsRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class UsageTrackingWorker_Factory {
  private final Provider<ActivityRepository> repositoryProvider;

  private final Provider<SettingsRepository> settingsRepositoryProvider;

  public UsageTrackingWorker_Factory(Provider<ActivityRepository> repositoryProvider,
      Provider<SettingsRepository> settingsRepositoryProvider) {
    this.repositoryProvider = repositoryProvider;
    this.settingsRepositoryProvider = settingsRepositoryProvider;
  }

  public UsageTrackingWorker get(Context context, WorkerParameters workerParams) {
    return newInstance(context, workerParams, repositoryProvider.get(), settingsRepositoryProvider.get());
  }

  public static UsageTrackingWorker_Factory create(Provider<ActivityRepository> repositoryProvider,
      Provider<SettingsRepository> settingsRepositoryProvider) {
    return new UsageTrackingWorker_Factory(repositoryProvider, settingsRepositoryProvider);
  }

  public static UsageTrackingWorker newInstance(Context context, WorkerParameters workerParams,
      ActivityRepository repository, SettingsRepository settingsRepository) {
    return new UsageTrackingWorker(context, workerParams, repository, settingsRepository);
  }
}
