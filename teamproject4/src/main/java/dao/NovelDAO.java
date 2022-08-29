package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import util.JDBCUtil;
import vo.NovelVO;

public class NovelDAO {
   Connection conn;
   PreparedStatement pstmt;
   final String sql_selectAll_NT="SELECT * FROM (SELECT * FROM NOVEL WHERE NTITLE LIKE '%'||?||'%' ORDER BY NID DESC) WHERE ROWNUM < = ?";
   final String sql_selectAll_NG="SELECT * FROM (SELECT * FROM NOVEL WHERE NGENRE LIKE '%'||?||'%'  ORDER BY NID DESC) WHERE ROWNUM < = ?";
   final String sql_selectAll_NW="SELECT * FROM (SELECT * FROM NOVEL WHERE NWRITER LIKE '%'||?||'%' ORDER BY NID DESC) WHERE ROWNUM < = ?";
   // 검색기능 추가로 selectAll을 3개로 나누었다.
   
   final String sql_selectAll_N="SELECT * FROM NOVEL WHERE ROWNUM  < = ?";
   
   final String sql_selectAll_AVG="SELECT AVG(OSTAR) FROM OPINION LEFT OUTER JOIN NOVEL ON OPINION.OID=NOVEL.OID WHERE OID=? GROUP BY NOVEL.NID";
   
   final String sql_selectOne_N="SELECT * FROM NOVEL WHERE NID=?";
   
   final String sql_insert_N="INSERT INTO NOVEL VALUES((SELECT NVL(MAX(NID),0)+1 FROM NOVEL),?,?,?,?,?,?)";
   final String sql_sample = "SELECT COUNT(*) AS CNT FROM NOVEL";
   
   
   public ArrayList<NovelVO> selectAll_N(NovelVO nvo){
	      ArrayList<NovelVO> datas=new ArrayList<NovelVO>();
	      conn=JDBCUtil.connect();
	      try {
	            pstmt=conn.prepareStatement(sql_selectAll_N);
	            pstmt.setInt(1,nvo.getNcnt());
	            ResultSet rs=pstmt.executeQuery();
//	            System.out.println("로그 : ");
	         while(rs.next()) {
	            NovelVO data=new NovelVO();
	            data.setNid(rs.getInt("NID"));
	            data.setNtitle(rs.getString("NTITLE"));
	            data.setNcontent(rs.getString("NCONTENT"));
	            data.setNimg(rs.getString("NIMG"));
	            data.setNgenre(rs.getString("NGENRE"));
	            data.setNwriter(rs.getString("NWRITER"));
//	            System.out.println("로그 :" + data);
	            datas.add(data);
	         }
	      } catch (SQLException e) {
	         // TODO Auto-generated catch block
	         e.printStackTrace();
	      } finally {
	         JDBCUtil.disconnect(pstmt, conn);
	      }      
	      return datas;
	   }
   
   public boolean insert_N(NovelVO vo) {
       conn = JDBCUtil.connect(); 
       try {
//      	pstmt = conn.prepareStatement(sql_selectAll_AVG);
//       	pstmt.setInt(1, vo.getNid());
//       	ResultSet rs2 = pstmt.executeQuery();
      	 
          pstmt = conn.prepareStatement(sql_insert_N);
          pstmt.setString(1, vo.getNtitle());
          pstmt.setString(2, vo.getNcontent());
          pstmt.setString(3, vo.getNimg());
          pstmt.setString(4, vo.getNgenre()); 
          pstmt.setString(5, vo.getNwriter()); 
          pstmt.setDouble(6, 0);
          //rs2.getDouble("AVG")

          pstmt.executeUpdate();
       } catch (SQLException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
          return false;
       } finally {
          JDBCUtil.disconnect(pstmt, conn); 
       }
       return true;

    }
 
   public NovelVO selectOne_N(NovelVO nvo) {
	   conn=JDBCUtil.connect();
      try {
         pstmt=conn.prepareStatement(sql_selectOne_N);
         pstmt.setInt(1, nvo.getNid());
         ResultSet rs=pstmt.executeQuery();
         if(rs.next()) {
            NovelVO data=new NovelVO();
            data.setNid(rs.getInt("NID"));
            data.setNtitle(rs.getString("NTITLE"));
            data.setNcontent(rs.getString("NCONTENT"));
            data.setNimg(rs.getString("NIMG"));
            data.setNgenre(rs.getString("NGENRE"));
            data.setNwriter(rs.getString("NWRITER"));
            return data;
         }
      } catch (SQLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } finally {
         JDBCUtil.disconnect(pstmt, conn);
      }      
      return null;
   }
   public ArrayList<NovelVO> selectAll_SEARCH(NovelVO nvo){
      ArrayList<NovelVO> datas=new ArrayList<NovelVO>();
      conn=JDBCUtil.connect();
      try {
    	  if(nvo.getSearchCondition().equals("Ntitle")) {
    		  pstmt=conn.prepareStatement(sql_selectAll_NT);
    		  pstmt.setString(1, nvo.getSearchContent());
    		  pstmt.setInt(4,nvo.getNcnt());
    	  }
    	  else if(nvo.getSearchCondition().equals("Ngenre")) {
    		  pstmt=conn.prepareStatement(sql_selectAll_NG);
    		  pstmt.setString(1, nvo.getSearchContent());
    		  pstmt.setInt(4,nvo.getNcnt());
    	  }
    	  else if(nvo.getSearchCondition().equals("Nwriter")) {
    		  pstmt=conn.prepareStatement(sql_selectAll_NW);
    		  pstmt.setString(1, nvo.getSearchContent());
    		  pstmt.setInt(4,nvo.getNcnt());
    	  }	  
            ResultSet rs=pstmt.executeQuery();
            System.out.println("로그 : ");
         while(rs.next()) {
            NovelVO data=new NovelVO();
            data.setNid(rs.getInt("NID"));
            data.setNtitle(rs.getString("NTITLE"));
            data.setNcontent(rs.getString("NCONTENT"));
            data.setNimg(rs.getString("NIMG"));
            data.setNgenre(rs.getString("NGENRE"));
            data.setNwriter(rs.getString("NWRITER"));
            System.out.println("로그 :" + data);
            datas.add(data);
         }
      } catch (SQLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } finally {
         JDBCUtil.disconnect(pstmt, conn);
      }      
      return datas;
   }

   public boolean hasSample(NovelVO vo) {
         conn = JDBCUtil.connect();
         try {
            pstmt = conn.prepareStatement(sql_sample);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            int cnt = rs.getInt("CNT");
            if (cnt >= 5) {
               return true;
            }
            return false;
         } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
         } finally {
            JDBCUtil.disconnect(pstmt, conn);
         }
      }

   
}