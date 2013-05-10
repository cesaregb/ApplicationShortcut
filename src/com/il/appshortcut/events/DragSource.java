package com.il.appshortcut.events;

import android.view.View;

public interface DragSource {
    void onDropCompleted(View target, boolean success);
}
