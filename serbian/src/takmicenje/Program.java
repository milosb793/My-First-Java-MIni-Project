package takmicenje;

public class Program
{
    public static void main(String[] args)
    {
        Metode.ocistiEkran();
        Metode.ucitavanje();

        //objekat koji se koristi
        Korisnik.setKorisnik(null);

        //glavni meni
         Korisnik.meni();

    }
}