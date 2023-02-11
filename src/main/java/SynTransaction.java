import com.squareup.okhttp.*;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TransactionCronJob {

    @Autowired
    private DSLContext dslContext;

    @Scheduled(cron = "0 0 0 * * *")
    public void syncTransactions() {
        List<Transaction> transactions = dslContext.selectFrom(TRANSACTION)
                .where(TRANSACTION.STATUS.eq(0))
                .limit(5)
                .fetchInto(Transaction.class);

        for (Transaction transaction : transactions) {
            String endpoint = "http://api.example.com/transactions";
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),
                    objectMapper.writeValueAsString(transaction));
            Request request = new Request.Builder()
                    .url(endpoint)
                    .post(requestBody)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    transaction.setStatus(1);
                    dslContext.update(TRANSACTION)
                            .set(TRANSACTION.STATUS, 1)
                            .where(TRANSACTION.ID.eq(transaction.getId()))
                            .execute();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
