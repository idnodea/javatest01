package swing;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class WinEmp2 extends JFrame {
	private JTextField tf1, tf2, tf3, tf4, tf5, tf6, tf7, tf8;
	private JTextArea ta;
	private JButton bt1, bt2, bt3, bt4, bt5, bt6;
	private JLabel lbState;
	private Connection conn;
	private PreparedStatement pstmt;

	public WinEmp2() {
		String url = "jdbc:mysql://localhost:3306/firm";
		String id = "root";
		String pass = "mysql";

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(url, id, pass);

			// ... (기존 코드 유지)

			bt2.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					insert();
				}
			});

			// ... (기존 코드 유지)

			bt3.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					correction();
					clearTextField();
				}
			});

			// ... (기존 코드 유지)

			bt4.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					delete();
				}
			});
			bt5.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("현재 이름으로만 검색가능합니다.");
					select2();
				}
			});

			bt6.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					confirmExit();
				}
			});
			
			
			// ... (기존 코드 유지)

			initUI();
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void initUI() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container con = this.getContentPane();

		JLabel lb1 = new JLabel("직원코드:");
		JLabel lb2 = new JLabel("이름:");
		JLabel lb3 = new JLabel("직업:");
		JLabel lb4 = new JLabel("mgr:");
		JLabel lb5 = new JLabel("입사날짜:");
		JLabel lb6 = new JLabel("급여:");
		JLabel lb7 = new JLabel("comm:");
		JLabel lb8 = new JLabel("deptno:");

		con.setLayout(new BorderLayout());
		JPanel jp1 = new JPanel(new FlowLayout());
		jp1.add(bt1);
		jp1.add(bt2);
		jp1.add(bt3);
		jp1.add(bt4);
		jp1.add(bt5);
		jp1.add(bt6);

		con.add(jp1, BorderLayout.SOUTH);
		con.add(lbState);

		JScrollPane scroll = new JScrollPane(ta);
		JPanel jp2 = new JPanel(new FlowLayout());
		jp2.add(scroll);
		con.add(jp2, BorderLayout.CENTER);

		JPanel jp3 = new JPanel(new FlowLayout());
		con.add(jp3, BorderLayout.NORTH);
		jp3.add(lb1);
		jp3.add(tf1);
		jp3.add(lb2);
		jp3.add(tf2);
		jp3.add(lb3);
		jp3.add(tf3);
		jp3.add(lb4);
		jp3.add(tf4);
		jp3.add(lb5);
		jp3.add(tf5);
		jp3.add(lb6);
		jp3.add(tf6);
		jp3.add(lb7);
		jp3.add(tf7);
		jp3.add(lb8);
		jp3.add(tf8);

		JPanel jp4 = new JPanel(new FlowLayout());
		jp4.add(lbState);
		con.add(jp4, BorderLayout.SOUTH);

		this.setTitle("Emp 관리");
		this.setLocation(500, 400);
		this.setSize(900, 300);
		this.setVisible(true);
	}

	private void clearTextField() {
		tf1.setText("");
		tf2.setText("");
		tf3.setText("");
		tf4.setText("");
		tf5.setText("");
		tf6.setText("");
		tf7.setText("");
		tf8.setText("");
	}

	// 조회
	public void select() {
		String sql = "select empno, ename, job, mgr, hiredate, sal, comm, deptno from emp";
		try {
			ResultSet rs = conn.createStatement().executeQuery(sql);
			ta.setText("");
			while (rs.next()) {
				int empno = rs.getInt("empno");
				String ename = rs.getString("ename");
				String job = rs.getString("job");
				int mgr = rs.getInt("mgr");
				String hiredate = rs.getString("hiredate");
				int sal = rs.getInt("sal");
				int comm = rs.getInt("comm");
				int deptno = rs.getInt("deptno");
				String str = String.format("%d, %s, %s, %d, %s, %d, %d, %d\n", empno, ename, job, mgr, hiredate, sal,
						comm, deptno);
				ta.append(str);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 데이터 검색
	// 데이터 수정을 위해 검색된 결과를 가져오는 메서드
	// 데이터 검색
	// 데이터 수정을 위해 검색된 결과를 가져오는 메서드
	public void select2() {
	    String sql = "select empno, ename, job, mgr, hiredate, sal, comm, deptno from emp where ename = ?";
	    try {
	        pstmt = conn.prepareStatement(sql);
	        // SQL 쿼리를 만들고 검색 조건으로 텍스트 필드 tf2의 값을 사용
	        pstmt.setString(1, tf2.getText());

	        ResultSet rs = pstmt.executeQuery();
	        ta.setText("");

	        boolean found = false;  // 중복된 이름이 있을 때를 확인하는 변수

	        while (rs.next()) {
	            found = true;

	            int empno = rs.getInt("empno");
	            String ename = rs.getString("ename");
	            String job = rs.getString("job");
	            int mgr = rs.getInt("mgr");
	            String hiredate = rs.getString("hiredate");
	            int sal = rs.getInt("sal");
	            int comm = rs.getInt("comm");
	            int deptno = rs.getInt("deptno");

	            String str = String.format("%d, %s, %s, %d, %s, %d, %d, %d\n", empno, ename, job, mgr, hiredate, sal,
	                    comm, deptno);
	            ta.append(str);

	            // 검색된 결과를 텍스트 필드에 설정
	            tf1.setText(String.valueOf(empno));
	            tf2.setText(ename);
	            tf3.setText(job);
	            tf4.setText(String.valueOf(mgr));
	            tf5.setText(hiredate);
	            tf6.setText(String.valueOf(sal));
	            tf7.setText(String.valueOf(comm));
	            tf8.setText(String.valueOf(deptno));
	        }

	        if (!found) {
	            clearTextField();
	            ta.append("현재 이름으로만 검색가능합니다.\n");
	            ta.append("해당 자료 없습니다.");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	// 신규데이터 입력
	public void insert() {
		if (hasData()) {
			String sql = "insert into emp(empno, ename, job, mgr, hiredate, sal, comm, deptno) values(?, ?, ?, ?, ?, ?, ?, ?)";
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, Integer.parseInt(tf1.getText()));
				pstmt.setString(2, tf2.getText());
				pstmt.setString(3, tf3.getText());
				pstmt.setInt(4, Integer.parseInt(tf4.getText()));
				pstmt.setString(5, tf5.getText());
				pstmt.setInt(6, Integer.parseInt(tf6.getText()));
				pstmt.setInt(7, Integer.parseInt(tf7.getText()));
				pstmt.setInt(8, Integer.parseInt(tf8.getText()));

				int res = pstmt.executeUpdate();

				if (res > 0) {
					ta.append("데이터가 성공적으로 추가되었습니다.\n");
				} else {
					ta.append("데이터 추가에 실패했습니다.\n");
				}
			} catch (SQLException e) {
				ta.append("데이터 추가에 실패했습니다. 오류: " + e.getMessage() + "\n");
			}
		} else {
			ta.append("빈 입력 필드가 있습니다. 모든 필드를 채우세요.\n");
		}
	}

	// ... (기존 코드 유지)
	// 데이터수정
	public void correction() {
		String empnoText = tf1.getText().trim();
		if (!empnoText.isEmpty()) {
			try {
				pstmt = conn.prepareStatement(
						"update emp set ename = ?, job = ?, mgr = ?, hiredate = ?, sal = ?, comm = ?, deptno = ? where empno = ?");
				pstmt.setString(1, tf2.getText());
				pstmt.setString(2, tf3.getText());
				pstmt.setInt(3, Integer.parseInt(tf4.getText()));
				pstmt.setString(4, tf5.getText());
				pstmt.setInt(5, Integer.parseInt(tf6.getText()));
				pstmt.setInt(6, Integer.parseInt(tf7.getText()));
				pstmt.setInt(7, Integer.parseInt(tf8.getText()));
				pstmt.setInt(8, Integer.parseInt(empnoText));

				int result = pstmt.executeUpdate();

				if (result > 0) {
					ta.append("데이터가 성공적으로 변경되었습니다.\n");
					clearTextField();
					select();
				} else {
					ta.append("변경할 데이터가 없습니다.\n");
				}
			} catch (NumberFormatException e) {
				ta.append("잘못된 숫자 형식입니다. 정수 값을 입력하세요.\n");
			} catch (SQLException e) {
				ta.append("데이터 수정에 실패했습니다. 오류: " + e.getMessage() + "\n");
			} finally {
				closeStatement();
			}
		} else {
			ta.append("직원 코드를 입력하세요.\n");
		}
	}

	// ... (기존 코드 유지)

	public void delete() {
		String empnoText = tf1.getText().trim();
		if (!empnoText.isEmpty()) {
			try {
				pstmt = conn.prepareStatement("delete from emp where empno = ?");
				pstmt.setInt(1, Integer.parseInt(empnoText));

				int result = pstmt.executeUpdate();

				if (result > 0) {
					ta.append("데이터가 성공적으로 삭제되었습니다.\n");
					clearTextField();
					select();
				} else {
					ta.append("삭제할 데이터가 없습니다.\n");
				}
			} catch (NumberFormatException e) {
				ta.append("잘못된 숫자 형식입니다. 정수 값을 입력하세요.\n");
			} catch (SQLException e) {
				ta.append("데이터 삭제에 실패했습니다. 오류: " + e.getMessage() + "\n");
			} finally {
				closeStatement();
			}
		} else {
			ta.append("직원 코드를 입력하세요.\n");
		}
	}

	// ... (기존 코드 유지)

	private void closeStatement() {
		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// 종료 버튼을 누르기 전, 한 번 더 확인가능하게끔
	public void confirmExit() {
		int answer = JOptionPane.showConfirmDialog(this, "종료하시겠습니까?", "확인", JOptionPane.YES_NO_OPTION);
		if (answer == JOptionPane.YES_OPTION) {
			System.out.println("프로그램을 종료합니다.");
			System.exit(0);
		} else {
			System.out.println("종료를 취소합니다");
			lbState.setText("종료를 취소합니다");
		}
	}

	// 입력 필드가 비어 있는지 확인
	// 수정된 hasData() 메서드
	private boolean hasData() {
		return !tf1.getText().isEmpty() || !tf2.getText().isEmpty() || !tf3.getText().isEmpty()
				|| !tf4.getText().isEmpty() || !tf5.getText().isEmpty() || !tf6.getText().isEmpty()
				|| !tf7.getText().isEmpty() || !tf8.getText().isEmpty();
	}

	public static void main(String[] args) {
		new WinEmp();
	}
}