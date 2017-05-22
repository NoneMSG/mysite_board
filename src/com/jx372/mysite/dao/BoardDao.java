package com.jx372.mysite.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.jx372.mysite.vo.BoardVo;

public class BoardDao {
	private Connection getConnection() throws SQLException {

		Connection conn = null;

		try {
			// 1. 드라이버 로딩
			Class.forName("com.mysql.jdbc.Driver");

			// 2. Connection 하기
			String url = "jdbc:mysql://localhost:3306/webdb?useUnicode=true&characterEncoding=utf8";
			conn = DriverManager.getConnection(url, "webdb", "webdb");
		} catch (ClassNotFoundException e) {
			System.out.println("JDBC Driver 를 찾을 수 없습니다.");
		}

		return conn;
	}
	
	
	public BoardVo getList(Long no){
		Connection conn = null;
		PreparedStatement pstmt=null;
		BoardVo boardVo=null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			String sql = "select title, content from board where no=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.executeQuery( sql );
			pstmt.setLong(1,no);
			rs=pstmt.executeQuery();
			if(rs.next()){
				boardVo = new BoardVo();
				boardVo.setTitle(rs.getString(1));
				boardVo.setContent(rs.getString(2));
			}
			return boardVo;
		} catch( SQLException e ) {
			e.printStackTrace();
		} finally {
			try {
				if(rs!=null){
					rs.close();
				}
				if( pstmt != null ) {
					pstmt.close();
				}
				if( conn != null ) {
					conn.close();
				}
			} catch( SQLException e ) {
				e.printStackTrace();
			}
		}
		return boardVo;
	}
	public List<BoardVo> getList() {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		List<BoardVo> list = new ArrayList<BoardVo>();
		
		try {
			conn = getConnection();
			stmt = conn.createStatement();

			String sql = "select a.no, title, content, hit, date_format(reg_date, '%Y-%m-%d') "+ 
					" from board a, user b "+ 
					" where a.user_no = b.no "+
					" order by a.group_no desc, a.order_no asc"; 
				
			rs = stmt.executeQuery( sql );
			
			while( rs.next() ) {
				Long no = rs.getLong( 1 );
				String title = rs.getString(2);
				String content = rs.getString(3);
				Long hit = rs.getLong(4);
				String regDate = rs.getString(5);
				BoardVo vo = new BoardVo();
				vo.setNo(no);
				vo.setTitle(title);
				vo.setHit(hit);
				vo.setContent(content);
				vo.setRegDate(regDate);
				
				list.add( vo );
			}
		} catch( SQLException e ) {
			e.printStackTrace();
		} finally {
			try {
				if( rs != null ) {
					rs.close();
				}
				if( stmt != null ) {
					stmt.close();
				}
				if( conn != null ) {
					conn.close();
				}
			} catch( SQLException e ) {
				e.printStackTrace();
			}
		}
		
		return list;
	}
	
	public boolean delete( BoardVo vo ) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = getConnection();
			
			String sql = "delete from guestbook where no = ? and passwd = ?";
			pstmt = conn.prepareStatement( sql );
			
			pstmt.setLong( 1, vo.getNo() );
			
			int count = pstmt.executeUpdate();
			
			return count == 1;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if( pstmt != null ) {
					pstmt.close();
				}
				
				if( conn != null ) {
					conn.close();
				}
			} catch( SQLException e ) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean update(Long no){
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = getConnection();
			
			String sql = "update board set hit=hit+1 where no=?";
			pstmt = conn.prepareStatement( sql );
			
			pstmt.setLong(1, no);
			
			int count = pstmt.executeUpdate();
			
			return count == 1;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if( pstmt != null ) {
					pstmt.close();
				}
				
				if( conn != null ) {
					conn.close();
				}
			} catch( SQLException e ) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean insert( BoardVo vo ) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = getConnection();
			
			String sql = "insert into board values( "+
								" null, ?, ?, "+
								" 0, now(), (select ifnull(max(group_no),0)+1 from board as a), ?, ?, ? )";
			//String sql1 = "insert into board values(null, ?, ?, 0, now(), (select ifnull(max(group_no),0)+1 from board a), ?, ?, ?)";
			pstmt = conn.prepareStatement( sql );
			
			//title, content, g_no, o_no, depth, u_no
			pstmt.setString( 1, vo.getTitle() );
			pstmt.setString( 2, vo.getContent());
			pstmt.setLong(3, vo.getOrderNo());
			pstmt.setLong(4, vo.getDepth());
			pstmt.setLong(5, vo.getUserNo());
			
			int count = pstmt.executeUpdate();
			
			return count == 1;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if( pstmt != null ) {
					pstmt.close();
				}
				
				if( conn != null ) {
					conn.close();
				}
			} catch( SQLException e ) {
				e.printStackTrace();
			}
		}
	}
	
	
	
}
