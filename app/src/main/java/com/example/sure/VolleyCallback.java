package com.example.sure;

import java.util.ArrayList;

public interface VolleyCallback {
    void onSuccess(ArrayList<Place> result);

    void onSuccess(Place result);
}
