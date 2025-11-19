package com.minactivitytracker.repository;

import com.minactivitytracker.data.dao.BatterySampleDao;
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
public final class BatteryRepository_Factory implements Factory<BatteryRepository> {
  private final Provider<BatterySampleDao> batterySampleDaoProvider;

  public BatteryRepository_Factory(Provider<BatterySampleDao> batterySampleDaoProvider) {
    this.batterySampleDaoProvider = batterySampleDaoProvider;
  }

  @Override
  public BatteryRepository get() {
    return newInstance(batterySampleDaoProvider.get());
  }

  public static BatteryRepository_Factory create(
      Provider<BatterySampleDao> batterySampleDaoProvider) {
    return new BatteryRepository_Factory(batterySampleDaoProvider);
  }

  public static BatteryRepository newInstance(BatterySampleDao batterySampleDao) {
    return new BatteryRepository(batterySampleDao);
  }
}
