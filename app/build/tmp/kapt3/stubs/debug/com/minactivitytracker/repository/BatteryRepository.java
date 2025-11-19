package com.minactivitytracker.repository;

import com.minactivitytracker.data.dao.BatterySampleDao;
import com.minactivitytracker.data.entity.BatterySample;
import kotlinx.coroutines.flow.Flow;
import javax.inject.Inject;
import javax.inject.Singleton;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0012\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006J\u0016\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\fR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2 = {"Lcom/minactivitytracker/repository/BatteryRepository;", "", "batterySampleDao", "Lcom/minactivitytracker/data/dao/BatterySampleDao;", "(Lcom/minactivitytracker/data/dao/BatterySampleDao;)V", "getAllSamples", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/minactivitytracker/data/entity/BatterySample;", "insertSample", "", "sample", "(Lcom/minactivitytracker/data/entity/BatterySample;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class BatteryRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.minactivitytracker.data.dao.BatterySampleDao batterySampleDao = null;
    
    @javax.inject.Inject()
    public BatteryRepository(@org.jetbrains.annotations.NotNull()
    com.minactivitytracker.data.dao.BatterySampleDao batterySampleDao) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.minactivitytracker.data.entity.BatterySample>> getAllSamples() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object insertSample(@org.jetbrains.annotations.NotNull()
    com.minactivitytracker.data.entity.BatterySample sample, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}