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
import com.minactivitytracker.data.entity.AppSession;
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
public final class AppSessionDao_Impl implements AppSessionDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<AppSession> __insertionAdapterOfAppSession;

  public AppSessionDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfAppSession = new EntityInsertionAdapter<AppSession>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `app_sessions` (`id`,`packageName`,`startTimestamp`,`endTimestamp`,`durationMs`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AppSession entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getPackageName());
        statement.bindLong(3, entity.getStartTimestamp());
        statement.bindLong(4, entity.getEndTimestamp());
        statement.bindLong(5, entity.getDurationMs());
      }
    };
  }

  @Override
  public Object insert(final AppSession session, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfAppSession.insert(session);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<AppSession>> getAllSessions() {
    final String _sql = "SELECT * FROM app_sessions ORDER BY startTimestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"app_sessions"}, new Callable<List<AppSession>>() {
      @Override
      @NonNull
      public List<AppSession> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPackageName = CursorUtil.getColumnIndexOrThrow(_cursor, "packageName");
          final int _cursorIndexOfStartTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "startTimestamp");
          final int _cursorIndexOfEndTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "endTimestamp");
          final int _cursorIndexOfDurationMs = CursorUtil.getColumnIndexOrThrow(_cursor, "durationMs");
          final List<AppSession> _result = new ArrayList<AppSession>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AppSession _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpPackageName;
            _tmpPackageName = _cursor.getString(_cursorIndexOfPackageName);
            final long _tmpStartTimestamp;
            _tmpStartTimestamp = _cursor.getLong(_cursorIndexOfStartTimestamp);
            final long _tmpEndTimestamp;
            _tmpEndTimestamp = _cursor.getLong(_cursorIndexOfEndTimestamp);
            final long _tmpDurationMs;
            _tmpDurationMs = _cursor.getLong(_cursorIndexOfDurationMs);
            _item = new AppSession(_tmpId,_tmpPackageName,_tmpStartTimestamp,_tmpEndTimestamp,_tmpDurationMs);
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

  @Override
  public Flow<List<AppSession>> getSessionsInRange(final long startTime, final long endTime) {
    final String _sql = "SELECT * FROM app_sessions WHERE startTimestamp >= ? AND endTimestamp <= ? ORDER BY startTimestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startTime);
    _argIndex = 2;
    _statement.bindLong(_argIndex, endTime);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"app_sessions"}, new Callable<List<AppSession>>() {
      @Override
      @NonNull
      public List<AppSession> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPackageName = CursorUtil.getColumnIndexOrThrow(_cursor, "packageName");
          final int _cursorIndexOfStartTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "startTimestamp");
          final int _cursorIndexOfEndTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "endTimestamp");
          final int _cursorIndexOfDurationMs = CursorUtil.getColumnIndexOrThrow(_cursor, "durationMs");
          final List<AppSession> _result = new ArrayList<AppSession>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AppSession _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpPackageName;
            _tmpPackageName = _cursor.getString(_cursorIndexOfPackageName);
            final long _tmpStartTimestamp;
            _tmpStartTimestamp = _cursor.getLong(_cursorIndexOfStartTimestamp);
            final long _tmpEndTimestamp;
            _tmpEndTimestamp = _cursor.getLong(_cursorIndexOfEndTimestamp);
            final long _tmpDurationMs;
            _tmpDurationMs = _cursor.getLong(_cursorIndexOfDurationMs);
            _item = new AppSession(_tmpId,_tmpPackageName,_tmpStartTimestamp,_tmpEndTimestamp,_tmpDurationMs);
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
