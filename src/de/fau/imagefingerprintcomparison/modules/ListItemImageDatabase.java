/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fau.imagefingerprintcomparison.modules;

import de.fau.imagefingerprint.database.ImageDatabase;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.SQLException;

/**
 *
 * @author Tobi
 */
public class ListItemImageDatabase implements ListItemImage{
    private String name = "";
    
    private byte[] mho = null;
    private byte[] bmv = null;
    private byte[] ch = null;
    
    public ListItemImageDatabase(String name) {
        this.name = name;
    }

    @Override
    public byte[] getMho() throws SQLException, IOException, Exception{
        if(mho == null) {
            mho = ImageDatabase.getImageByName(name).getFingerprint();
        }
        return mho;
    }

    @Override
    public byte[] getBmv() throws SQLException, IOException, Exception{
        if(bmv == null) {
            bmv = ImageDatabase.getImageByName(name).getFingerprintBlockMean();
        }
        return bmv;
    }

    @Override
    public byte[] getCh() throws SQLException, IOException, Exception{
        if(ch == null) {
            ch = ImageDatabase.getImageByName(name).getFingerprintColorHistogram();
        }
        return ch;
    }

    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public BufferedImage getImage() throws SQLException, IOException, Exception {
        return ImageDatabase.getImageByName(name).getImage();
    }
}
