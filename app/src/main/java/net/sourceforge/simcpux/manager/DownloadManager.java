package net.sourceforge.simcpux.manager;

import net.sourceforge.simcpux.asynctask.DownloadTask;

import java.util.HashMap;

public class DownloadManager {
    private HashMap<String,DownloadTask> downloadMap = new HashMap<>();

    private DownloadManager(){}

    private static DownloadManager downloadManager = new DownloadManager();

    public static DownloadManager getInstance(){
        return downloadManager;
    }

    public void addTask(String url,DownloadTask task){
        if(!isExistTask(url)){
            downloadMap.put(url,task);
        }
    }

    public boolean isExistTask(String url){
        return downloadMap.containsKey(url);
    }
}
