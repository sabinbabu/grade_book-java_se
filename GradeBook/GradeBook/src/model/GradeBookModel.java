/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

/**
 *
 * @author PC
 */
public class GradeBookModel implements IConnect, IQuery<GradeBookModel.Query, Student> {

    /**
     * The Query enum specifies the queries that are supported by this manager
     */
    public static enum Query {
        INSERT, ALL, STUDENTID, UPDATE, GRADE, TOLERANCEVALUE, TOLERANCERANGE
    };
    // The connection to the grade book
    private Connection connection = null;
    // Database details for the address book being managed
    private static final String URL = "jdbc:derby://localhost:1527/GradeBook";
    private static final String USERNAME = "gradebook";
    private static final String PASSWORD = "gradebook";
    // variables for Grades -- HD, D, C , P -- minVAlues
    private static final int HD = 85;
    private static final int D = 75;
    private static final int C = 65;
    private static final int P = 50;

    /* 
     * We use enummaps to map queries (enum values) to SQL commands and prepared 
     * statements in a typesafe manner. Hashmaps could be used to the same effect,
     * but they would be less eficient, not that it matters. You could use arrays
     * indexed by enum.ordinal() or by int constants, but you then lose type safety 
     * among other things.
     */
    private final EnumMap<GradeBookModel.Query, String> sqlCommands = new EnumMap<>(GradeBookModel.Query.class);
    private final EnumMap<GradeBookModel.Query, PreparedStatement> statements = new EnumMap<>(GradeBookModel.Query.class);

    /**
     * Create an instance of the address book manager. Clients have no access to
     * the implementation details of the address book. Also, clients can create
     * multiple instances of the manager, which is probably a bad idea.
     */
    public GradeBookModel() {
        // Specify the queries that are supported
        //insert into database
        sqlCommands.put(GradeBookModel.Query.INSERT,
                "INSERT INTO MARKS (STUDENTID, ASSIGNMENT1, ASSIGNMENT2, EXAM, TOTAL, GRADE ) VALUES ( ?, ?, ?, ?, ?, ? )");
        //get records of all students
        sqlCommands.put(GradeBookModel.Query.ALL,
                "SELECT * FROM MARKS");
        // get students by student id
        sqlCommands.put(Query.STUDENTID,
                "SELECT * FROM MARKS WHERE STUDENTID = ?");
        // get students achieving a certain grade
        sqlCommands.put(Query.GRADE,
                "SELECT * FROM MARKS WHERE GRADE = ? ORDER BY TOTAL ASC");
        // update students marks
        sqlCommands.put(Query.UPDATE,
                "UPDATE MARKS SET ASSIGNMENT1 = ? , ASSIGNMENT2 =?, EXAM = ? , TOTAL = ?, GRADE = ? WHERE STUDENTID = ?");
        //get students acheieving certain marks
        sqlCommands.put(GradeBookModel.Query.TOLERANCEVALUE,
                "SELECT * FROM MARKS WHERE TOTAL BETWEEN ? AND ? OR TOTAL BETWEEN ? AND ? OR TOTAL BETWEEN ? AND ? OR TOTAL BETWEEN ? AND ? ORDER BY TOTAL ASC");
        //get students acheieving certain mark range
        sqlCommands.put(GradeBookModel.Query.TOLERANCERANGE,
                "SELECT * FROM MARKS WHERE TOTAL BETWEEN ? AND ? ORDER BY TOTAL ASC");
    }

    // IConnect implementation
    /**
     * Connect to the address book
     *
     */
    @Override
    public void connect() throws ConnectionException {
        // Connect to the address book database
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

        } catch (SQLException e) {
            throw new ConnectionException("Unable to open data source", e);
        }
    }

    /**
     * Perform any initialization that is needed before queries can be
     * performed.
     *
     *
     */
    @Override
    public void initialise() throws ConnectionException {
        // Create prepared statements for each query
        try {
            for (Query q : Query.values()) {
                statements.put(q, connection.prepareStatement(sqlCommands.get(q)));
            }
        } catch (SQLException e) {
            throw new ConnectionException("Unable to initialise data source", e);
        }
    }

    /**
     * Disconnect from the grade book
     *
     * @throws ConnectionException
     */
    @Override
    public void disconnect() throws ConnectionException {
        // Close the connection 

        try (Connection c = connection) {
            // connection is closed automatically with try with resources
            // close prepared statements first
            for (Query q : Query.values()) {
                statements.get(q).close();
            }
        } catch (SQLException e) {
            throw new ConnectionException("Unable to close data source", e);
        }
    }

    // IQuery implementation
    /**
     * Perform a command insert and update on the grade book
     *
     *
     */
    @Override
    public int command(Query query, Student student) throws QueryException {
        switch (query) {
            case INSERT:
                return addStudent(student);
            case UPDATE:
                return updateStudent(student);
        }
        // Should never happen
        return -1;

    }

    @Override
    public List<Student> select(Query q, Object... o) throws QueryException {
        switch (q) {
            case ALL:
                return getAllStudent();
            case TOLERANCEVALUE:
                return getToleranceValue((String) o[0]);
            case TOLERANCERANGE:
                return getToleranceRange((String) o[0], (String) o[1]);
            case STUDENTID:
                return getStudentById((String) o[0]);
            case GRADE:
                return getStudentByGrade((String) o[0]);
        }
        // Should never happen
        return null;
    }

    private Student createStudent(ResultSet rs) throws QueryException {
        Student s = null;
        try {
            s = new Student(
                    rs.getInt("studentId"),
                    rs.getInt("assignment1"),
                    rs.getInt("assignment2"),
                    rs.getInt("exam"),
                    rs.getInt("total"),
                    rs.getString("grade")
            );
        } catch (SQLException e) {
            throw (new QueryException("Unable to process the result of selection query", e));
        }
        return s;
    }

    /*
     * Add a record to the grade book. Record fields are extracted from the method
     * parameter, which is a Student object. 
     */
    private int addStudent(Student s) throws QueryException {
        // Look up prepared statement
        PreparedStatement ps = statements.get(Query.INSERT);

        // insert student attributes into prepared statement
        try {
            ps.setInt(1, s.getStudentID());
            ps.setDouble(2, s.getAssignment1());
            ps.setDouble(3, s.getAssignment2());
            ps.setDouble(4, s.getExam());
            ps.setDouble(5, s.getTotal());
            ps.setString(6, s.getGrade());
        } catch (SQLException e) {
            throw (new QueryException("Unable to paramaterise selection query", e));
        }

        try {
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw (new QueryException("Unable to perform insert command", e));
        }
    }

    private int updateStudent(Student s) throws QueryException {
        // Look up prepared statement
        PreparedStatement ps = statements.get(Query.UPDATE);

        // update student attributes into prepared statement
        try {

            ps.setDouble(1, s.getAssignment1());
            ps.setDouble(2, s.getAssignment2());
            ps.setDouble(3, s.getExam());
            ps.setDouble(4, s.getTotal());
            ps.setString(5, s.getGrade());
            ps.setInt(6, s.getStudentID());
        } catch (SQLException e) {
            throw (new QueryException("Unable to paramaterise selection query", e));
        }

        try {
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw (new QueryException("Unable to perform update command", e));
        }
    }

    /*
     * Select all of the entries in the grade book
     */
    private List<Student> getAllStudent() throws QueryException {
        // get prepared statement
        PreparedStatement ps = statements.get(Query.ALL);

        try (ResultSet resultSet = ps.executeQuery()) {
            List<Student> results = new ArrayList<>();
            while (resultSet.next()) {
                Student s = createStudent(resultSet);
                results.add(s);
            }
            return results;
        } catch (SQLException e) {
            throw (new QueryException("Unable to execute selection query", e));
        }
    }

    private List<Student> getToleranceValue(String value) throws QueryException {
        // Look up prepared statement
        int toleranceValue = Integer.parseInt(value);
        int minValue = 1;
        PreparedStatement ps = statements.get(Query.TOLERANCEVALUE);
        try {
            // Insert tolerance value into prepared statement
            ps.setString(1, Integer.toString(HD - toleranceValue));
            ps.setString(2, Integer.toString(HD - minValue));
            ps.setString(3, Integer.toString(D - toleranceValue));
            ps.setString(4, Integer.toString(D - minValue));
            ps.setString(5, Integer.toString(C - toleranceValue));
            ps.setString(6, Integer.toString(C - minValue));
            ps.setString(7, Integer.toString(P - toleranceValue));
            ps.setString(8, Integer.toString(P - minValue));

        } catch (SQLException e) {
            throw (new QueryException("Unable to paramaterise selection query", e));
        }

        try (ResultSet resultSet = ps.executeQuery()) {
            List<Student> results = new ArrayList<>();
            while (resultSet.next()) {
                results.add(createStudent(resultSet));
            }
            return results;
        } catch (SQLException e) {
            throw (new QueryException("Unable to execute selection query", e));
        }
    }

    private List<Student> getToleranceRange(String minValue, String maxValue) throws QueryException {
        // Look up prepared statement

        PreparedStatement ps = statements.get(Query.TOLERANCERANGE);
        try {
            // Insert student tolerance range into prepared statement
            ps.setString(1, minValue);
            ps.setString(2, maxValue);

        } catch (SQLException e) {
            throw (new QueryException("Unable to paramaterise selection query", e));
        }

        try (ResultSet resultSet = ps.executeQuery()) {
            List<Student> results = new ArrayList<>();
            while (resultSet.next()) {
                results.add(createStudent(resultSet));
            }
            return results;
        } catch (SQLException e) {
            throw (new QueryException("Unable to execute selection query", e));
        }
    }

    /*
     * Select people by student ID
     */
    private List<Student> getStudentById(String id) throws QueryException {
        // Look up prepared statement
        PreparedStatement ps = statements.get(Query.STUDENTID);
        try {
            // Insert student id into prepared statement
            ps.setString(1, id);
        } catch (SQLException e) {
            throw (new QueryException("Unable to paramaterise selection query", e));
        }

        try (ResultSet resultSet = ps.executeQuery()) {
            List<Student> results = new ArrayList<>();
            while (resultSet.next()) {
                results.add(createStudent(resultSet));
            }
            return results;
        } catch (SQLException e) {
            throw (new QueryException("Unable to execute selection query", e));
        }
    }

    private List<Student> getStudentByGrade(String grade) throws QueryException {
        // Look up prepared statement
        PreparedStatement ps = statements.get(Query.GRADE);
        try {
            // Insert grade into prepared statement
            ps.setString(1, grade);
        } catch (SQLException e) {
            throw (new QueryException("Unable to paramaterise selection query", e));
        }

        try (ResultSet resultSet = ps.executeQuery()) {
            List<Student> results = new ArrayList<>();
            while (resultSet.next()) {
                results.add(createStudent(resultSet));
            }
            return results;
        } catch (SQLException e) {
            throw (new QueryException("Unable to execute selection query", e));
        }
    }

}
