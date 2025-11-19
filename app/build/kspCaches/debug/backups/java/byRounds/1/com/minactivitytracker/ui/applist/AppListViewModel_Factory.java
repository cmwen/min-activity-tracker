package com.minactivitytracker.ui.applist;

import com.minactivitytracker.repository.ActivityRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class AppListViewModel_Factory implements Factory<AppListViewModel> {
  private final Provider<ActivityRepository> repositoryProvider;

  public AppListViewModel_Factory(Provider<ActivityRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public AppListViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static AppListViewModel_Factory create(Provider<ActivityRepository> repositoryProvider) {
    return new AppListViewModel_Factory(repositoryProvider);
  }

  public static AppListViewModel newInstance(ActivityRepository repository) {
    return new AppListViewModel(repository);
  }
}
