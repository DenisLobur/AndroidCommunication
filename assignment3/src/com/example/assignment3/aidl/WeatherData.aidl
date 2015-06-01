package com.example.assignment3.aidl;

/**
 * AIDL definition for the WeatherData class, which the AIDL compiler
 * needs to integrate the code for marshaling/demarshaling WeatherData
 * objects.  An .com.example.assignment3.activities.aidl file is needed for any parcelable class used in
 * any other .com.example.assignment3.activities.aidl file, even if they are defined in the same package
 * as an interface.
 */
parcelable WeatherData;
