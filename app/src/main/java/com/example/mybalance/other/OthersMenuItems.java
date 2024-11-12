package com.example.mybalance.other;


import com.example.mybalance.settings.Settings_editor;

import java.util.ArrayList;
import java.util.List;

public class OthersMenuItems{

    public List<OthersMenuItem> itemsList;

    public OthersMenuItems() {
        itemsList = new ArrayList<>();
        itemsList.add(new OthersMenuItem("Settings", Settings_editor.class));
        itemsList.add(new OthersMenuItem("Guide", null));
        itemsList.add(new OthersMenuItem("Bericht", null));
    }

    public static class OthersMenuItem {
        final protected String name;
        final protected Class<?> classActivity;

        public OthersMenuItem(String name, Class<?> classAct) {
            this.name = name;
            this.classActivity = classAct;
        }
    }
}

