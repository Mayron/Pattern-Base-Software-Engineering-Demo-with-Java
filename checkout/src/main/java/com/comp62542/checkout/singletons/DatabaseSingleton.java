package com.comp62542.checkout.singletons;

import com.comp62542.checkout.domain.PaymentMethod;
import com.comp62542.checkout.domain.Product;
import net.ravendb.client.documents.DocumentStore;
import net.ravendb.client.documents.IDocumentStore;
import net.ravendb.client.documents.session.IDocumentSession;
import net.ravendb.client.serverwide.DatabaseRecord;
import net.ravendb.client.serverwide.operations.CreateDatabaseOperation;
import net.ravendb.client.serverwide.operations.DeleteDatabasesOperation;
import net.ravendb.embedded.EmbeddedServer;
import java.math.BigDecimal;

public class DatabaseSingleton {

    private static IDocumentStore store;
    public static boolean embedded = true;

    public static IDocumentSession createSession() {
        if (store == null) {
            createDocumentStore();
        }

        return store.openSession();
    }

    public static void createDocumentStore() {
        try {
            String[] databaseUrls = new String[]{ "http://127.0.0.1:8080/" };
            String dbName = "COMP62542-Checkout";

            if (embedded) {
                EmbeddedServer server = new EmbeddedServer();

                if (store == null) {
                    server.startServer();
                    store = server.getDocumentStore(dbName);
                }

                DeleteDatabasesOperation deleteDatabasesOperation = new DeleteDatabasesOperation(dbName, true);
                store.maintenance().server().send(deleteDatabasesOperation);
            }
            else {
                store = new DocumentStore(databaseUrls, dbName);
                store.getConventions().setMaxNumberOfRequestsPerSession(1024);
                store.initialize();
            }

            try {
                String database = store.getDatabase();
                DatabaseRecord record = new DatabaseRecord(database);
                CreateDatabaseOperation operation = new CreateDatabaseOperation(record);
                store.maintenance().server().send(operation);
            }
            catch (Exception ex) {
                // database exists
            }

            try (IDocumentSession session = store.openSession()) {
                seedData(session);
            }

        } catch (Exception ex) {
            System.out.println("Unable to initialize RavenDB:" + ex.getMessage());
        }
    }

    private static void seedData(IDocumentSession session) {
        if (!session.query(Product.class).any()) {
            // Milk
            Product semiSkimmedMilk = new Product();
            semiSkimmedMilk.price = new BigDecimal("1.15");
            semiSkimmedMilk.discount = 5;
            semiSkimmedMilk.name = "Semi-Skimmed Milk";
            semiSkimmedMilk.perUnit = "50.6p per litre";
            semiSkimmedMilk.imageName = "semi-skimmed-milk.jpg";
            semiSkimmedMilk.type = "milk";
            session.store(semiSkimmedMilk);

            Product skimmed = new Product();
            skimmed.price = new BigDecimal("1.89");
            skimmed.discount = 10;
            skimmed.name = "Skimmed Milk";
            skimmed.perUnit = "94.5p per litre";
            skimmed.imageName = "skimmed-milk.jpg";
            skimmed.type = "milk";
            session.store(skimmed);

            Product wholeMilk = new Product();
            wholeMilk.price = new BigDecimal("1.72");
            wholeMilk.discount = 0;
            wholeMilk.name = "Whole Milk";
            wholeMilk.perUnit = "92.4p per litre";
            wholeMilk.imageName = "whole-milk.jpg";
            wholeMilk.type = "milk";
            session.store(wholeMilk);

            // Fruit
            Product apples = new Product();
            apples.price = new BigDecimal("1.80");
            apples.discount = 0;
            apples.name = "Apples";
            apples.perUnit = "36p each";
            apples.imageName = "apples.jpg";
            apples.type = "fruit";
            session.store(apples);

            Product bananas = new Product();
            bananas.price = new BigDecimal("1.00");
            bananas.discount = 5;
            bananas.name = "Bananas";
            bananas.perUnit = "20p each";
            bananas.imageName = "bananas.jpg";
            bananas.type = "fruit";
            session.store(bananas);

            Product oranges = new Product();
            oranges.price = new BigDecimal("2.50");
            oranges.discount = 30;
            oranges.name = "Oranges";
            oranges.perUnit = "£4.17 per kg";
            oranges.imageName = "oranges.jpg";
            oranges.type = "fruit";
            session.store(oranges);

            // Bread
            Product wholeWheat = new Product();
            wholeWheat.price = new BigDecimal("2.40");
            wholeWheat.discount = 15;
            wholeWheat.name = "Whole Wheat Bread";
            wholeWheat.perUnit = "40p per 100g";
            wholeWheat.imageName = "whole-wheat-bread.jpg";
            wholeWheat.type = "bread";
            session.store(wholeWheat);

            Product sourdough = new Product();
            sourdough.price = new BigDecimal("2.00");
            sourdough.discount = 10;
            sourdough.name = "Sourdough Bread";
            sourdough.perUnit = "35p per 100g";
            sourdough.imageName = "sourdough-bread.jpg";
            sourdough.type = "bread";
            session.store(sourdough);

            Product ryeBead = new Product();
            ryeBead.price = new BigDecimal("1.89");
            ryeBead.discount = 0;
            ryeBead.name = "Rye Bread";
            ryeBead.perUnit = "23.6p per 100g";
            ryeBead.imageName = "rye-bread.jpg";
            ryeBead.type = "bread";
            session.store(ryeBead);

            Product pita = new Product();
            pita.price = new BigDecimal("1.20");
            pita.discount = 40;
            pita.name = "Pita Bread";
            pita.perUnit = "20p each";
            pita.imageName = "pita-bread.jpg";
            pita.type = "bread";
            session.store(pita);
        }

        if (!session.query(PaymentMethod.class).any()) {
            PaymentMethod card = new PaymentMethod();
            card.expiry = "05/20";
            card.type = "card";
            card.description = "12345678";
            card.name = "J Smith";
            session.store(card);

            PaymentMethod gpay = new PaymentMethod();
            gpay.type = "gpay";
            gpay.description = "GooglePay:12345678";
            gpay.name = "J Smith";
            session.store(gpay);

            PaymentMethod paypal = new PaymentMethod();
            paypal.type = "gpay";
            paypal.description = "PayPal:12345678";
            paypal.name = "J Smith";
            session.store(paypal);
        }

        session.saveChanges();
    }
}