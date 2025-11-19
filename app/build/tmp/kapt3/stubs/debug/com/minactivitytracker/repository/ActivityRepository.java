package com.minactivitytracker.repository;

import com.minactivitytracker.data.dao.AppSessionDao;
import com.minactivitytracker.data.dao.DeviceEventDao;
import com.minactivitytracker.data.entity.AppSession;
import com.minactivitytracker.data.entity.DeviceEvent;
import kotlinx.coroutines.flow.Flow;
import javax.inject.Inject;
import javax.inject.Singleton;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0006\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0012\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\bJ\u0012\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\t0\bJ\"\u0010\r\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\t0\b2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000fJ\u0016\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\nH\u0086@\u00a2\u0006\u0002\u0010\u0014J\u0016\u0010\u0015\u001a\u00020\u00122\u0006\u0010\u0016\u001a\u00020\fH\u0086@\u00a2\u0006\u0002\u0010\u0017R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0018"}, d2 = {"Lcom/minactivitytracker/repository/ActivityRepository;", "", "appSessionDao", "Lcom/minactivitytracker/data/dao/AppSessionDao;", "deviceEventDao", "Lcom/minactivitytracker/data/dao/DeviceEventDao;", "(Lcom/minactivitytracker/data/dao/AppSessionDao;Lcom/minactivitytracker/data/dao/DeviceEventDao;)V", "getAllEvents", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/minactivitytracker/data/entity/DeviceEvent;", "getAllSessions", "Lcom/minactivitytracker/data/entity/AppSession;", "getSessionsInRange", "startTime", "", "endTime", "insertEvent", "", "event", "(Lcom/minactivitytracker/data/entity/DeviceEvent;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertSession", "session", "(Lcom/minactivitytracker/data/entity/AppSession;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class ActivityRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.minactivitytracker.data.dao.AppSessionDao appSessionDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.minactivitytracker.data.dao.DeviceEventDao deviceEventDao = null;
    
    @javax.inject.Inject()
    public ActivityRepository(@org.jetbrains.annotations.NotNull()
    com.minactivitytracker.data.dao.AppSessionDao appSessionDao, @org.jetbrains.annotations.NotNull()
    com.minactivitytracker.data.dao.DeviceEventDao deviceEventDao) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.minactivitytracker.data.entity.AppSession>> getAllSessions() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.minactivitytracker.data.entity.AppSession>> getSessionsInRange(long startTime, long endTime) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object insertSession(@org.jetbrains.annotations.NotNull()
    com.minactivitytracker.data.entity.AppSession session, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.minactivitytracker.data.entity.DeviceEvent>> getAllEvents() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object insertEvent(@org.jetbrains.annotations.NotNull()
    com.minactivitytracker.data.entity.DeviceEvent event, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}