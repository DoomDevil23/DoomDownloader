package com.bits.dooms_downloader;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URI;
import javax.swing.JOptionPane;
/**
 *
 * @author DoomDevil
 */
public class YouTube {
    private HttpURLConnection httpURLConnection;
    private InputStream inputStream;
    private int contentLength;
    private URL url;
    private URI uri;
    
    public boolean url_Validate(String url){
        return (url.contains("youtube.com") || url.contains("youtu.be")) && (url.contains("v=") || url.contains("shorts"));
    }

    public String get_VideoID(String url){
        String videoID=null;
        int i, j;
        if(url.contains("shorts") || url.contains("youtu.be")){                                 //TO DOWNLOAD FROM SHORTS
            i = url.lastIndexOf("/");
            videoID = url.substring(i+1, url.length());
        } else if (url.contains("v=") && url.contains("music") && !url.contains("list=")) {     //TO DOWNLOAD FROM YT MUSIC
            i = url.indexOf("v=");
            videoID = url.substring(i+2);
        } else if (url.contains("youtube.com") && url.contains("v=")) {                         //TO DOWNLOAD FROM YT IN GENERAL
            i = url.indexOf("v=");
            j = url.indexOf("&");
            videoID = url.substring(i+2, j);
        }
        return videoID;
    }

    public String get_MediaDuration(int timeMs){
        int secs, mins=0, hrs=0;
        String duration;
        secs=timeMs/1000;
        
        //GETTING MINUTES AND HOURS WHEN THE VIDEO IS OVER 59 SECONDS
        if (secs>59) {
            mins = secs / 60;
            secs = secs % 60;
            
            if (mins>60) {
                hrs = mins / 60;
                mins = mins % 60;
            }
        }

        //FORMATING THE TIME DURATION TO SHOW IN VIDEO INFORMATION
        duration = hrs<10 ? "0"+String.valueOf(hrs)+":" : String.valueOf(hrs);
        duration = mins<10 ? duration+"0"+String.valueOf(mins)+":" : duration+String.valueOf(mins)+":";
        duration = secs<10 ? duration+"0"+String.valueOf(secs) : duration+String.valueOf(secs);

        return duration;
    }
    
    //DEPRECATED METHOD TO DOWNLOAD FILES
    public void download_File(String urlString){
        try{
            
            uri = new URI(urlString);
            url = uri.toURL();
            httpURLConnection = (HttpURLConnection) url.openConnection();
            int responseCode = httpURLConnection.getResponseCode();
            
            if(responseCode==HttpURLConnection.HTTP_OK){
                contentLength = httpURLConnection.getContentLength();
                inputStream = httpURLConnection.getInputStream();
            }
            else
                throw new IOException("No file to download. Server replied HTTP code: " + responseCode);
            
            
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error", "Algo ha ido mal\n"+e, JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public int getContentLength(){
        return contentLength;
    }
    public InputStream getInputStream(){
        return inputStream;
    }
    
    public void disconnect() throws IOException{
        inputStream.close();
        httpURLConnection.disconnect();
    }
}
