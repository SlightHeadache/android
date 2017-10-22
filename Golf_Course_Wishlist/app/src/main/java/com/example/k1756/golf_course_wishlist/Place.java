package com.example.k1756.golf_course_wishlist;

import android.content.Context;

/**
 * Created by Juuso_2 on 22.10.2017.
 */

public class Place {
    public String name;
    public String imageName;

    public int getImageResourceId(Context context) {
        return context.getResources().getIdentifier(this.imageName, "drawable", context.getPackageName());
    }
}
