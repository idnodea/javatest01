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

public class WinEmp extends JFrame {
	JTextField tf1 = new JTextField(5);
	JTextField tf2 = new JTextField(5);
	JTextField tf3 = new JTextField(5);
	JTextField tf4 = new JTextField(5);
	JTextField tf5 = new JTextField(5);
	JTextField tf6 = new JTextField(5);
	JTextField tf7 = new JTextField(5);
	JTextField tf8 = new JTextField(5);

	JButton bt1 = new JButton("전체 내용 보기");
	JButton bt2 = new JButton("신규");
	JButton bt3 = new JButton("수정");
	JButton bt4 = new JButton("삭제");
	JButton bt5 = new JButton("검색");
	JButton bt6 = new JButton("종료");

	JTextArea ta = new JTextArea(10, 40);

	Connection conn;
	PreparedStatement pstmt;

	JLabel lbState = new JLabel("메세지를 보여 줍니다");

	public WinEmp() {
		String url = "jdbc:mysql://localhost:3306/firm";
		String id = "root";
		String pass = "mysql";

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(url, id, pass);

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
//					select();
				}
			});

			bt3.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("의도치 않은 수정이 일어날 수 있어, 검색된 자료만 수정가능합니다.");
					correction(); // 검색을 통한, 모든 데이터가 입력된 상태에서 수정을 해야
									// 따라서 클리어텍스트필드를 뒤로
					clearTextField();
				}
			});

			bt4.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("의도치 않은 삭제가 일어날 수 있어, 검색된 자료만 삭제가능합니다.");
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

			initUI();
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	// 뼈대
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

	// 데이터보기
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
	public void select2() {
		String sql = "select empno, ename, job, mgr, hiredate, sal, comm, deptno from emp where ename = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			// SQL 쿼리를 만들고 검색 조건으로 텍스트 필드 tf2의 값을 사용
			pstmt.setString(1, tf2.getText());

			ResultSet rs = pstmt.executeQuery();
			ta.setText("");

			if (rs.next()) {
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
			} else {
				clearTextField();
				ta.append("현재 이름으로만 검색가능합니다.\n");
				ta.append("해당 자료 없습니다.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

//    //신규데이터 입력
//    public void insert() {
//        String sql = "insert into emp(empno, ename, job, mgr, hiredate, sal, comm, deptno) values(?, ?, ?, ?, ?, ?, ?, ?)";
//        try {
//            pstmt = conn.prepareStatement(sql);
//            pstmt.setInt(1, Integer.parseInt(tf1.getText()));
//            pstmt.setString(2, tf2.getText());
//            pstmt.setString(3, tf3.getText());
//            pstmt.setInt(4, Integer.parseInt(tf4.getText()));
//            pstmt.setString(5, tf5.getText());
//            pstmt.setInt(6, Integer.parseInt(tf6.getText()));
//            pstmt.setInt(7, Integer.parseInt(tf7.getText()));
//            pstmt.setInt(8, Integer.parseInt(tf8.getText()));
//
//            int res = pstmt.executeUpdate();
//
//            if (res > 0) {
//                System.out.println("데이터가 성공적으로 추가되었습니다.");
//            } else {
//                System.out.println("데이터 추가에 실패했습니다.");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//	// 신규데이터 입력
//	public void insert() {
//		if (!isEmptyFields()) {
//			String sql = "insert into emp(empno, ename, job, mgr, hiredate, sal, comm, deptno) values(?, ?, ?, ?, ?, ?, ?, ?)";
//			try {
//				pstmt = conn.prepareStatement(sql);
//				pstmt.setInt(1, Integer.parseInt(tf1.getText()));
//				pstmt.setString(2, tf2.getText());
//				pstmt.setString(3, tf3.getText());
//				pstmt.setInt(4, Integer.parseInt(tf4.getText()));
//				pstmt.setString(5, tf5.getText());
//				pstmt.setInt(6, Integer.parseInt(tf6.getText()));
//				pstmt.setInt(7, Integer.parseInt(tf7.getText()));
//				pstmt.setInt(8, Integer.parseInt(tf8.getText()));
//
//				int res = pstmt.executeUpdate();
//
//				if (res > 0) {
//					System.out.println("데이터가 성공적으로 추가되었습니다.");
//				} else {
//					System.out.println("데이터 추가에 실패했습니다.");
//				}
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		} else {
//			System.out.println("빈 입력 필드가 있습니다. 모든 필드를 채우세요.");
//			
//		}
//	}
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

	            if (res > 0) {   //어떤 값을 쓰든 0은 아닐거고 0보다 큰 값이니
	            				//굳이 트루 펄스로 변환안해도 될 것 같아서
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
	
////    //데이터 수정
//    public void correction() {
//        String sql = "update emp set ename = ?, job = ?, mgr = ?, hiredate = ?, sal = ?, comm = ?, deptno = ? where empno = ?";
//        try {
//            pstmt = conn.prepareStatement(sql);
//            pstmt.setString(1, tf2.getText());
//            pstmt.setString(2, tf3.getText());
//            pstmt.setInt(3, Integer.parseInt(tf4.getText()));
//            pstmt.setString(4, tf5.getText());
//            pstmt.setInt(5, Integer.parseInt(tf6.getText()));
//            pstmt.setInt(6, Integer.parseInt(tf7.getText()));
//            pstmt.setInt(7, Integer.parseInt(tf8.getText()));
//            pstmt.setInt(8, Integer.parseInt(tf1.getText()));  // Ensure this is the correct order
//
//            int result = pstmt.executeUpdate();
//
//            if (result > 0) {
//                System.out.println("데이터가 성공적으로 변경되었습니다.");
//            } else {
//                System.out.println("변경할 데이터가 없습니다.");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

// // 데이터 수정
//    public void correction() {
//        String empnoText = tf1.getText().trim();  // 공백을 제거한 후에 사용
//        if (!empnoText.isEmpty()) {
//            try {
//                pstmt = conn.prepareStatement("update emp set ename = ?, job = ?, mgr = ?, hiredate = ?, sal = ?, comm = ?, deptno = ? where empno = ?");
//                pstmt.setString(1, tf2.getText());
//                pstmt.setString(2, tf3.getText());
//                pstmt.setInt(3, Integer.parseInt(tf4.getText()));
//                pstmt.setString(4, tf5.getText());
//                pstmt.setInt(5, Integer.parseInt(tf6.getText()));
//                pstmt.setInt(6, Integer.parseInt(tf7.getText()));
//                pstmt.setInt(7, Integer.parseInt(tf8.getText()));
//                pstmt.setInt(8, Integer.parseInt(empnoText));  // 수정 대상의 직원 번호
//
//                int result = pstmt.executeUpdate();
//
//                if (result > 0) {
//                    System.out.println("데이터가 성공적으로 변경되었습니다.");
//                    clearTextField();
//                    select();  // 수정 후 전체 내용 보기
//                } else {
//                    System.out.println("변경할 데이터가 없습니다.");
//                }
//            } catch (NumberFormatException e) {
//                System.out.println("잘못된 숫자 형식입니다. 정수 값을 입력하세요.");
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        } else {
//            System.out.println("직원 번호가 비어 있습니다.");
//        }
//    }
//    
	// 데이터 수정
//    public void correction() {
//        String empnoText = tf1.getText().trim();  // 공백을 제거한 후에 사용
//        System.out.println(empnoText);
//        if (!empnoText.isEmpty()) {
//            try {
//                pstmt = conn.prepareStatement("update emp set ename = ?, job = ?, mgr = ?, hiredate = ?, sal = ?, comm = ?, deptno = ? where empno = ?");
//                pstmt.setString(1, tf2.getText());
//                pstmt.setString(2, tf3.getText());
//                pstmt.setInt(3, Integer.parseInt(tf4.getText()));
//                pstmt.setString(4, tf5.getText());
//                pstmt.setInt(5, Integer.parseInt(tf6.getText()));
//                pstmt.setInt(6, Integer.parseInt(tf7.getText()));
//                pstmt.setInt(7, Integer.parseInt(tf8.getText()));
//                pstmt.setInt(8, Integer.parseInt(empnoText));  // 수정 대상의 직원 번호
//
//                int result = pstmt.executeUpdate();
//
//                if (result > 0) {
//                    System.out.println("데이터가 성공적으로 변경되었습니다.");
//                    clearTextField();
//                    select();  // 수정 후 전체 내용 보기
//                } else {
//                    System.out.println("변경할 데이터가 없습니다.");
//                }
//            } catch (NumberFormatException e) {
//                System.out.println("잘못된 숫자 형식입니다. 정수 값을 입력하세요.");
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        } else {
//            System.out.println("직원 번호가 비어 있습니다.");
//        }
//    }

	// 데이터 수정
	public void correction() {
	    String empnoText = tf1.getText().trim(); // 공백을 제거한 후에 사용
	    if (hasData()) {
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
	            pstmt.setInt(8, Integer.parseInt(empnoText)); // 수정 대상의 직원 번호

	            int result = pstmt.executeUpdate();

	            if (result > 0) {
	                ta.append("데이터가 성공적으로 변경되었습니다.\n");
	                clearTextField();
	                select(); // 수정 후 전체 내용 보기
	            } else {
	                ta.append("변경할 데이터가 없습니다.\n");
	            }
	        } catch (NumberFormatException e) {
	            ta.append("잘못된 숫자 형식입니다. 정수 값을 입력하세요.\n");
	        } catch (SQLException e) {
	            ta.append("데이터 수정에 실패했습니다. 오류: " + e.getMessage() + "\n");
	        }
	    } else {
	        ta.append("직원 번호가 비어 있습니다. 수정 대상을 선택하세요.\n");
	    }
	}
//	pstmt.setString(1, tf2.getText());에서
//	첫 번째 매개변수인 1은 SQL 쿼리의 물음표(placeholder) 위치를 나타냅니다.
//	JDBC에서는 물음표가 1부터 시작하는 인덱스를 갖습니다.
//	만약 이름으로 검색하는 경우라면 tf2.getText()의 값을
//	첫 번째 물음표 위치에 대입하게 됩니다. 
//	그렇기 때문에 pstmt.setString(1, tf2.getText());에서 
//	1은 첫 번째 물음표를 의미합니다.
//	만약 직원번호를 기준으로 검색하는 경우라면 
//	pstmt.setInt(1, Integer.parseInt(tf1.getText()));와 같이
//	tf1에 해당하는 값을 첫 번째 물음표에 대입했을 것입니다.
	
	
// // 전체 내용 삭제
//    public void delete() {
//        String sql = "delete from emp where empno = ?";
//        try {
//            pstmt = conn.prepareStatement(sql);
//            pstmt.setInt(1, Integer.parseInt(tf1.getText()));
//
//            int result = pstmt.executeUpdate();
//
//            if (result > 0) {
//                System.out.println("데이터가 성공적으로 삭제되었습니다.");
//                clearTextField();
//                select();  // 삭제 후 전체 내용 보기
//            } else {
//                System.out.println("삭제할 데이터가 없습니다.");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//	 전체 내용 삭제
//	public void delete() {
//		String empnoText = tf1.getText().trim(); // 공백을 제거한 후에 사용
//		if (!empnoText.isEmpty()) {
//			String sql = "delete from emp where empno = ?";
//			try {
//				pstmt = conn.prepareStatement(sql);
//				pstmt.setInt(1, Integer.parseInt(empnoText));
//
//				int result = pstmt.executeUpdate();
//
//				if (result > 0) {
//					System.out.println("데이터가 성공적으로 삭제되었습니다.");
//					clearTextField();
//					select(); // 삭제 후 전체 내용 보기
//				} else {
//					System.out.println("삭제할 데이터가 없습니다.");
//				}
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		} else {
//			System.out.println("직원 번호가 비어 있습니다. 삭제 대상을 선택하세요.");
//		}
//	}
//	
	// 전체 내용 삭제
	public void delete() {
	    String empnoText = tf1.getText().trim(); //trim없어도 되지만,정확도
	    if (hasData()) {
	        String sql = "delete from emp where empno = ?";
	        try {
	            pstmt = conn.prepareStatement(sql);
	            pstmt.setInt(1, Integer.parseInt(empnoText));

	            int result = pstmt.executeUpdate();

	            if (result > 0) {
	                ta.append("데이터가 성공적으로 삭제되었습니다.\n");
	            } else {
	                ta.append("삭제할 데이터가 없습니다.\n");
	            }
	        } catch (SQLException e) {
	            ta.append("데이터 삭제에 실패했습니다. 오류: " + e.getMessage() + "\n");
	        }
	    } else {
	        ta.append("직원 번호가 비어 있습니다. 삭제 대상을 선택하세요.\n");
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
	    return  !tf1.getText().isEmpty() || !tf2.getText().isEmpty() ||
	    		!tf3.getText().isEmpty() || !tf4.getText().isEmpty() ||
	    		!tf5.getText().isEmpty() || !tf6.getText().isEmpty() ||
	    		!tf7.getText().isEmpty() || !tf8.getText().isEmpty();
	}
	/*
	해당 메서드는 hasData라는 이름의 boolean 형태의 메서드입니다. 
	이 메서드는 여러 개의 텍스트 필드(tf1부터 tf8까지) 중 어느 하나라도 비어있지 않으면
	 true를 반환하고, 모든 텍스트 필드가 비어있다면 false를 반환합니다. 
	 여기서 tf1, tf2, ..., tf8은 텍스트 필드 객체로 가정되며, 
	 각 텍스트 필드의 getText() 메서드를 통해 해당 필드의 텍스트를 가져오고, 
	 isEmpty() 메서드를 사용하여 해당 텍스트가 비어 있는지를 확인합니다.
	*/
	
	
	
	
//	private boolean hasData() {
//	    return tf1.getText().isNotEmpty() || tf2.getText().isNotEmpty() || tf3.getText().isNotEmpty()
//	            || tf4.getText().isNotEmpty() || tf5.getText().isNotEmpty() || tf6.getText().isNotEmpty()
//	            || tf7.getText().isNotEmpty() || tf8.getText().isNotEmpty();
//	}
	
	
	
	
	
	
	public static void main(String[] args) {
		new WinEmp();
	}
}