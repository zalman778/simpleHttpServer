import Http.HttpSession;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

/**
 * Created by Hiwoo on 18.12.2017.
 * Helper class for html code generating - to list files
 */
public class HtmlExplorer {
    public static String htmlExploreCurrentFolder(HttpSession httpSession) throws IOException {

        StringBuilder sb = new StringBuilder();
        String linkPrefix = "http://"+String.valueOf(InetAddress.getLoopbackAddress()).replaceAll("localhost/", "")+":"+Constants.SV_SRV_CONFIG_IP_PORT;

        String linkDiff = httpSession.getUserCurrent().toString().replace(httpSession.getUserRoot().toString(), "");
        String linkOffset = "";



        //del first / from req (if new link)
        if (linkDiff.length() > 0) {
            if (linkDiff.charAt(0) == '\\')
                linkDiff = linkDiff.substring(1, linkDiff.length());
        }


        if (linkDiff != null && !linkDiff.equals(""))
            linkOffset = "/"+linkDiff;

        // getting parent link. if len(curr) > getParent then substr root+root, else root
        String parentFileDiff = "/";
        if (httpSession.getUserCurrent().getParent().length() > httpSession.getUserRoot().toString().length())
            parentFileDiff = httpSession.getUserCurrent().getParent().replace(httpSession.getUserRoot().toString(), "").replaceAll("^/+", "");



        sb.append("<html><head><title>list of "+httpSession.getUserCurrent().getName()+"</title></head><body>");
        sb.append("<a href=\""+linkPrefix+parentFileDiff+"\">..</a><br>");
        for (File f : httpSession.getUserCurrent().listFiles()) {
            sb.append("<a href=\""+linkPrefix+linkOffset+"/"+f.getName()+"\">"+f.getName()+"</a><br>");
        }
        sb.append("</body></html>");

        return sb.toString();
    }


}
