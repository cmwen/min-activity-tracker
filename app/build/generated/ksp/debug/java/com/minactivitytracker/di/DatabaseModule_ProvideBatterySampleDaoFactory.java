package com.minactivitytracker.di;

import com.minactivitytracker.data.AppDatabase;
import com.minactivitytracker.data.dao.BatterySampleDao;
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
public final class DatabaseModule_ProvideBatterySampleDaoFactory implements Factory<BatterySampleDao> {
  private final Provider<AppDatabase> databaseProvider;

  public DatabaseModule_ProvideBatterySampleDaoFactory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public BatterySampleDao get() {
    return provideBatterySampleDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideBatterySampleDaoFactory create(
      Provider<AppDatabase> databaseProvider) {
    return new DatabaseModule_ProvideBatterySampleDaoFactory(databaseProvider);
  }

  public static BatterySampleDao provideBatterySampleDao(AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideBatterySampleDao(database));
  }
}
