package com.gire.socialapplication.model;

import android.graphics.drawable.Drawable;

/**
 * Created by girish on 7/7/2017.
 */

public class HomePageModeTypes {
    String modeName;
    String modeId;
    Drawable viewBg;

    public HomePageModeTypes(String modeName, String modeId) {
        this.modeName = modeName;
        this.modeId = modeId;
    }

    public HomePageModeTypes(String modeName, String modeId, Drawable viewBg) {
        this.modeName = modeName;
        this.modeId = modeId;
        this.viewBg = viewBg;
    }

    public String getModeName() {
        return modeName;
    }

    public void setModeName(String modeName) {
        this.modeName = modeName;
    }

    public String getModeId() {
        return modeId;
    }

    public void setModeId(String modeId) {
        this.modeId = modeId;
    }

    public Drawable getViewBg() {
        return viewBg;
    }

    public void setViewBg(Drawable viewBg) {
        this.viewBg = viewBg;
    }

}
