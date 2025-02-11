// Source code is decompiled from a .class file using FernFlower decompiler.
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class OrderInventoryPage extends JFrame {
   private static final String DB_URL = "jdbc:mysql://localhost:3306/warehouse_management";
   private static final String DB_USER = "root";
   private static final String DB_PASSWORD = "1234";
   private JTextField orderIdField;
   private JTextField itemIdField;
   private JTextField itemNameField;
   private JTextField quantityField;
   private JTextField statusField;
   private JTextField purchaseDateField;

   public OrderInventoryPage(JFrame var1) {
      this.initializeFrame();
      this.setupUIComponents();
      this.loadOrderData();
   }

   private void initializeFrame() {
      this.setTitle("Order Management System");
      this.setSize(800, 600);
      this.setMinimumSize(new Dimension(600, 400));
      this.setDefaultCloseOperation(3);
      this.setLocationRelativeTo((Component)null);
      this.setLayout(new BorderLayout());
   }

   private void setupUIComponents() {
      this.add(this.createHeader(), "North");
      this.add(this.createContentPanel(), "Center");
      this.add(this.createButtonPanel(), "South");
   }

   private JPanel createHeader() {
      JPanel var1 = new JPanel();
      var1.setBackground(new Color(224, 238, 238));
      var1.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
      JLabel var2 = new JLabel("Order Inventory");
      var2.setFont(new Font("Segoe UI", 1, 24));
      var2.setForeground(new Color(50, 50, 50));
      var1.add(var2);
      return var1;
   }

   private JPanel createContentPanel() {
      JPanel var1 = new JPanel(new GridBagLayout());
      var1.setBackground(new Color(240, 248, 255));
      var1.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
      GridBagConstraints var2 = new GridBagConstraints();
      var2.anchor = 17;
      var2.insets = new Insets(10, 15, 10, 15);
      this.orderIdField = this.createInputField("");
      this.itemIdField = this.createInputField("");
      this.itemNameField = this.createInputField("");
      this.quantityField = this.createInputField("");
      this.statusField = this.createInputField("");
      this.purchaseDateField = this.createInputField("");
      this.addFormField(var1, var2, 0, "Order ID:", this.orderIdField);
      this.addFormField(var1, var2, 1, "Item ID:", this.itemIdField);
      this.addFormField(var1, var2, 2, "Item Name:", this.itemNameField);
      this.addFormField(var1, var2, 3, "Quantity:", this.quantityField);
      this.addFormField(var1, var2, 4, "Status:", this.statusField);
      this.addFormField(var1, var2, 5, "Purchase Date:", this.purchaseDateField);
      return var1;
   }

   private void addFormField(JPanel var1, GridBagConstraints var2, int var3, String var4, JTextField var5) {
      var2.gridx = 0;
      var2.gridy = var3;
      JLabel var6 = this.createLabel(var4);
      var1.add(var6, var2);
      var2.gridx = 1;
      var1.add(var5, var2);
   }

   private JLabel createLabel(String var1) {
      JLabel var2 = new JLabel(var1);
      var2.setFont(new Font("Segoe UI", 0, 16));
      var2.setForeground(new Color(80, 80, 80));
      return var2;
   }

   private JTextField createInputField(String var1) {
      JTextField var2 = new JTextField(var1, 20);
      var2.setFont(new Font("Segoe UI", 0, 16));
      var2.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)), BorderFactory.createEmptyBorder(5, 10, 5, 10)));
      var2.setBackground(Color.WHITE);
      var2.setEditable(true);
      return var2;
   }

   private JPanel createButtonPanel() {
      JPanel var1 = new JPanel(new FlowLayout(1, 15, 10));
      var1.setBackground(new Color(240, 248, 255));
      var1.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));
      JButton var2 = this.createActionButton("back", "back inventory details");
      JButton var3 = this.createActionButton("View", "View order details");
      JButton var4 = this.createActionButton("Add", "Add new order");
      var3.addActionListener((var1x) -> {
         this.openOrderViewPage();
      });
      var4.addActionListener((var1x) -> {
         JOptionPane.showMessageDialog(this, "Add functionality not implemented yet.");
      });
      var2.addActionListener((var2x) -> {
         this.dispose();
         var2.setVisible(true);
      });
      var1.add(var2);
      var1.add(var3);
      var1.add(var4);
      return var1;
   }

   private JButton createActionButton(String var1, String var2) {
      JButton var3 = new JButton(var1);
      var3.setPreferredSize(new Dimension(120, 40));
      var3.setFont(new Font("Segoe UI", 1, 16));
      var3.setBackground(new Color(211, 211, 211));
      var3.setFocusPainted(false);
      var3.setToolTipText(var2);
      var3.addMouseListener(new OrderInventoryPage$1(this, var3));
      return var3;
   }

   private void openOrderViewPage() {
      SwingUtilities.invokeLater(() -> {
         OrderView var1 = new OrderView(this);
         var1.setVisible(true);
         this.setVisible(false);
      });
   }

   private void loadOrderData() {
      try {
         Connection var1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/warehouse_management", "root", "1234");

         try {
            String var2 = "SELECT o.order_id, i.item_id, i.item_name, o.quantity, o.status, o.purchase_date FROM orders o JOIN items i ON o.item_id = i.item_id LIMIT 1";
            PreparedStatement var3 = var1.prepareStatement(var2);
            ResultSet var4 = var3.executeQuery();
            if (var4.next()) {
               this.orderIdField.setText(var4.getString("order_id"));
               this.itemIdField.setText(var4.getString("item_id"));
               this.itemNameField.setText(var4.getString("item_name"));
               this.quantityField.setText(var4.getString("quantity"));
               this.statusField.setText(var4.getString("status"));
               this.purchaseDateField.setText(var4.getString("purchase_date"));
            }
         } catch (Throwable var6) {
            if (var1 != null) {
               try {
                  var1.close();
               } catch (Throwable var5) {
                  var6.addSuppressed(var5);
               }
            }

            throw var6;
         }

         if (var1 != null) {
            var1.close();
         }
      } catch (SQLException var7) {
         var7.printStackTrace();
         JOptionPane.showMessageDialog(this, "Database connection error");
      }

   }
}
