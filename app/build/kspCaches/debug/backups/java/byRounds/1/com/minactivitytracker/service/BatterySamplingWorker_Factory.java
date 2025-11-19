package com.minactivitytracker.service;

import android.content.Context;
import androidx.work.WorkerParameters;
import com.minactivitytracker.repository.BatteryRepository;
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
public final class BatterySamplingWorker_Factory {
  private final Provider<BatteryRepository> repositoryProvider;

  private final Provider<SettingsRepository> settingsRepositoryProvider;

  public BatterySamplingWorker_Factory(Provider<BatteryRepository> repositoryProvider,
      Provider<SettingsRepository> settingsRepositoryProvider) {
    this.repositoryProvider = repositoryProvider;
    this.settingsRepositoryProvider = settingsRepositoryProvider;
  }

  public BatterySamplingWorker get(Context context, WorkerParameters workerParams) {
    return newInstance(context, workerParams, repositoryProvider.get(), settingsRepositoryProvider.get());
  }

  public static BatterySamplingWorker_Factory create(Provider<BatteryRepository> repositoryProvider,
      Provider<SettingsRepository> settingsRepositoryProvider) {
    return new BatterySamplingWorker_Factory(repositoryProvider, settingsRepositoryProvider);
  }

  public static BatterySamplingWorker newInstance(Context context, WorkerParameters workerParams,
      BatteryRepository repository, SettingsRepository settingsRepository) {
    return new BatterySamplingWorker(context, workerParams, repository, settingsRepository);
  }
}
