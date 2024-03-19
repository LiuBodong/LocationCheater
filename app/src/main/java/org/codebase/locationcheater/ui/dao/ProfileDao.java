package org.codebase.locationcheater.ui.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.util.List;

@Dao
@TypeConverters({ProfileTypeConverters.class})
public interface ProfileDao {

    @Query("SELECT * FROM profile ORDER BY create_time ASC")
    List<ProfileDto> getAllProfiles();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(ProfileDto... profileDtoArray);

    @Delete
    void deleteAll(ProfileDto... profileDtoArray);

}
