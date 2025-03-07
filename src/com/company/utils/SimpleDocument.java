package com.company.utils;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.pixelmed.dicom.AttributeList;
import com.pixelmed.dicom.AttributeTag;
import com.pixelmed.dicom.DicomException;
import com.pixelmed.dicom.DicomInputStream;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Set;

public class SimpleDocument {
    private long size;
    private String fileName;
    private String source;
    private String topic;
    private String language;
    private String description;
    private String mimeType;

    public SimpleDocument() {
    }
    public long getSize() {
        return size;
    }

    public String getFileName() {
        return fileName;
    }

    public String getSource() {
        return source;
    }

    public String getTopic() {
        return topic;
    }

    public String getLanguage() {
        return language;
    }

    public String getDescription() {
        return description;
    }
    public String getMimeType() {
        return mimeType;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Override
    public String toString() {
        return "SimpleDocument{" +
                "size=" + String.format("%,d bytes", size) +
                ", fileName='" + fileName + '\'' +
                ", source='" + source + '\'' +
                ", topic='" + topic + '\'' +
                ", language='" + language + '\'' +
                ", description='" + description + '\'' +
                ", mimeType='" + mimeType + '\'' +
                '}';
    }

    public String fileFormat(){return fileName.substring(fileName.lastIndexOf(".")+1);}
    public synchronized void analysis(File target, String source){
        this.fileName = target.getName();
        mimeType = fileFormat();
        this.source = source;
        language = "National language";
        Path fileLocation = Paths.get(target.getAbsolutePath());
        try {
            BasicFileAttributes attr = Files.readAttributes(fileLocation,BasicFileAttributes.class);
            this.size = attr.size();
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringBuffer bufferDes = new StringBuffer();
        description = "";
        bufferDes.append(description);
        if (mimeType.equals("jpg")||mimeType.equals("png")){
            try {
                Metadata metadata = ImageMetadataReader.readMetadata(target);
                for (Directory directory:metadata.getDirectories()) {
                    for (Tag tag:directory.getTags()) {
                        bufferDes.append(tag);
                    }
                }
            } catch (ImageProcessingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (mimeType.equals("svg")){

        }else if(mimeType.equals("csv")||mimeType.equals("txt")){
            try {
                BufferedReader reader = new BufferedReader(new FileReader(target.getAbsolutePath()));
                String line = "";
                while ((line = reader.readLine())!=null){
//                    String[]values = line.split(",");
                    bufferDes.append(line);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (mimeType.equals("json")){

        }else if (mimeType.equals("DCM")||mimeType.equals("dcm")){
            try {
                DicomInputStream dis = new DicomInputStream(target);
                AttributeList attributeList = new AttributeList();
                attributeList.read(dis);
                Set<AttributeTag>keys = attributeList.keySet();
                for (AttributeTag key:keys) {
                    System.out.println("("+key.getGroup()+","+key.getElement()+")"+attributeList.get(key).getDelimitedStringValuesOrEmptyString());
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (DicomException e) {
                e.printStackTrace();
            }
        }
        description = bufferDes.toString();
    }
}
