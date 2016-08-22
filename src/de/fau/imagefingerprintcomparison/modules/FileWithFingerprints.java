/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fau.imagefingerprintcomparison.modules;

import java.io.File;

/**
 *
 * @author Tobi
 */
public class FileWithFingerprints {
    protected File file;
    protected byte[] mho;
    protected byte[] bmv;
    protected byte[] ch;

    public FileWithFingerprints(File file, byte[] mho, byte[] bmv, byte[] ch) {
        this.file = file;
        this.mho = mho;
        this.bmv = bmv;
        this.ch = ch;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public byte[] getMho() {
        return mho;
    }

    public void setMho(byte[] mho) {
        this.mho = mho;
    }

    public byte[] getBmv() {
        return bmv;
    }

    public void setBmv(byte[] bmv) {
        this.bmv = bmv;
    }

    public byte[] getCh() {
        return ch;
    }

    public void setCh(byte[] ch) {
        this.ch = ch;
    }
    
    
}
