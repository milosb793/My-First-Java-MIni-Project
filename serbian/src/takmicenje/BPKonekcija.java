package takmicenje;
import java.sql.*;

public class BPKonekcija
{
        private Connection konekcija;
        private  static BPKonekcija instanca;

        private BPKonekcija()
        {
            try
            {
                Class.forName("org.sqlite.JDBC");
                konekcija = DriverManager.getConnection("jdbc:sqlite:projekat_baza.db");
            }
            catch( ClassNotFoundException | SQLException e )
            {
                System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                System.exit(0);
            }
        }


        public static BPKonekcija getInstanca()
        {
            if(BPKonekcija.instanca==null)
                BPKonekcija.instanca = new BPKonekcija();

            return BPKonekcija.instanca;
        }

        public ResultSet upit(String upit) throws SQLException//tipa SELECT
        {
            ResultSet rezultat;
            //System.out.println("\n"+upit);
            Statement stmt = konekcija.createStatement();

            return  stmt.executeQuery(upit);
        }

        //upiti tipa INSERT, UPDATE, DELETE
        public int upitIUD(String upit) throws SQLException
        {
            konekcija.setAutoCommit(true);
           // System.out.println("\n"+upit);
            Statement statement = konekcija.createStatement();
            return statement.executeUpdate(upit); //executeUpdate vraca int koji predstavlja proj promeljenih linija
        }

        public void snimiTransakciju() throws SQLException
        {
            konekcija.commit();
        }


}//klasa


