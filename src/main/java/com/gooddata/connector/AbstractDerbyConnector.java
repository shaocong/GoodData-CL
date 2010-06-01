package com.gooddata.connector;

import com.gooddata.connector.exceptions.InternalErrorException;
import com.gooddata.integration.model.DLIPart;
import com.gooddata.transformation.generator.DerbySqlGenerator;

import java.io.*;
import java.sql.*;
import java.util.List;

import static org.apache.derby.tools.ij.runScript;

/**
 * GoodData  abstract derby connector
 * This connector creates a GoodData LDM schema from a source schema, extracts the data from the source,
 * normalizes the data, and create the GoodData data deployment package. This connector uses the embedded Derby SQL
 * database.
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public abstract class AbstractDerbyConnector extends AbstractConnector {

    /**
     * The derby SQL generator
     */
    protected DerbySqlGenerator sg = new DerbySqlGenerator();


    /**
     * static initializer of the Derby SQL JDBC driver
     */
    static {
        String driver = "org.apache.derby.jdbc.EmbeddedDriver";
        try {
            Class.forName(driver).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructor
     * @param projectId project id
     * @param name schema name
     */
    protected AbstractDerbyConnector(String projectId, String name) {
        super(projectId, name);
    }

    /**
     * Constructor
     * @param projectId project id
     * @param name schema name
     * @param configFileName config file name
     * @throws java.io.IOException in case of an IO issue
     */
    protected AbstractDerbyConnector(String projectId, String name, String configFileName) throws IOException {
        super(projectId, name, configFileName);
    }


    /**
     * Connects to the Derby database
     * @return JDBC connection
     * @throws SQLException
     */
    protected Connection connect() throws SQLException {
        String protocol = "jdbc:derby:";
        return DriverManager.getConnection(protocol + projectId +"_" + name + ";create=true");
    }

    /**
     * Initializes the Derby database schema that is going to be used for the data normalization
     */
    public void initialize() {
        Connection con = null;
        try {
            con = connect();
            String sql = sg.generateDDL(schema);
            // run the SQL script
            InputStream is = new ByteArrayInputStream(sql.getBytes("UTF-8"));
            int result = runScript(con, is, "UTF-8", System.out, "UTF-8");
        }
        catch (SQLException e) {
            throw new InternalError(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            throw new InternalError(e.getMessage());
        } finally {
            try {
                if (con != null && !con.isClosed())
                    con.close();
            }
            catch (SQLException e) {
                throw new InternalError(e.getMessage());
            }
        }
    }


    /**
     * Perform the data normalization (generate lookups) in the Derby database. The database must contain the required
     * tables
     */
    public void transform() {
        Connection con = null;
        try {
            con = connect();
            String sql = sg.generateDML(schema);
            // run the SQL script
            InputStream is = new ByteArrayInputStream(sql.getBytes("UTF-8"));
            int result = runScript(con, is, "UTF-8", System.out, "UTF-8");
        }
        catch (SQLException e) {
            throw new InternalError(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            throw new InternalError(e.getMessage());
        } finally {
            try {
                if (con != null && !con.isClosed())
                    con.close();
            }
            catch (SQLException e) {
                throw new InternalError(e.getMessage());
            }
        }
    }

    /**
     * Drops all snapshots
     */
    public void dropSnapshots() {
        File derbyDir = new File (System.getProperty("derby.system.home") +
                System.getProperty("file.separator") + projectId + "_" + name);
        derbyDir.delete();
    }

    /**
     * Lists the current snapshots
     * @return list of snapshots as String
     * @throws InternalErrorException in case of internal issues (e.g. uninitialized schema)
     */
    public String listSnapshots() throws InternalErrorException {
        String result = "ID\t\tFROM ROWID\t\tTO ROWID\t\tTIME\n";
        result += "------------------------------------------------\n";        
        Connection con = null;
        Statement s = null;
        ResultSet r = null;
        try {
            con = connect();
            s = con.createStatement();
            r = s.executeQuery("SELECT id,firstid,lastid,tmstmp FROM snapshots");
            for(boolean rc = r.next(); rc; rc = r.next()) {
                int id = r.getInt(1);
                int firstid = r.getInt(2);
                int lastid = r.getInt(3);
                long tmstmp = r.getLong(4);
                Date tm = new Date(tmstmp);
                result += id + "\t\t" + firstid + "\t\t" + lastid + "\t\t" + tm + "\n";
            }
        }
        catch (SQLException e) {
            throw new InternalErrorException(e.getMessage());
        }
        finally {
            try {
                if(r != null && !r.isClosed())
                    r.close();
                if(s != null && !s.isClosed())
                    s.close();
                if(con != null && !con.isClosed())
                    con.close();
            }
            catch (SQLException ee) {
                ee.printStackTrace();
            }
        }
        return result;
    }

    /**
     * Get last snapshot number
     * @return last snapshot number
     * @throws InternalErrorException in case of internal issues (e.g. uninitialized schema)
     */
    public int getLastSnapshotId() throws InternalErrorException {
        Connection con = null;
        Statement s = null;
        ResultSet r = null;
        try {
            con = connect();
            s = con.createStatement();
            r = s.executeQuery("SELECT MAX(id) FROM snapshots");
            for(boolean rc = r.next(); rc; rc = r.next()) {
                int id = r.getInt(1);
                return id;
            }
        }
        catch (SQLException e) {
            throw new InternalErrorException(e.getMessage());
        }
        finally {
            try {
                if(r != null && !r.isClosed())
                    r.close();
                if(s != null && !s.isClosed())
                    s.close();
                if(con != null && !con.isClosed())
                    con.close();
            }
            catch (SQLException ee) {
                ee.printStackTrace();
            }
        }
        throw new InternalErrorException("Can't retrieve the last snapshot number.");
    }

    /**
     * Figures out if the connector is initialized
     * @return the initialization status
     */
    public boolean isInitialized() {
        Connection con = null;
        Statement s = null;
        ResultSet r = null;
        try {
            con = connect();
            s = con.createStatement();
            r = s.executeQuery("SELECT id,firstid,lastid,tmstmp FROM snapshots WHERE id=0");
            r.next();
        }
        catch (SQLException e) {
            return false;
        }
        finally {
            try {
                if(r != null && !r.isClosed())
                    r.close();
                if(s != null && !s.isClosed())
                    s.close();
                if(con != null && !con.isClosed())
                    con.close();
            }
            catch (SQLException ee) {
                ee.printStackTrace();
            }
        }
        return true;
    }

    /**
     * Load the all normalized data from the Derby SQL to the GoodData data package on the disk
     * @param parts the Data Loading Interface parts
     * @param dir target directory where the data package will be stored
     */
    public void load(List<DLIPart> parts, String dir) {
        loadSnapshot(parts, dir, null);
    }

    /**
     * Load the normalized data from the Derby SQL to the GoodData data package on the disk
     * incrementally (specific snapshot)
     * @param parts the Data Loading Interface parts
     * @param dir target directory where the data package will be stored
     * @param snapshotIds snapshot ids that are going to be loaded (if NULL, all snapshots are going to be loaded)
     */
    public void loadSnapshot(List<DLIPart> parts, String dir, int[] snapshotIds) {
        Connection con = null;
        try {
            con = connect();
            String sql = "";
            // generate SELECT INTO CSV Derby SQL
            // the required data structures are taken for each DLI part
            for (DLIPart p : parts) {
                sql += sg.generateLoad(p, dir, snapshotIds);
            }
            // run the SQL script
            InputStream is = new ByteArrayInputStream(sql.getBytes("UTF-8"));
            int result = runScript(con, is, "UTF-8", System.out, "UTF-8");
        }
        catch (SQLException e) {
            throw new InternalError(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            throw new InternalError(e.getMessage());
        } finally {
            try {
                if (con != null && !con.isClosed())
                    con.close();
            }
            catch (SQLException e) {
                throw new InternalError(e.getMessage());
            }
        }
    }


}