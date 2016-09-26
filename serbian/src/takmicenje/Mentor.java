package takmicenje;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class Mentor extends Korisnik
{
    private ArrayList<Takmicar> lista;

    public Mentor(int id, String kor_ime, String lozinka, char tip_kor)
    {
        super(id, kor_ime, lozinka, tip_kor);
    }

    private void ucitajMojeTakmicare()
    {
        this.lista = new ArrayList<Takmicar>();

        ArrayList<Integer> id_ovi = new ArrayList<Integer>();
        String upit2 = "SELECT * FROM takmicar WHERE mentor_id='" + ((Mentor)Korisnik.getKorisnik()).korisnik_id + "';";

        try {
            ResultSet rs = BPKonekcija.getInstanca().upit(upit2);
            while(rs.next())
            {
                id_ovi.add(rs.getInt("takmicar_id")); //uzimamo id-ove svih takmicara kojima je mentor Mentor
            }

            //uzimanje svih takmicara
            ArrayList<Takmicar> sviTakm = new ArrayList<Takmicar>();
            sviTakm = Takmicar.sviTakmicari();

            for(Takmicar t : sviTakm)
            {
                for(Integer i : id_ovi)
                {
                    if(t.korisnik_id == i)
                        this.lista.add(t);
                }
            }
        }
        catch (SQLException | NullPointerException e)
        {
            e.printStackTrace();
        }
    }


    public void prikaziMojeTakmicare()
    {
        int i = 0;
        this.ucitajMojeTakmicare();

        Metode.sacekaj(500);
        Metode.ocistiEkran();
        Metode.ispisN("Моји такмичари:");
        Metode.dekoracija();

        if (this.lista != null)
        {
            Metode.ispisN("\nР. број     Кор. име     Лозинка     Тип     \n");
            for (Takmicar t : this.lista)
            {
                Metode.sacekaj(200);
                Metode.ispisN("\t" + ++i + ".     \t" + t.kor_ime + "     \t\t" + t.lozinka + "     \t" + t.tip_kor);
            }
            Metode.dekoracija();
        }
        else
        {
            Metode.sacekaj(400);
            Metode.ispisN("Тренутно немате ни једног такмичара. ");
        }
    }

}//klasa
