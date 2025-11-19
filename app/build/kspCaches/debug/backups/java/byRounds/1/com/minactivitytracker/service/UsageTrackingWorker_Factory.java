package com.minactivitytracker.service;

import android.content.Context;
import androidx.work.WorkerParameters;
import com.minactivitytracker.repository.ActivityRepository;
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

  public UsageTrackingWorker_Factory(Provider<ActivityRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  public UsageTrackingWorker get(Context context, WorkerParameters workerParams) {
    return newInstance(context, workerParams, repositoryProvider.get());
  }

  public static UsageTrackingWorker_Factory create(
      Provider<ActivityRepository> repositoryProvider) {
    return new UsageTrackingWorker_Factory(repositoryProvider);
  }

  public static UsageTrackingWorker newInstance(Context context, WorkerParameters workerParams,
      ActivityRepository repository) {
    return new UsageTrackingWorker(context, workerParams, repository);
  }
}
