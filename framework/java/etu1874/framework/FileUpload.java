/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etu1874.framework;

/**
 *
 * @author ITU
 */
public class FileUpload {
    String fileName;

    public FileUpload() {
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public byte[] getFileByte() {
        return fileByte;
    }

    public void setFileByte(byte[] fileByte) {
        this.fileByte = fileByte;
    }

    public FileUpload(String fileName, String path, byte[] fileByte) {
        this.fileName = fileName;
        this.path = path;
        this.fileByte = fileByte;
    }
    String path;
    byte[] fileByte;
}
