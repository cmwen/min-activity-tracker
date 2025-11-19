package com.minactivitytracker.data;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.minactivitytracker.data.dao.AppSessionDao;
import com.minactivitytracker.data.dao.AppSessionDao_Impl;
import com.minactivitytracker.data.dao.BatterySampleDao;
import com.minactivitytracker.data.dao.BatterySampleDao_Impl;
import com.minactivitytracker.data.dao.DeviceEventDao;
import com.minactivitytracker.data.dao.DeviceEventDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile AppSessionDao _appSessionDao;

  private volatile DeviceEventDao _deviceEventDao;

  private volatile BatterySampleDao _batterySampleDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `app_sessions` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `packageName` TEXT NOT NULL, `startTimestamp` INTEGER NOT NULL, `endTimestamp` INTEGER NOT NULL, `durationMs` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `device_events` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `type` TEXT NOT NULL, `timestamp` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `battery_samples` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `timestamp` INTEGER NOT NULL, `levelPct` INTEGER NOT NULL, `isCharging` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'c34f89650b2ed4e31eb7e914d9ff1236')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `app_sessions`");
        db.execSQL("DROP TABLE IF EXISTS `device_events`");
        db.execSQL("DROP TABLE IF EXISTS `battery_samples`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsAppSessions = new HashMap<String, TableInfo.Column>(5);
        _columnsAppSessions.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppSessions.put("packageName", new TableInfo.Column("packageName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppSessions.put("startTimestamp", new TableInfo.Column("startTimestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppSessions.put("endTimestamp", new TableInfo.Column("endTimestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppSessions.put("durationMs", new TableInfo.Column("durationMs", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAppSessions = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAppSessions = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAppSessions = new TableInfo("app_sessions", _columnsAppSessions, _foreignKeysAppSessions, _indicesAppSessions);
        final TableInfo _existingAppSessions = TableInfo.read(db, "app_sessions");
        if (!_infoAppSessions.equals(_existingAppSessions)) {
          return new RoomOpenHelper.ValidationResult(false, "app_sessions(com.minactivitytracker.data.entity.AppSession).\n"
                  + " Expected:\n" + _infoAppSessions + "\n"
                  + " Found:\n" + _existingAppSessions);
        }
        final HashMap<String, TableInfo.Column> _columnsDeviceEvents = new HashMap<String, TableInfo.Column>(3);
        _columnsDeviceEvents.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDeviceEvents.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDeviceEvents.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDeviceEvents = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesDeviceEvents = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoDeviceEvents = new TableInfo("device_events", _columnsDeviceEvents, _foreignKeysDeviceEvents, _indicesDeviceEvents);
        final TableInfo _existingDeviceEvents = TableInfo.read(db, "device_events");
        if (!_infoDeviceEvents.equals(_existingDeviceEvents)) {
          return new RoomOpenHelper.ValidationResult(false, "device_events(com.minactivitytracker.data.entity.DeviceEvent).\n"
                  + " Expected:\n" + _infoDeviceEvents + "\n"
                  + " Found:\n" + _existingDeviceEvents);
        }
        final HashMap<String, TableInfo.Column> _columnsBatterySamples = new HashMap<String, TableInfo.Column>(4);
        _columnsBatterySamples.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBatterySamples.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBatterySamples.put("levelPct", new TableInfo.Column("levelPct", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBatterySamples.put("isCharging", new TableInfo.Column("isCharging", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBatterySamples = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesBatterySamples = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoBatterySamples = new TableInfo("battery_samples", _columnsBatterySamples, _foreignKeysBatterySamples, _indicesBatterySamples);
        final TableInfo _existingBatterySamples = TableInfo.read(db, "battery_samples");
        if (!_infoBatterySamples.equals(_existingBatterySamples)) {
          return new RoomOpenHelper.ValidationResult(false, "battery_samples(com.minactivitytracker.data.entity.BatterySample).\n"
                  + " Expected:\n" + _infoBatterySamples + "\n"
                  + " Found:\n" + _existingBatterySamples);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "c34f89650b2ed4e31eb7e914d9ff1236", "93180047ce5816e1cdc7f592c1fc9809");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "app_sessions","device_events","battery_samples");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `app_sessions`");
      _db.execSQL("DELETE FROM `device_events`");
      _db.execSQL("DELETE FROM `battery_samples`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(AppSessionDao.class, AppSessionDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(DeviceEventDao.class, DeviceEventDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(BatterySampleDao.class, BatterySampleDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public AppSessionDao appSessionDao() {
    if (_appSessionDao != null) {
      return _appSessionDao;
    } else {
      synchronized(this) {
        if(_appSessionDao == null) {
          _appSessionDao = new AppSessionDao_Impl(this);
        }
        return _appSessionDao;
      }
    }
  }

  @Override
  public DeviceEventDao deviceEventDao() {
    if (_deviceEventDao != null) {
      return _deviceEventDao;
    } else {
      synchronized(this) {
        if(_deviceEventDao == null) {
          _deviceEventDao = new DeviceEventDao_Impl(this);
        }
        return _deviceEventDao;
      }
    }
  }

  @Override
  public BatterySampleDao batterySampleDao() {
    if (_batterySampleDao != null) {
      return _batterySampleDao;
    } else {
      synchronized(this) {
        if(_batterySampleDao == null) {
          _batterySampleDao = new BatterySampleDao_Impl(this);
        }
        return _batterySampleDao;
      }
    }
  }
}
