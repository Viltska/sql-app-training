package Tikape;

import java.sql.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws SQLException {
        Scanner lukija = new Scanner(System.in);
        String databaseName = "tikape.db";
        System.out.println("Current database: " + databaseName);

        DatabaseManager manager = new DatabaseManager(databaseName);
   
        System.out.println("Komennot:");
        System.out.println("");
        System.out.println("1: Lisää asiakas ");
        System.out.println("2: Lisää paikka ");
        System.out.println("3: Lisää paketti");
        System.out.println("4: Lisää tapahtuma");
        System.out.println("8: Luo taulukot");
        System.out.println("0: Lopettaa ohjelman");
        

        while (true) {
            System.out.print("Syötä komento: ");
            String komento = lukija.nextLine();
            if (komento.equals("0")) {
                System.out.println("Kiitos ja näkemiin.");
                break;
            }
            if (komento.equals("1")) {
                System.out.print("Syötä nimi: ");
                String nimi = lukija.nextLine();
                manager.uusiAsiakas(nimi);

            }
            if (komento.equals("2")) {
                System.out.print("Syötä paikannimi: ");
                String paikka = lukija.nextLine();
                manager.uusiPaikka(paikka);

            }

            if (komento.equals("3")) {
                System.out.print("Syötä paketin asiakas: ");
                String asiakas = lukija.nextLine();
                System.out.print("Syötä paketin seurantakoodi: ");
                String koodi = lukija.nextLine();

                manager.uusiPaketti(asiakas, koodi);
            }
            if (komento.equals("4")) {
                System.out.print("Syötä tapahtuman paikka: ");
                String paikka = lukija.nextLine();
                System.out.print("Syötä paketin seurantakoodi: ");
                String seurantaKoodi = lukija.nextLine();
                System.out.print("Syötä tapahtuman kuvaus: ");
                String kuvaus = lukija.nextLine();
                manager.uusiTapahtuma(paikka, seurantaKoodi, kuvaus);

            }

            if (komento.equals("8")) {
                System.out.println("Luodaan taulukot");
                manager.createTables();
            }
        }
    }
}
