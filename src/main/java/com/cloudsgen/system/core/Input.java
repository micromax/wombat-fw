package com.cloudsgen.system.core;


import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.DecodeException;
import io.vertx.ext.web.Cookie;
import io.vertx.ext.web.RoutingContext;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class Input {


    private  Set<io.vertx.ext.web.FileUpload> FileUpload;
    private MultiMap _POST = null;
    private HttpServerRequest _GET = null;


    private String AgentBrowser;
    private String AgentIp;
    private String referrer;
    private  RoutingContext rx ;
    private MultiMap headers;

    public io.vertx.ext.web.FileUpload GetFile(String var_name){
        io.vertx.ext.web.FileUpload file = null;
        for (io.vertx.ext.web.FileUpload f : this.getFileUpload() ) {
            if(f.name() == var_name )
            {
                file = f;
                return file;
            }
        }
        return file;
    }

    public String getAgentBrowser() {
        return AgentBrowser;
    }


    public String GetUploadedFilePath(String var_name){
        io.vertx.ext.web.FileUpload file = null;
        for (io.vertx.ext.web.FileUpload f : this.getFileUpload() ) {
            if(f.name().equals(var_name)  )
            {
                    file = f;

                    String  exit = FilenameUtils.getExtension(file.fileName());

                    File uploadedFile = new File(file.uploadedFileName());


                     uploadedFile.renameTo(new File(file.uploadedFileName() + "."+exit));
                try {
                    uploadedFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                new File(file.uploadedFileName()).delete();
                return file.uploadedFileName() + "."+exit;
            }
            else {
                System.out.println(f.name());
            }
        }
        return null;
    }


    public Set<io.vertx.ext.web.FileUpload> getFileUpload() {
        return FileUpload;
    }

    public void setFileUpload(Set<io.vertx.ext.web.FileUpload> fileUpload) {
        FileUpload = fileUpload;
    }

    public Input(RoutingContext delegate) {

        try {

            final MultiMap headers = delegate.request().headers();
            this.rx = delegate;
            this._GET = delegate.request();
            this._POST = delegate.request().formAttributes();
            this.AgentIp = delegate.request().remoteAddress().host().toString();
            this.FileUpload = delegate.fileUploads();
            this.referrer = headers.contains("referrer") ? headers.get("referrer") : headers.get("referer");
            this.AgentBrowser = delegate.request().headers().get("user-agent");

            this.headers = headers;

                    //System.out.println(this._POST);
            System.out.println("RX : "+this.rx.toString());
        }catch (DecodeException e)
        {
            e.printStackTrace();
        }

    }

    public String getReferrer() {
        return referrer;
    }


    public MultiMap getHeaders() {
        return headers;
    }

    public String GET(String Key)
    {
        return this._GET.getParam(Key);
    }

    public String POST(String Key){

        if(this._POST.contains(Key))
        {
            return this._POST.get(Key);
        }
        return null;
    }

        public String REQUEST(String Key){

                String _rr;
                    _rr = this.POST(Key);
                if(_rr == null )
                {
                    _rr = this.GET(Key);
                }
            if(_rr == null )
            {
                _rr = "false";
            }
            return _rr;
        }
    public String Signal(int n){
        String p = this.rx.request().path();

        String[] pa = p.split("/");
        if(pa.length > n && pa[n] != null)
        {
            return pa[n];
        }else {
            return null;
        }
    }


    public String getAgentIp() {
        return AgentIp;
    }
    public void setCookie(String CookieName , String CookieVal ){


        Cookie cookie = Cookie.cookie(CookieName, CookieVal);
        cookie.setPath("/");
        cookie.setMaxAge(18000);
        this.rx.addCookie(cookie);
    }

    public void setFlash(String CookieName , String CookieVal ){


        Cookie cookie = Cookie.cookie(CookieName, CookieVal);
        cookie.setPath("/");
        cookie.setMaxAge(3);
        this.rx.addCookie(cookie);
    }

    public String GetCookie(String CookieName)
    {

        String r = null;
        try {
            r = this.rx.getCookie(CookieName).getValue();
        }catch (NullPointerException e){

        }
        return r;
    }



    public boolean RemoveCookie(String CookieName)
    {
        Boolean r = false;

        try {

            Cookie rk = this.rx.removeCookie(CookieName);
            if(rk != null)
            {


                rk.setMaxAge(0);
                r = true;
            }else {
                r = false;
            }
        }catch (Exception e)
        {
            r = false;
        }


        return  r;
    }

    public void setAgentIp(String agentIp) {
        AgentIp = agentIp;
    }
}
