package ru.fireway.endpoint;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import ru.fireway.ws.AccountWebServiceImpl;
import ru.fireway.ws.Stats;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Александр on 08.07.2015.
 */
public class JettyServer extends AbstractHandler {

    Stats tempStat;

    public JettyServer(AccountWebServiceImpl accountWebService) {
        tempStat = accountWebService.getStats();
    }

    @Override
    public void handle(String s, Request request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, ServletException {
        httpServletResponse.setContentType("text/html;charset=utf-8");
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        request.setHandled(true);
        switch (s) {
            case "/reset":
                tempStat.resetStat();
                httpServletResponse.getWriter().println("Statistic was zeroed");
                break;
            default:
                httpServletResponse.getWriter().println("Run total: " + tempStat.getTotalRequests() + ", with RPS: " + tempStat.getRps());
                break;
        }
    }
}
