/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fau.imagefingerprintcomparison.modules;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import javax.imageio.ImageIO;

/**
 *
 * @author Tobi
 */
public class ListItemImagePath implements ListItemImage{
    private String path = "";
    private String name = null;
    
    private byte[] mho;
    private byte[] bmv;
    private byte[] ch;
    
    public ListItemImagePath(String path, String name, byte[] mho, byte[] bmv, byte[] ch) {
        this.path = path;
        this.name = name;
        this.mho = mho;
        this.bmv = bmv;
        this.ch = ch;
    }
    
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public byte[] getMho() throws SQLException, IOException, Exception {
        return mho;
    }

    @Override
    public byte[] getBmv() throws SQLException, IOException, Exception {
        return bmv;
    }

    @Override
    public byte[] getCh() throws SQLException, IOException, Exception {
        return ch;
    }    
    
    @Override
    public BufferedImage getImage() throws SQLException, IOException, Exception{
        return ImageIO.read(new File(path));
    }
}
