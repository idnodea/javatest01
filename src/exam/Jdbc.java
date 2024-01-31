package exam;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Jdbc {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/firm";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "mysql";
    private static Scanner scanner = new Scanner(System.in);
    private static Connection conn = null;

    private int empno;
    private String ename;
    private String job;
    private int mgr;       
    private int hiredate;   
    private int sal;
    private int comm;
    private int deptno;

    //유징필드
    public Jdbc(int empno, String ename, String job, int mgr, int hiredate, int sal, int comm, int deptno) {
		super();
		this.empno = empno;
		this.ename = ename;
		this.job = job;
		this.mgr = mgr;
		this.hiredate = hiredate;
		this.sal = sal;
		this.comm = comm;
		this.deptno = deptno;
	}

	public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            boolean exit = false;

            while (!exit) {
                System.out.println("1. 데이터 보기");
                System.out.println("2. 데이터 삽입");
                System.out.println("3. 데이터 수정");
                System.out.println("4. 데이터 삭제");
                System.out.println("5. 종료");
                System.out.print("선택하세요: ");

                String choice = scanner.nextLine();

                switch (choice) {
                    case "1":
                        viewData();
                        break;
                    case "2":
                        insertData();
                        break;
                    case "3": 
                    	correctionData();
                    	break;
                    case "4": 
                    	deleteData();
                        break;    
                    case "5":
                        exit = true;
                        break;
                    default:
                        System.out.println("유효하지 않은 선택. 다시 시도하세요.");
                        break;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("드라이버 클래스를 찾을 수 없습니다.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("데이터베이스 연결에 실패하였습니다.");
        } finally {
            closeResources();
        }
    }

    private static void viewData() {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from emp");

            // 조회된 데이터 출력
            while (rs.next()) {
                System.out.print(rs.getInt("empno") + "\t");
                System.out.print(rs.getString("ename") + "\t");
                System.out.print(rs.getString("job") + "\t");
                System.out.print(rs.getInt("mgr") + "\t");
                System.out.print(rs.getDate("hiredate") + "\t");
                System.out.print(rs.getInt("sal") + "\t");
                System.out.print(rs.getInt("comm") + "\t");
                System.out.println(rs.getInt("deptno"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("데이터 조회에 실패하였습니다.");
        }
    }

    private static void insertData() {
        try {
            // 스캐너
            Scanner scanner = new Scanner(System.in);

            System.out.print("부서 번호 입력: ");
            int empno = scanner.nextInt();
            scanner.nextLine(); // Enter 처리

            System.out.print("부서 이름 입력: ");
            String ename = scanner.nextLine();

            System.out.print("직업 이름 입력: ");
            String job = scanner.nextLine();
            
            System.out.print("mgr 입력: ");
            int mgr = scanner.nextInt();
            
            System.out.print("부서 입사날짜 입력: ");
            int hiredate = scanner.nextInt();
            
            System.out.print("급여 입력: ");
            int sal = scanner.nextInt();
            
            System.out.print("comm 입력: ");
            int comm = scanner.nextInt();
            
            System.out.print("deptno번호 입력: ");
            int deptno = scanner.nextInt();

            // 데이터 삽입 로직 작성
            Statement stmt;
            String sql = "insert into emp(empno, ename, job, mgr, hiredate, sal, comm, deptno) "
                    + "values (" + empno + ", '" + ename + "', '" + job + "', "+ mgr +", "+ hiredate +","+ sal +","+ comm +","+ deptno +")";
            try {
            	stmt = conn.createStatement();
                int result = stmt.executeUpdate(sql);

                if (result > 0) {
                    System.out.println("데이터가 성공적으로 삽입되었습니다.");
                } else {
                    System.out.println("데이터 삽입에 실패했습니다.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("데이터 삽입 중 오류가 발생하였습니다.");
            }
        } finally {
        	
		}
    }

    private static void correctionData() {
        Scanner scanner = new Scanner(System.in);

        try {
            // 스캐너
            System.out.print("부서 번호 입력: ");
            int empno = scanner.nextInt();
            scanner.nextLine(); // Enter 처리

            System.out.print("변경할 부서 이름 입력: ");
            String newEname = scanner.nextLine();

            System.out.print("변경할 부서 위치 입력: ");
            String newJob = scanner.nextLine();
             
            System.out.print("변경할 mgr 입력: ");
            int newMgr = scanner.nextInt();
            
            System.out.print("변경할 부서입사날짜: ");
            int newHiredate = scanner.nextInt();
            
            System.out.print("변경할 급여: ");
            int newSal = scanner.nextInt();
            
            System.out.print("변경할 comm: ");
            int newComm = scanner.nextInt();
            
            System.out.print("변경할 deptno: ");
            int newDeptno = scanner.nextInt();

            // 데이터 변경 로직 작성
            Statement stmt = null;
            String sql = "update emp set ename = '" + newEname + "', job = '" + newJob + "', mgr = " + newMgr +
                    ", hiredate = " + newHiredate + ", sal = " + newSal + ", comm = " + newComm + ", deptno = " + newDeptno +
                    " where empno = " + empno;
            			
            try {
            	stmt = conn.createStatement();
                int result = stmt.executeUpdate(sql);

                if (result > 0) {
                    System.out.println("데이터가 성공적으로 변경되었습니다.");
                } else {
                    System.out.println("변경할 데이터가 없습니다.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("데이터 수정 중 오류가 발생하였습니다.");
            }
        } finally {
            // 스캐너 닫지 않음
        }
    }

    private static void deleteData() {
        Scanner scanner = new Scanner(System.in);

        try {
            // 스캐너
            System.out.print("부서 번호 입력: ");
            int empno = scanner.nextInt();
            scanner.nextLine(); // Enter 처리

            System.out.print("부서 이름 입력: ");
            String ename = scanner.nextLine();

            System.out.print("부서 위치 입력: ");
            String job = scanner.nextLine();

            // 데이터 삭제 로직 작성
            Statement stmt;
            String sql = "delete from emp where empno = " + empno + " and ename = '" + ename + "' and job = '" + job + "'";
            try {
            	stmt = conn.createStatement();
                int result = stmt.executeUpdate(sql);

                if (result > 0) {
                    System.out.println("데이터가 성공적으로 삭제되었습니다.");
                } else {
                    System.out.println("삭제할 데이터가 없습니다.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("데이터 삭제 중 오류가 발생하였습니다.");
            }
        } finally {
            // 스캐너 닫기
            scanner.close();
        }
    }

    private static void closeResources() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("데이터베이스 연결 해제 중 오류가 발생하였습니다.");
        }
    }
}