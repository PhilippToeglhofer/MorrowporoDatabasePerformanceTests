package view;

import com.mongodb.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import model.sources.TableRowData;
import model.sources.Title;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

public class GUI_Elements {

    private final double windowWidth;
    private final double windowHeight;
    private final TextField textCSV;
    private final TextField searchField;
    private BorderPane csvBorderPane;
    private final TableView<TableRowData> table;
    private ObservableList<TableRowData> observableTableRowDataList;
    private Button editButton;
    private final CheckBox preventClosing;
    private boolean isFullscreen;
    private boolean canClose;
    private long durschnittINSERT;
    private long durschnittUPDATE;
    private long durschnittSELECT;
    private long durschnittDELETE;
    private long minINSERT;
    private long minUPDATE;
    private long minSELECT;
    private long minDELETE;
    private int iterations;
    private Random random = new Random();
    private static DateTimeFormatter formatter;
    private java.awt.Label dateTime;
    private Spinner<Integer> editorFontSizeSpinner;
    private static ArrayList<TableRowData> ListOfPlayers;
    private static String player_id;
    private static String nickname;
    private static String first_name;
    private static String last_name;
    private static String place;
    private static String level;
    private static String exp;
    private static String HP;
    private static String MP;
    private static String SP;
    private static String STR;
    private static String GES;
    private static String INT;
    private static String AUS;
    private static String KON;
    private static String gamelevel;
    private static String isJumping;
    private static String skillpoints;
    private static String resourcepoints;
    private static String highscore;
    private static String lastlogin;

    public GUI_Elements(double windowWidth, double windowHeight) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.searchField = new TextField();
        this.textCSV = new TextField();
        this.csvBorderPane = new BorderPane(textCSV);
        this.csvBorderPane.setVisible(false);
        this.table = new TableView<>();
        this.editButton = new Button("Edit");
        this.preventClosing = new CheckBox("prevent from closing");
        this.preventClosing.setSelected(true);
        this.isFullscreen= false;
        this.canClose = true;
        ListOfPlayers = new ArrayList<>();
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.dateTime = new java.awt.Label();
        this.durschnittINSERT = 0;
        this.durschnittUPDATE = 0;
        this.durschnittSELECT = 0;
        this.durschnittDELETE = 0;
        this.minUPDATE = 2147483647;
        this.minSELECT = 2147483647;
        this.minDELETE = 2147483647;
        this.minINSERT = 2147483647;
        minINSERT += Long.parseLong("22222222222"); //Can cause to an Error because its a higher number then max long number => Solution BigInterger()
    }

    public Parent createStartMenu(MenuWindow view) {
        AnchorPane anchorPane = new AnchorPane();
        FlowPane flowPane = new FlowPane();
        flowPane.setStyle("-fx-background-color: DARKGRAY;");
        setAnchor(flowPane, 0, 0, 0, 0);
        GridPane grid = createGrid();
        createTable();

        editorFontSizeSpinner = createSpinner(12);
        HBox editorFontSizeHBox = createHBox("Editor Font Size", editorFontSizeSpinner);
        setEventsOnSpinners();

        loadProject();
        createEditButton(view);
        createCsvBorderPane();
        createButtons(grid, editorFontSizeHBox, view);
        setAnchor(table, windowWidth*0.2, windowWidth*0.005, windowHeight*0.01, windowHeight*0.01);
        setAnchor(grid, 0.0, 0.0, 0.0, 0.0);
        anchorPane.getChildren().addAll(flowPane, grid, table, csvBorderPane);
        AtomicBoolean isHeight = new AtomicBoolean(false);
        table.heightProperty().addListener((obs, oldVal, newVal) -> {
            if (!isHeight.get()){
                isHeight.set(true);
            }else {
                double diff = newVal.doubleValue() - oldVal.doubleValue();
                csvBorderPane.setTranslateY(csvBorderPane.getTranslateY() + diff);
            }
        });

        AtomicBoolean isWidth = new AtomicBoolean(false);
        table.widthProperty().addListener((obs, oldVal, newVal) -> {
            if (!isWidth.get()){
                isWidth.set(true);
            }else {
                csvBorderPane.setMinWidth(newVal.doubleValue());
                csvBorderPane.setMaxWidth(newVal.doubleValue());
            }
        });

        return anchorPane;
    }

    private void loadProject(){
        System.out.println("LOAD Project from Online Database ...");
        loadTableVariablesFromOnlineDatabase();
    }

    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    private void loadTableVariablesFromOnlineDatabase() {
        System.out.println("START HERE");
        dateTime.setText(LocalDateTime.now().format(formatter));
        ListOfPlayers.clear();
        Connection con;
        int selectDatabase = 2; //1 = Oracle, 2 = PostgreSQL, 3 = MySQL, 4 = MongoDB
        iterations = 1;
        int numberOfRecords = 1;

        try {
            System.out.println("Start database connection");
            switch(selectDatabase){
                case 1:
                    con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "SYSTEM", "root"); //Oracle
                    selectDataFromTable(con, selectDatabase);
                    for (int x = 0; x < iterations; x++) {
                        SQL_PerformanceTest(con, true, numberOfRecords, selectDatabase);
                    }
                    con.close();
                    System.out.println("closed database connection");
                    break;
                case 2:
                    con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Morrow2", "postgres", "root"); //PostgreSQL
                    selectDataFromTable(con, selectDatabase);
                    for (int x = 0; x < iterations; x++) {
                        SQL_PerformanceTest(con, false, numberOfRecords, selectDatabase);
                    }
                    con.close();
                    System.out.println("closed database connection");
                    break;
                case 3:
                    con = DriverManager.getConnection("jdbc:mysql://localhost:3306/morrow", "root", ""); //MySQL
                    selectDataFromTable(con, selectDatabase);
                    for (int x = 0; x < iterations; x++) {
                        SQL_PerformanceTest(con, true, numberOfRecords, selectDatabase);
                    }
                    con.close();
                    System.out.println("closed database connection");
                    break;
                case 4:
                    MongoClient mongoConnection = new MongoClient("localhost", 27017); //MongoDB
                    DBCollection mongoTable = mongoConnection.getDB("Morrow3").getCollection("player_data_and_status");
                    selectDataFromTableMongoDB(mongoTable);
                    for (int x = 0; x < iterations; x++) {
                        performanceTestMongoDB(mongoTable, numberOfRecords);
                    }
                    mongoConnection.close();
                    System.out.println("closed database connection");
                    break;
                default:
                    System.out.println("database connection failed");
                    break;
            } } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("database connection failed: " + ex);
        }

        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        System.out.println();
        System.out.println("Iterations: " + iterations + " | NumberOfRecords: " + numberOfRecords);
        durschnittINSERT = durschnittINSERT / iterations;
        durschnittUPDATE = durschnittUPDATE / iterations;
        durschnittSELECT = durschnittSELECT / iterations;
        durschnittDELETE = durschnittDELETE / iterations;
        System.out.println("Durchschnitt Nanosekunden INSERT statement: " + durschnittINSERT);
        System.out.println("Durchschnitt Nanosekunden UPDATE statement: " + durschnittUPDATE);
        System.out.println("Durchschnitt Nanosekunden SELECT statement: " + durschnittSELECT);
        System.out.println("Durchschnitt Nanosekunden DELETE statement: " + durschnittDELETE);
        System.out.println();
        System.out.println("Minimum Nanosekunden INSERT statement: " + minINSERT);
        System.out.println("Minimum Nanosekunden UPDATE statement: " + minUPDATE);
        System.out.println("Minimum Nanosekunden SELECT statement: " + minSELECT);
        System.out.println("Minimum Nanosekunden DELETE statement: " + minDELETE);
        System.out.println();
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        System.out.println();
        System.out.println("END HERE");

        if (ListOfPlayers != null) {
            table.setItems(sortList(ListOfPlayers));
        }else { System.out.println("Database has no entries"); }
    }

    private void selectDataFromTable(Connection con, int dbSelection) {
        try {
            Statement stmt = con.createStatement();
            System.out.println("create SELECT statement");
            ResultSet rs = null;

            if (dbSelection == 1 || dbSelection == 3) {
                String slevel = "Level_ID";
                if (dbSelection == 3){ slevel = "Level"; }
                rs = stmt.executeQuery("SELECT player_data.*," +
                        " player_status." + slevel +
                        ", player_status.Experience," +
                        " player_status.HP," +
                        " player_status.MP," +
                        " player_status.SP," +
                        " player_status.STR," +
                        " player_status.GES," +
                        " player_status.INT," +
                        " player_status.AUS," +
                        " player_status.KON," +
                        " player_status.Gamelevel," +
                        " player_status.isJumping," +
                        " player_status.Skillpoints," +
                        " player_status.Resourcepoints," +
                        " player_status.Highscore," +
                        " player_status.Lastlogin" +
                        " FROM player_data INNER JOIN player_status ON player_data.Player_id = player_status.Player_id");
            }

            if (dbSelection == 2) {
                rs = stmt.executeQuery("SELECT player_data.*," +
                        " player_status.\"Level\"," +
                        " player_status.\"Experience\"," +
                        " player_status.\"HP\"," +
                        " player_status.\"MP\"," +
                        " player_status.\"SP\"," +
                        " player_status.\"STR\"," +
                        " player_status.\"GES\"," +
                        " player_status.\"INT\"," +
                        " player_status.\"AUS\"," +
                        " player_status.\"KON\"," +
                        " player_status.\"Gamelevel\"," +
                        " player_status.\"isJumping\"," +
                        " player_status.\"Skillpoints\"," +
                        " player_status.\"Resourcepoints\"," +
                        " player_status.\"Highscore\"," +
                        " player_status.\"Lastlogin\"" +
                        " FROM player_data INNER JOIN player_status ON player_data.\"Player_id\" = player_status.\"Player_id\";");
            }

            while (rs.next()) {
                player_id = rs.getString(1);
                nickname = rs.getString(2);
                first_name = rs.getString(3);
                last_name = rs.getString(4);
                place = rs.getString(5);
                level = rs.getString(6);
                exp = rs.getString(7);
                HP = rs.getString(8);
                MP = rs.getString(9);
                SP = rs.getString(10);
                STR = rs.getString(11);
                GES = rs.getString(12);
                INT = rs.getString(13);
                AUS = rs.getString(14);
                KON = rs.getString(15);
                gamelevel = rs.getString(16);
                isJumping = rs.getString(17);
                skillpoints = rs.getString(18);
                resourcepoints = rs.getString(19);
                highscore = rs.getString(20);
                lastlogin = rs.getString(21);

                ListOfPlayers.add(createTableRow(player_id, nickname, first_name, last_name, place, level, exp, HP, MP, SP, STR, GES, INT, AUS, KON, gamelevel, isJumping, skillpoints, resourcepoints, highscore, lastlogin));
        }
            rs.close();
            stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    private void selectDataFromTableMongoDB(DBCollection mongoTable) {
        DBCursor cursor = mongoTable.find();
        while (cursor.hasNext()){
            DBObject dbObject = cursor.next();
            ListOfPlayers.add(createTableRow(dbObject.get("Player_id"),
                    dbObject.get("Nickname").toString(),
                    dbObject.get("First_Name").toString(),
                    dbObject.get("Last_Name").toString(),
                    dbObject.get("Place").toString(),
                    dbObject.get("Level").toString(),
                    dbObject.get("Experience").toString(),
                    dbObject.get("HP").toString(),
                    dbObject.get("MP").toString(),
                    dbObject.get("SP").toString(),
                    dbObject.get("STR").toString(),
                    dbObject.get("GES").toString(),
                    dbObject.get("INT").toString(),
                    dbObject.get("AUS").toString(),
                    dbObject.get("KON").toString(),
                    dbObject.get("Gamelevel").toString(),
                    dbObject.get("isJumping").toString(),
                    dbObject.get("Skillpoints").toString(),
                    dbObject.get("Resourcepoints").toString(),
                    dbObject.get("Highscore").toString(),
                    dbObject.get("Lastlogin").toString()));
        }
        cursor.close();
    }

    private void performanceTestMongoDB(DBCollection mongoTable, int numberOfRecords) {
        System.out.println("\nStart Performance Test");
        BasicDBObject document;
        long startTimer;
        long endTimer;
        long elapsedTime;
        long tempMinInsert = 0;

        for (int counter = 0; counter < numberOfRecords; counter++) {
            //System.out.println("create INSERT statement");
            document = new BasicDBObject();
            document.put("Nickname", "Morrow " + (counter + 1));
            document.put("Level", random.nextInt(5) + 1);
            document.put("Experience", random.nextInt(11));
            document.put("HP", random.nextInt(10) + 5);
            document.put("MP", random.nextInt(10) + 5);
            document.put("SP", random.nextInt(10) + 5);
            document.put("STR", random.nextInt(5) + 4);
            document.put("GES", random.nextInt(5) + 4);
            document.put("INT", random.nextInt(5) + 4);
            document.put("AUS", random.nextInt(5) + 4);
            document.put("KON", random.nextInt(5) + 4);
            document.put("Gamelevel", random.nextInt(3) + 1);
            document.put("isJumping", random.nextInt(2));
            document.put("Skillpoints", random.nextInt(6));
            document.put("Resourcepoints", random.nextInt(6));
            document.put("Highscore", random.nextInt(3333));
            document.put("Lastlogin", dateTime.getText());
            startTimer = System.nanoTime();
            mongoTable.insert(document);
            endTimer = System.nanoTime();
            elapsedTime = endTimer - startTimer;
            System.out.println("INSERT Statement " + (counter + 1) + " Time: " + elapsedTime + " Nanoseconds");
            durschnittINSERT += elapsedTime;
            tempMinInsert += elapsedTime;
        }

        if (tempMinInsert < minINSERT) {
            minINSERT = tempMinInsert;
        }

        //System.out.println("create UPDATE statement");
        BasicDBObject searchQuery = new BasicDBObject();
        Pattern pattern = Pattern.compile("(^Morrow)"); //regex search
        searchQuery.put("Nickname", pattern); //"Morrow"
        BasicDBObject updateDocument = new BasicDBObject();
        //updateDocument.put("Name", "Morrow");
        updateDocument.put("Level", random.nextInt(5) + 1);
        updateDocument.put("Experience", random.nextInt(11));
        updateDocument.put("HP", random.nextInt(10) + 5);
        updateDocument.put("MP", random.nextInt(10) + 5);
        updateDocument.put("SP", random.nextInt(10) + 5);
        updateDocument.put("STR", random.nextInt(5) + 4);
        updateDocument.put("GES", random.nextInt(5) + 4);
        updateDocument.put("INT", random.nextInt(5) + 4);
        updateDocument.put("AUS", random.nextInt(5) + 4);
        updateDocument.put("KON", random.nextInt(5) + 4);
        updateDocument.put("Gamelevel", random.nextInt(3) + 1);
        updateDocument.put("isJumping", random.nextBoolean()); //random.nextInt(2));
        updateDocument.put("Skillpoints", random.nextInt(6));
        updateDocument.put("Resourcepoints", random.nextInt(6));
        updateDocument.put("Highscore", random.nextInt(3333));
        updateDocument.put("Lastlogin", dateTime.getText());
        BasicDBObject update = new BasicDBObject();
        update.put("$set", updateDocument);
        //mongoTable.update(searchQuery, update);
        startTimer = System.nanoTime();
        mongoTable.updateMulti(searchQuery, update);
        endTimer = System.nanoTime();
        elapsedTime = endTimer - startTimer;
        System.out.println("UPDATE Statement Time: " + elapsedTime + " Nanoseconds");
        durschnittUPDATE += elapsedTime;
        if (elapsedTime < minUPDATE) {
            minUPDATE = elapsedTime;
        }

        //System.out.println("create SELECT statement");
        startTimer = System.nanoTime();
        DBCursor cursor = mongoTable.find(searchQuery);
        endTimer = System.nanoTime();
        elapsedTime = endTimer - startTimer;
        //System.out.println("Number of Records: " + cursor.size());
        /*while (cursor.hasNext()){
            cursor.next();
        }*/
        System.out.println("SELECT Statement Time: " + elapsedTime + " Nanoseconds");
        durschnittSELECT += elapsedTime;
        if (elapsedTime < minSELECT) {
            minSELECT = elapsedTime;
        }
        cursor.close();

        //System.out.println("create DELETE statement");
        startTimer = System.nanoTime();
        mongoTable.remove(searchQuery);
        endTimer = System.nanoTime();
        elapsedTime = endTimer - startTimer;
        System.out.println("DELETE Statement Time: " + elapsedTime + " Nanoseconds");
        durschnittDELETE += elapsedTime;
        if (elapsedTime < minDELETE) {
            minDELETE = elapsedTime;
        }

        System.out.println("End of Performance Test\n");
    }

    private void SQL_PerformanceTest(Connection con, boolean isDate, int numberOfRecords, int selecDatabase) {
        try {
            System.out.println();
            System.out.println("\nStart Performance Test");
            int row;
            long startTimer;
            long endTimer;
            long elapsedTime;
            long tempMinInsert = 0;
            Statement stmt = con.createStatement();
            String sqlInsertPlayerData = null;
            String sqlInsertPlayerStatus = null;
            String sqlUpdate = null;
            String sqlSelect = null;
            String sqlDelete = null;

            if (selecDatabase == 1 || selecDatabase == 3) { //Oracle || MySQL
                sqlInsertPlayerData = "INSERT INTO player_data (Player_id, Nickname, First_Name, Last_Name, Place) VALUES(?,?,?,?,?)";
                if (selecDatabase == 1) {
                    sqlInsertPlayerStatus = "INSERT INTO player_status (Player_id, Level_id, Experience, HP, MP, SP, STR, GES, INT, AUS, KON, Gamelevel, isJumping, Skillpoints, Resourcepoints, Highscore, Lastlogin) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    sqlUpdate = "UPDATE (SELECT player_status.Highscore as OLD FROM player_status INNER JOIN player_data ON player_data.Player_id = player_status.Player_id WHERE player_data.Nickname LIKE 'Morrow%') t SET t.OLD = 33333";
                }else {
                    sqlInsertPlayerStatus = "INSERT INTO `player_status` (`Player_id`, `Level`, `Experience`, `HP`, `MP`, `SP`, `STR`, `GES`, `INT`, `AUS`, `KON`, `Gamelevel`, `isJumping`, `Skillpoints`, `Resourcepoints`, `Highscore`, `Lastlogin`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    sqlUpdate = "UPDATE player_status INNER JOIN player_data ON player_data.Player_id = player_status.Player_id SET player_status.Highscore = 33333 WHERE Nickname LIKE 'Morrow%'";
                }
                sqlSelect = "SELECT player_data.*, player_status.* FROM player_data INNER JOIN player_status ON player_data.Player_id = player_status.Player_id WHERE player_data.Nickname LIKE 'Morrow%'";
                sqlDelete = "DELETE FROM player_data WHERE Nickname LIKE 'Morrow%'";
            }
            if (selecDatabase == 2) { //PostgreSQL
                sqlInsertPlayerData = "INSERT INTO player_data (\"Player_id\", \"Nickname\", \"First_Name\", \"Last_Name\", \"Place\") VALUES(?,?,?,?,?)";
                sqlInsertPlayerStatus = "INSERT INTO player_status (\"Player_id\", \"Level\", \"Experience\", \"HP\", \"MP\", \"SP\", \"STR\", \"GES\", \"INT\", \"AUS\", \"KON\", \"Gamelevel\", \"isJumping\", \"Skillpoints\", \"Resourcepoints\", \"Highscore\", \"Lastlogin\") VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                sqlUpdate = "UPDATE player_status AS B SET \"Highscore\" = 33333 FROM player_data AS A WHERE A.\"Player_id\" = B.\"Player_id\" AND A.\"Nickname\" LIKE 'Morrow%'";
                sqlSelect = "SELECT player_data.*, player_status.* FROM player_data INNER JOIN player_status ON player_data.\"Player_id\" = player_status.\"Player_id\" WHERE player_data.\"Nickname\" LIKE 'Morrow%'";
                sqlDelete = "DELETE FROM player_data WHERE \"Nickname\" LIKE 'Morrow%'";
            }

            for (int counter = 0; counter < numberOfRecords; counter++) {

                try (PreparedStatement ps = con.prepareStatement(sqlInsertPlayerData)) {
                    ps.setInt(1, 30001 + counter);
                    ps.setString(2, "Morrow " + (counter + 1));
                    ps.setString(3, "Morrow");
                    ps.setString(4, "Poro");
                    ps.setString(5, "Morrowlad");

                    startTimer = System.nanoTime();
                    row = ps.executeUpdate();
                    endTimer = System.nanoTime();
                    elapsedTime = endTimer - startTimer;
                    //System.out.println("Inserted rows: " + row);
                    System.out.println("INSERT Statement Player Data " + (counter + 1) + " Time: " + elapsedTime + " Nanoseconds");
                    durschnittINSERT += elapsedTime;
                    tempMinInsert += elapsedTime;
                }

                try (PreparedStatement ps = con.prepareStatement(sqlInsertPlayerStatus)) {
                    ps.setInt(1, 30001 + counter);
                    ps.setInt(2, random.nextInt(4) + 1);
                    ps.setInt(3, random.nextInt(11));
                    ps.setInt(4, random.nextInt(10) + 5);
                    ps.setInt(5, random.nextInt(10) + 5);
                    ps.setInt(6, random.nextInt(10) + 5);
                    ps.setInt(7, random.nextInt(5) + 4);
                    ps.setInt(8, random.nextInt(5) + 4);
                    ps.setInt(9, random.nextInt(5) + 4);
                    ps.setInt(10, random.nextInt(5) + 4);
                    ps.setInt(11, random.nextInt(5) + 4);
                    ps.setInt(12, random.nextInt(3) + 1);
                    ps.setInt(14, random.nextInt(6));
                    ps.setInt(15, random.nextInt(6));
                    ps.setInt(16, random.nextInt(3333));
                    if (isDate) {
                        ps.setInt(13, random.nextInt(2));
                        ps.setDate(17, Date.valueOf(dateTime.getText()));
                    } else {
                        ps.setBoolean(13, random.nextBoolean());
                        ps.setDate(17, Date.valueOf(dateTime.getText()));
                    }
                    startTimer = System.nanoTime();
                    row = ps.executeUpdate();
                    endTimer = System.nanoTime();
                    elapsedTime = endTimer - startTimer;
                    //System.out.println("Inserted rows: " + row);
                    System.out.println("INSERT Statement Player Status " + (counter + 1) + " Time: " + elapsedTime + " Nanoseconds");
                    durschnittINSERT += elapsedTime;
                    tempMinInsert += elapsedTime;
                }
            }

            if (tempMinInsert < minINSERT) {
                minINSERT = tempMinInsert;
            }

            //System.out.println("create UPDATE statement");
            startTimer = System.nanoTime();
            row = stmt.executeUpdate(sqlUpdate);
            endTimer = System.nanoTime();
            elapsedTime = endTimer - startTimer;
            //System.out.println("Updated rows: " + row);
            System.out.println("UPDATE Statement Time: " + elapsedTime + " Nanoseconds");
            durschnittUPDATE += elapsedTime;
            if (elapsedTime < minUPDATE) {
                minUPDATE = elapsedTime;
            }

            //System.out.println("create SELECT statement");
            startTimer = System.nanoTime();
            ResultSet rs = stmt.executeQuery(sqlSelect);
            endTimer = System.nanoTime();
            elapsedTime = endTimer - startTimer;
            /*while (rs.next()) {
                for (int x = 1; x < rs.getMetaData().getColumnCount() + 1; x++){
                    rs.getString(x);
                    System.out.print(rs.getString(x) + " ");
                }
                System.out.println();
            }*/
            System.out.println("Select Statement Time: " + elapsedTime + " Nanoseconds");
            durschnittSELECT += elapsedTime;
            if (elapsedTime < minSELECT) {
                minSELECT = elapsedTime;
            }

            //System.out.println("create DELETE statement");
            startTimer = System.nanoTime();
            row = stmt.executeUpdate(sqlDelete);
            endTimer = System.nanoTime();
            elapsedTime = endTimer - startTimer;
            //System.out.println("Deleted rows: " + row);
            System.out.println("DELETE Statement Time: " + elapsedTime + " Nanoseconds");
            durschnittDELETE += elapsedTime;
            if (elapsedTime < minDELETE) {
                minDELETE = elapsedTime;
            }

            System.out.println("End of Performance Test\n");
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    private SortedList<TableRowData> sortList(ArrayList<TableRowData> list) {
        observableTableRowDataList = FXCollections.observableArrayList(list);

        FilteredList<TableRowData> filteredData = new FilteredList<>(observableTableRowDataList, p -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> filteredData.setPredicate(tableRowData -> {
            // If filter text is empty, display all Players.
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }
            // Compare searchText with name of Player
            String lowerCaseFilter = newValue.toLowerCase();

            return tableRowData.getNickname().toLowerCase().contains(lowerCaseFilter); // Filter matches with name of Player
        }));

        SortedList<TableRowData> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        return sortedData;
    }

    private void createTable() {
        TableColumn<TableRowData, String> player_idCol = createColumn("Player_id");
        TableColumn<TableRowData, String> nicknameCol = createColumn("Nickname");
        TableColumn<TableRowData, String> first_nameCol = createColumn("First_Name");
        TableColumn<TableRowData, String> last_nameCol = createColumn("Last_Name");
        TableColumn<TableRowData, String> placeCol = createColumn("Place");
        TableColumn<TableRowData, String> levelCol = createColumn("Level");
        TableColumn<TableRowData, String> expCol = createColumn("Exp");
        TableColumn<TableRowData, String> hpCol = createColumn("HP");
        TableColumn<TableRowData, String> mpCol = createColumn("MP");
        TableColumn<TableRowData, String> spCol = createColumn("SP");
        TableColumn<TableRowData, String> strCol = createColumn("STR");
        TableColumn<TableRowData, String> gesCol = createColumn("GES");
        TableColumn<TableRowData, String> intCol = createColumn("INT");
        TableColumn<TableRowData, String> ausCol = createColumn("AUS");
        TableColumn<TableRowData, String> konCol = createColumn("KON");
        TableColumn<TableRowData, String> gamelevelCol = createColumn("Gamelevel");
        TableColumn<TableRowData, String> isJumpingCol = createColumn("isJumping");
        TableColumn<TableRowData, String> skillpointsCol = createColumn("Skillpoints");
        TableColumn<TableRowData, String> resourcepointsCol = createColumn("Resourcepoints");
        TableColumn<TableRowData, String> highscoreCol = createColumn("Highscore");
        TableColumn<TableRowData, String> lastloginCol = createColumn("Lastlogin");

        //Einzelne Spalten editierbar machen <===============================================
        table.setEditable(false); //true
        /*player_idCol.setEditable(false);
        levelCol.setEditable(false);
        expCol.setEditable(false);
        hpCol.setEditable(false);
        mpCol.setEditable(false);
        spCol.setEditable(false);
        strCol.setEditable(false);
        gesCol.setEditable(false);
        intCol.setEditable(false);
        ausCol.setEditable(false);
        konCol.setEditable(false);
        gamelevelCol.setEditable(false);
        isJumpingCol.setEditable(false);
        skillpointsCol.setEditable(false);
        resourcepointsCol.setEditable(false);
        highscoreCol.setEditable(false);
        lastloginCol.setEditable(false);*/

        table.getColumns().addAll(player_idCol, nicknameCol, first_nameCol, last_nameCol, placeCol, levelCol, expCol, hpCol, mpCol, spCol, strCol, gesCol, intCol, ausCol, konCol, gamelevelCol, isJumpingCol, skillpointsCol, resourcepointsCol, highscoreCol, lastloginCol);
        for (TableColumn current: table.getColumns()) {
            current.setMinWidth(50);
            current.setPrefWidth(new Text(current.getText()).getLayoutBounds().getWidth() + 25);

        }
        player_idCol.prefWidthProperty().bind(table.widthProperty().divide(8));
        table.getSortOrder().add(player_idCol);

        //Das Editierte speichern <===============================================
        //konCol.setOnEditCommit(event -> saveTableContent(event));
    }

    private void setEventsOnSpinners() {
        editorFontSizeSpinner.setOnMouseClicked(event -> setTextAreaFontSize(editorFontSizeSpinner.getValue()));

        //Check if the changed input is an integer
        editorFontSizeSpinner.getEditor().setOnKeyReleased(event -> {
            String text = editorFontSizeSpinner.getEditor().getText();
            if (isStringOnlyNumeric(text)){
                editorFontSizeSpinner.getValueFactory().setValue(Integer.valueOf(text));
            }else {
                editorFontSizeSpinner.getEditor().textProperty().set(String.valueOf(editorFontSizeSpinner.getValueFactory().getValue()));
            }
        });
    }

    private GridPane createGrid() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(0, 10, 10, 10));
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHgrow(Priority.ALWAYS);
        grid.getColumnConstraints().add(col1);
        grid.setVgap(windowHeight/100);
        grid.setHgap(windowWidth/100);
        return grid;
    }

    private void createEditButton(MenuWindow view) {
        editButton.setMinWidth(40);
        editButton.setMaxWidth(60);
        editButton.setPrefSize(windowWidth*0.15, windowHeight*0.04);
        GridPane.setConstraints(editButton, 26, 0);
    }

    private void createButtons(GridPane grid, HBox editorFontSizeHBox, MenuWindow view) {
        searchField.setMaxWidth(windowWidth*0.15);
        searchField.setPromptText("Suche");
        GridPane.setConstraints(searchField, 0, 1);

        Button settingsButton = new Button("Settings");
        settingsButton.setPrefSize(windowWidth*0.15, windowHeight*0.04);
        GridPane.setConstraints(settingsButton, 0, 10);
        settingsButton.setOnMouseClicked(event -> openSettings(editorFontSizeHBox));

        Button quitButton = new Button("QUIT");
        quitButton.setPrefSize(windowWidth*0.15, windowHeight*0.04);
        GridPane.setConstraints(quitButton, 0, 11);
        quitButton.setOnMouseClicked(event -> Platform.exit());

        Button presentationModeButton = new Button("Presentation Mode");
        presentationModeButton.setPrefSize(windowWidth*0.15, windowHeight*0.04);
        GridPane.setConstraints(presentationModeButton, 0, 4);
        presentationModeButton.setOnMouseClicked(event -> enableDisablePresentationMode(quitButton, settingsButton, presentationModeButton, view));

        grid.getChildren().addAll(searchField, presentationModeButton, quitButton, settingsButton);
    }

    private void createCsvBorderPane() {
        csvBorderPane.setAlignment(textCSV, Pos.CENTER_LEFT);
        csvBorderPane.setMaxWidth(windowWidth*79);
        csvBorderPane.setMaxHeight(50);
        csvBorderPane.setMinWidth(windowWidth*0.79);
        csvBorderPane.setMinHeight(50);
        csvBorderPane.setTranslateX(windowWidth*0.2);
        csvBorderPane.setTranslateY(windowHeight);
        csvBorderPane.setStyle("-fx-padding: 10; -fx-background-color: white");
    }

    private void openSettings(HBox editorFontSizeHBox) {
        System.out.println("Open Settings");
        Dialog<ArrayList<String>> dialog = new Dialog<>();
        dialog.setTitle("Database Settings");
        dialog.setHeaderText("Settings");
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK);

        FlowPane editorFontSizePane = createNewFlowPane();
        editorFontSizePane.getChildren().addAll(editorFontSizeHBox);

        dialogPane.setContent(new VBox(8, editorFontSizePane));
        Platform.runLater(dialog.getDialogPane().lookupButton(ButtonType.OK)::requestFocus);

        dialog.getDialogPane().setOnKeyReleased((KeyEvent event) -> {
            if(event.getCode() == KeyCode.ESCAPE) {
                System.out.println("ESCAPE");
                dialog.close();
            }
        });
        dialog.showAndWait();
    }

    private void enableDisablePresentationMode(Button quitButton, Button settingsButton, Button presentationModeButton, MenuWindow view) {
        if (quitButton.isVisible()) {
            System.out.println("PRESENTATION MODE");
            presentationModeButton.setText("Normal Mode");
            view.enableFullscreen();
            isFullscreen = true;

            view.getPrimaryScene().setOnKeyPressed((KeyEvent event) -> {
                if(event.getCode() == KeyCode.ESCAPE) {
                    System.out.println("ESCAPE");
                    disableFullScreen(presentationModeButton, view, quitButton, settingsButton);
                }
            });

            quitButton.setVisible(false);
            GridPane.setConstraints(presentationModeButton, 0, 4);
            GridPane.setConstraints(settingsButton, 0, 8);
            setTableSize();
            csvBorderPane.setTranslateX(csvBorderPane.getTranslateX() - windowWidth*0.03);
        }else {
            disableFullScreen(presentationModeButton, view, quitButton, settingsButton);
        }
    }

    private void disableFullScreen(Button presentationModeButton, MenuWindow view, Button quitButton,
                                   Button settingsButton){
        System.out.println("NORMAL MODE");
        isFullscreen = false;
        presentationModeButton.setText("Presentation Mode");
        view.disableFullscreen();
        quitButton.setVisible(true);
        GridPane.setConstraints(presentationModeButton, 0, 4);
        GridPane.setConstraints(settingsButton, 0, 9);
        setTableSize();
        csvBorderPane.setTranslateX(csvBorderPane.getTranslateX() + windowWidth*0.03);
        view.getPrimaryScene().setOnKeyPressed(e -> {});
    }

    private void setTableSize() {
        if (isFullscreen) { setAnchor(table, windowWidth * 0.17, windowWidth * 0.005, windowHeight * 0.01, windowHeight * 0.01); }
        else { setAnchor(table, windowWidth*0.2, windowWidth*0.005, windowHeight*0.01, windowHeight*0.01); }
    }

    private static boolean isStringOnlyNumeric(String str) {
        return ((str != null)
                && (!str.equals(""))
                && (str.matches("^[0-9]*$")));
    }

    private FlowPane createNewFlowPane() {
        FlowPane flowPane = new FlowPane();
        flowPane.setMaxWidth(windowWidth*0.15);
        flowPane.setMinWidth(windowWidth*0.15);
        flowPane.setHgap(10);
        flowPane.setVgap(10);
        flowPane.setPadding(new Insets(10,10,10,10));
        flowPane.setAlignment(Pos.CENTER_LEFT);
        flowPane.setStyle("-fx-border-color: black");
        return flowPane;
    }

    private HBox createHBox(String title, Spinner<Integer> spinner){
        Title title1 = new Title(title);
        title1.setAlignment(Pos.CENTER_LEFT);
        HBox hBox = new HBox();
        hBox.getChildren().addAll(title1, spinner);
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER_LEFT);
        return hBox;
    }

    private Spinner<Integer>  createSpinner(int initialValue){
        Spinner<Integer> spinner = new Spinner<>();
        spinner.setMinWidth(55);
        spinner.setMaxWidth(60);
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, initialValue);
        spinner.setValueFactory(valueFactory);
        spinner.setEditable(true);
        return spinner;
    }

    public void setTextAreaFontSize(int fontSize) {
        table.setStyle("-fx-font-size: " + fontSize);
        if (fontSize <= 23) {
            textCSV.setStyle("-fx-font-size: " + fontSize);
        }
    }

    private void setAnchor(Node parent, double left, double right, double top, double bottom){
        AnchorPane.setLeftAnchor(parent, left);
        AnchorPane.setRightAnchor(parent, right);
        AnchorPane.setTopAnchor(parent, top);
        AnchorPane.setBottomAnchor(parent, bottom);
    }

    private TableColumn<TableRowData, String> createColumn(String title){
        TableColumn<TableRowData, String> valueCol = new TableColumn<>(title);
        valueCol.setCellValueFactory(new PropertyValueFactory<>(title));
        valueCol.setCellFactory(TextFieldTableCell.forTableColumn());
        valueCol.setMinWidth(100);

        valueCol.setOnEditCommit((TableColumn.CellEditEvent<TableRowData, String> event) -> { TablePosition<TableRowData, String> pos = event.getTablePosition();
            String eventNewValue = event.getNewValue();
            int row = pos.getRow();
            TableRowData TableRowData = event.getTableView().getItems().get(row);
            TableRowData.setKey(eventNewValue);
        });
        return valueCol;
    }

    private TableRowData createTableRow(Object player_id, String nickname, String first_name, String last_name, String place, String level, String exp, String hp, String mp, String sp, String str, String ges, String inT, String aus, String kon, String gamelevel, String isJumping, String skillpoints, String resourcepoints, String highscore, String lastlogin) {
        return new TableRowData(player_id.toString(), nickname, first_name, last_name, place, level, exp, hp, mp, sp, str, ges, inT, aus, kon, gamelevel, isJumping, skillpoints, resourcepoints, highscore, lastlogin);
    }
}