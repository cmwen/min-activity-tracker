package com.minactivitytracker;

import androidx.hilt.work.HiltWorkerFactory;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class MinActivityTrackerApp_MembersInjector implements MembersInjector<MinActivityTrackerApp> {
  private final Provider<HiltWorkerFactory> workerFactoryProvider;

  public MinActivityTrackerApp_MembersInjector(Provider<HiltWorkerFactory> workerFactoryProvider) {
    this.workerFactoryProvider = workerFactoryProvider;
  }

  public static MembersInjector<MinActivityTrackerApp> create(
      Provider<HiltWorkerFactory> workerFactoryProvider) {
    return new MinActivityTrackerApp_MembersInjector(workerFactoryProvider);
  }

  @Override
  public void injectMembers(MinActivityTrackerApp instance) {
    injectWorkerFactory(instance, workerFactoryProvider.get());
  }

  @InjectedFieldSignature("com.minactivitytracker.MinActivityTrackerApp.workerFactory")
  public static void injectWorkerFactory(MinActivityTrackerApp instance,
      HiltWorkerFactory workerFactory) {
    instance.workerFactory = workerFactory;
  }
}
