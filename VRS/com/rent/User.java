package com.rent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import com.mysql.cj.jdbc.result.ResultSetMetaData;

public class User {
    static Scanner sc = new Scanner(System.in);
    static DB db = new DB();
    static Connection con;

    static {
        try {
            con = db.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    static void showUser(String name, String id) {
        System.out.println("---Welcome " + name + "!!---");
        System.out.println();
        UserMenu(id);
    }

    static void UserMenu(String u_id) {
        int n;
        do {
            System.out.println("Enter Your Choice:");
            System.out.println("1. Display Vehicle");
            System.out.println("2. Rent Vehicle");
            System.out.println("3. Return Vehicle");
            System.out.println("4. Exit");
            System.out.println();
            n = sc.nextInt();
            switch (n) {
                case 1: {
                    UserDisp();
                    break;
                }
                case 2: {
                    Rent(u_id);
                    break;
                }
                case 3: {
                    Return(u_id);
                    break;
                }
                case 4: {
                    System.out.println("---Exiting Successfullt---");
                    break;
                }
                default: {
                    System.out.println("---Invalid Option---");
                }
            }
        } while (n != 4);
    }

    static void UserDisp() {
        try {

            String sql = "SELECT * FROM disp WHERE u_id=0";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData rsmd = (ResultSetMetaData) rs.getMetaData();
            int col = rsmd.getColumnCount();
            for (int i = 1; i < col; i++) {
                if (i > 1)
                    System.out.print("\t");
                System.out.print(rsmd.getColumnName(i));
            }
            System.out.println();
            while (rs.next()) {
                for (int i = 1; i < col; i++) {
                    if (i > 1)
                        System.out.print("\t");
                    String val = rs.getString(i);
                    System.out.print(val);
                }
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void Rent(String u_id) {
        try {
            String l = "SELECT u_id FROM disp WHERE u_id=?";
            PreparedStatement k = con.prepareStatement(l);
            k.setString(1, u_id);
            ResultSet kk = k.executeQuery();
            if (kk.next()) {
                String h = "SELECT model FROM disp WHERE u_id=?";
                PreparedStatement h1 = con.prepareStatement(h);
                h1.setString(1, u_id);
                ResultSet h2 = h1.executeQuery();
                if (h2.next()) {
                    System.out.println("Return " + h2.getString("model") + " to rent another Vehicle");
                }
            } else {
                sc.nextLine();
                System.out.print("Enter ID to Choose Vehicle: ");
                String id = sc.nextLine();
                String sql = "SELECT * FROM disp WHERE id=?";
                PreparedStatement stmt = con.prepareStatement(sql);
                stmt.setString(1, id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    System.out.println();
                    System.out.println("Vehicle detail:");
                    System.out.println("ID:" + rs.getString("id"));
                    System.out.println("Model:" + rs.getString("model"));
                    System.out.println("Type:" + rs.getString("type"));
                    System.out.println("Color:" + rs.getString("color"));
                    System.out.println("Kilometer:" + rs.getString("k_meter"));
                    System.out.println("Deposit:" + rs.getString("deposit"));
                    System.out.println("Number:" + rs.getString("ve_no"));
                    System.out.println("Seat:" + rs.getString("seat"));
                    System.out.println();
                    System.out.println("If you need to Rent enter y, else n");
                    System.out.println("y/n");
                    String ag = sc.nextLine();
                    if (ag.equals("y")) {
                        int u = 0;
                        String q = "SELECT u_id FROM disp WHERE id=?";
                        PreparedStatement p = con.prepareStatement(q);
                        p.setString(1, id);
                        ResultSet r = p.executeQuery();
                        if (r.next()) {
                            u = Integer.valueOf(r.getString("u_id"));
                        }
                        if (u > 0 && u != Integer.valueOf(u_id)) {
                            System.out.println("--- Vehicle Not Available ---");
                        } else if (u > 0 && u == Integer.valueOf(u_id)) {
                            System.out.println("--- Vehicle Already rented by You ---");
                        } else {
                            String sq = "UPDATE disp SET u_id=? WHERE id=?";
                            PreparedStatement st = con.prepareStatement(sq);
                            st.setString(1, u_id);
                            st.setString(2, id);
                            int rt = st.executeUpdate();
                            if (rt > 0)
                                System.out.println("--- Vehicle Rented Succesfully ---");
                            else {
                                System.out.println("--- TryAgain ---");
                                Rent(u_id);
                            }
                        }
                    }
                } else {
                    System.out.println("--- Vehicle not found ---");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    static void Return(String u_id) {
        try {
            sc.nextLine();
            System.out.print("Enter the Vehicle ID to Return:");
            String v = sc.nextLine();
            String sq = "SELECT * FROM disp WHERE u_id=?";
            PreparedStatement rt = con.prepareStatement(sq);
            rt.setString(1, u_id);
            ResultSet tt = rt.executeQuery();
            int uu = 0;
            if (tt.next()) {
                uu = Integer.valueOf(tt.getString("u_id"));
            }
            if (uu != Integer.valueOf(u_id)) {
                System.out.println("You Does n't Rent This Vehicle");
                UserMenu(u_id);
            }

                String sql = "UPDATE disp SET u_id=? WHERE id=?";
                PreparedStatement stmt = con.prepareStatement(sql);
                stmt.setString(1, "0");
                stmt.setString(2, v);
                int rs = stmt.executeUpdate();
                if (rs > 0) {
                    System.out.println("----Wait for Admin----");
                    Auth.SignIn(v);
                }
            

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

    }
}
