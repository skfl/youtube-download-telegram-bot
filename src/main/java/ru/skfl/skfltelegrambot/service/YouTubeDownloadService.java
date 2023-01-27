package ru.skfl.skfltelegrambot.service;

import com.sapher.youtubedl.YoutubeDL;
import com.sapher.youtubedl.YoutubeDLException;
import com.sapher.youtubedl.YoutubeDLRequest;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class YouTubeDownloadService {
    private static final String DOWNLOAD_FOLDER_NAME = "download";

    private final File downloadFolder;

    public YouTubeDownloadService() {
        this.downloadFolder = new File(DOWNLOAD_FOLDER_NAME);
        if (!downloadFolder.exists()) {
            downloadFolder.mkdir();
        }
    }

    private void setRequestOptions(YoutubeDLRequest req) {
        req.setDirectory(downloadFolder.getAbsolutePath());
        req.setOption("retries", 2);
        req.setOption("format", "best");
        req.setOption("id");
    }

    public File download(String url) throws YoutubeDLException {
        YoutubeDLRequest req = new YoutubeDLRequest(url);
        setRequestOptions(req);
        String filename = YoutubeDL.getVideoInfo(url).id;
        YoutubeDL.execute(req);
        return new File(DOWNLOAD_FOLDER_NAME + "/" + filename + ".mp4");
    }
}
