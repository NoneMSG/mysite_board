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
import com.jx372.mysite.vo.UserVo;

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
	
	public void increaseGroupOrder(Integer gNo, Integer oNo){
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = getConnection();
			
			String sql = "update board set order_no = order_no + 1 where group_no = ? and order_no > ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, gNo );
			pstmt.setInt(2, oNo );
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println( "error:" + e );
		} finally {
			try {
				if( pstmt != null ) {
					pstmt.close();
				}
				if( conn != null ) {
					conn.close();
				}
			} catch ( SQLException e ) {
				System.out.println( "error:" + e );
			}  
		}
	}
	
	public BoardVo get(Long no){
		Connection conn = null;
		PreparedStatement pstmt=null;
		BoardVo boardVo=null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			String sql = "select no, title, content, group_no, order_no, depth, user_no from board where no=?";
			
			pstmt = conn.prepareStatement( sql );
			pstmt.setLong( 1, no );
			
			rs = pstmt.executeQuery();
			
			if( rs.next() ) {
				boardVo = new BoardVo();
				boardVo.setNo(rs.getLong(1));
				boardVo.setTitle( rs.getString( 2 ) );
				boardVo.setContent( rs.getString( 3 ) );
				boardVo.setGroupNo(rs.getInt(4));
				boardVo.setOrderNo(rs.getInt(5));
				boardVo.setDepth(rs.getInt(6));
				boardVo.setUserNo(rs.getLong(6));
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
	
	public BoardVo getList(Long no){
		Connection conn = null;
		PreparedStatement pstmt=null;
		BoardVo boardVo=null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			String sql = "select title, content from board where no=?";
			pstmt = conn.prepareStatement( sql );
			
			pstmt.setLong( 1, no );
			
			rs = pstmt.executeQuery();
			if( rs.next() ) {
				boardVo = new BoardVo();
				boardVo.setTitle( rs.getString( 1 ) );
				boardVo.setContent( rs.getString( 2 ) );
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

			String sql = "select a.no, title, content, hit, date_format(reg_date, '%Y-%m-%d'), b.name, a.user_no, a.depth "+ 
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
				String userName = rs.getString(6);
				Long userNo = rs.getLong(7);
				Integer depth = rs.getInt(8);
				BoardVo vo = new BoardVo();
				vo.setNo(no);
				vo.setTitle(title);
				vo.setHit(hit);
				vo.setContent(content);
				vo.setRegDate(regDate);
				vo.setUserName(userName);
				vo.setUserNo(userNo);
				vo.setDepth(depth);
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
			
			String sql = "delete from board where no = ? and user_no = ?";
			pstmt = conn.prepareStatement( sql );
			
			pstmt.setLong( 1, vo.getNo() );
			pstmt.setLong( 2,  vo.getUserNo());
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
	
	public boolean modify(BoardVo vo){
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = getConnection();
			
			String sql = "update board set title=?, content=? "+
						" where no=? and user_no=? ";
			pstmt = conn.prepareStatement( sql );
			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getContent());
			pstmt.setLong(3, vo.getNo());
			pstmt.setLong(4, vo.getUserNo());
			
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
			System.out.println("before if(vo.getGroupNo()==0)");
			if( vo.getGroupNo() == 0 ) { // 새글
				String sql = 
					" insert" +
					"  into board" +
					" values (null, ?, ?, 0, now(), (select ifnull(max(group_no), 0) + 1 from board as a), 1, 0, ?)";

				pstmt = conn.prepareStatement( sql );
				
				pstmt.setString( 1, vo.getTitle() );
				pstmt.setString( 2, vo.getContent() );
				pstmt.setLong( 3, vo.getUserNo() );
			} else { // 답글
				String sql = 
						" insert" +
						"   into board" +
						" values (null, ?, ?, 0, now(), ?, ?, ?, ?)";

				pstmt = conn.prepareStatement( sql );
				
				pstmt.setString( 1, vo.getTitle() );
				pstmt.setString( 2, vo.getContent() );
				pstmt.setInt( 3, vo.getGroupNo() );
				pstmt.setInt( 4, vo.getOrderNo() );
				pstmt.setInt( 5, vo.getDepth() );
				pstmt.setLong( 6, vo.getUserNo() );
			}
			System.out.println("after if(vo.getGroupNo()==0)");
			int count = pstmt.executeUpdate();
			return count == 1;
		} catch( SQLException e ) {
			e.printStackTrace();
		} finally {
			try {
				if( pstmt != null ) {
					pstmt.close();
				}
				if( conn != null ) {
					conn.close();
				}
			}catch( SQLException e ) {
				e.printStackTrace();
			} 
		}
		
		return false;
	}
	
	public int getTotalCount( String keyword ) {
		int totalCount = 0;

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			if( "".equals( keyword ) ) {
				String sql = "select count(*) from board";
				pstmt = conn.prepareStatement(sql);
			} else { 
				String sql =
					"select count(*)" +
					"  from board" +
					" where title like ? or content like ?";
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setString(1, "%" + keyword + "%");
				pstmt.setString(2, "%" + keyword + "%");
			}
			rs = pstmt.executeQuery();
			if( rs.next() ) {
				totalCount = rs.getInt( 1 );
			}
		} catch (SQLException e) {
			System.out.println( "error:" + e );
		} finally {
			try {
				if( rs != null ) {
					rs.close();
				}
				if( pstmt != null ) {
					pstmt.close();
				}
				if( conn != null ) {
					conn.close();
				}
			} catch ( SQLException e ) {
				System.out.println( "error:" + e );
			}  
		}
		
		return totalCount;
	}
	
	
	
}
