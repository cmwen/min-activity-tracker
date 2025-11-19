package com.minactivitytracker.di;

import com.minactivitytracker.data.AppDatabase;
import com.minactivitytracker.data.dao.DeviceEventDao;
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
public final class DatabaseModule_ProvideDeviceEventDaoFactory implements Factory<DeviceEventDao> {
  private final Provider<AppDatabase> databaseProvider;

  public DatabaseModule_ProvideDeviceEventDaoFactory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public DeviceEventDao get() {
    return provideDeviceEventDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideDeviceEventDaoFactory create(
      Provider<AppDatabase> databaseProvider) {
    return new DatabaseModule_ProvideDeviceEventDaoFactory(databaseProvider);
  }

  public static DeviceEventDao provideDeviceEventDao(AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideDeviceEventDao(database));
  }
}
