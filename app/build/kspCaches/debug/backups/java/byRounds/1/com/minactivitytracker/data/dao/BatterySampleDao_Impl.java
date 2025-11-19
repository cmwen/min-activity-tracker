package com.minactivitytracker.data.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.minactivitytracker.data.entity.BatterySample;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class BatterySampleDao_Impl implements BatterySampleDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<BatterySample> __insertionAdapterOfBatterySample;

  public BatterySampleDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBatterySample = new EntityInsertionAdapter<BatterySample>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `battery_samples` (`id`,`timestamp`,`levelPct`,`isCharging`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BatterySample entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getTimestamp());
        statement.bindLong(3, entity.getLevelPct());
        final int _tmp = entity.isCharging() ? 1 : 0;
        statement.bindLong(4, _tmp);
      }
    };
  }

  @Override
  public Object insert(final BatterySample sample, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfBatterySample.insert(sample);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<BatterySample>> getAllSamples() {
    final String _sql = "SELECT * FROM battery_samples ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"battery_samples"}, new Callable<List<BatterySample>>() {
      @Override
      @NonNull
      public List<BatterySample> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfLevelPct = CursorUtil.getColumnIndexOrThrow(_cursor, "levelPct");
          final int _cursorIndexOfIsCharging = CursorUtil.getColumnIndexOrThrow(_cursor, "isCharging");
          final List<BatterySample> _result = new ArrayList<BatterySample>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BatterySample _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final int _tmpLevelPct;
            _tmpLevelPct = _cursor.getInt(_cursorIndexOfLevelPct);
            final boolean _tmpIsCharging;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCharging);
            _tmpIsCharging = _tmp != 0;
            _item = new BatterySample(_tmpId,_tmpTimestamp,_tmpLevelPct,_tmpIsCharging);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
