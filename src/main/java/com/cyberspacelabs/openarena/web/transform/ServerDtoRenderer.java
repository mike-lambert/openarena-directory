package com.cyberspacelabs.openarena.web.transform;

import com.cyberspacelabs.openarena.web.dto.Server;

/**
 * Created by mike on 10.12.16.
 */
public class ServerDtoRenderer {
    public String renderServerType(Server server){
        String title = server.getType().equalsIgnoreCase("OPENARENA") ? "OpenArena" : "Quake 3";
        String icon = server.getType().equalsIgnoreCase("OPENARENA") ? "../static/icon_openarena.png" : "../static/icon_quake3.png";
        return  "<img alt=\"" + title + "\" title=\"" + title +"\" src=\""+ icon + "\" width=\"32\"/>" +
                "<span class=\"text-value\">" + title +"</span>";
    }

    public String renderServerPing(Server server){
        String led = "led-red";
        if (server.getPing() <= 100){ led = "led-green"; }
        else if (server.getPing() > 100 && server.getPing() <= 300){ led = "led-yellow"; }
        return  "<div class=\"" + led +"\" title=\""+ server.getPing() +"\">" +
                "<span class=\"text-value\">"+ server.getPing() + "</span></div>";
    }

    public String renderServerLoad(Server server){
        String[] values = server.getLoad().split("/", 2);
        int present = Integer.parseInt(values[0]);
        int total = Integer.parseInt(values[1]);
        float rate = (present * 1.0f) / (total * 1.0f);
        String style = "text-red";
        if (rate <= 0.5){ style = "text-green"; }
        else if (rate > 0.5 && rate <= 0.8) { style = "text-yellow"; }
        return "<span class=\""+ style +"\">"+ server.getLoad() +"</span>";
    }

    public String renderServerLocation(Server server){
        String result = "";
        if (server.getLocation() == null || server.getLocation().getDomain() == null || server.getLocation().getDomain().isEmpty()){
            return "&lt;Unknown&gt;";
        }
        if (server.getLocation().getCode() != null && !server.getLocation().getCode().isEmpty()){
            result = "<span><img src=\"http://www.geognos.com/api/en/countries/flag/"
                    + server.getLocation().getCode() +".png\""
                    + " style=\"width: 30px; margin-right: 6px;\"></span>";
        }
        return result +
               "<span>" + server.getLocation().getDomain() +"</span>";
    }

    public String render(Server server){
        StringBuilder sb = new StringBuilder();
        sb.append("<tr>").append("\r\n")
                .append(" <td>").append(server.getName()).append("</td>").append("\r\n")
                .append(" <td>").append(renderServerType(server)).append("</td>").append("\r\n")
                .append(" <td>").append(renderServerPing(server)).append("</td>").append("\r\n")
                .append(" <td>").append(renderServerLoad(server)).append("</td>").append("\r\n")
                .append(" <td>").append(renderServerLocation(server)).append("</td>").append("\r\n")
                .append(" <td>").append(server.getAddress()).append("</td>").append("\r\n")
                .append(" <td>").append(server.getMode()).append("</td>").append("\r\n")
                .append(" <td>").append(server.getMap()).append("</td>").append("\r\n")
                .append("</tr>").append("\r\n");
        return sb.toString();
    }
}
