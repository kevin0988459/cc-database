package edu.cmu.cs.cloud;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;
import java.io.IOException;
import com.mongodb.client.MongoCursor;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.regex;


public class MongoDBTasks {
    /**
     * The variable storing the mongodb connection string
     */
    private static String connectionString;
    /**
     * MongoClientURI object
     */
    private static MongoClientURI mongoClientURI;
    /**
     * MongoClient object
     */
    private static MongoClient mongoClient;
    /**
     * MongoDarabase object
     */
    private static MongoDatabase mongoDatabase;
    /**
     * MongoCollection Object
     */
    private static MongoCollection<Document> mongoCollection;
    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getRootLogger();

    /**
     * Initialize MongoDB connection.
     *
     * @throws IOException if a network exception occurs.
     */
    private static void initializeConnection() throws IOException {
        // Turn of the logging to get rid of unnecessary standard output.
        LOGGER.setLevel(Level.OFF);
        connectionString = System.getenv("CS");

        // Validate the connection string
        if (connectionString == null || connectionString.isEmpty()) {
            System.out.println("Connection string (CS) is not set in the environment variables.");
            return;
        }

        // create a mongoURI out of the connection string
        mongoClientURI = new MongoClientURI(connectionString);

        // use the URI object to create a mongoClient object
        mongoClient = new MongoClient(mongoClientURI);

        // fetch the database
        mongoDatabase = mongoClient.getDatabase("cosmos");

        // fetch the collection from the database
        mongoCollection = mongoDatabase.getCollection("yelp");
    }

    /**
     * Clean up resources.
     *
     * @throws IOException
     * Throw IOEXception
     */
    private static void cleanup() throws IOException {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }

    /**
     * You should complete the missing parts in the following method.
     * Feel free to add helper functions if necessary.
     *
     * For all questions, output your answer in ONE single line,
     * i.e. use System.out.print().
     *
     * @param args The arguments for main method.
     * @throws IOException if a remote or network exception occurs.
     */
    public static void main(String[] args) throws Throwable {
        initializeConnection();
        switch (args[0]) {
            case "demo":
                demo();
                break;
            case "q9":
                q9();
                break;
            case "q10":
                q10();
                break;
            case "q11":
                q11();
                break;
            case "q12":
                q12();
                break;
            default:
                break;
        }
        cleanup();
    }

    /**
     * This is a demo of how to use MongoDB Java API.
     * It will print the number of businesses in "Pittsburgh".
     *
     * @throws IOException if a remote or network exception occurs.
     */
    private static void demo() throws IOException {
        Bson query = regex("city", "Pittsburgh");

        long count = mongoCollection.countDocuments(query);

        System.out.println("Scan finished. " + count + " match(es) found.");
    }

    /**
     * Question 9.
     *
     * Scenario:
     * What's that new "Asian Fusion" place in "Shadyside" with free wifi and
     * bike parking?
     *
     * Print each name of the business on a single line.
     * If there are multiple answers, print all of them.
     *
     * Hint:
     * 1. The "neighborhood" column should contain "Shadyside" as a substring.
     * 2. The "categories" column should contain "Asian Fusion" as a substring.
     * 3. The "WiFi" and "BikeParking" information can be found in the
     * "attributes" column. Please be careful about the format of the data.
     *
     * You are allowed to make changes such as modifying method name, parameter
     * list and/or return type.
     */
    private static void q9() throws IOException {
        Bson neighborhood = regex("neighborhood", "Shadyside", "i");
        Bson categories = regex("categories", "Asian Fusion", "i");
        Bson wifi = regex("attributes", "'WiFi': 'free'");
        Bson bikeParking = eq("attributes.BikeParking", true);
        Bson query = and(neighborhood, categories, wifi, bikeParking);

        try (MongoCursor<Document> cursor = mongoCollection.find(query).iterator()) {
            if (!cursor.hasNext()) {
            System.out.println("No matches found.");
        }
        while (cursor.hasNext()) {
            Document doc = cursor.next();
            String name = doc.getString("name"); // Assuming the name of the business is stored in the 'name' field
            if (name != null) {
                System.out.println(name);
            }
        }
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Question 10.
     *
     * Scenario:
     * I'm looking for some Indian food to eat in Downtown or Oakland of Pittsburgh
     * that start serving on Fridays at 5pm, but still deliver in case I'm too lazy
     * to drive there.
     *
     * Print each name of the business on a single line.
     * If there are multiple answers, print all of them
     * in ascending lexicographical order.
     *
     * Hint:
     * 1. The "name" column should contain "India" as a substring.
     * 2. The "neighborhood" column should contain "Downtown" or "Oakland"
     * as a substring.
     * 3. The "city" column should contain "Pittsburgh" as a substring.
     * 4. The "hours" column shows the hours when businesses start serving.
     * 5. The "RestaurantsDelivery" information can be found in the
     * "attributes" column.
     *
     * Note:
     * Only for this question, to guarrentee the ascending lexicographical
     * order, you are allowed to sort the result of your query with Java code.
     * No other further processing is allowed.
     *
     * You are allowed to make changes such as modifying method name, parameter
     * list and/or return type.
     */
    private static void q10() throws IOException {
        throw new UnsupportedOperationException(
                "Waiting to be implemented");
    }

    /**
     * Question 11.
     *
     * Find the names and addresses of "Dental" businesses that accept insurance,
     * are open for appointments only, and have a star rating of 4 or higher.
     * These businesses should be within the city of "Ahwatukee".
     *
     * Print each result on a single line with the format "Name: <name>, Address: <address>"
     *
     * You are allowed to make changes such as modifying method name, parameter
     * list and/or return type.
     */
    private static void q11() throws IOException {
        throw new UnsupportedOperationException(
                "Waiting to be implemented");
    }

    /**
     * Question 12.
     *
     * Count the number of unique cities represented in the businesses collection.
     *
     * Print the number on a single line.
     *
     * Note: You are not allowed to scan the whole table and count the number of records.
     *
     * You are allowed to make changes such as modifying method name, parameter
     * list and/or return type.
     */
    private static void q12() throws Throwable {
        throw new UnsupportedOperationException(
                "Waiting to be implemented");
    }
}