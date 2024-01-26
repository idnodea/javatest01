package swing;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class WinEmp extends JFrame {
//	JLabel lb1 = new JLabel("부서코드:");
	JTextField tf1 = new JTextField(5);
//	JLabel lb2 = new JLabel("부서명:");
	JTextField tf2 = new JTextField(5);
//	JLabel lb3 = new JLabel("부서위치:");
	JTextField tf3 = new JTextField(5);
	
	JButton bt1 = new JButton("전체 내용 보기");
	JButton bt2 = new JButton("신규");
	JButton bt3 = new JButton("수정");
	JButton bt4 = new JButton("삭제");
	JButton bt5 = new JButton("검색");
	
	JButton bt6 = new JButton("종료");
	JTextArea ta = new JTextArea(10, 40);
	
	Connection conn;  //트라이캐치 이후 이 부분 분리
	Statement stmt;	  //트라이캐치 이후 이 부분 분리
	
	
	 int empno;
     String ename;
     String job;
     
     JLabel lbState = new JLabel("메세지를 보여 줍니다");
	
	public WinEmp()  {
		//
		// MySQL 연결 정보 설정
				String url = "jdbc:mysql://localhost:3306/firm";
				String id = "root";
				String pass = "mysql";

				// 복사붙여넣은 이후, try캐치구문 만든 뒤, 몰아넣기
				try {
					Class.forName("com.mysql.cj.jdbc.Driver");
					conn = DriverManager.getConnection(url, id, pass);
					stmt = conn.createStatement();
					
				} catch (SQLException | ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				

				// "emp" 테이블에서 모든 데이터를 조회하는 SQL 실행
//				ResultSet rs = stmt.executeQuery("select * from emp");
		
		
		//
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container con = this.getContentPane();
		
		//라벨을 지역변수로
		JLabel lb1 = new JLabel("직원코드:");
		JLabel lb2 = new JLabel("이름:");
		JLabel lb3 = new JLabel("직업:");
		
		//버튼
		con.setLayout(new BorderLayout()); //플로우에서보더로 변경
		JPanel jp1 = new JPanel(new FlowLayout());
		jp1.add(bt1);
		jp1.add(bt2);
		jp1.add(bt3);
		jp1.add(bt4);
		jp1.add(bt5);
		jp1.add(bt6);
		
		con.add(jp1,BorderLayout.SOUTH);
		
		//종료메세지
		con.add(lbState);
		
		//스크롤바
		JScrollPane scroll = new JScrollPane(ta);
		JPanel jp2 = new JPanel(new FlowLayout());
		jp2.add(scroll);
		con.add(jp2, BorderLayout.CENTER);
		
		//윗라벨
		JPanel jp3 = new JPanel(new FlowLayout());
		con.add(jp3,BorderLayout.NORTH);
		jp3.add(lb1);jp3.add(tf1);
		jp3.add(lb2);jp3.add(tf2);
		jp3.add(lb3);jp3.add(tf3);
		this.setTitle("Emp 관리");
		
//		con.add(bt1, BorderLayout.EAST);
//		con.add(bt2, BorderLayout.WEST);
//		con.add(bt3, BorderLayout.SOUTH);
//		con.add(bt4, BorderLayout.NORTH);
//		con.add(bt5, BorderLayout.CENTER);
		
		this.setLocation(500, 400);
		this.setSize(500, 300);
		this.setVisible(true);
		
		bt1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				select();
			}
		});
		
		bt2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				insert();
				clearTextField();
				select();
			}
		});
		
		bt3.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				clearTextField();
				correction();
			}
		});
		bt5.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				select2();
				
			}
		});
		bt6.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        confirmExit();
		    }
		});
	}
	
	private void clearTextField() {
		tf1.setText(""); tf2.setText(""); tf3.setText("");
	}
	
	public void select() {
		String sql = "select empno,ename,job from emp";
		try {
			ResultSet rs = stmt.executeQuery(sql);
			ta.setText("");
			while(rs.next()) {
				int empno = rs.getInt("empno");
				String ename = rs.getString("ename");
				String job = rs.getString("job");
				String str = String.format("%d, %s, %s\n",
						empno, ename, job);
				ta.append(str);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	}
	
	public void select2() {
	    String sql = String.format("select empno, ename, job from emp where ename = '%s'", tf2.getText());
	    
	    try {
	        ResultSet rs = stmt.executeQuery(sql);
	        ta.setText("");
	        
	        if (rs.next()) {
	            int empno = rs.getInt("empno");
	            String ename = rs.getString("ename");
	            String job = rs.getString("job");
	            
	            String str = String.format("%d, %s, %s\n", empno, ename, job);
	            ta.append(str);
	            
	            tf1.setText(String.valueOf(empno));
	            tf3.setText(job);
	        } else {
	            tf1.setText("");
	            tf2.setText("");
	            tf3.setText("");
	            ta.append("해당 자료 없습니다.");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public void insert() {
		String sql = String.format(
				"insert into emp values(%s, '%s', '%s')",
				tf1.getText(),tf2.getText(), tf3.getText());
		try {
			int res = stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//데이터 수정
	public void correction() {
	    // empno, ename, job 값을 가져와서 SQL 문에 사용
	    int empno = Integer.parseInt(tf1.getText());
	    String ename = tf2.getText();
	    String job = tf3.getText();

	    String sql = "update emp set ename = '" + ename + "', job = '" + job + "' where empno = " + empno;
	    
	    try {
	        int result = stmt.executeUpdate(sql);

	        if (result > 0) {
	            System.out.println("데이터가 성공적으로 변경되었습니다.");
	        } else {
	            System.out.println("변경할 데이터가 없습니다.");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	//데이터 삭제

	public void delete() {
	    // empno, ename, job 값을 가져와서 SQL 문에 사용
	    int empno = Integer.parseInt(tf1.getText());
	    String ename = tf2.getText();
	    String job = tf3.getText();

	    String sql = "delete from emp where empno = " + empno + " and ename = '" + ename + "' and job = '" + job + "'";
	    
	    try {
	        int result = stmt.executeUpdate(sql);

	        if (result > 0) {
	            System.out.println("데이터가 성공적으로 삭제되었습니다.");
	        } else {
	            System.out.println("삭제할 데이터가 없습니다.");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	
	//종료
	
	public void confirmExit() {
		int answer = JOptionPane.showConfirmDialog(this, "종료하시겠습니까?",
													"확인",JOptionPane.
													YES_NO_OPTION);
		if(answer == JOptionPane.YES_OPTION) { //사용자가 yes를 눌렀을 경우
			System.out.println("프로그램을 종료합니다.");
			//파일로 객체를 보내는 일 처리
			System.exit(0);
		}else { //사용자가 yes외의 값을 눌렀을 경우
			System.out.println("종료를 취소합니다");
			lbState.setText("종료를 취소합니다");
		}
	}
	
	
	public static void main(String[] args) {
		new WinEmp();
	}
}