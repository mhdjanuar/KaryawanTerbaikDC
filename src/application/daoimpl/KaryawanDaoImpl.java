/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package application.daoimpl;

import application.dao.KaryawanDao;
import application.models.KaryawanModel;
import application.models.ListDataModel;
import application.utils.DatabaseUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mhdja
 */
public class KaryawanDaoImpl implements KaryawanDao {
    
    private Connection dbConnection = null;
    private PreparedStatement pstmt = null;
    private ResultSet resultSet = null;
    private String query;
    
    public KaryawanDaoImpl() {
        dbConnection = DatabaseUtil.getInstance().getConnection();
    }

    @Override
    public List<KaryawanModel> findAll() {
        List<KaryawanModel> listDataAll = new ArrayList<>();

        try {
            query = "SELECT * FROM karyawan";
            pstmt = dbConnection.prepareStatement(query);
            resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                KaryawanModel listData = new KaryawanModel();
                listData.setId(resultSet.getInt("id_karyawan"));
                listData.setName(resultSet.getString("nama"));
                listData.setUsia(resultSet.getInt("usia"));
                listData.setKontak(resultSet.getString("kontak"));
                listData.setEmail(resultSet.getString("email"));
                listData.setAlamat(resultSet.getString("alamat"));
                listData.setGender(resultSet.getString("gender"));

                listDataAll.add(listData);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeStatement();
        }

        return listDataAll;
    }


    @Override
    public int create(KaryawanModel karyawan) {
        try {
            query = "INSERT INTO karyawan(nama, usia, kontak, email, gender, alamat) VALUES (?, ?, ?, ?, ?, ?)";
            pstmt = dbConnection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, karyawan.getName());
            pstmt.setInt(2, karyawan.getUsia());
            pstmt.setString(3, karyawan.getKontak());
            pstmt.setString(4, karyawan.getEmail());
            pstmt.setString(5, karyawan.getGender());
            pstmt.setString(6, karyawan.getAlamat());

            // Log sebelum insert
            System.out.println("=== Menyimpan Data Karyawan ===");
            System.out.println("Nama: " + karyawan.getName());
            System.out.println("Usia: " + karyawan.getUsia());
            System.out.println("Kontak: " + karyawan.getKontak());
            System.out.println("Email: " + karyawan.getEmail());
            System.out.println("Gender: " + karyawan.getGender());
            System.out.println("Alamat: " + karyawan.getAlamat());

            int result = pstmt.executeUpdate();
            resultSet = pstmt.getGeneratedKeys();
            if (resultSet.next()) {
                int insertedId = resultSet.getInt(1);
                System.out.println("Data karyawan berhasil disimpan. ID: " + insertedId);
                return insertedId;
            }

            System.out.println("Data karyawan disimpan tanpa ID yang dihasilkan.");
            return result;
        } catch (SQLException e) {
            System.out.println("Gagal menyimpan data karyawan: " + e.getMessage());
            throw new RuntimeException(e);
        } finally {
            closeStatement();
        }
    }

    
    private void closeStatement() {
        try {
            if(pstmt != null){
                pstmt.close();
                pstmt = null;
            }
            if(resultSet != null){
                resultSet.close();
                resultSet = null;
            }   
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
}
