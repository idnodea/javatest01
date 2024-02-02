package swing;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class WinEmp3 extends JFrame{
	//필드선언
	JTextField tf1 = new JTextField(5);
	JTextField tf2 = new JTextField(5);
	JTextField tf3 = new JTextField(5);
	JTextField tf4 = new JTextField(5);
	JTextField tf5 = new JTextField(5);
	JTextField tf6 = new JTextField(5);
	JTextField tf7 = new JTextField(5);
	JTextField tf8 = new JTextField(5);
	JTextField tf9 = new JTextField(5);
	
	JButton bt1 = new JButton("조회");
	JButton bt2 = new JButton("신규");
	JButton bt3 = new JButton("수정");
	JButton bt4 = new JButton("삭제");
	JButton bt5 = new JButton("검색");
	JButton bt6 = new JButton("종료");
	
	JTextArea ta = new JTextArea(10,40);
	JLabel lbState = new JLabel("메세지를 보여줍니다");
	
	//연결 ->생성자로 이동
//	Class.forName("com.mysql.cj.jdbc.Driver");
//	String url="jdbc:mysql://localhost:3306/firm";
//	String id = "root";
//	String pass = "mysql";
	
	//트라이캐치로 이동, 필드로 변경
//	Connection conn = DriverManager.getConnection(url,id,pass); 
//	PreparedStatement pstmt = conn.prepareStatement(sql);
	Connection conn;
	PreparedStatement pstmt;
	
//	pstmt.executeQuery();
	
	
	//생성자
	public WinEmp3() {
		
		String url="jdbc:mysql://localhost:3306/firm";
		String id = "root";
		String pass = "mysql";
		
		
		//오류를 없애고자 트라이캐치로 바꿔준 후, 중복되는 트라이캐치대신 하나로
		//이후, 트라이캐치에서 3번째 추가
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(url,id,pass);
			//뼈대
			initUI();
			
			//트라이 안으로 넣어줌
			//버튼 실행시  
			bt1.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					
					
				}
			});
			
			bt2.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					
					
				}
			});
			
			bt3.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					
					
				}
			});
			
			bt4.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					
					
				}
			});
			
			bt5.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					
					
				}
			});
			
			bt6.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					
					
				}
			});
			
			
		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
			
		
	}
	
	//뼈대
	private void initUI() {
		//틀을 잡아줄 컨테이너와, x키만 눌러도 시스템이 종료되게끔 배려
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container container = this.getContentPane();
	}
	
	
	public static void main(String[] args) {
		
	}

}
