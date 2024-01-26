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
    private static Statement stmt = null;
    private static Connection conn = null;

    private int empno;
    private String ename;
    private String job;

    public Jdbc(int empno, String ename, String job) {
        super();
        this.empno = empno;
        this.ename = ename;
        this.job = job;
    }

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            stmt = conn.createStatement();

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
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            // 예외가 발생하면 여기에서 리소스를 안전하게 닫습니다.
            closeResources();
        } finally {
            // 마지막으로 리소스를 닫습니다.
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
                System.out.println(rs.getString("job"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
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

            System.out.print("부서 위치 입력: ");
            String job = scanner.nextLine();

            // 데이터 삽입 로직 작성
            String sql = "insert into dept(empno, ename, job) values (" + empno + ", '" + ename + "', '" + job + "')";
            try {
                int result = stmt.executeUpdate(sql);

                if (result > 0) {
                    System.out.println("데이터가 성공적으로 삽입되었습니다.");
                } else {
                    System.out.println("데이터 삽입에 실패했습니다.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } finally {
        	
		}
    }

//    private static void correctionData() {
//    	//조회하고   //조회한 데이터에서 변경할 데이터값을 입력해서   //변경한 후   //변경한 후의 데이터를 보여줌
//    	Scanner scanner = new Scanner(System.in);
//
//        try {
//            // 스캐너
//            System.out.print("부서 번호 입력: ");
//            int deptno = scanner.nextInt();
//            scanner.nextLine(); // Enter 처리
//
//            System.out.print("부서 이름 입력: ");
//            String dname = scanner.nextLine();
//
//            System.out.print("부서 위치 입력: ");
//            String loc = scanner.nextLine();
//
//            // 데이터 변경 로직 작성
//            
//            String sql = "update dept set dname = '"+dname+"', loc = '"+loc+"' where deptno = "+deptno;
//            try {
//                int result = stmt.executeUpdate(sql);
//
//                if (result > 0) {
//                    System.out.println("데이터가 성공적으로 삭제되었습니다.");
//                } else {
//                    System.out.println("삭제할 데이터가 없습니다.");
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        } finally {
//            // 스캐너 닫기
//            scanner.close();
//        }
//    	
//    	
//    	
//    }
    private static void correctionData() {
    	//조회하고   //조회한 데이터에서 변경할 데이터값을 입력해서   //변경한 후   //변경한 후의 데이터를 보여줌
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

            // 데이터 변경 로직 작성
            String sql = "update emp set ename = '" + newEname + "', job = '" + newJob + "' where empno = " + empno;
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
        } finally {
            // 스캐너 닫지 않음
        }
    }
    
    
//    private static void deleteData() {
//    	
//    //조회하고   //조회한 데이터에서 삭제할 데이터값을 입력해서   //삭제한 후   //삭제한 후의 데이터를 보여줌	
//    Scanner scan = new Scanner(System.in); 
//    	System.out.print("부서 번호 입력: ");
//        int deptno = scanner.nextInt();
//        scanner.nextLine(); // Enter 처리
//
//        System.out.print("부서 이름 입력: ");
//        String dname = scanner.nextLine();
//
//        System.out.print("부서 위치 입력: ");
//        String loc = scanner.nextLine();
//    	
//        
//        
//    	try {
//    		String scan = "select from emp where values (" + deptno + ", '" + dname + "', '" + loc + "')"; 
//            Statement stmt = conn.createStatement();
//            ResultSet rs = stmt.executeQuery("delete * from dept where " + scan +"+");
//            
//            String deletedata = stmt.executeUpdate(rs);
//            if(deletedata>=1) {
//            	System.out.println("삭제성공"+deletedata);
//            }else {
//            	System.out.println("삭제실패"+deletedata);
//            }
//            // 조회된 데이터 출력
//            while (rs.next()) {
//                System.out.print(rs.getInt("deptno") + "\t");
//                System.out.print(rs.getString("dname") + "\t");
//                System.out.println(rs.getString("loc"));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
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
        } finally {
            // 스캐너 닫기
            scanner.close();
        }
    }
    
    
    private static void closeResources() {
        try {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}