package com.mageshowdown.gameclient;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.StreamUtils;

import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class GamePreferences implements Preferences {

    private final Properties properties = new Properties();
    private final FileHandle fileHandle;

    /**
     * Default location for UserPrefs
     **/
    public GamePreferences() throws IOException {
        String prefsPath;
        if (OSDetector.getOSType() == OSDetector.OSType.WINDOWS)
            prefsPath = FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "\\My Games\\MageShowdown\\UserPrefs.xml";
        else
            prefsPath = FileSystemView.getFileSystemView().getHomeDirectory().getPath() + "\\.MageShowdown\\UserPrefs.xml";
        File file = new File(prefsPath);
        file.getParentFile().mkdirs(); // Will create parent directories if not exists
        file.createNewFile();
        this.fileHandle = new FileHandle(prefsPath);
        properties.loadFromXML(fileHandle.read());
    }

    /**
     * Specific location for UserPrefs
     **/
    public GamePreferences(String path_to_file) throws IOException {
        File file = new File(path_to_file);
        file.getParentFile().mkdirs();
        file.createNewFile();
        this.fileHandle = new FileHandle(path_to_file);
        properties.loadFromXML(fileHandle.read());
    }

    @Override
    public Preferences putBoolean(String key, boolean val) {
        properties.put(key, Boolean.toString(val));
        return this;
    }

    @Override
    public Preferences putInteger(String key, int val) {
        properties.put(key, Integer.toString(val));
        return this;
    }

    @Override
    public Preferences putLong(String key, long val) {
        properties.put(key, Long.toString(val));
        return this;
    }

    @Override
    public Preferences putFloat(String key, float val) {
        properties.put(key, Float.toString(val));
        return this;
    }

    @Override
    public Preferences putString(String key, String val) {
        properties.put(key, val);
        return this;
    }

    @Override
    public Preferences put(Map<String, ?> vals) {
        for (Map.Entry<String, ?> val : vals.entrySet()) {
            if (val.getValue() instanceof Boolean) putBoolean(val.getKey(), (Boolean) val.getValue());
            if (val.getValue() instanceof Integer) putInteger(val.getKey(), (Integer) val.getValue());
            if (val.getValue() instanceof Long) putLong(val.getKey(), (Long) val.getValue());
            if (val.getValue() instanceof String) putString(val.getKey(), (String) val.getValue());
            if (val.getValue() instanceof Float) putFloat(val.getKey(), (Float) val.getValue());
        }
        return this;
    }

    @Override
    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    @Override
    public int getInteger(String key) {
        return getInteger(key, 0);
    }

    @Override
    public long getLong(String key) {
        return getLong(key, 0);
    }

    @Override
    public float getFloat(String key) {
        return getFloat(key, 0);
    }

    @Override
    public String getString(String key) {
        return getString(key, "");
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        return Boolean.parseBoolean(properties.getProperty(key, Boolean.toString(defValue)));
    }

    @Override
    public int getInteger(String key, int defValue) {
        return Integer.parseInt(properties.getProperty(key, Integer.toString(defValue)));
    }

    @Override
    public long getLong(String key, long defValue) {
        return Long.parseLong(properties.getProperty(key, Long.toString(defValue)));
    }

    @Override
    public float getFloat(String key, float defValue) {
        return Float.parseFloat(properties.getProperty(key, Float.toString(defValue)));
    }

    @Override
    public String getString(String key, String defValue) {
        return properties.getProperty(key, defValue);
    }

    @Override
    public Map<String, ?> get() {
        Map<String, Object> map = new HashMap<String, Object>();
        for (Map.Entry<Object, Object> val : properties.entrySet()) {
            if (val.getValue() instanceof Boolean)
                map.put((String) val.getKey(), (Boolean) Boolean.parseBoolean((String) val.getValue()));
            if (val.getValue() instanceof Integer)
                map.put((String) val.getKey(), (Integer) Integer.parseInt((String) val.getValue()));
            if (val.getValue() instanceof Long)
                map.put((String) val.getKey(), (Long) Long.parseLong((String) val.getValue()));
            if (val.getValue() instanceof String) map.put((String) val.getKey(), (String) val.getValue());
            if (val.getValue() instanceof Float)
                map.put((String) val.getKey(), (Float) Float.parseFloat((String) val.getValue()));
        }

        return map;
    }

    @Override
    public boolean contains(String key) {
        return properties.containsKey(key);
    }

    @Override
    public void clear() {
        properties.clear();
    }

    @Override
    public void flush() {
        OutputStream out = null;
        try {
            out = new BufferedOutputStream(fileHandle.write(false));
            properties.storeToXML(out, null);
        } catch (Exception ex) {
            throw new GdxRuntimeException("Error writing preferences: " + fileHandle, ex);
        } finally {
            StreamUtils.closeQuietly(out);
        }
    }

    @Override
    public void remove(String key) {
        properties.remove(key);
    }
}
