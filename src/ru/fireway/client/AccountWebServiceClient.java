package ru.fireway.client;

/**
 * Created by Александр on 02.07.2015.
 */

import ru.fireway.ws.AccountWebService;
import ru.fireway.ws.AccountWebServiceImplService;

import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AccountWebServiceClient {

    private static volatile boolean running = true;
    private static AccountWebService account;
    private static int maxIdQuantity;
    private static int amountRange;

    public static void main(String[] args) throws Exception {
        Properties properties = new Properties();
        properties.load(AccountWebServiceClient.class.getClassLoader().getResourceAsStream("client.properties"));
        AccountWebServiceImplService accountService = new AccountWebServiceImplService();
        account = accountService.getAccountWebServiceImplPort();

        maxIdQuantity = Integer.parseInt(properties.getProperty("max.id"));
        amountRange = Integer.parseInt(properties.getProperty("max.addamountrange"));

        runReaders(Integer.parseInt(properties.getProperty("readers.quantity")));
        runWriters(Integer.parseInt(properties.getProperty("writers.quantity")));
    }

    private static void runReaders(int count) {
        if (count <= 0) {
            System.out.println("No readers to run");
            return;
        }
        ExecutorService executorService = Executors.newFixedThreadPool(count);
        System.out.println("Run readers: " + count);
        Random rand = new Random(System.currentTimeMillis());
        while (count > 0) {
            executorService.submit(() -> {
                while (running) {
                    int randId = rand.nextInt(maxIdQuantity) + 1;
                    account.getAmount(randId);
                }
            });
            count--;
        }
    }

    private static void runWriters(int count) {
        if (count <= 0) {
            System.out.println("No writers to run");
            return;
        }
        ExecutorService executorService = Executors.newFixedThreadPool(count);
        System.out.println("Run writers: " + count);
        Random rand = new Random(System.currentTimeMillis());
        while (count > 0) {
            executorService.submit(() -> {
                while (running) {
                    int randId = rand.nextInt(maxIdQuantity) + 1;
                    int balance = rand.nextInt(amountRange) - amountRange/2 + 1;
                    account.addAmount(randId, (long)balance);
                }
            });
            count--;
        }
    }
}
