package org.codebase.locationcheater.ui.dao;

import android.util.Log;

import androidx.room.ProvidedTypeConverter;
import androidx.room.TypeConverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ProvidedTypeConverter
public class ProfileTypeConverters {

    private static final String TAG = "TypeConvert";

    private static final JsonMapper MAPPER = JsonMapper.builder().build();

    @TypeConverter
    public WifiDto StringToWifiDto(String json) {
        try {
            return MAPPER.readValue(json, WifiDto.class);
        } catch (Exception e) {
            Log.e(TAG, "Could not convert from " + json, e);
            return new WifiDto();
        }
    }

    @TypeConverter
    public String WifiDtoToString(WifiDto wifiDto) {
        try {
            return MAPPER.writer().writeValueAsString(wifiDto);
        } catch (JsonProcessingException e) {
            Log.e(TAG, "Could not convert to json! ", e);
            return "{}";
        }
    }

    @TypeConverter
    public List<WifiDto> StringToWifiDtoList(String json) {
        try {
            CollectionType type = TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, WifiDto.class);
            return MAPPER.readValue(json, type);
        } catch (Exception e) {
            Log.e(TAG, "Could not convert from " + json, e);
            return Collections.emptyList();
        }
    }

    @TypeConverter
    public String WifiDtoListToString(List<WifiDto> wifiDtoList) {
        try {
            return MAPPER.writer().writeValueAsString(wifiDtoList);
        } catch (JsonProcessingException e) {
            Log.e(TAG, "Could not convert to json! ", e);
            return "{}";
        }
    }

    @TypeConverter
    public TelephoneDto StringToTelephoneDto(String json) {
        try {
            return MAPPER.readValue(json, TelephoneDto.class);
        } catch (Exception e) {
            Log.e(TAG, "Could not convert from " + json, e);
            return new TelephoneDto();
        }
    }

    @TypeConverter
    public String TelephoneDtoToString(TelephoneDto telephoneDto) {
        try {
            return MAPPER.writer().writeValueAsString(telephoneDto);
        } catch (JsonProcessingException e) {
            Log.e(TAG, "Could not convert to json! ", e);
            return "{}";
        }
    }

    @TypeConverter
    public List<TelephoneDto> StringToTelephoneDtoList(String json) {
        try {
            CollectionType type = TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, TelephoneDto.class);
            return MAPPER.readValue(json, type);
        } catch (Exception e) {
            Log.e(TAG, "Could not convert from " + json, e);
            return Collections.emptyList();
        }
    }

    @TypeConverter
    public String TelephoneDtoListToString(List<TelephoneDto> telephoneDtoList) {
        try {
            return MAPPER.writer().writeValueAsString(telephoneDtoList);
        } catch (JsonProcessingException e) {
            Log.e(TAG, "Could not convert to json! ", e);
            return "{}";
        }
    }

    @TypeConverter
    public LocationDto StringToLocationDto(String json) {
        try {
            return MAPPER.readValue(json, LocationDto.class);
        } catch (Exception e) {
            Log.e(TAG, "Could not convert from " + json, e);
            return new LocationDto();
        }
    }

    @TypeConverter
    public String LocationDtoToString(LocationDto locationDto) {
        try {
            return MAPPER.writer().writeValueAsString(locationDto);
        } catch (JsonProcessingException e) {
            Log.e(TAG, "Could not convert to json! ", e);
            return "{}";
        }
    }

    @TypeConverter
    public List<LocationDto> StringToLocationDtoList(String json) {
        try {
            CollectionType type = TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, LocationDto.class);
            return MAPPER.readValue(json, type);
        } catch (Exception e) {
            Log.e(TAG, "Could not convert from " + json, e);
            return Collections.emptyList();
        }
    }

    @TypeConverter
    public String LocationDtoListToString(List<LocationDto> locationDtoList) {
        try {
            return MAPPER.writer().writeValueAsString(locationDtoList);
        } catch (JsonProcessingException e) {
            Log.e(TAG, "Could not convert to json! ", e);
            return "{}";
        }
    }

}
