package ru.fireway.ws;

import javax.jws.WebService;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Александр on 02.07.2015.
 */

@WebService(endpointInterface = "ru.fireway.ws.AccountWebService")
public class AccountWebServiceImpl implements AccountWebService {

    private AccountDao accountDao;
    private Map<Integer, AtomicLong> cache = new ConcurrentHashMap<>();
    private ExecutorService executorService = Executors.newFixedThreadPool(256);
    private Stats countClient;

    private void fillCache() throws SQLException {
        Collection<AccountEntity> all = accountDao.getAll();
        for (AccountEntity e : all) {
            cache.put(e.getId(), new AtomicLong(e.getAmount()));
        }
    }

    @Override
    public Long getAmount(Integer id) {
        AtomicLong amount = cache.get(id);
        countClient.incReaderR();
        if (null == amount) {
            return 0L;
        } else {
            return amount.get();
        }
    }

    @Override
    public void addAmount(final Integer id, Long value) {
        countClient.incWriterR();
        AtomicLong amount = cache.get(id);
        if (amount == null) {
            amount = new AtomicLong(0);
            cache.put(id, new AtomicLong(value));
        }
        amount.addAndGet(value);
        final AtomicLong finalAmount = amount;
        executorService.submit(() -> {
            try {
                accountDao.save(id, finalAmount.get());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void init() throws Exception{
        accountDao = new AccountDao();
        countClient = new Stats();
        fillCache();
    }

    public Stats getStats() {
        return countClient;
    }
}
