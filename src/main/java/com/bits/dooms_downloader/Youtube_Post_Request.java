package com.bits.dooms_downloader;

/**
 *
 * @author DoomDevil
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Youtube_Post_Request {
    //API CALL URL
    static final String YT_API_URL = "https://www.youtube.com/youtubei/v1/player?key=";
    static final String YT_API_KEY = "AIzaSyAO_FJ2SlqU8Q4STEHLGCilw_Y9_11qcW8";

    //JSON BODY PARAMETERS
    static final String CLIENT_NAME = "ANDROID_TESTSUITE";
    static final String CLIENT_VERSION = "1.9";
    static final int ANDROID_SDK_VERSION = 30;
    static final String HL = "en";
    static final String GL = "US";
    static final int UTC_OFFSET_MINUTES = 0;
    
    /*OLD PARAMETERS
    static final String HL = "en";
    static final String CLIENT_NAME = "WEB";
    static final String CLIENT_VERSION = "2.20240105.01.00";
    static final String CLIENT_FORM_FACTOR = "UNKNOWN_FORM_FACTOR";
    static final String CLIENT_SCREEN = "WATCH";
    static final String GRAFT_URL = "/watch?v=";//NEEDS THE VIDEO ID ON THE JSON REQUEST BODY
    static final String LOCKED_SAFTY_MODE = "false";
    static final String USE_SSL = "true";
    static final int VIS = 0;
    static final String SPLAY = "false";
    static final String AUTO_CAPTIONS_DEFAULT_ON = "false";
    static final String AUTO_NAV_STATE = "STATE_NONE";
    static final String HTML5_PREFERENCE = "HTML5_PREF_WANTS";
    static final String LACT_MILLISECONDS = "-1";
    static final int SIGNATURE_TIMESTAMP = 19725;
    static final String RACY_CHECK_OK = "false";
    static final String CONTENT_CHECK_OK = "false";
    */

    private String video_id="";

    public Youtube_Post_Request(String video_id){
        this.video_id = video_id;
    }

    //THIS METHOD CALLS THE YOUTUBE API AND GETS THE RESPONSE FROM YOUTUBE WITH ACTUAL DOWNLOAD URLS
    public String getResponse(){
        URL url = null;
        HttpURLConnection con = null;
        OutputStream os;
        StringBuilder response = new StringBuilder();
        String responseLine = null;
        String jsonString = createJSON();
        byte[] input;// = jsonString.getBytes("utf-8");

        //HERE WE GET THE URL TO CALL THE API
        try {
            url = new URL(createURL());
        }
        catch (MalformedURLException e){
            System.out.println("ERROR: "+e);
        }


        try {
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
        }
        catch (IOException e){
            System.out.println("ERROR: "+e);
        }

        try {
            os = con.getOutputStream();
            input = jsonString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        catch (IOException e){
            System.out.println("ERROR: "+e);
        }

        try {

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));


            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }
        catch (IOException e){
            System.out.println("ERROR: "+e);
        }
        return response.toString();
    }

    //THIS METHOD FORMAT URL TO CALL THE YOUTUBE API
    private String createURL (){
        return YT_API_URL+YT_API_KEY;
    }

    //THIS METHOD CREATES THE JSON BODY TO SEND TO YOUTUBE API
    private String createJSON (){
        return "{" +
                    "'videoId': '" + video_id + "'," +
                    "'context':{" +
                        "'client':{"+
                            "'clientName': '"+CLIENT_NAME+"'," +
                            "'clientVersion': '"+CLIENT_VERSION+"'," +
                            "'androidSdkVersion': '"+ANDROID_SDK_VERSION+"'," +
                            "'hl': '"+HL+"'," +
                            "'gl': '"+GL+"'," +
                            "'utcOffsetMinutes': "+UTC_OFFSET_MINUTES+
                        "}" +
                    "}" +
                "}";
    }
}
