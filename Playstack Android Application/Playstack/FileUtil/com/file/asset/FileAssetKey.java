package com.file.asset;

public class FileAssetKey implements Comparable<FileAssetKey> {

    private final String value;

    public FileAssetKey(String value) {
        this.value = value;
    }

    public String getValue(){
        return value;
    }

    @Override
    public int compareTo(FileAssetKey o) {
        return value.compareTo(o.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass())
            return false;
        FileAssetKey key = (FileAssetKey) o;
        return value.equals(key.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}