/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.groupc.saleberry.View;

import com.groupc.saleberry.Dao.AuthDao;
import com.groupc.saleberry.Database.MySqlConnection;
import com.groupc.saleberry.Model.Delete;
import com.groupc.saleberry.Model.IconImage;
import com.groupc.saleberry.Model.Inventory;
import com.groupc.saleberry.Model.SalesModel;
import com.groupc.saleberry.Model.Search;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
//import org.apache.commons.dbutils.DbUtils;
/**
 *
 * @author sabin
 */
public class DashBoard extends javax.swing.JFrame {
    Color defaultColor, clickedColor, green,same,white;
    DefaultTableModel model;
    String filename= null;
    byte [] person_image = null;
    private IconImage image;

    public DashBoard() {
        initComponents();
//        displayImageFromDatabase();
        fetchDataFromDatabase();
        inventoryTableValue();
        salesTableValue();
        createDataset();
        lineDataset();
        showUser();
        inventoryCountProduct();
        inventoryCountCategory();
        inventoryOutOfStock();
        userCount();

        defaultColor = new Color (51,51,51);
        clickedColor = new Color(102,255,102);
        green = new Color(102,255,102);
        same = new Color(204,204,204);
        white = new Color(255,255,255);
    }

public DefaultPieDataset createDataset() {
            DefaultPieDataset dataset = new DefaultPieDataset();
            MySqlConnection mysqlconnection = new MySqlConnection();
            Connection conn = mysqlconnection.openConnection();
            if (conn != null) {
            String query = "SELECT product_name, sum(total_price) from sellProducts group by product_name order by sum(total_price) desc limit 2";

            try (PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet resultSet = pstmt.executeQuery()) {
            Map<String, Double> productSalesMap = new HashMap<>();

            while (resultSet.next()) {
        String productName = resultSet.getString("product_name");
        double totalSales = resultSet.getDouble("sum(total_price)");
        dataset.setValue(productName, totalSales);
        productSalesMap.put(productName, totalSales);
    }

    JFreeChart piechart = ChartFactory.createPieChart("Top sales", dataset, false, true, false);
    PiePlot piePlot = (PiePlot) piechart.getPlot();

    // Set section paints outside the loop using the stored values
    int i = 0;
    for (Map.Entry<String, Double> entry : productSalesMap.entrySet()) {
        String productName = entry.getKey();
        double totalSales = entry.getValue();
        piePlot.setSectionPaint(productName, i == 0 ? new Color(102, 255, 102) : new Color(255, 102, 153));
        i++;
    }

    piePlot.setBackgroundPaint(Color.white);
    ChartPanel barChartPanel = new ChartPanel(piechart);
    panelBarChart.add(barChartPanel, BorderLayout.CENTER);
} catch (SQLException e) {
    e.printStackTrace();
}
    }

    return dataset;
}
public DefaultCategoryDataset lineDataset() {
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    MySqlConnection mysqlconnection = new MySqlConnection();
    Connection conn = mysqlconnection.openConnection();

    if (conn != null) {
        String query = "SELECT product_name, SUM(total_price) AS total_sales, AVG(price) AS average_price\n" +
                     "FROM sellProducts\n" +
                     "GROUP BY product_name\n" +
                     "ORDER BY total_sales DESC\n" +
                     "LIMIT 2;";

        try (PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet resultSet = pstmt.executeQuery()) {

            Map<String, Double> productSalesMap = new HashMap<>();

            while (resultSet.next()) {
               String productName = resultSet.getString("product_name");
               double totalSales = resultSet.getDouble("total_sales");
               double averagePrice = resultSet.getDouble("average_price");

            dataset.addValue(averagePrice, "label", productName);
            productSalesMap.put(productName, totalSales);
            }

            // Create a line chart
            JFreeChart lineChart = ChartFactory.createLineChart(
                    "Top Sales",    // Chart title
                    "Product",      // X-axis label
                    "Amount",       // Y-axis label
                    dataset,        // Dataset
                    PlotOrientation.VERTICAL,
                    true,           // Include legend
                    true,
                    false
            );

            // Customize the chart as needed
            CategoryPlot plot = (CategoryPlot) lineChart.getPlot();
            plot.setBackgroundPaint(Color.white);

            // Set section paints outside the loop using the stored values
            int i = 0;
            for (Map.Entry<String, Double> entry : productSalesMap.entrySet()) {
                String productName = entry.getKey();
                double totalSales = entry.getValue();
                plot.getRenderer().setSeriesPaint(i, i == 0 ? new Color(102, 255, 102) : new Color(255, 102, 153));
                i++;
            }

            // Add the chart to a panel or display it as needed
            ChartPanel lineChartPanel = new ChartPanel(lineChart);
            lineVariable.add(lineChartPanel, BorderLayout.CENTER);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    return dataset;
}
    
        private void fetchDataFromDatabase() {
            MySqlConnection mysqlconnection = new MySqlConnection();
            Connection conn = mysqlconnection.openConnection();
            if (conn != null) {
            String query = "SELECT user_id, password, first_name,last_name,contact_no FROM user";

            try (PreparedStatement pstmt = conn.prepareStatement(query);
                 ResultSet resultSet = pstmt.executeQuery()) {
                while (resultSet.next()) {
                    String userId = resultSet.getString("user_id");
                    String password = resultSet.getString("password");
                    String first = resultSet.getString("first_name");
                    String last = resultSet.getString("last_name");
                    String contact = resultSet.getString("contact_no");
                    Object [] obj ={userId,first,last,contact,password};
                    model = (DefaultTableModel) tableUser.getModel();
                    model.addRow(obj);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Provided database connection is null. Check your connection.");
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        pictureBox1 = new com.groupc.saleberry.custombutton.PictureBox();
        pictureBox2 = new com.groupc.saleberry.custombutton.PictureBox();
        jLabel3 = new javax.swing.JLabel();
        pictureBox4 = new com.groupc.saleberry.custombutton.PictureBox();
        pictureBox5 = new com.groupc.saleberry.custombutton.PictureBox();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        pictureBox3 = new com.groupc.saleberry.custombutton.PictureBox();
        jPanel6 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        label1 = new javax.swing.JPanel();
        label1Tab = new javax.swing.JLabel();
        pictureBox6 = new com.groupc.saleberry.custombutton.PictureBox();
        label3 = new javax.swing.JPanel();
        label3Tab = new javax.swing.JLabel();
        pictureBox13 = new com.groupc.saleberry.custombutton.PictureBox();
        label2 = new javax.swing.JPanel();
        label2Tab = new javax.swing.JLabel();
        pictureBox9 = new com.groupc.saleberry.custombutton.PictureBox();
        jLabel11 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        pictureBox8 = new com.groupc.saleberry.custombutton.PictureBox();
        jPanel14 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        pictureBox15 = new com.groupc.saleberry.custombutton.PictureBox();
        label4 = new javax.swing.JPanel();
        label4Tab = new javax.swing.JLabel();
        pictureBox7 = new com.groupc.saleberry.custombutton.PictureBox();
        label5 = new javax.swing.JPanel();
        label5Tab = new javax.swing.JLabel();
        pictureBox14 = new com.groupc.saleberry.custombutton.PictureBox();
        label6 = new javax.swing.JPanel();
        label6Tab = new javax.swing.JLabel();
        pictureBox16 = new com.groupc.saleberry.custombutton.PictureBox();
        jPanel16 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        pictureBox17 = new com.groupc.saleberry.custombutton.PictureBox();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        tab1 = new javax.swing.JPanel();
        tab11 = new javax.swing.JPanel();
        lbl_image = new javax.swing.JLabel();
        imageUser = new javax.swing.JButton();
        savebtn = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        pictureBox10 = new com.groupc.saleberry.custombutton.PictureBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        table1 = new rojeru_san.complementos.RSTableMetro();
        tab2 = new javax.swing.JPanel();
        tab22 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        salesTableValue = new rojeru_san.complementos.RSTableMetro();
        panelro = new com.groupc.saleberry.custombutton.PanelRound();
        jLabel43 = new javax.swing.JLabel();
        addPrice1 = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        addCategoryId1 = new javax.swing.JTextField();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        addProductId2 = new javax.swing.JTextField();
        addProductName1 = new javax.swing.JTextField();
        jButton6 = new javax.swing.JButton();
        addCategoryName1 = new javax.swing.JTextField();
        addPrice2 = new javax.swing.JTextField();
        addPrice3 = new javax.swing.JTextField();
        jLabel48 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        tab3 = new javax.swing.JPanel();
        tab33 = new javax.swing.JPanel();
        m3 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        lbl_fast = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        lbl_product = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        lbl_category = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        lbl_stock = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        inventoryTableData = new rojeru_san.complementos.RSTableMetro();
        jLabel25 = new javax.swing.JLabel();
        tab4 = new javax.swing.JPanel();
        tab44 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        panelRound2 = new com.groupc.saleberry.custombutton.PanelRound();
        jLabel29 = new javax.swing.JLabel();
        addPrice = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        addCategoryId = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        addProductId1 = new javax.swing.JTextField();
        addProductName = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        addCategoryName = new javax.swing.JTextField();
        panelRound3 = new com.groupc.saleberry.custombutton.PanelRound();
        jLabel34 = new javax.swing.JLabel();
        updatePrice = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        updateCId = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        updateCName = new javax.swing.JTextField();
        updatePId = new javax.swing.JTextField();
        updatePName = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        panelRound4 = new com.groupc.saleberry.custombutton.PanelRound();
        jLabel41 = new javax.swing.JLabel();
        deletePId = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        deleteCId = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        tab5 = new javax.swing.JPanel();
        tab55 = new javax.swing.JPanel();
        panelBarChart = new javax.swing.JPanel();
        lineVariable = new javax.swing.JPanel();
        tab6 = new javax.swing.JPanel();
        tab66 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        totalUser = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tableUser = new rojeru_san.complementos.RSTableMetro();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(204, 0, 51));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 22)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("SalesBerry");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 30, 110, 30));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText(" Hi Admin");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(1420, 40, -1, -1));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("|");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1310, 40, 10, -1));

        jLabel5.setIcon(new javax.swing.ImageIcon("C:\\Users\\sabin\\Documents\\NetBeansProjects\\Saleberry\\src\\main\\java\\com\\groupc\\saleberry\\image\\icons8_menu_48px_1.png")); // NOI18N
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 50, 30));

        pictureBox1.setImage(new javax.swing.ImageIcon("C:\\Users\\sabin\\Documents\\NetBeansProjects\\Saleberry\\src\\main\\java\\com\\groupc\\saleberry\\image\\search_26px.png")); // NOI18N
        jPanel1.add(pictureBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1190, 40, 30, 30));

        pictureBox2.setImage(new javax.swing.ImageIcon("C:\\Users\\sabin\\Documents\\NetBeansProjects\\Saleberry\\src\\main\\java\\com\\groupc\\saleberry\\image\\group_message_26px.png")); // NOI18N
        jPanel1.add(pictureBox2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1260, 40, 30, 30));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setText("|");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1240, 40, 10, -1));

        pictureBox4.setImage(new javax.swing.ImageIcon("C:\\Users\\sabin\\Documents\\NetBeansProjects\\Saleberry\\src\\main\\java\\com\\groupc\\saleberry\\image\\male_user_50px.png")); // NOI18N
        jPanel1.add(pictureBox4, new org.netbeans.lib.awtextra.AbsoluteConstraints(1380, 30, 40, 40));

        pictureBox5.setImage(new javax.swing.ImageIcon("C:\\Users\\sabin\\Documents\\NetBeansProjects\\Saleberry\\src\\main\\java\\com\\groupc\\saleberry\\image\\bell_26px.png")); // NOI18N
        jPanel1.add(pictureBox5, new org.netbeans.lib.awtextra.AbsoluteConstraints(1330, 40, 30, 30));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1640, 80));

        jPanel2.setBackground(new java.awt.Color(51, 51, 51));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(0, 0, 0));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(204, 0, 51));
        jLabel6.setText("   E-Billing");
        jPanel3.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 20, 150, 40));

        pictureBox3.setImage(new javax.swing.ImageIcon("C:\\Users\\sabin\\Documents\\NetBeansProjects\\Saleberry\\src\\main\\java\\com\\groupc\\saleberry\\image\\shopping_cart_24px.png")); // NOI18N
        jPanel3.add(pictureBox3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 40, 30));

        jPanel6.setBackground(new java.awt.Color(0, 0, 0));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(204, 0, 51));
        jLabel9.setText("E-Billing");
        jPanel6.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 20, 150, 40));

        jPanel3.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 300, 70));

        jPanel2.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 300, 70));

        label1.setBackground(new java.awt.Color(51, 51, 51));
        label1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        label1Tab.setBackground(new java.awt.Color(51, 51, 51));
        label1Tab.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        label1Tab.setForeground(new java.awt.Color(204, 204, 204));
        label1Tab.setText("   Admin Dashboard");
        label1Tab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                label1TabMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                label1TabMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                label1TabMouseReleased(evt);
            }
        });
        label1.add(label1Tab, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, 190, 40));

        pictureBox6.setImage(new javax.swing.ImageIcon("C:\\Users\\sabin\\Documents\\NetBeansProjects\\Saleberry\\src\\main\\java\\com\\groupc\\saleberry\\image\\home_24px.png")); // NOI18N
        label1.add(pictureBox6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 40, 40));

        jPanel2.add(label1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 110, 300, 70));

        label3.setBackground(new java.awt.Color(51, 51, 51));
        label3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        label3Tab.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        label3Tab.setForeground(new java.awt.Color(204, 204, 204));
        label3Tab.setText("   Inventory");
        label3Tab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                label3TabMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                label3TabMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                label3TabMouseReleased(evt);
            }
        });
        label3.add(label3Tab, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, 190, 40));

        pictureBox13.setImage(new javax.swing.ImageIcon("C:\\Users\\sabin\\Documents\\NetBeansProjects\\Saleberry\\src\\main\\java\\com\\groupc\\saleberry\\image\\edit_property_24px.png")); // NOI18N
        label3.add(pictureBox13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 40, 40));

        jPanel2.add(label3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 290, 300, 70));

        label2.setBackground(new java.awt.Color(51, 51, 51));
        label2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        label2Tab.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        label2Tab.setForeground(new java.awt.Color(204, 204, 204));
        label2Tab.setText("   Sell Details");
        label2Tab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                label2TabMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                label2TabMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                label2TabMouseReleased(evt);
            }
        });
        label2.add(label2Tab, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, 190, 40));

        pictureBox9.setImage(new javax.swing.ImageIcon("C:\\Users\\sabin\\Documents\\NetBeansProjects\\Saleberry\\src\\main\\java\\com\\groupc\\saleberry\\image\\template_26px.png")); // NOI18N
        label2.add(pictureBox9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 40, 40));

        jPanel2.add(label2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 210, 300, 70));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(204, 204, 204));
        jLabel11.setText("Features");
        jPanel2.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, 130, 40));

        jPanel5.setBackground(new java.awt.Color(51, 51, 51));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(204, 204, 204));
        jLabel12.setText("   Log Out");
        jLabel12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel12MouseClicked(evt);
            }
        });
        jPanel5.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, 190, 40));

        pictureBox8.setImage(new javax.swing.ImageIcon("C:\\Users\\sabin\\Documents\\NetBeansProjects\\Saleberry\\src\\main\\java\\com\\groupc\\saleberry\\image\\logout.png")); // NOI18N
        jPanel5.add(pictureBox8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 30, 40));

        jPanel14.setBackground(new java.awt.Color(51, 51, 51));
        jPanel14.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(204, 204, 204));
        jLabel18.setText("   Tables & Data");
        jPanel14.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, 190, 40));

        pictureBox15.setImage(new javax.swing.ImageIcon("C:\\Users\\sabin\\Documents\\NetBeansProjects\\Saleberry\\src\\main\\java\\com\\groupc\\saleberry\\image\\sort_window_24px.png")); // NOI18N
        jPanel14.add(pictureBox15, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 40, 40));

        jPanel5.add(jPanel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 720, 300, 70));

        jPanel2.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 620, 300, 70));

        label4.setBackground(new java.awt.Color(51, 51, 51));
        label4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        label4Tab.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        label4Tab.setForeground(new java.awt.Color(204, 204, 204));
        label4Tab.setText("   Form");
        label4Tab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                label4TabMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                label4TabMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                label4TabMouseReleased(evt);
            }
        });
        label4.add(label4Tab, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, 190, 40));

        pictureBox7.setImage(new javax.swing.ImageIcon("C:\\Users\\sabin\\Documents\\NetBeansProjects\\Saleberry\\src\\main\\java\\com\\groupc\\saleberry\\image\\google_forms_24px.png")); // NOI18N
        label4.add(pictureBox7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 40, 40));

        jPanel2.add(label4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 370, 300, 70));

        label5.setBackground(new java.awt.Color(51, 51, 51));
        label5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        label5Tab.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        label5Tab.setForeground(new java.awt.Color(204, 204, 204));
        label5Tab.setText("   Charts");
        label5Tab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                label5TabMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                label5TabMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                label5TabMouseReleased(evt);
            }
        });
        label5.add(label5Tab, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, 190, 40));

        pictureBox14.setImage(new javax.swing.ImageIcon("C:\\Users\\sabin\\Documents\\NetBeansProjects\\Saleberry\\src\\main\\java\\com\\groupc\\saleberry\\image\\account_24px.png")); // NOI18N
        label5.add(pictureBox14, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 40, 40));

        jPanel2.add(label5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 460, 300, 70));

        label6.setBackground(new java.awt.Color(51, 51, 51));
        label6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        label6Tab.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        label6Tab.setForeground(new java.awt.Color(204, 204, 204));
        label6Tab.setText("   User Data");
        label6Tab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                label6TabMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                label6TabMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                label6TabMouseReleased(evt);
            }
        });
        label6.add(label6Tab, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, 190, 40));

        pictureBox16.setImage(new javax.swing.ImageIcon("C:\\Users\\sabin\\Documents\\NetBeansProjects\\Saleberry\\src\\main\\java\\com\\groupc\\saleberry\\image\\grid_24px.png")); // NOI18N
        label6.add(pictureBox16, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 40, 40));

        jPanel16.setBackground(new java.awt.Color(51, 51, 51));
        jPanel16.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(204, 204, 204));
        jLabel20.setText("   Tables & Data");
        jPanel16.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, 190, 40));

        pictureBox17.setImage(new javax.swing.ImageIcon("C:\\Users\\sabin\\Documents\\NetBeansProjects\\Saleberry\\src\\main\\java\\com\\groupc\\saleberry\\image\\sort_window_24px.png")); // NOI18N
        jPanel16.add(pictureBox17, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 40, 40));

        label6.add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 720, 300, 70));

        jPanel2.add(label6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 540, 300, 70));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 80, 300, 770));

        tab1.setBackground(new java.awt.Color(255, 255, 255));
        tab1.setMinimumSize(new java.awt.Dimension(1340, 760));
        tab1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tab11.setBackground(new java.awt.Color(102, 255, 102));
        tab11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        tab11.add(lbl_image, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 410, 390, 270));

        imageUser.setBackground(new java.awt.Color(0, 255, 255));
        imageUser.setText("SELECT");
        imageUser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                imageUserMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                imageUserMouseEntered(evt);
            }
        });
        imageUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imageUserActionPerformed(evt);
            }
        });
        tab11.add(imageUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 430, 120, 40));

        savebtn.setBackground(new java.awt.Color(102, 102, 255));
        savebtn.setText("SAVE");
        savebtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                savebtnMouseClicked(evt);
            }
        });
        savebtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                savebtnActionPerformed(evt);
            }
        });
        tab11.add(savebtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 520, 120, 40));

        jButton7.setBackground(new java.awt.Color(255, 153, 51));
        jButton7.setText("DISPLAY");
        jButton7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton7MouseClicked(evt);
            }
        });
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        tab11.add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 610, 120, 40));

        pictureBox10.setImage(new javax.swing.ImageIcon("C:\\Users\\sabin\\Documents\\NetBeansProjects\\Saleberry\\src\\main\\java\\com\\groupc\\saleberry\\image\\admin.jpg")); // NOI18N
        pictureBox10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        tab11.add(pictureBox10, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 10, 590, 690));

        table1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "S.No", "Image"
            }
        ));
        table1.setColorBackgoundHead(new java.awt.Color(255, 102, 102));
        table1.setColorBordeFilas(new java.awt.Color(255, 153, 153));
        table1.setColorBordeHead(new java.awt.Color(255, 204, 204));
        table1.setColorFilasForeground1(new java.awt.Color(255, 153, 153));
        table1.setColorFilasForeground2(new java.awt.Color(255, 153, 153));
        table1.setColorSelBackgound(new java.awt.Color(255, 153, 153));
        table1.setRowHeight(34);
        jScrollPane2.setViewportView(table1);

        tab11.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, 560, 370));

        tab1.add(tab11, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1340, 760));

        jTabbedPane1.addTab("tab1", tab1);

        tab2.setBackground(new java.awt.Color(255, 102, 102));
        tab2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tab22.setBackground(new java.awt.Color(255, 255, 153));
        tab22.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        salesTableValue.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Caterogy ID", "Category Name", "Product ID", "Product Name", "Price", "Quantity", "Total Price"
            }
        ));
        salesTableValue.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        salesTableValue.setFuenteFilas(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        salesTableValue.setFuenteFilasSelect(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        salesTableValue.setFuenteHead(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        salesTableValue.setRowHeight(25);
        jScrollPane4.setViewportView(salesTableValue);

        tab22.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 70, 730, 580));

        panelro.setBackground(new java.awt.Color(255, 255, 255));
        panelro.setRoundBottomLeft(20);
        panelro.setRoundBottomRight(20);
        panelro.setRoundTopLeft(20);
        panelro.setRoundTopRight(20);
        panelro.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel43.setText("Total");
        panelro.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 390, 100, 40));

        addPrice1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                addPrice1MouseEntered(evt);
            }
        });
        addPrice1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addPrice1ActionPerformed(evt);
            }
        });
        panelro.add(addPrice1, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 430, 100, 40));

        jLabel44.setText("Catergory ID");
        panelro.add(jLabel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 80, 40));

        addCategoryId1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                addCategoryId1MouseEntered(evt);
            }
        });
        addCategoryId1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addCategoryId1ActionPerformed(evt);
            }
        });
        addCategoryId1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                addCategoryId1KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                addCategoryId1KeyTyped(evt);
            }
        });
        panelro.add(addCategoryId1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, 340, 40));

        jLabel45.setText("Catergory Name");
        panelro.add(jLabel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, 110, 40));

        jLabel46.setText("Product ID");
        panelro.add(jLabel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 210, 110, 40));

        jLabel47.setText("Product Name");
        panelro.add(jLabel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 300, 110, 40));

        addProductId2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                addProductId2MouseEntered(evt);
            }
        });
        addProductId2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addProductId2ActionPerformed(evt);
            }
        });
        panelro.add(addProductId2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 250, 340, 40));

        addProductName1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addProductName1ActionPerformed(evt);
            }
        });
        panelro.add(addProductName1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 340, 340, 40));

        jButton6.setBackground(new java.awt.Color(255, 102, 102));
        jButton6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jButton6.setText("Confirm Sell");
        jButton6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton6MouseClicked(evt);
            }
        });
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        panelro.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 500, -1, 40));

        addCategoryName1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addCategoryName1ActionPerformed(evt);
            }
        });
        panelro.add(addCategoryName1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 160, 340, 40));

        addPrice2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addPrice2ActionPerformed(evt);
            }
        });
        panelro.add(addPrice2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 430, 100, 40));

        addPrice3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addPrice3ActionPerformed(evt);
            }
        });
        panelro.add(addPrice3, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 430, 100, 40));

        jLabel48.setText("Quantity");
        panelro.add(jLabel48, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 390, 100, 40));

        jLabel50.setText("Price");
        panelro.add(jLabel50, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 390, 100, 40));

        tab22.add(panelro, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, 410, 590));

        tab2.add(tab22, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1340, 760));

        jTabbedPane1.addTab("tab2", tab2);

        tab3.setBackground(new java.awt.Color(255, 255, 255));
        tab3.setMinimumSize(new java.awt.Dimension(1340, 760));
        tab3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tab33.setBackground(new java.awt.Color(153, 255, 153));
        tab33.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        m3.setBackground(new java.awt.Color(255, 255, 255));
        m3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        m3.setText("       Inventory Management");
        tab33.add(m3, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 40, 260, 50));

        jPanel4.setBackground(new java.awt.Color(153, 255, 204));
        jPanel4.setBorder(javax.swing.BorderFactory.createMatteBorder(20, 0, 0, 0, new java.awt.Color(0, 128, 128)));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_fast.setFont(new java.awt.Font("Segoe UI", 0, 30)); // NOI18N
        lbl_fast.setText("  3");
        jPanel4.add(lbl_fast, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 30, 50, 40));

        tab33.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 150, 160, 120));

        jPanel7.setBackground(new java.awt.Color(153, 255, 204));
        jPanel7.setBorder(javax.swing.BorderFactory.createMatteBorder(20, 0, 0, 0, new java.awt.Color(0, 128, 128)));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setText("No Of Products");
        jPanel7.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 160, 110, 30));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setText("No Of Products");
        jPanel7.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 160, 110, 30));

        lbl_product.setFont(new java.awt.Font("Segoe UI", 0, 30)); // NOI18N
        lbl_product.setText("1500");
        jPanel7.add(lbl_product, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, 80, 40));

        tab33.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 150, 160, 120));

        jPanel8.setBackground(new java.awt.Color(153, 255, 204));
        jPanel8.setBorder(javax.swing.BorderFactory.createMatteBorder(20, 0, 0, 0, new java.awt.Color(0, 128, 128)));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_category.setFont(new java.awt.Font("Segoe UI", 0, 30)); // NOI18N
        lbl_category.setText(" 15");
        jPanel8.add(lbl_category, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 40, 50, 40));

        tab33.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 150, 160, 120));

        jPanel9.setBackground(new java.awt.Color(153, 255, 204));
        jPanel9.setBorder(javax.swing.BorderFactory.createMatteBorder(20, 0, 0, 0, new java.awt.Color(0, 128, 128)));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_stock.setFont(new java.awt.Font("Segoe UI", 0, 30)); // NOI18N
        lbl_stock.setText("  5");
        jPanel9.add(lbl_stock, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 40, 50, 40));

        tab33.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 150, 160, 120));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(102, 102, 102));
        jLabel13.setText(" Fast Moving Products");
        tab33.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 120, 160, 30));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(102, 102, 102));
        jLabel14.setText("      No Of Products");
        tab33.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 120, 160, 30));

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(102, 102, 102));
        jLabel21.setText("     No Of Categories");
        tab33.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 120, 150, 30));

        jLabel23.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(102, 102, 102));
        jLabel23.setText("         Out Of Stock");
        tab33.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 120, 160, 30));

        inventoryTableData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Category_ID", "Product_ID", "Product_Name", "Price", "Category_Name"
            }
        ));
        inventoryTableData.setColorBordeFilas(new java.awt.Color(102, 102, 102));
        inventoryTableData.setColorFilasBackgound2(new java.awt.Color(255, 255, 255));
        inventoryTableData.setFuenteFilas(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        inventoryTableData.setFuenteFilasSelect(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        inventoryTableData.setFuenteHead(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        inventoryTableData.setRowHeight(25);
        jScrollPane1.setViewportView(inventoryTableData);

        tab33.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 330, 1060, 350));

        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel25.setText("                   Inventory Table");
        tab33.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 286, 330, 40));

        tab3.add(tab33, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1340, 760));

        jTabbedPane1.addTab("tab3", tab3);

        tab4.setMinimumSize(new java.awt.Dimension(1340, 760));
        tab4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tab44.setBackground(new java.awt.Color(153, 255, 102));
        tab44.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel28.setBackground(new java.awt.Color(255, 255, 255));
        jLabel28.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 102, 0));
        jLabel28.setText("UPDATE PRODUCTS");
        tab44.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 50, 230, 40));

        panelRound2.setBackground(new java.awt.Color(255, 255, 255));
        panelRound2.setRoundBottomLeft(20);
        panelRound2.setRoundBottomRight(20);
        panelRound2.setRoundTopLeft(20);
        panelRound2.setRoundTopRight(20);
        panelRound2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel29.setText("Price");
        panelRound2.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 390, 110, 40));

        addPrice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addPriceActionPerformed(evt);
            }
        });
        panelRound2.add(addPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 430, 340, 40));

        jLabel30.setText("Catergory ID");
        panelRound2.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 80, 40));

        addCategoryId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addCategoryIdActionPerformed(evt);
            }
        });
        panelRound2.add(addCategoryId, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, 340, 40));

        jLabel31.setText("Catergory Name");
        panelRound2.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, 110, 40));

        jLabel32.setText("Product ID");
        panelRound2.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 210, 110, 40));

        jLabel33.setText("Product Name");
        panelRound2.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 300, 110, 40));

        addProductId1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addProductId1ActionPerformed(evt);
            }
        });
        panelRound2.add(addProductId1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 250, 340, 40));

        addProductName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addProductNameActionPerformed(evt);
            }
        });
        panelRound2.add(addProductName, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 340, 340, 40));

        jButton3.setBackground(new java.awt.Color(255, 102, 102));
        jButton3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jButton3.setText("Add");
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton3MouseClicked(evt);
            }
        });
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        panelRound2.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 490, -1, 40));

        addCategoryName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addCategoryNameActionPerformed(evt);
            }
        });
        panelRound2.add(addCategoryName, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 160, 340, 40));

        tab44.add(panelRound2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, 410, 550));

        panelRound3.setBackground(new java.awt.Color(255, 255, 255));
        panelRound3.setRoundBottomLeft(20);
        panelRound3.setRoundBottomRight(20);
        panelRound3.setRoundTopLeft(20);
        panelRound3.setRoundTopRight(20);
        panelRound3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel34.setText("Price");
        panelRound3.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 390, 110, 40));

        updatePrice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updatePriceActionPerformed(evt);
            }
        });
        panelRound3.add(updatePrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 430, 340, 40));

        jLabel35.setText("Catergory ID");
        panelRound3.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 80, 40));

        updateCId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateCIdActionPerformed(evt);
            }
        });
        panelRound3.add(updateCId, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, 340, 40));

        jLabel36.setText("Catergory Name");
        panelRound3.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, 110, 40));

        jLabel37.setText("Product ID");
        panelRound3.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 210, 110, 40));

        jLabel38.setText("Product Name");
        panelRound3.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 300, 110, 40));

        updateCName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateCNameActionPerformed(evt);
            }
        });
        panelRound3.add(updateCName, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 160, 340, 40));

        updatePId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updatePIdActionPerformed(evt);
            }
        });
        panelRound3.add(updatePId, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 260, 340, 40));

        updatePName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updatePNameActionPerformed(evt);
            }
        });
        panelRound3.add(updatePName, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 340, 340, 40));

        jButton2.setBackground(new java.awt.Color(255, 102, 51));
        jButton2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jButton2.setText("Update");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton2MouseClicked(evt);
            }
        });
        panelRound3.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 492, -1, 40));

        tab44.add(panelRound3, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 100, 410, 560));

        panelRound4.setBackground(new java.awt.Color(255, 255, 255));
        panelRound4.setRoundBottomLeft(20);
        panelRound4.setRoundBottomRight(20);
        panelRound4.setRoundTopLeft(20);
        panelRound4.setRoundTopRight(20);
        panelRound4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel41.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel41.setText("Product ID");
        panelRound4.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 110, 80, 30));

        deletePId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deletePIdActionPerformed(evt);
            }
        });
        panelRound4.add(deletePId, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 210, 40));

        jButton1.setBackground(new java.awt.Color(255, 51, 51));
        jButton1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jButton1.setText("Delete");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        panelRound4.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 210, 90, 30));

        deleteCId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteCIdActionPerformed(evt);
            }
        });
        panelRound4.add(deleteCId, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 210, 40));

        jLabel42.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel42.setText("Category ID");
        panelRound4.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 30, 80, 30));

        jButton4.setBackground(new java.awt.Color(255, 51, 51));
        jButton4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jButton4.setText("Search");
        jButton4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton4MouseClicked(evt);
            }
        });
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        panelRound4.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 210, 90, 30));

        tab44.add(panelRound4, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 230, 250, 270));

        jLabel39.setBackground(new java.awt.Color(255, 255, 255));
        jLabel39.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(255, 51, 51));
        jLabel39.setText("EDIT PRODUCTS");
        tab44.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 180, 220, 40));

        jLabel40.setBackground(new java.awt.Color(255, 255, 255));
        jLabel40.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(255, 102, 102));
        jLabel40.setText("ADD PRODUCTS");
        tab44.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 50, 210, 40));

        tab4.add(tab44, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1340, 760));

        jLabel49.setText("Quantity");
        tab4.add(jLabel49, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 390, 100, 40));

        jTabbedPane1.addTab("tab4", tab4);

        tab5.setMinimumSize(new java.awt.Dimension(1340, 760));
        tab5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tab55.setBackground(new java.awt.Color(153, 255, 204));
        tab55.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelBarChart.setLayout(new java.awt.BorderLayout());
        tab55.add(panelBarChart, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 160, 460, 470));

        lineVariable.setLayout(new java.awt.BorderLayout());
        tab55.add(lineVariable, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 160, 520, 470));

        tab5.add(tab55, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1340, 760));

        jTabbedPane1.addTab("tab5", tab5);

        tab6.setMinimumSize(new java.awt.Dimension(1340, 760));
        tab6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tab66.setBackground(new java.awt.Color(102, 255, 102));
        tab66.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel10.setBackground(new java.awt.Color(153, 255, 204));
        jPanel10.setBorder(javax.swing.BorderFactory.createMatteBorder(20, 0, 0, 0, new java.awt.Color(0, 128, 128)));
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        totalUser.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        totalUser.setText("           1000");
        jPanel10.add(totalUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 160, 30));

        tab66.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 130, 160, 130));

        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel17.setText("             Total User");
        tab66.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 100, 160, 30));

        tableUser.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "User ID", "First Name", "Last Name", "Contact No", "Password"
            }
        ));
        tableUser.setFuenteFilas(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        tableUser.setFuenteFilasSelect(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        tableUser.setFuenteHead(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        tableUser.setRowHeight(25);
        jScrollPane5.setViewportView(tableUser);

        tab66.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 130, 820, 350));

        tab6.add(tab66, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1340, 760));

        jTabbedPane1.addTab("tab6", tab6);

        getContentPane().add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 50, 1340, 800));

        setSize(new java.awt.Dimension(1652, 856));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void label1TabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_label1TabMouseClicked
        jTabbedPane1.setSelectedIndex(0);
        revalidate();
        repaint();
    }//GEN-LAST:event_label1TabMouseClicked

    private void label2TabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_label2TabMouseClicked
        jTabbedPane1.setSelectedIndex(1);
        revalidate();
        repaint();
    }//GEN-LAST:event_label2TabMouseClicked

    private void label3TabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_label3TabMouseClicked
        jTabbedPane1.setSelectedIndex(2);
        DefaultTableModel model = (DefaultTableModel) inventoryTableData.getModel();
        model.setRowCount(0); 
        inventoryTableValue();

    }//GEN-LAST:event_label3TabMouseClicked

    private void label1TabMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_label1TabMousePressed
          tab11.setBackground(clickedColor);
          label1.setBackground(green);
          revalidate();
          repaint();
    }//GEN-LAST:event_label1TabMousePressed

    private void label1TabMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_label1TabMouseReleased
        label1.setBackground(defaultColor);
        revalidate();
        repaint();
          
    }//GEN-LAST:event_label1TabMouseReleased

    private void label2TabMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_label2TabMousePressed
        tab22.setBackground(clickedColor);
        label2.setBackground(green);
        revalidate();
        repaint();
    }//GEN-LAST:event_label2TabMousePressed

    private void label2TabMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_label2TabMouseReleased
        label2.setBackground(defaultColor);
        revalidate();
        repaint();
    }//GEN-LAST:event_label2TabMouseReleased

    private void label3TabMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_label3TabMousePressed
        tab33.setBackground(clickedColor);
        label3.setBackground(green);    
        revalidate();
        repaint();
    }//GEN-LAST:event_label3TabMousePressed

    private void label3TabMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_label3TabMouseReleased
        label3.setBackground(defaultColor);
        revalidate();
        repaint();
    }//GEN-LAST:event_label3TabMouseReleased

    private void label4TabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_label4TabMouseClicked
        jTabbedPane1.setSelectedIndex(3);
         revalidate();
        repaint();
    }//GEN-LAST:event_label4TabMouseClicked

    private void label5TabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_label5TabMouseClicked
        jTabbedPane1.setSelectedIndex(4);
         revalidate();
        repaint();
    }//GEN-LAST:event_label5TabMouseClicked

    private void label6TabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_label6TabMouseClicked
        jTabbedPane1.setSelectedIndex(5);
        
        revalidate();
        repaint();
    }//GEN-LAST:event_label6TabMouseClicked

    private void label4TabMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_label4TabMousePressed
        tab44.setBackground(clickedColor);
        label4.setBackground(green);
        revalidate();
        repaint();
    }//GEN-LAST:event_label4TabMousePressed

    private void label4TabMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_label4TabMouseReleased
        label4.setBackground(defaultColor);
        revalidate();
        repaint();
    }//GEN-LAST:event_label4TabMouseReleased

    private void label5TabMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_label5TabMousePressed
        tab55.setBackground(clickedColor);
        label5.setBackground(green);
        revalidate();
        repaint();
    }//GEN-LAST:event_label5TabMousePressed

    private void label5TabMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_label5TabMouseReleased
        label5.setBackground(defaultColor);
        revalidate();
        repaint();
    }//GEN-LAST:event_label5TabMouseReleased

    private void label6TabMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_label6TabMousePressed
        tab66.setBackground(clickedColor);
        label6.setBackground(green);
        revalidate();
        repaint();
    }//GEN-LAST:event_label6TabMousePressed

    private void label6TabMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_label6TabMouseReleased
        label6.setBackground(defaultColor);
        revalidate();
        repaint();
    }//GEN-LAST:event_label6TabMouseReleased

    private void jLabel12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel12MouseClicked
        SignUpForm sign = new SignUpForm();
        sign.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jLabel12MouseClicked

    private void addPriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addPriceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addPriceActionPerformed

    private void addCategoryIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addCategoryIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addCategoryIdActionPerformed

    private void addProductId1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addProductId1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addProductId1ActionPerformed

    private void addProductNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addProductNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addProductNameActionPerformed

    private void updatePriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updatePriceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_updatePriceActionPerformed

    private void updateCIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateCIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_updateCIdActionPerformed

    private void updateCNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateCNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_updateCNameActionPerformed

    private void updatePIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updatePIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_updatePIdActionPerformed

    private void updatePNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updatePNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_updatePNameActionPerformed

    private void deletePIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletePIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_deletePIdActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        
    }//GEN-LAST:event_jButton3ActionPerformed

    private void deleteCIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteCIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_deleteCIdActionPerformed

    private void jButton3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseClicked
        AuthDao auth=new AuthDao();
        try{
        int category1=Integer.parseInt(addCategoryId.getText());
        String category2 = addCategoryName.getText();
        int product1 = Integer.parseInt(addProductId1.getText());
        String product2=addProductName.getText();
        int price1 = Integer.parseInt(addPrice.getText());
       if (category2.equals("") || product2.equals("")) {
        JOptionPane.showMessageDialog(rootPane, "All field is required");
       }
        else{
        Inventory im=new Inventory(category1,category2,product1,product2,price1);
        boolean check=auth.productInventory(im);
        if(check==true){
            System.out.println("Insert success");
            JOptionPane.showMessageDialog(null, "Category added successfully");
        }else{
            System.out.println("Insert failure");
            JOptionPane.showMessageDialog(null, "Category addition unsuccessful");
        }
        }}
        catch(Exception e){
            JOptionPane.showMessageDialog(rootPane, "Please enter valid numbers in numeric fields");
        }
               addCategoryId.setText("");
               addCategoryName.setText("");
               addProductId1.setText("");
               addProductName.setText("");
               addPrice.setText(""); 
     
    }//GEN-LAST:event_jButton3MouseClicked

    private void addCategoryNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addCategoryNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addCategoryNameActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    
    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked
        AuthDao auth=new AuthDao();
        try{
        int category1=Integer.parseInt(updateCId.getText());
        String category2 = updateCName.getText();
        int product1 = Integer.parseInt(updatePId.getText());
        String product2=updatePName.getText();
        int price1 = Integer.parseInt(updatePrice.getText());
       if (category2.equals("") || product2.equals("")) {
        JOptionPane.showMessageDialog(rootPane, "All field is required");
       }
        else{
        Inventory im=new Inventory(category1,category2,product1,product2,price1);
        boolean check=auth.inventoryUpdate(im);
        if(check==true){
            JOptionPane.showMessageDialog(null, "Category updated successfully");
        }else{
            JOptionPane.showMessageDialog(null, "Category updation unsuccessful");
        }
        }}
        catch(Exception e){
            JOptionPane.showMessageDialog(rootPane, "Please enter valid numbers in numeric fields");
        }
                    updateCId.setText("");
                    updateCName.setText("");
                    updatePId.setText("");
                    updatePName.setText("");
                    updatePrice.setText("");
    }//GEN-LAST:event_jButton2MouseClicked

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        AuthDao auth=new AuthDao();
        try{
        int category1=Integer.parseInt(deleteCId.getText());
        int product1 = Integer.parseInt(deletePId.getText());
       if (deleteCId.getText().equals("") || deleteCId.getText().equals("")) {
        JOptionPane.showMessageDialog(rootPane, "All field is required");
       }
        else{
        Delete im=new Delete(category1,product1);
        boolean check=auth.inventoryDelete(im);
        if(check==true){
            JOptionPane.showMessageDialog(null, "Product deleted successfully");
        }else{
            JOptionPane.showMessageDialog(null, "Product deletion unsuccessful");
        }
        }}
        catch(Exception e){
            JOptionPane.showMessageDialog(rootPane, "Please enter valid numbers in numeric fields");
        }
        deleteCId.setText("");
        deletePId.setText("");
    }//GEN-LAST:event_jButton1MouseClicked

    private void jButton4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MouseClicked
            AuthDao auth = new AuthDao();
            try{
            int dcid = Integer.parseInt(deleteCId.getText());
            int bcid = Integer.parseInt(deletePId.getText());
            Search check = auth.searchInventory(dcid,bcid);
            String ss = String.valueOf(check.getCategory_id());
            String ss1 = String.valueOf(check.getProduct_id());
            String ss2 = String.valueOf(check.getPrice());
            updateCId.setText(ss);
            updateCName.setText(check.getCategory_name());
            updatePId.setText(ss1);
            updatePName.setText(check.getProduct_name());
            updatePrice.setText(ss2);
            }
            catch(Exception e){
                JOptionPane.showMessageDialog(rootPane, "Please enter valid id's");
            }
            deleteCId.setText("");
            deletePId.setText("");
    }//GEN-LAST:event_jButton4MouseClicked

    private void addPrice1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addPrice1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addPrice1ActionPerformed

    private void addCategoryId1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addCategoryId1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addCategoryId1ActionPerformed

    private void addProductId2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addProductId2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addProductId2ActionPerformed

    private void addProductName1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addProductName1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addProductName1ActionPerformed

    private void jButton6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton6MouseClicked
        AuthDao auth=new AuthDao();
        try{
        int category1=Integer.parseInt(addCategoryId1.getText());
        String category2 = addCategoryName1.getText();
        int product1 = Integer.parseInt(addProductId2.getText());
        String product2=addProductName1.getText();
        int price1 = Integer.parseInt(addPrice2.getText());
        int price2 = Integer.parseInt(addPrice3.getText());
        int price3 = Integer.parseInt(addPrice1.getText());
       if (category2.equals("") || product2.equals("")) {
        JOptionPane.showMessageDialog(rootPane, "All field is required");
       }
        else{
        SalesModel sm=new SalesModel(category1,category2,product1,product2,price1,price2,price3);
        boolean check=auth.salesProduct(sm);
        if(check==true){
            System.out.println("Insert success");
            JOptionPane.showMessageDialog(null, "product added successfully");
        }else{
            System.out.println("Insert failure");
            JOptionPane.showMessageDialog(null, "product addition unsuccessful");
        }
        }}
        catch(Exception e){
            JOptionPane.showMessageDialog(rootPane, "Please enter valid numbers in numeric fields");
        }
               addCategoryId1.setText("");
               addCategoryName1.setText("");
               addProductId2.setText("");
               addProductName1.setText("");
               addPrice1.setText(""); 
               addPrice2.setText(""); 
               addPrice3.setText(""); 
    }//GEN-LAST:event_jButton6MouseClicked

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton6ActionPerformed

    private void addCategoryName1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addCategoryName1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addCategoryName1ActionPerformed

    private void addPrice2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addPrice2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addPrice2ActionPerformed

    private void addPrice3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addPrice3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addPrice3ActionPerformed

    private void addCategoryId1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_addCategoryId1KeyPressed

    }//GEN-LAST:event_addCategoryId1KeyPressed

    private void addCategoryId1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_addCategoryId1KeyTyped
 
        
    }//GEN-LAST:event_addCategoryId1KeyTyped

    private void addPrice1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addPrice1MouseEntered
       int var = Integer.parseInt(addPrice2.getText());
       int var1 = Integer.parseInt(addPrice3.getText());
       int result = var1*var;
       addPrice1.setText(String.valueOf(result));
       
    }//GEN-LAST:event_addPrice1MouseEntered

    private void addCategoryId1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addCategoryId1MouseEntered

    }//GEN-LAST:event_addCategoryId1MouseEntered

    private void imageUserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_imageUserMouseClicked

    JFileChooser chooser = new JFileChooser();
    int result = chooser.showOpenDialog(null);

    if (result == JFileChooser.APPROVE_OPTION) {
    File f = chooser.getSelectedFile();
    if (f != null) {
        filename = f.getAbsolutePath();
        ImageIcon imageicon = new ImageIcon(new ImageIcon(filename).getImage().getScaledInstance(lbl_image.getWidth(), lbl_image.getHeight(), Image.SCALE_SMOOTH));
        lbl_image.setIcon(imageicon);

        try {
            File img = new File(filename);
            FileInputStream fis = new FileInputStream(img);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            for (int readNum; (readNum = fis.read(buf)) != -1;) {
                bos.write(buf, 0, readNum);
            }
            person_image = bos.toByteArray();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    } else {
        // User canceled the file selection
        System.out.println("File selection canceled by user");
    }
}

    }//GEN-LAST:event_imageUserMouseClicked
private ArrayList<IconImage> userList() {
    ArrayList<IconImage> usersList = new ArrayList<>();
    try {
        MySqlConnection mysqlconnection = new MySqlConnection();
        try (Connection conn = mysqlconnection.openConnection()) {
            if (conn != null) {
                String sql = "SELECT * FROM imageStore";
                IconImage icon;
                try (PreparedStatement statement = conn.prepareStatement(sql);
                     ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        byte[] imageData = resultSet.getBytes("images");
                        int num = resultSet.getInt("s_no");
                        icon = new IconImage(num, imageData);
                        usersList.add(icon);
                    }
                }
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error retrieving image from the database", "Error", JOptionPane.ERROR_MESSAGE);
    }
    return usersList;
}
public void showUser() {
    ArrayList<IconImage> img = userList();
    DefaultTableModel model = (DefaultTableModel) table1.getModel();

    for (IconImage iconImage : img) {
        Object[] row = new Object[2];
        row[0] = iconImage.getS_num();
        row[1] = iconImage.getPicture();
        model.addRow(row);
    }
    
}

    private void imageUserMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_imageUserMouseEntered

    }//GEN-LAST:event_imageUserMouseEntered

    private void imageUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imageUserActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_imageUserActionPerformed

    private void savebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_savebtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_savebtnActionPerformed

    private void savebtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_savebtnMouseClicked
        AuthDao auth=new AuthDao();
        try{
            ImageIcon imageIcon = (ImageIcon) lbl_image.getIcon();
            Image image = imageIcon.getImage();
            BufferedImage bufferedImage = convertToBufferedImage(image);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
            byte[] img = byteArrayOutputStream.toByteArray();
            IconImage im=new IconImage(img);
            boolean check=auth.imageInsert(im);
            if(check==true){
            System.out.println("Insert success");
            JOptionPane.showMessageDialog(null, "image added successfully");
        }else{
            System.out.println("Insert failure");
            JOptionPane.showMessageDialog(null, "image addition unsuccessful");
        }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_savebtnMouseClicked

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton7MouseClicked
        int i = table1.getSelectedRow();
        DefaultTableModel model1 = (DefaultTableModel) table1.getModel();
        table1.setModel(model1);
        byte [] img  = (userList().get(i).getPicture());
        ImageIcon imageicon = new ImageIcon(new ImageIcon(img).getImage().getScaledInstance(lbl_image.getWidth(), lbl_image.getHeight(), Image.SCALE_SMOOTH));
        lbl_image.setIcon(imageicon);
        table1.repaint();
    }//GEN-LAST:event_jButton7MouseClicked

    private void addProductId2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addProductId2MouseEntered
          try{ 
              int categoryId = Integer.parseInt(addCategoryId1.getText());
              int productId = Integer.parseInt(addProductId2.getText());  // Replace with the actual text field for productid

            // Establish a database connection (replace the connection URL, username, and password)
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/signupuser", "root", "password");

            // Prepare a SQL query to fetch category name, product name, and non-primary attribute
            String sqlQuery = "SELECT category_name, product_name,price FROM inventory WHERE category_id = ? AND product_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
                preparedStatement.setInt(1, categoryId);
                preparedStatement.setInt(2, productId);

                // Execute the query
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        // Retrieve the values of category name, product name, and the non-primary attribute
                        String categoryName = resultSet.getString("category_name");
                        String productName = resultSet.getString("product_name");
                        int price = resultSet.getInt("price");
                        // Assuming jTextField9 and jTextField10 are fields for category name and product name
                        addCategoryName1.setText(categoryName);
                        addProductName1.setText(productName);
                        addPrice2.setText(String.valueOf(price));
             
                    } else {
                        // No matching record found
                      JOptionPane.showConfirmDialog(rootPane, "Error");
                    }
                }
            }
        } catch (SQLException | NumberFormatException ex) {
            ex.printStackTrace(); // Handle the exception according to your needs
        } 
    }//GEN-LAST:event_addProductId2MouseEntered
    private BufferedImage convertToBufferedImage(Image image) {
    if (image instanceof BufferedImage) {
        return (BufferedImage) image;
    }
    // Create a new BufferedImage and draw the Image on it
    BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
    Graphics2D graphics = bufferedImage.createGraphics();
    graphics.drawImage(image, 0, 0, null);
    graphics.dispose();

    return bufferedImage;
}
    public void inventoryTableValue(){
          MySqlConnection mysqlconnection = new MySqlConnection();
          Connection conn = mysqlconnection.openConnection();
          if (conn != null) {
          String query = "SELECT * FROM inventory";

          try (PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet resultSet = pstmt.executeQuery()) {
                while (resultSet.next()) {
                    int categoryId = resultSet.getInt("category_id");
                    String categoryName = resultSet.getString("category_name");
                    int productId = resultSet.getInt("product_id");
                    String productName = resultSet.getString("product_name");
                    int Price = resultSet.getInt("price");
                    Object [] obj ={categoryId,productId,productName,Price,categoryName};
                    model = (DefaultTableModel) inventoryTableData.getModel();
                    model.addRow(obj);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Provided database connection is null. Check your connection.");
        }
    }
    public void inventoryCountProduct(){
          MySqlConnection mysqlconnection = new MySqlConnection();
          Connection conn = mysqlconnection.openConnection();
          if (conn != null) {
          String query = "SELECT SUM(COUNT(*)) OVER () AS total_count FROM inventory group by category_id, product_id;";
          try (PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet resultSet = pstmt.executeQuery()) {
                while (resultSet.next()) {
                    int count = resultSet.getInt("total_count");
                    lbl_product.setText(String.valueOf(count));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Provided database connection is null. Check your connection.");
        }
    }
    public void userCount(){
          MySqlConnection mysqlconnection = new MySqlConnection();
          Connection conn = mysqlconnection.openConnection();
          if (conn != null) {
          String query = "SELECT SUM(COUNT(*)) OVER () AS total_count FROM user group by user_id;";
          try (PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet resultSet = pstmt.executeQuery()) {
                while (resultSet.next()) {
                    int count = resultSet.getInt("total_count");
                    totalUser.setText(String.valueOf(count));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Provided database connection is null. Check your connection.");
        }
    }
    public void inventoryCountCategory(){
          MySqlConnection mysqlconnection = new MySqlConnection();
          Connection conn = mysqlconnection.openConnection();
          if (conn != null) {
          String query = "SELECT SUM(COUNT(*)) OVER () AS total_count FROM inventory group by category_id;";

          try (PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet resultSet = pstmt.executeQuery()) {
                while (resultSet.next()) {
                    int count = resultSet.getInt("total_count");
                    lbl_category.setText(String.valueOf(count));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Provided database connection is null. Check your connection.");
        }
    }
    public void inventoryOutOfStock(){
          MySqlConnection mysqlconnection = new MySqlConnection();
          Connection conn = mysqlconnection.openConnection();
          if (conn != null) {
          String query = "SELECT COUNT(*) AS total_count\n" +
                            "FROM (\n" +
                            "    SELECT product_id, COUNT(*) AS product_count\n" +
                            "    FROM sellProducts\n" +
                            "    GROUP BY product_id\n" +
                            "    HAVING product_count > 2\n" +
                            ") AS subquery;";
          try (PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet resultSet = pstmt.executeQuery()) {
                while (resultSet.next()) {
                    int count = resultSet.getInt("total_count");
                    lbl_fast.setText(String.valueOf(count));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Provided database connection is null. Check your connection.");
        }
    }
    public void salesTableValue(){
          MySqlConnection mysqlconnection = new MySqlConnection();
          Connection conn = mysqlconnection.openConnection();
          if (conn != null) {
          String query = "SELECT * FROM sellproducts";

          try (PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet resultSet = pstmt.executeQuery()) {
                while (resultSet.next()) {
                    int categoryId = resultSet.getInt("category_id");
                    String categoryName = resultSet.getString("category_name");
                    int productId = resultSet.getInt("product_id");
                    String productName = resultSet.getString("product_name");
                    int price = resultSet.getInt("price");
                    int quantity = resultSet.getInt("quantity");
                    int total = resultSet.getInt("total_price");
                    Object [] obj ={categoryId,categoryName,productId,productName,price,quantity,total};
                    model = (DefaultTableModel) salesTableValue.getModel();
                    model.addRow(obj);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Provided database connection is null. Check your connection.");
        }
    }


  
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField addCategoryId;
    private javax.swing.JTextField addCategoryId1;
    private javax.swing.JTextField addCategoryName;
    private javax.swing.JTextField addCategoryName1;
    private javax.swing.JTextField addPrice;
    private javax.swing.JTextField addPrice1;
    private javax.swing.JTextField addPrice2;
    private javax.swing.JTextField addPrice3;
    private javax.swing.JTextField addProductId1;
    private javax.swing.JTextField addProductId2;
    private javax.swing.JTextField addProductName;
    private javax.swing.JTextField addProductName1;
    private javax.swing.JTextField deleteCId;
    private javax.swing.JTextField deletePId;
    private javax.swing.JButton imageUser;
    private rojeru_san.complementos.RSTableMetro inventoryTableData;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JPanel label1;
    private javax.swing.JLabel label1Tab;
    private javax.swing.JPanel label2;
    private javax.swing.JLabel label2Tab;
    private javax.swing.JPanel label3;
    private javax.swing.JLabel label3Tab;
    private javax.swing.JPanel label4;
    private javax.swing.JLabel label4Tab;
    private javax.swing.JPanel label5;
    private javax.swing.JLabel label5Tab;
    private javax.swing.JPanel label6;
    private javax.swing.JLabel label6Tab;
    private javax.swing.JLabel lbl_category;
    private javax.swing.JLabel lbl_fast;
    private javax.swing.JLabel lbl_image;
    private javax.swing.JLabel lbl_product;
    private javax.swing.JLabel lbl_stock;
    private javax.swing.JPanel lineVariable;
    private javax.swing.JLabel m3;
    private javax.swing.JPanel panelBarChart;
    private com.groupc.saleberry.custombutton.PanelRound panelRound2;
    private com.groupc.saleberry.custombutton.PanelRound panelRound3;
    private com.groupc.saleberry.custombutton.PanelRound panelRound4;
    private com.groupc.saleberry.custombutton.PanelRound panelro;
    private com.groupc.saleberry.custombutton.PictureBox pictureBox1;
    private com.groupc.saleberry.custombutton.PictureBox pictureBox10;
    private com.groupc.saleberry.custombutton.PictureBox pictureBox13;
    private com.groupc.saleberry.custombutton.PictureBox pictureBox14;
    private com.groupc.saleberry.custombutton.PictureBox pictureBox15;
    private com.groupc.saleberry.custombutton.PictureBox pictureBox16;
    private com.groupc.saleberry.custombutton.PictureBox pictureBox17;
    private com.groupc.saleberry.custombutton.PictureBox pictureBox2;
    private com.groupc.saleberry.custombutton.PictureBox pictureBox3;
    private com.groupc.saleberry.custombutton.PictureBox pictureBox4;
    private com.groupc.saleberry.custombutton.PictureBox pictureBox5;
    private com.groupc.saleberry.custombutton.PictureBox pictureBox6;
    private com.groupc.saleberry.custombutton.PictureBox pictureBox7;
    private com.groupc.saleberry.custombutton.PictureBox pictureBox8;
    private com.groupc.saleberry.custombutton.PictureBox pictureBox9;
    private rojeru_san.complementos.RSTableMetro salesTableValue;
    private javax.swing.JButton savebtn;
    private javax.swing.JPanel tab1;
    private javax.swing.JPanel tab11;
    private javax.swing.JPanel tab2;
    private javax.swing.JPanel tab22;
    private javax.swing.JPanel tab3;
    private javax.swing.JPanel tab33;
    private javax.swing.JPanel tab4;
    private javax.swing.JPanel tab44;
    private javax.swing.JPanel tab5;
    private javax.swing.JPanel tab55;
    private javax.swing.JPanel tab6;
    private javax.swing.JPanel tab66;
    private rojeru_san.complementos.RSTableMetro table1;
    private rojeru_san.complementos.RSTableMetro tableUser;
    private javax.swing.JLabel totalUser;
    private javax.swing.JTextField updateCId;
    private javax.swing.JTextField updateCName;
    private javax.swing.JTextField updatePId;
    private javax.swing.JTextField updatePName;
    private javax.swing.JTextField updatePrice;
    // End of variables declaration//GEN-END:variables
}
