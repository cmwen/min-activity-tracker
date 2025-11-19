package com.minactivitytracker.ui.export;

import android.content.Context;
import com.minactivitytracker.repository.ActivityRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class ExportViewModel_Factory implements Factory<ExportViewModel> {
  private final Provider<ActivityRepository> repositoryProvider;

  private final Provider<Context> contextProvider;

  public ExportViewModel_Factory(Provider<ActivityRepository> repositoryProvider,
      Provider<Context> contextProvider) {
    this.repositoryProvider = repositoryProvider;
    this.contextProvider = contextProvider;
  }

  @Override
  public ExportViewModel get() {
    return newInstance(repositoryProvider.get(), contextProvider.get());
  }

  public static ExportViewModel_Factory create(Provider<ActivityRepository> repositoryProvider,
      Provider<Context> contextProvider) {
    return new ExportViewModel_Factory(repositoryProvider, contextProvider);
  }

  public static ExportViewModel newInstance(ActivityRepository repository, Context context) {
    return new ExportViewModel(repository, context);
  }
}
