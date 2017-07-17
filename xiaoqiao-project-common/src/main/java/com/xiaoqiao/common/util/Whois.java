package com.xiaoqiao.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by qiaowentao on 2017/7/13.
 */
public class Whois {

    private static final int DEFAULT_PORT = 43;

    private static Logger LOG = LoggerFactory.getLogger(Whois.class);
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 这里的查询用了直接从whois.chinaz.com的网页中用正则取出所需信息，方法不是很好
     * 不过还是可以查到一些相关信息
     *
     * @param domain 待检测域名，如：baidu.com
     * @return 该域名的Whois信息
     * */
    public static DomainInfo whoisQuery(String domain){
        LOG.info("## whoisQuery ## start domain:",domain);
        Assert.hasText(domain);
        StringBuilder stringBuffer = new StringBuilder();  //查询到的信息
        stringBuffer.append("域名 ").append(domain).append(" 的Whois信息：\n\n");
        DomainInfo domainInfo = null;
        try {
            URL url = new URL("http://whois.chinaz.com?Domain=" + java.net.URLEncoder.encode(domain,"utf-8"));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setConnectTimeout(10000);  //毫秒
            connection.setReadTimeout(10000);

            InputStream inputStream = new BufferedInputStream(connection.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
            String line,str;
            String reg = "<!--IcpMain02-begin-->(.*)<!--IcpMain02-end-->";
            StringBuilder ret = new StringBuilder();
            String lineSeparator = "\r\n";
            while ((line = reader.readLine()) != null) {
                ret.append(line);
            }
            String info;
            Pattern pattern = Pattern.compile(reg);
            Matcher matcher = pattern.matcher(ret.toString());

            a:if(matcher.find()){
                str = matcher.group(1);
                System.out.println(str);
                pattern = Pattern.compile("未被注册或被隐藏(.*?)");
                matcher = pattern.matcher(str);
                if(matcher.find()){
                    stringBuffer.append("该域名未被注册，没有相关Whois信息！").append(lineSeparator);
                    return null;
                }
                domainInfo = new DomainInfo();
                domainInfo.setDomainName(domain);
                pattern = Pattern.compile("<span class=\"fz12 col-gray04 plr5 fl mt10\">以下信息更新时间：(.*?)</span>");
                matcher = pattern.matcher(str);
                if(matcher.find()) {
                    stringBuffer.append("该数据更新于： ").append(matcher.group(1)).append(lineSeparator);
                }
                pattern = Pattern.compile("<ul class=\"WhoisLeft fl\" id=\"sh_info\">(.*)</ul>");
                matcher = pattern.matcher(str);
                if(!matcher.find()){
                    break a;
                }
                str = matcher.group(1);
                //System.out.println("信息："+str);

                pattern = Pattern.compile("<span class=\"pt10 dinline\">(.*)</span>");
                matcher = pattern.matcher(str);
                if(matcher.find()) {
                    stringBuffer.append("域名： ").append(domain).append(lineSeparator);
                }

                pattern = Pattern.compile("<div class=\"fl WhLeList-left\">域名ID(.*)</div>");
                matcher = pattern.matcher(str);
                if(matcher.find()) {
                    pattern = Pattern.compile("<span>(.*?)</span>");
                    matcher = pattern.matcher(matcher.group(1));
                    if (matcher.find())
                        stringBuffer.append("域名ID： ").append(matcher.group(1)).append(lineSeparator);
                }

                pattern = Pattern.compile("<div class=\"fl WhLeList-left\">注册商(.*)</div>");
                matcher = pattern.matcher(str);
                if(matcher.find()) {
                    pattern = Pattern.compile("<span>(.*?)</span>");
                    matcher = pattern.matcher(matcher.group(1));
                    if (matcher.find()) {
                        info = matcher.group(1);
                        domainInfo.setSponsoringRegistrar(info);
                        stringBuffer.append("注册商： ").append(info).append(lineSeparator);
                    }
                }

                pattern = Pattern.compile("<div class=\"fl WhLeList-left\">联系人(.*)</div>");
                matcher = pattern.matcher(str);
                if(matcher.find()) {
                    pattern = Pattern.compile("<span>(.*?)</span>");
                    matcher = pattern.matcher(matcher.group(1));
                    if (matcher.find()) {
                        info = matcher.group(1);
                        domainInfo.setRegistrant(info);
                        stringBuffer.append("联系人： ").append(info).append(lineSeparator);
                    }
                }

                pattern = Pattern.compile("<div class=\"fl WhLeList-left\">联系邮箱(.*)</div>");
                matcher = pattern.matcher(str);
                if(matcher.find()) {
                    pattern = Pattern.compile("<span>(.*?)</span>");
                    matcher = pattern.matcher(matcher.group(1));
                    if (matcher.find()) {
                        info = matcher.group(1);
                        domainInfo.setRegistrantContactEmail(info);
                        stringBuffer.append("联系邮箱： ").append(info).append(lineSeparator);
                    }
                }

                pattern = Pattern.compile("<div class=\"fl WhLeList-left\">联系电话(.*)</div>");
                matcher = pattern.matcher(str);
                if(matcher.find()) {
                    pattern = Pattern.compile("<span>(.*?)</span>");
                    matcher = pattern.matcher(matcher.group(1));
                    if (matcher.find()) {
                        info = matcher.group(1);
                        domainInfo.setRegistrantTel(info);
                        stringBuffer.append("联系电话： ").append(info).append(lineSeparator);
                    }
                }

                pattern = Pattern.compile("<div class=\"fl WhLeList-left\">更新时间(.*)</div>");
                matcher = pattern.matcher(str);
                if(matcher.find()) {
                    pattern = Pattern.compile("<span>(.*?)</span>");
                    matcher = pattern.matcher(matcher.group(1));
                    if (matcher.find()) {
                        info = matcher.group(1);
                        domainInfo.setUpdatedDate(sdf.parse(info.replace('年','-').replace('月','-').replace('日',' ')+" 00:00:00"));
                        stringBuffer.append("更新时间： ").append(info).append(lineSeparator);
                    }
                }

                pattern = Pattern.compile("<div class=\"fl WhLeList-left\">创建时间(.*)</div>");
                matcher = pattern.matcher(str);
                if(matcher.find()) {
                    pattern = Pattern.compile("<span>(.*?)</span>");
                    matcher = pattern.matcher(matcher.group(1));
                    if (matcher.find()) {
                        info = matcher.group(1);
                        domainInfo.setCreationDate(sdf.parse(info.replace('年','-').replace('月','-').replace('日',' ')+" 00:00:00"));
                        stringBuffer.append("创建时间： ").append(info).append(lineSeparator);
                    }
                }

                pattern = Pattern.compile("<div class=\"fl WhLeList-left\">过期时间(.*)</div>");
                matcher = pattern.matcher(str);
                if(matcher.find()) {
                    pattern = Pattern.compile("<span>(.*?)</span>");
                    matcher = pattern.matcher(matcher.group(1));
                    if (matcher.find()) {
                        info = matcher.group(1);
                        domainInfo.setExpirationDate(sdf.parse(info.replace('年','-').replace('月','-').replace('日',' ')+" 00:00:00"));
                        stringBuffer.append("过期时间： ").append(info).append(lineSeparator);
                    }
                }

                pattern = Pattern.compile("<div class=\"fl WhLeList-left\">公司(.*)</div>");
                matcher = pattern.matcher(str);
                if(matcher.find()) {
                    pattern = Pattern.compile("<span>(.*?)</span>");
                    matcher = pattern.matcher(matcher.group(1));
                    if (matcher.find()) {
                        info = matcher.group(1);
                        stringBuffer.append("公司： ").append(info).append(lineSeparator);
                    }
                }

                pattern = Pattern.compile("<div class=\"fl WhLeList-left\">域名服务器(.*)</div>");
                matcher = pattern.matcher(str);
                if(matcher.find()) {
                    pattern = Pattern.compile("<span>(.*?)</span>");
                    matcher = pattern.matcher(matcher.group(1));
                    if (matcher.find()) {
                        info = matcher.group(1);
                        domainInfo.setWhoisServer(info);
                        stringBuffer.append("域名服务器： ").append(info).append(lineSeparator);
                    }
                }

                pattern = Pattern.compile("<div class=\"fl WhLeList-left\">DNS(.*)</div>");
                matcher = pattern.matcher(str);
                if(matcher.find()) {
                    pattern = Pattern.compile("<div class=\"fr WhLeList-right\">(.*?)</div>");
                    matcher = pattern.matcher(matcher.group(1));
                    if (matcher.find()) {
                        info = matcher.group(1).replace("<br/>",";");
                        if(info.contains(";")){
                            domainInfo.setNameServer(info.substring(0, info.length() - 1));
                            stringBuffer.append("DNS： ").append(info.substring(0, info.length() - 1)).append(lineSeparator);
                        }else{
                            domainInfo.setNameServer(info);
                            stringBuffer.append("DNS： ").append(info).append(lineSeparator);
                        }
                    }
                }

                pattern = Pattern.compile("<div class=\"fl WhLeList-left\">状态(.*)</div>");
                matcher = pattern.matcher(str);
                if(matcher.find()) {
                    pattern = Pattern.compile("<div class=\"fr WhLeList-right clearfix\">(.*)</div>");
                    matcher = pattern.matcher(matcher.group(0));
                    if (matcher.find()) {
                        info = matcher.group(1);
                        domainInfo.setStatus(info);
                        stringBuffer.append("状态： ").append(info).append(lineSeparator);
                    }
                }
            }


        }  catch (IOException | ParseException e) {
            LOG.error("## whoisQuery error ##",e.getMessage());
        }
        LOG.info("## whoisQuery end {}##",stringBuffer);
        return domainInfo;
    }

    /*public String query(String domain) throws Exception {
        String server = "";
        String tld = getTLD(domain);
        if ("com".equals(tld)) {
            server = "whois.verisign-grs.com";
        } else if ("net".equals(tld)) {
            server = "whois.verisign-grs.com";
        } else if ("org".equals(tld)) {
            server = "whois.pir.org";
        } else if ("cn".equals(tld)) {
            server = "whois.cnnic.cn";
        } else if ("jp".equals(tld)) {
            server = "whois.jprs.jp";
        } else if ("kr".equals(tld)) {
            server = "whois.kr";
        }
        return query(domain, server);
    }

    public String query(String domain, String server) throws Exception {
        Socket socket = new Socket(server, DEFAULT_PORT);
        String lineSeparator = "\r\n";

        PrintWriter out = new PrintWriter(socket.getOutputStream());
        out.println(domain);
        out.flush();

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        StringBuilder ret = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            if(!"".equals(line) && line.contains("Expiration Date")){
                //ret.append(line.trim().replace("Expiration Date:",""));
                System.out.println("域名："+domain+" 到期时间为："+line.trim().replace("Expiration Date:",""));
            }
            ret.append(line + lineSeparator);
        }
        socket.close();
        return ret.toString();
    }

    private String getTLD(String domain) {
        final int index;
        return (domain == null || (index = domain.lastIndexOf('.') + 1) < 1) ? domain
                : (index < (domain.length())) ? domain.substring(index) : "";
    }*/

    public static void main(String[] args) throws Exception {
        Whois w = new Whois();
        //System.out.println(w.whoisQuery("baidu.com"));

        //System.out.println(w.whoisQuery("360.cn"));          //china

        /*System.out.println(w.whoisQuery("apache.org"));

        System.out.println(w.whoisQuery("mixi.jp"));         //japan
        System.out.println(w.whoisQuery("laneige.co.kr"));   //korea*/
        //System.out.println(w.whoisQuery("csdn.net"));
        //System.out.println(w.whoisQuery("gomeplus.com"));

        DomainInfo result = w.whoisQuery("购了.在线");
        System.out.println(result);
        /*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse("2017年03月24日".replace('年','-').replace('月','-').replace('日',' ')+" 00:00:00");
        System.out.println(date);*/
    }

}
