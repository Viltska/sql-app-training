
import java.sql.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws SQLException {
        Scanner lukija = new Scanner(System.in);
        String databaseName = "tikape.db";
        DatabaseManager manager = new DatabaseManager(databaseName);

        System.out.println("Tervetuloa!");
        System.out.println("Nykyinen tietokanta: " + databaseName);
        System.out.println("");
        System.out.println("Komennot:");
        System.out.println("1: Lisää asiakas ");
        System.out.println("2: Lisää paikka ");
        System.out.println("3: Lisää paketti");
        System.out.println("4: Lisää tapahtuma");
        System.out.println("5: Hae paketin tapahtumat");
        System.out.println("6: Hae asiakkaan paketit");
        System.out.println("7: Hae paikan tapahtumien määrä päivämäärällä");
        System.out.println("8: Tehokkuus testi");
        System.out.println("9: Luo tietokannan ja/tai puuttuvat taulukot");
        System.out.println("0: Lopettaa ohjelman");

        while (true) {

            System.out.println("");
            System.out.print("Syötä komento (0-9): ");
            String komento = lukija.nextLine();

            if (komento.equals("0")) {
                manager.tikapePrint();
                System.out.println("Ohjelma suljetaan.");
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
            if (komento.equals("5")) {
                System.out.print("Syötä paketin seurantakoodi: ");
                String seurantaKoodi = lukija.nextLine();
                manager.haePaketinTapahtumat(seurantaKoodi);
            }
            if (komento.equals("6")) {
                System.out.print("Syötä asiakkaan nimi: ");
                String asiakas = lukija.nextLine();
                manager.haeAsiakkaanPaketit(asiakas);
            }
            if (komento.equals("7")) {
                System.out.println("Syötä päivämäärä muodossa 'YYYY-MM-DD'");
                System.out.print("Päivämäärä: ");
                String pvm = lukija.nextLine();
                System.out.print("Syötä paikannimi: ");
                String paikannimi = lukija.nextLine();
                manager.haePaikanTapahtumatPaivamaaralla(pvm, paikannimi);
            }
            if (komento.equals("8")) {
                /* System.out.print("Tallennetaanko testin aikana tehdyt muutokset taulukkoon? (Y/N): ");
                String yesno = lukija.nextLine();
                boolean poistetaan = false;
                if (yesno.equals("N") || yesno.equals("n")) {
                    poistetaan = true;
                } */
                manager.tehokkuusTesti(false);
            }
            if (komento.equals("9")) {
                System.out.println("Tarkistetaan tietokantaa..");
                manager.createTables();
                
            }
        }
    }
}
