package telegrambot.trivialchalenge;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@SuppressWarnings("ALL")
public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:trivia.db";

    public static void createNewDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                System.out.println("A new database has been created.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createNewTable() {
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS trivia_questions (\n"
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " question TEXT NOT NULL,\n"
                + " answer TEXT NOT NULL,\n"
                + " category TEXT NOT NULL,\n"
                + " options TEXT NOT NULL\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
            System.out.println("Table has been created.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void insertInitialData() {
        String sql = "INSERT INTO trivia_questions (question, answer, category, options) VALUES "
                + "('What is the capital of France?', 'Paris', 'Geography', 'Lyon,Paris,Marseille,Bordeaux'),"
                + "('Who wrote \"To Kill a Mockingbird\"?', 'Harper Lee', 'Literature', 'Mark Twain,Ernest Hemingway,F. Scott Fitzgerald,Harper Lee'),"
                + "('What is the tallest mountain in the world?', 'Mount Everest', 'Geography', 'Mount Everest,K2,Kangchenjunga,Lhotse'),"
                + "('Who developed the theory of relativity?', 'Albert Einstein', 'Science', 'Albert Einstein,Isaac Newton,Nikola Tesla,Galileo Galilei'),"
                + "('Who painted the Mona Lisa?', 'Leonardo da Vinci', 'Art', 'Leonardo da Vinci,Michelangelo,Raphael,Donatello'),"
                + "('What is the smallest prime number?', '2', 'Mathematics', '2,3,5,7'),"
                + "('What element does \"O\" represent on the periodic table?', 'Oxygen', 'Science', 'Oxygen,Gold,Silver,Iron'),"
                + "('Who is the author of \"Pride and Prejudice\"?', 'Jane Austen', 'Literature', 'Charles Dickens,Jane Austen,Emily Bronte,George Eliot'),"
                + "('What is the largest planet in our solar system?', 'Jupiter', 'Science', 'Saturn,Mars, Jupiter,Venus'),"
                + "('Who discovered penicillin?', 'Alexander Fleming', 'Science', 'Marie Curie,Louis Pasteur,Alexander Fleming,Joseph Lister'),"
                + "('What is the chemical symbol for water?', 'H2O', 'Science', 'CO2,CH4,NaCl,H2O'),"
                + "('What is the currency of Japan?', 'Yen', 'Geography', 'Dollar,Yen,Euro,Pound'),"
                + "('Who is the Greek god of thunder?', 'Zeus', 'Mythology', 'Poseidon,Hades,Zeus,Hera');";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Initial data has been inserted.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
