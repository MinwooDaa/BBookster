package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import util.JDBCUtil;
import vo.LlikeVO;
import vo.MemberVO;

public class MemberDAO {
	Connection conn;
	PreparedStatement pstmt;
	final String sql_selectOne_M="SELECT * FROM MEMBER WHERE MID=? AND MPW=?"; // 로그인 로직
	final String sql_selectAll_M="SELECT * FROM MEMBER"; // 관리자에서 필요
	final String sql_selectAll_ST="SELECT * FROM BOARD LEFT OUTER JOIN MEMBER ON MEMBER.MID=LLIKE.MID WHERE MID=?";
	final String sql_selectAll_NL="SELECT * FROM BOARD LEFT OUTER JOIN MEMBER ON MEMBER.MID=LLIKE.MID WHERE MID=?";
	final String sql_selectAll_RP="SELECT * FROM BOARD LEFT OUTER JOIN MEMBER ON MEMBER.MID=LLIKE.MID WHERE MID=?";
	// 신고/추천/비추천을 가리기위한 SELECTALL
	final String sql_insert_M="INSERT INTO MEMBER VALUES(?,?,?,?,?,?,?)";
	final String sql_update_M="UPDATE MEMBER SET MPW=?, NICKNAME=? WHERE MID=?";
	final String sql_update_ADMIN="UPDATE MEMBER SET ROLE=? WHERE MID=?"; // 멤버의 계정권한 업데이트
	final String sql_delete_M="DELETE FROM MEMBER WHERE MID=? AND MPW=?";

	// 관리자 페이지에서 사용할 것 selectAll_ST = 추천 selectAll_NL = 비추천 selectAll_RP = 신고

	public ArrayList<MemberVO> selectAll_ST(MemberVO mvo) {
		ArrayList<MemberVO> datas = new ArrayList<MemberVO>();
		conn = JDBCUtil.connect();
		try {
			pstmt = conn.prepareStatement(sql_selectAll_ST); // 추천
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				MemberVO data = new MemberVO();
				data.setMid(rs.getString("MID"));
				data.setMpw(rs.getString("MPW"));
				data.setMname(rs.getString("MNAME"));
				data.setMphone(rs.getString("MPHONE"));
				data.setMemail(rs.getString("MEMAIL"));
				data.setRole(rs.getString("ROLE"));
				if(rs.getString("NICKNAME")==null) {
					data.setMid("[이름없음]");
				}else {
					data.setMid(rs.getString("NICKNAME"));
				}
				datas.add(data);
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCUtil.disconnect(pstmt, conn);
		}
		return datas;
	}
	public ArrayList<MemberVO> selectAll_NL(MemberVO mvo) {
		ArrayList<MemberVO> datas = new ArrayList<MemberVO>();

		conn = JDBCUtil.connect();
		try {
			pstmt = conn.prepareStatement(sql_selectAll_NL); // 비추천
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				MemberVO data = new MemberVO();
				data.setMid(rs.getString("MID"));
				data.setMpw(rs.getString("MPW"));
				data.setMname(rs.getString("MNAME"));
				data.setMphone(rs.getString("MPHONE"));
				data.setMemail(rs.getString("MEMAIL"));
				data.setRole(rs.getString("ROLE"));
				if(rs.getString("NICKNAME")==null) {
					data.setMid("[이름없음]");
				}else {
					data.setMid(rs.getString("NICKNAME"));
				}
				datas.add(data);
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCUtil.disconnect(pstmt, conn);
		}
		return datas;
	}
	public ArrayList<MemberVO> selectAll_RP(MemberVO mvo) {
		ArrayList<MemberVO> datas = new ArrayList<MemberVO>();
		conn = JDBCUtil.connect();
		try {
			pstmt = conn.prepareStatement(sql_selectAll_RP);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				MemberVO data = new MemberVO();
				data.setMid(rs.getString("MID"));
				data.setMpw(rs.getString("MPW"));
				data.setMname(rs.getString("MNAME"));
				data.setMphone(rs.getString("MPHONE"));
				data.setMemail(rs.getString("MEMAIL"));
				data.setRole(rs.getString("ROLE"));
				data.setReport(rs.getInt("REPORT"));
				if(rs.getString("NICKNAME")==null) {
					data.setMid("[이름없음]");
				}else {
					data.setMid(rs.getString("NICKNAME"));
				}
				datas.add(data);
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCUtil.disconnect(pstmt, conn);
		}
		return datas;
	}

	public boolean insert_M(MemberVO mvo) { //회원가입
		conn = JDBCUtil.connect();
		try {
			pstmt = conn.prepareStatement(sql_insert_M);
			pstmt.setString(1, mvo.getMid()); 
			pstmt.setString(2, mvo.getMpw()); 
			pstmt.setString(3, mvo.getMname());
			pstmt.setString(4, mvo.getNickname());
			pstmt.setString(5, mvo.getMphone());
			pstmt.setString(6, mvo.getMemail());
			pstmt.setString(7, "member");
			pstmt.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} finally {
			JDBCUtil.disconnect(pstmt, conn); 
		}
		return true;

	}

	public boolean update_M(MemberVO mvo) { //회원정보수정
		conn = JDBCUtil.connect();
		try {
			pstmt = conn.prepareStatement(sql_update_M); 
			pstmt.setString(1, mvo.getMpw());
			pstmt.setString(2, mvo.getMname());
			pstmt.setString(3, mvo.getMid());
			int res = pstmt.executeUpdate();
			if (res == 0) {
				System.out.println("로그 : update 실패");
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} finally {
			JDBCUtil.disconnect(pstmt, conn);
		}
		return true;
	}
	public boolean update_ADMIN(MemberVO mvo) { //회원정보수정
		conn = JDBCUtil.connect();
		try {
			pstmt = conn.prepareStatement(sql_update_ADMIN); 
			pstmt.setString(1, mvo.getRole());
			pstmt.setString(2, mvo.getMid());
			int res = pstmt.executeUpdate();
			if (res == 0) {
				System.out.println("로그 : update 실패");
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} finally {
			JDBCUtil.disconnect(pstmt, conn);
		}
		return true;
	}

	public boolean delete_M(MemberVO mvo) { //회원탈퇴
		conn = JDBCUtil.connect();
		try {
			pstmt = conn.prepareStatement(sql_delete_M);
			pstmt.setString(1, mvo.getMid());
			pstmt.setString(2, mvo.getMpw());
			int res = pstmt.executeUpdate();
			if (res == 0) {
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} finally {
			JDBCUtil.disconnect(pstmt, conn);
		}
		return true;
	}

	public MemberVO selectOne_M(MemberVO mvo) { //로그인
		conn = JDBCUtil.connect();
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql_selectOne_M);
			pstmt.setString(1, mvo.getMid());
			pstmt.setString(2, mvo.getMpw());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				MemberVO data = new MemberVO();
				data.setMid(rs.getString("MID"));
				data.setMpw(rs.getString("MPW"));
				data.setMname(rs.getString("MNAME"));
				data.setNickname(rs.getString("NICKNAME"));
				data.setMphone(rs.getString("MPHONE"));
				data.setMemail(rs.getString("MEMAIL"));
				data.setRole(rs.getString("ROLE"));
				return data;
			} else {
				return null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			JDBCUtil.disconnect(pstmt, conn);
		}
	}

	public ArrayList<MemberVO> selectAll_M(MemberVO mvo) {
		ArrayList<MemberVO> datas = new ArrayList<MemberVO>();
		conn = JDBCUtil.connect();
		try {
			pstmt = conn.prepareStatement(sql_selectAll_M);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				MemberVO data = new MemberVO();
				data.setMid(rs.getString("MID"));
				data.setMpw(rs.getString("MPW"));
				data.setMname(rs.getString("MNAME"));
				data.setNickname(rs.getString("NICKNAME"));
				data.setMphone(rs.getString("MPHONE"));
				data.setMemail(rs.getString("MEMAIL"));
				data.setRole(rs.getString("ROLE"));
				datas.add(data);
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCUtil.disconnect(pstmt, conn);
		}
		return datas;
	}


}