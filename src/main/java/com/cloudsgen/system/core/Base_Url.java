package com.cloudsgen.system.core;

import io.vertx.ext.web.RoutingContext;
import org.apache.commons.lang3.RandomStringUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.cloudsgen.system.utils.Security.SHA225;
public class Base_Url {

    private RoutingContext rx;
    private String burl;


    static final String youTubeUrlRegEx = "^(https?)?(://)?(www.)?(m.)?((youtube.com)|(youtu.be))/";
    static final String[] videoIdRegex = { "\\?vi?=([^&]*)","watch\\?.*v=([^&]*)", "(?:embed|vi?)/([^/?]*)", "^([A-Za-z0-9\\-]*)"};


    public Base_Url(RoutingContext rx)
    {
        this.rx = rx;
        if(this.rx.request().isSSL()) {
            this.burl = "https://"+this.rx.request().host().trim();
        }else {
            this.burl = "http://"+this.rx.request().host().trim();
        }
    }
    public String b(){

        return this.burl;
    }
    public String b(String p){

        return this.burl+"/"+p;
    }

    public  String getRandomNumberInRange() {
        int min = 100000;
        int max = 999999999;
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        return SHA225(((Math.random() * ((max - min) + 1)) + min)+"") ;
    }


    public  String RandomString() {



        return  RandomStringUtils.randomAlphanumeric(8);
    }


    public static String encode(String str)
    {
        return Base64.getEncoder().encodeToString(str.getBytes());
    }

    public static String decode(String str)
    {
        byte[] decodedBytes = Base64.getDecoder().decode(str);
        return new String(decodedBytes);
    }

    public static String Youtube(String playlod){

        String out= "";
        if(isYoutubeUrl(playlod))
        {
                String id = extractVideoIdFromUrl(playlod);
                out = "<div class=\"view overlay\"><div class=\"youtube\" id=\""+id+"\"  >\n" +
                        "</div></div>";
        }else{
            out = playlod;
        }

        return out;

    }

    public static String extractVideoIdFromUrl(String url) {
        String youTubeLinkWithoutProtocolAndDomain = youTubeLinkWithoutProtocolAndDomain(url);

        for(String regex : videoIdRegex) {
            Pattern compiledPattern = Pattern.compile(regex);
            Matcher matcher = compiledPattern.matcher(youTubeLinkWithoutProtocolAndDomain);

            if(matcher.find()){
                return matcher.group(1);
            }
        }

        return null;
    }
    private static String youTubeLinkWithoutProtocolAndDomain(String url) {
        Pattern compiledPattern = Pattern.compile(youTubeUrlRegEx);
        Matcher matcher = compiledPattern.matcher(url);

        if(matcher.find()){
            return url.replace(matcher.group(), "");
        }
        return url;
    }


    public static boolean isYoutubeUrl(String youTubeURl)
    {
        boolean success;
        //https://www.youtube.com/watch?v=gXk74t_uC9o&feature=youtu.be
        String patternx = "^(https?)?(://)?(www.)?(m.)?((youtube.com)|(youtu.be))/";
        Pattern pattern = Pattern.compile( patternx );
        System.out.println(youTubeURl.trim());
        System.out.println(pattern.matcher(youTubeURl.trim()));
        if ( pattern.matcher(youTubeURl.trim()).matches())
        {
            success = true;
            System.out.println("IS URL YOUTUBE");
        }
        else
        {
            if(youTubeURl.contains("youtu")){
                success = true;
                System.out.println("IS URL YOUTUBE");
            }else {
                System.out.println("Not Valid youtube URL");
                //
                success = false;
            }

        }
        return success;
    }


    public  String calcd(Timestamp data)
    {
        String r ="الان";


            //2020-04-20 02:41:21.666000
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date now = new Date();

            Date d1 =   data ;
            long diff = now.getTime() - d1.getTime();


            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);

            if(diffSeconds != 0)
            {
                r = diffSeconds +"ثانيه";
            }
            if(diffMinutes != 0)
            {
                r = diffMinutes +"د";
            }

            if(diffHours != 0)
            {
                r = diffHours +"س";
            }
            if(diffDays != 0)
            {
                r = diffDays +"يوم";
            }





        return  r;
    }


    /**
     * TODO: delete ths test
     *
     * @param args
     */
     public static void main(String[] args) {
         String ar = Base_Url.encode("::-س");
         String ar_en = Base_Url.decode(ar);


         System.out.println(ar);
         System.out.println(ar_en);


    }




}
