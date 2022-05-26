package org.yzr.poi.model;

public class RepeatEntity {

    private String key;

    private String value;

    private String filePath;

    private String fileName;

    public RepeatEntity(String key, String value, String filePath, String fileName) {
        this.key = key;
        this.value = value;
        this.filePath = filePath;
        this.fileName = fileName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "RepeatEntity{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", filePath='" + filePath + '\'' +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}
