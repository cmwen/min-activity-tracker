package com.minactivitytracker.repository;

import com.minactivitytracker.data.dao.AppSessionDao;
import com.minactivitytracker.data.dao.DeviceEventDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class ActivityRepository_Factory implements Factory<ActivityRepository> {
  private final Provider<AppSessionDao> appSessionDaoProvider;

  private final Provider<DeviceEventDao> deviceEventDaoProvider;

  public ActivityRepository_Factory(Provider<AppSessionDao> appSessionDaoProvider,
      Provider<DeviceEventDao> deviceEventDaoProvider) {
    this.appSessionDaoProvider = appSessionDaoProvider;
    this.deviceEventDaoProvider = deviceEventDaoProvider;
  }

  @Override
  public ActivityRepository get() {
    return newInstance(appSessionDaoProvider.get(), deviceEventDaoProvider.get());
  }

  public static ActivityRepository_Factory create(Provider<AppSessionDao> appSessionDaoProvider,
      Provider<DeviceEventDao> deviceEventDaoProvider) {
    return new ActivityRepository_Factory(appSessionDaoProvider, deviceEventDaoProvider);
  }

  public static ActivityRepository newInstance(AppSessionDao appSessionDao,
      DeviceEventDao deviceEventDao) {
    return new ActivityRepository(appSessionDao, deviceEventDao);
  }
}
