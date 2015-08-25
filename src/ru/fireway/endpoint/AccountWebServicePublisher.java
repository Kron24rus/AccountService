package ru.fireway.endpoint;

import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import ru.fireway.ws.AccountWebServiceImpl;

import javax.xml.ws.Endpoint;
import java.util.Timer;
import java.util.TimerTask;

import static org.apache.logging.log4j.LogManager.getLogger;

/**
 * Created by Александр on 02.07.2015.
 */
public class AccountWebServicePublisher {

    Timer timer;

    public static final Logger log = getLogger(AccountWebServicePublisher.class);
    public static AccountWebServiceImpl accountWebService;

    public static void main(String[] args) throws Exception {
        accountWebService = new AccountWebServiceImpl();
        accountWebService.init();
        Endpoint.publish("http://localhost:1986/wss/account", accountWebService);
        System.out.println("Server started");
        new AccountWebServicePublisher(1);
        requestServer();
    }

    public AccountWebServicePublisher(int seconds) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                log.info("Readers: " + accountWebService.getStats().getTotalReaders() + "; Writers: "
                        + accountWebService.getStats().getTotalWriters() + "; Total: "
                        + accountWebService.getStats().getTotalRequests() + "; With rps: "
                        + accountWebService.getStats().getRps());
            }
        }, 0, seconds * 10000);
    }

    public static void requestServer() throws Exception {
        Server server = new Server(8123);
        server.setHandler(new JettyServer(accountWebService));
        server.start();
        server.join();
    }
}
