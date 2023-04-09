package com.jollykai.photoFinderAPI.service;

import com.jollykai.photoFinderAPI.config.ConnectConfig;
import com.jollykai.photoFinderAPI.config.SearchConfig;
import com.jollykai.photoFinderAPI.entity.File;
import com.jollykai.photoFinderAPI.entity.Photo;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Project: photoFinderAPI
 *
 * @author Dmitry Istomin aka Jollykai
 * https://github.com/Jollykai
 */

@Component
public class FTPService {
    final FTPClient ftpClient;
    final ConnectConfig connectConfig;
    final SearchConfig searchConfig;
    private final Logger logger = LoggerFactory.getLogger(FTPService.class);

    @Autowired
    public FTPService(FTPClient ftpClient,
                      ConnectConfig connectConfig,
                      SearchConfig searchConfig) {
        this.ftpClient = ftpClient;
        this.connectConfig = connectConfig;
        this.searchConfig = searchConfig;
    }

    public void connectAndLogin() throws IOException {
        try {
            ftpClient.connect(connectConfig.getHost(), connectConfig.getPort());
            logger.info("Connection success");
            logger.info(ftpClient.getReplyString());
            ftpClient.enterLocalPassiveMode();
            logger.info("Enter Local Passive Mode");
        } catch (IOException e) {
            logger.info("Connection error: " + e.getMessage());
            throw e;
        }
        try {
            ftpClient.login(connectConfig.getUsername(), connectConfig.getPassword());
            logger.info("Login success");
        } catch (IOException e) {
            logger.info("Login error: " + e.getMessage());
            throw e;
        }
    }

    public void disconnect() throws IOException {
        if (ftpClient.isConnected()) {
            try {
                ftpClient.logout();
                logger.info("Logout success");
                ftpClient.disconnect();
                logger.info("Disconnected success");

            } catch (IOException e) {
                logger.info("Error disconnecting from FTP server: " + e.getMessage());
                throw e;
            }
        }
    }

    List<String> searchPhotoDirectories(String parentDir, String photoDirName) throws IOException {
        List<String> photoDirList = new ArrayList<>();
        FTPFile[] files = getListFiles(parentDir);

        for (FTPFile file : files) {
            if (file.isDirectory() && file.getName().equals(photoDirName)) {
                photoDirList.add(parentDir + "/" + file.getName());
                photoDirList.addAll(searchPhotoDirectories(parentDir + "/" + file.getName(), photoDirName));
            } else if (file.isDirectory() && !searchConfig.getIgnoredList().contains(file.getName())) {
                photoDirList.addAll(searchPhotoDirectories(parentDir + "/" + file.getName(), photoDirName));
            }
        }

        return photoDirList;
    }

    FTPFile[] getListFiles(String parentDir) throws IOException {
        if (parentDir.contains(" ")) {
            ftpClient.cwd(parentDir);
            FTPFile[] filesList = ftpClient.listFiles();
            ftpClient.cdup();
            return filesList;
        } else {
            return ftpClient.listFiles(parentDir);
        }
    }

    public List<Photo> getPhotoList() throws IOException {
        connectAndLogin();
        List<Photo> photosList = new ArrayList<>();
        List<String> photoDirList = searchPhotoDirectories(searchConfig.getRoot(), searchConfig.getDir());

        for (String photoDir : photoDirList) {
            ftpClient.changeWorkingDirectory(photoDir);
            FTPFile[] files = ftpClient.listFiles();

            for (FTPFile file : files) {
                if (file.getName().startsWith(searchConfig.getMask())) {
                    String path = ftpClient.printWorkingDirectory() + "/" + file.getName();
                    Date creationDate = file.getTimestamp().getTime();
                    Long size = file.getSize();
                    photosList.add(new Photo(path, creationDate, size));
                }
            }
        }
        disconnect();
        return photosList;
    }

    public List<String> getPhotosPathList() throws IOException {
        connectAndLogin();
        List<String> photosPathList = new ArrayList<>();
        List<String> photoDirList = searchPhotoDirectories(searchConfig.getRoot(), searchConfig.getDir());

        for (String photoDir : photoDirList) {
            ftpClient.changeWorkingDirectory(photoDir);
            FTPFile[] files = ftpClient.listFiles();

            for (FTPFile file : files) {
                if (file.getName().startsWith(searchConfig.getMask())) {
                    String path = ftpClient.printWorkingDirectory() + "/" + file.getName();
                    photosPathList.add(path);
                }
            }
        }
        disconnect();
        return photosPathList;
    }

    public Photo getPhotoInfo(String path) throws IOException {
        connectAndLogin();
        FTPFile file = ftpClient.mlistFile(path);
        if (file == null) {
            logger.info("File not found");
            throw new IOException("File not found");
        }
        if (file.isDirectory()) {
            logger.info("Path is a directory");
            throw new IOException("Path is a directory");
        }
        Photo photo = new Photo(path, file.getTimestamp().getTime(), file.getSize());
        disconnect();
        return photo;
    }

    public File downloadPhoto(String path) throws IOException {
        connectAndLogin();
        FTPFile ftpFile = ftpClient.mlistFile(path);

        if (ftpFile == null) {
            logger.info("File not found: " + path);
            throw new IOException("File not found: " + path);
        }
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ftpClient.retrieveFile(path, outputStream);
        byte[] bytes = outputStream.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        String encodedPath = URLEncoder.encode(ftpFile.getName(), StandardCharsets.UTF_8);
        headers.setContentDisposition(ContentDisposition.attachment().filename(encodedPath).build());

        outputStream.close();
        disconnect();

        return new File(bytes, headers);
    }
}
