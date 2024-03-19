package org.codebase.locationcheater.ui.dao;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {ProfileDto.class}, version = 1)
@TypeConverters({ProfileTypeConverters.class})
public abstract class ProfileDatabase extends RoomDatabase {

    public abstract ProfileDao profileDao();

}
