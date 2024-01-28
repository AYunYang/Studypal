package com.sp.mad_studypal;

import com.google.firebase.firestore.PropertyName;

public class StudyArea {
    // Fields matching the Firestore document
    @PropertyName("aircon")
    private boolean hasAircon;

    @PropertyName("charging")
    private boolean hasCharging;

    @PropertyName("location")
    private String location;
    @PropertyName("name")
    private String name;



    // Default constructor (needed for Firestore)
    public StudyArea() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    // Getters and setters
    public boolean getHasAircon() { return hasAircon; }
    public void setHasAircon(boolean hasAircon) { this.hasAircon = hasAircon; }

    public boolean getHasCharging() { return hasCharging; }
    public void setHasCharging(boolean hasCharging) { this.hasCharging = hasCharging; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}


