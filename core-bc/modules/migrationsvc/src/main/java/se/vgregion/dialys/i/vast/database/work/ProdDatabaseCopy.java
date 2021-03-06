package se.vgregion.dialys.i.vast.database.work;

import java.io.IOException;
import java.text.ParseException;

/**
 * Usage: Install a dump of the old db somewhere and notes its login. Likewise do the same with a another, new database.
 * Go to your home directory and create the file structure home/.app/dialys-i-vast/.
 * Then create 2 jdbc-files: <code> main.jdbc.properties </code> and <code> legacy.jdbc.properties </code>.
 *
 * Inside <code> main.jdbc.properties </code> put the connection information to your New-Db, like this:
 * <code>
 * jdbc.url=jdbc:jtds:sqlserver://localhost:1433;databaseName=pd
 * jdbc.driver=org.postgresql.Driver
 * jdbc.user=sa
 * jdbc.password=scott
 * </code>
 * Do the corresponding thingy
 */
public class ProdDatabaseCopy extends AbstractDatabaseCopy {

    public static void main(String[] args) throws IOException, ParseException {
        getApplicationInfo(); // You don't need this. Just to check if another property file is present.

        AbstractDatabaseCopy dc = new ProdDatabaseCopy();

        dc.init();

        dc.dropAllTablesInTargetDatabase();
        dc.createTablesInTarget();
        dc.addAllTuplesFromLegacyIntoTarget();
        dc.connectUsersWithAnsvarig();

        // Todo: This should not be done when running job for production... comment away then.
        /*dc.obfuscateUserPasswords();*/

        dc.miscPatientsUpdates();
        /*dc.obfuscatePatients();*/
        dc.removeOrphanPdAndBestInfoAndBestPDRad();
        dc.insertObsoleteFlik();

        dc.fixJpaSequence();
    }

}
