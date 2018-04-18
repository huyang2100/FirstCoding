package net.sourceforge.simcpux.manager;

import net.sourceforge.simcpux.service.DownloadService;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yanghu on 2018/4/2.
 */

public class DownloadManager {
    private HashMap<String, DownloadService.DownloadLisenter> map = new HashMap<>();
    private ArrayList<String> urlList = new ArrayList<>();

    private DownloadManager() {
    }

    private static DownloadManager manager = new DownloadManager();

    public static DownloadManager getInstance() {
        return manager;
    }

    public void register(String url, DownloadService.DownloadLisenter lisenter) {
        map.put(url, lisenter);
    }

    public void unRegister(String key) {
        if (!map.isEmpty() && map.containsKey(key)) {
            map.remove(key);
        }
    }

    public void notifySuccess(String url) {
        for (String key : map.keySet()) {
            if (url.equals(key)) {
                map.get(url).onSuccess();
            }
        }
    }

    public void notifyFailed(String url, String info) {
        for (String key : map.keySet()) {
            if (key.equals(url)) {
                map.get(url).onFailed(info);
            }
        }
    }

    public void notifyProgress(String url, String fileName, int progress) {
        for (String key : map.keySet()) {
            if (key.equals(url)) {
                map.get(url).onProgress(fileName, progress);
            }
        }
    }

    public void addDownload(String url) {
        if (!urlList.contains(url)) {
            urlList.add(url);
        }
    }

    public void delDownload(String url) {
        if (!urlList.isEmpty() && urlList.contains(url)) {
            urlList.remove(url);
        }
    }

    public boolean isExist(String url) {
        return urlList.contains(url);
    }
}
