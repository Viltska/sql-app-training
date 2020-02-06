package Tikape;

import java.sql.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws SQLException {
        Scanner lukija = new Scanner(System.in);
        String database = "testi.db";
        System.out.println("Current database = " + database);

        DatabaseManager manager = new DatabaseManager(database);
        manager.createTables();
        System.out.println("");

        System.out.println("Komennot:");
        System.out.println("1: Lisää asiakas ");
        System.out.println("2: Lisää paikka ");
        System.out.println("3: Lisää paketti");
        System.out.println("4: Tulosta taulukot");
        System.out.println("5: Hae asiakkaan ID");
        System.out.println("0: Lopettaa ohjelman");

        while (true) {
            System.out.print("Anna komento: ");
            String komento = lukija.nextLine();
            if (komento.equals("0")) {
                System.out.println("Kiitos ja näkemiin.");
                break;
            }
            if (komento.equals("1")) {
                System.out.print("Anna nimi: ");
                String nimi = lukija.nextLine();
                manager.uusiAsiakas(nimi);
                System.out.println("---");

            }
            if (komento.equals("2")) {
                System.out.print("Paikan nimi: ");
                String paikka = lukija.nextLine();
                manager.uusiPaikka(paikka);
                System.out.println("----");

            }

            if (komento.equals("3")) {
                System.out.print("Asiakas jolle paketti kuuluu: ");
                String asiakas = lukija.nextLine();
                System.out.println("");
                System.out.print("Seurantakoodi: ");
                String koodi = lukija.nextLine();

                manager.uusiPaketti(asiakas, koodi);
                System.out.println("---");
            }

            if (komento.equals("4")) {
                manager.printTable("Asiakkaat");

            }
            if (komento.equals("5")) {
                System.out.print("Asiakkaan nimi:");
                String asiakas = lukija.nextLine();
                System.out.println(manager.haeAsiakkaanID(asiakas));

            }
        }
    }
}
