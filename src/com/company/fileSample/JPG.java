package com.company.fileSample;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifSubIFDDirectory;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Date;
import java.util.Iterator;

import static org.apache.commons.exec.util.DebugUtils.isDebugEnabled;

public class JPG {
    private String pngPath;
    private String jpgPath;

    public JPG(String pngPath, String jpgPath) {
        this.pngPath = pngPath;
        this.jpgPath = jpgPath;
    }

    public String getPngPath() {
        return pngPath;
    }

    public void setPngPath(String pngPath) {
        this.pngPath = pngPath;
    }

    public String getJpgPath() {
        return jpgPath;
    }

    public void setJpgPath(String jpgPath) {
        this.jpgPath = jpgPath;
    }

    public void pngToJPG(){
        File png = new File(pngPath);
        File jpg = new File(jpgPath);
        try {
            BufferedImage pngBuffer = ImageIO.read(png);
            BufferedImage jpgBuffer = new BufferedImage(pngBuffer.getWidth(),pngBuffer.getHeight(),BufferedImage.TYPE_INT_RGB);
            jpgBuffer.createGraphics().drawImage(pngBuffer,0,0, Color.CYAN,null);
            ImageIO.write(jpgBuffer,"jpg",jpg);
            System.out.println(pngBuffer.getData());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void readMetaData(){
        File jpg = new File(jpgPath);
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(jpg);
            for (Directory directory : metadata.getDirectories()) {
                for (Tag tag : directory.getTags()) {
                    System.out.println(tag);
                }
            }
            // obtain the Exif SubIFD directory
            ExifSubIFDDirectory directory
                    = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);

// query the datetime tag's value
            Date date = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
            System.out.println(date.getTime());
        } catch (ImageProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
