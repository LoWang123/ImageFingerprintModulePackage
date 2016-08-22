/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fau.imagefingerprintcomparison.modules;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.SQLException;

/**
 *
 * @author Tobi
 */
public interface ListItemImage {
    public BufferedImage getImage() throws SQLException, IOException, Exception;
    public String getName();
    
    public byte[] getMho() throws SQLException, IOException, Exception;
    public byte[] getBmv() throws SQLException, IOException, Exception;
    public byte[] getCh() throws SQLException, IOException, Exception;
}
