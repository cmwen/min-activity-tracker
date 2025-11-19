package com.minactivitytracker.ui.home;

import androidx.lifecycle.ViewModel;
import com.minactivitytracker.data.entity.AppSession;
import com.minactivitytracker.repository.ActivityRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.SharingStarted;
import kotlinx.coroutines.flow.StateFlow;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0002J\f\u0010\u000f\u001a\u00020\b*\u00020\u0010H\u0002R\u001d\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u0011"}, d2 = {"Lcom/minactivitytracker/ui/home/HomeViewModel;", "Landroidx/lifecycle/ViewModel;", "repository", "Lcom/minactivitytracker/repository/ActivityRepository;", "(Lcom/minactivitytracker/repository/ActivityRepository;)V", "recentSessions", "Lkotlinx/coroutines/flow/StateFlow;", "", "Lcom/minactivitytracker/ui/home/AppSessionUiModel;", "getRecentSessions", "()Lkotlinx/coroutines/flow/StateFlow;", "formatDuration", "", "millis", "", "toUiModel", "Lcom/minactivitytracker/data/entity/AppSession;", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class HomeViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.minactivitytracker.ui.home.AppSessionUiModel>> recentSessions = null;
    
    @javax.inject.Inject()
    public HomeViewModel(@org.jetbrains.annotations.NotNull()
    com.minactivitytracker.repository.ActivityRepository repository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.minactivitytracker.ui.home.AppSessionUiModel>> getRecentSessions() {
        return null;
    }
    
    private final com.minactivitytracker.ui.home.AppSessionUiModel toUiModel(com.minactivitytracker.data.entity.AppSession $this$toUiModel) {
        return null;
    }
    
    private final java.lang.String formatDuration(long millis) {
        return null;
    }
}