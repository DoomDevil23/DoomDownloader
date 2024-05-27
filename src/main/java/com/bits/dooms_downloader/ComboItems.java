package com.bits.dooms_downloader;

/**
 *
 * @author DoomDevil
 */
public class ComboItems {
    private String key, value;

    public ComboItems(String key, String value){
        this.key = key;
        this.value = value;
    }

    public String toString(){
        return key;
    }

    public String getKey(){
        return key;
    }

    public String getValue() {
        return value;
    }
}