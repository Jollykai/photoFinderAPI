package com.jollykai.photoFinderAPI.service;

import com.jollykai.photoFinderAPI.config.ConnectConfig;
import com.jollykai.photoFinderAPI.config.SearchConfig;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Project: photoFinderAPI
 *
 * @author Dmitry Istomin aka Jollykai
 * https://github.com/Jollykai
 */
@RunWith(SpringRunner.class)
public class FTPServiceTest {
    private FTPClient ftpClient;
    @Mock
    private ConnectConfig connectionConfig;
    @Mock
    private SearchConfig searchConfig;

    FTPService ftpService;

    @Before
    public void setUp() {
        ftpClient = Mockito.mock(FTPClient.class);
        ftpService = new FTPService(ftpClient, connectionConfig, searchConfig);
    }

    @Test
    public void successfulConnectAndLoginTest() throws IOException {
        doNothing().when(ftpClient).connect(anyString(), anyInt());
        doNothing().when(ftpClient).enterLocalPassiveMode();
        when(ftpClient.login(anyString(), anyString())).thenReturn(true);
        ftpService.connectAndLogin();
    }

    @Test
    public void getListFilesTest() throws IOException {
        when(ftpClient.cwd("/ ")).thenReturn(200);
        when(ftpClient.listFiles()).thenReturn(new FTPFile[1]);
        when(ftpClient.cdup()).thenReturn(200);
        var files = ftpService.getListFiles("/ ");
        assertNotNull(files);
    }

    @Test
    public void disconnectTest() throws IOException {
        ftpService.connectAndLogin();
        ftpService.disconnect();
        assertFalse(ftpService.ftpClient.isConnected());
    }
}
