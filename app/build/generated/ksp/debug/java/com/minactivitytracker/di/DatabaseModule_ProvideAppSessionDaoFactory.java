package com.minactivitytracker.di;

import com.minactivitytracker.data.AppDatabase;
import com.minactivitytracker.data.dao.AppSessionDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideAppSessionDaoFactory implements Factory<AppSessionDao> {
  private final Provider<AppDatabase> databaseProvider;

  public DatabaseModule_ProvideAppSessionDaoFactory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public AppSessionDao get() {
    return provideAppSessionDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideAppSessionDaoFactory create(
      Provider<AppDatabase> databaseProvider) {
    return new DatabaseModule_ProvideAppSessionDaoFactory(databaseProvider);
  }

  public static AppSessionDao provideAppSessionDao(AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideAppSessionDao(database));
  }
}
