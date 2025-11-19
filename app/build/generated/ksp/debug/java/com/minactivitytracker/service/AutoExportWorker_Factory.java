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
public final class AutoExportWorker_Factory {
  private final Provider<ActivityRepository> repositoryProvider;

  public AutoExportWorker_Factory(Provider<ActivityRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  public AutoExportWorker get(Context context, WorkerParameters workerParams) {
    return newInstance(context, workerParams, repositoryProvider.get());
  }

  public static AutoExportWorker_Factory create(Provider<ActivityRepository> repositoryProvider) {
    return new AutoExportWorker_Factory(repositoryProvider);
  }

  public static AutoExportWorker newInstance(Context context, WorkerParameters workerParams,
      ActivityRepository repository) {
    return new AutoExportWorker(context, workerParams, repository);
  }
}
