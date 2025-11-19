package com.minactivitytracker.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.minactivitytracker.data.dao.AppSessionDao;
import com.minactivitytracker.data.dao.BatterySampleDao;
import com.minactivitytracker.data.dao.DeviceEventDao;
import com.minactivitytracker.data.entity.AppSession;
import com.minactivitytracker.data.entity.BatterySample;
import com.minactivitytracker.data.entity.DeviceEvent;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\'\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&J\b\u0010\u0005\u001a\u00020\u0006H&J\b\u0010\u0007\u001a\u00020\bH&\u00a8\u0006\t"}, d2 = {"Lcom/minactivitytracker/data/AppDatabase;", "Landroidx/room/RoomDatabase;", "()V", "appSessionDao", "Lcom/minactivitytracker/data/dao/AppSessionDao;", "batterySampleDao", "Lcom/minactivitytracker/data/dao/BatterySampleDao;", "deviceEventDao", "Lcom/minactivitytracker/data/dao/DeviceEventDao;", "app_debug"})
@androidx.room.Database(entities = {com.minactivitytracker.data.entity.AppSession.class, com.minactivitytracker.data.entity.DeviceEvent.class, com.minactivitytracker.data.entity.BatterySample.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends androidx.room.RoomDatabase {
    
    public AppDatabase() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.minactivitytracker.data.dao.AppSessionDao appSessionDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.minactivitytracker.data.dao.DeviceEventDao deviceEventDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.minactivitytracker.data.dao.BatterySampleDao batterySampleDao();
}