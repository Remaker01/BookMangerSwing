package dao;

import pojo.BookInfo;
import pojo.BookType;
import pojo.Borrow;
import pojo.User;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//���ݿ⹤���࣬�����ݿ���صĲ����������������
public class Dao {
    private final String dbDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";//���ݿ���������
    private final String dbUrl = "jdbc:sqlserver://localhost:1433;DatabaseName=bookMgr";//���ݿ�����·��
    private final String dbUser = "sa";//���ݿ��û���
    private final String dbPass = "321cba";//���ݿ�����
    private static Connection conn = null;//���ݿ����Ӷ���
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//ʱ���ʽ������
    private Dao() {
        try {
            if(conn == null) {
                Class.forName(dbDriver);//�������ݿ�����
                conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);//�������ݿ�
            }
        } catch (ClassNotFoundException e) {
            System.out.println("���ݿ���������ʧ��");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("���ݿ�����ʧ��");
            e.printStackTrace();
        }
    }

    private static void close() {//�ر����ݿ�����
        try {
            if(conn!=null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            conn = null;
        }
    }

    public static User loginCheck(User user) {//��¼��֤
        if(conn==null) new Dao();
        User newUser = null;
        String sql = "select userId, userName, userPassword, isAdmin from [user] " +
                "where userName=? and userPassword=?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);//ִ��SQL����һ���ӿ�
            pstmt.setString(1, user.getUserName());
            pstmt.setString(2, user.getUserPassword());
            ResultSet rs = pstmt.executeQuery();
            //executeQuery()����������ݿ���Ӧ�Ĳ�ѯ��������ResultSet�����ʹ�á�

            while(rs.next()) {//next():ָ�����ƣ�ȡ����
                newUser = new User();
                newUser.setUserId(rs.getInt("userId"));
                newUser.setUserName(rs.getString("userName"));
                newUser.setUserPassword(rs.getString("userPassword"));
                newUser.setAdmin(rs.getBoolean("isAdmin"));
            }
            rs.close();  pstmt.close();//�����sql�ر�
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        Dao.close(); return newUser;
    }

    //ע��
    public static User registerUser(User user) {
        if(conn==null) new Dao();
        // TODO: 0203
        //��ѯuser�Ƿ��Ѿ����������ݿ��У�
        //дһ��sql��䣬��ѯuser�����Ƿ���userNameΪuser.getName()���û�
        String sql = "select userName from [user] where userName=?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getUserName());
            ResultSet rs = pstmt.executeQuery();

            if(rs.next()) {//����û��Ѿ����ڣ��򷵻�null��ע��ʧ��
                rs.close(); pstmt.close(); return null;
            } else {//����û������ڣ��������û������ݣ�ע��ɹ�
                user.setAdmin(false);
                sql = "insert into [user](userName,userPassword,isAdmin) values(?,?,?)";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, user.getUserName());
                pstmt.setString(2, user.getUserPassword());
                pstmt.setBoolean(3, false);
                pstmt.executeUpdate(); pstmt.close();
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        Dao.close(); return user;
    }

    //�޸��û�����
    public static boolean modifyUserPassword(int userId, String password) {
        if(conn==null) new Dao();

        boolean flag = false;//�޸ĳɹ�����true��ʧ�ܷ���false
        String sql = "update [user] set userPassword=? where userId=?";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, password);
            pstmt.setInt(2, userId);
            int i = pstmt.executeUpdate();
            if(i>0) flag = true;
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Dao.close(); return flag;
    }

    //ͨ������ģ����ѯͼ����Ϣ
    public static List<BookInfo> findBooksByAuthor(String bookAuthor) {
        if(conn==null) new Dao();

        List<BookInfo> list = new ArrayList<>();
        String sql = "select * from [book_info] where bookAuthor like ?";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%"+bookAuthor+"%");
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                BookInfo bookInfo = new BookInfo();
                bookInfo.setBookId(rs.getInt("bookId"));
                bookInfo.setBookName(rs.getString("bookName"));
                bookInfo.setBookAuthor(rs.getString("bookAuthor"));
                bookInfo.setBookPrice(rs.getDouble("bookPrice"));
                bookInfo.setBookTypeId(rs.getInt("bookTypeId"));
                bookInfo.setBookDesc(rs.getString("bookDesc"));
                bookInfo.setBorrowed(rs.getBoolean("isBorrowed"));
                list.add(bookInfo);
            }
            rs.close(); pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Dao.close(); return list;
    }

    //ͨ��ͼ�����ID��ѯͼ���������
    public static String selectBookTypeName(int bookTypeId) {
        if(conn==null) new Dao();

        String bookTypeName = null;
        String sql = "select bookTypeName from [book_type] where bookTypeId=?";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bookTypeId);
            ResultSet rs = pstmt.executeQuery();

            if(rs.next()) bookTypeName = rs.getString("bookTypeName");

            rs.close(); pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Dao.close(); return bookTypeName;
    }

    // TODO: 0304
    /*ͨ������ģ����ѯͼ����Ϣ,����ͼ������ͼ����Ϣ����ģ����ѯ
    �ο�findBooksByAuthor����
    */
    public static List<BookInfo> findBooksByName(String bookName) {
        if(conn==null) new Dao();

        List<BookInfo> list = new ArrayList<>();
        String sql = "select * from [book_info] where bookName like ?";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%"+bookName+"%");
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                BookInfo bookInfo = new BookInfo();
                bookInfo.setBookId(rs.getInt("bookId"));
                bookInfo.setBookName(rs.getString("bookName"));
                bookInfo.setBookAuthor(rs.getString("bookAuthor"));
                bookInfo.setBookPrice(rs.getDouble("bookPrice"));
                bookInfo.setBookTypeId(rs.getInt("bookTypeId"));
                bookInfo.setBookDesc(rs.getString("bookDesc"));
                bookInfo.setBorrowed(rs.getBoolean("isBorrowed"));
                list.add(bookInfo);
            }
            rs.close(); pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Dao.close(); return list;
    }

    // TODO: 0305
    /*��ѯ����ͼ����Ϣ
    �ο�findBooksByAuthor����
    */
    public static List<BookInfo> findAllBooks() {
        if(conn==null)  new Dao();
        List<BookInfo> list = new ArrayList<>();
        String sql = "select * from [book_info]";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                BookInfo bookInfo = new BookInfo();
                bookInfo.setBookId(rs.getInt("bookId"));
                bookInfo.setBookName(rs.getString("bookName"));
                bookInfo.setBookAuthor(rs.getString("bookAuthor"));
                bookInfo.setBookPrice(rs.getDouble("bookPrice"));
                bookInfo.setBookTypeId(rs.getInt("bookTypeId"));
                bookInfo.setBookDesc(rs.getString("bookDesc"));
                bookInfo.setBorrowed(rs.getBoolean("isBorrowed"));
                list.add(bookInfo);
            }
            rs.close(); pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Dao.close(); return list;
    }

    //���飬��borrow�������������¼
    public static boolean borrowBookByBookId1(int userId, int bookId) {
        if(conn==null)  new Dao();

        boolean flag = false;
        String sql = "insert into [borrow](userId,bookId,borrowTime) values(?,?,?)";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.setInt(2, bookId);
            pstmt.setString(3, sdf.format(new Date()));
            int i = pstmt.executeUpdate();
            if(i>=0)  flag = true;
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Dao.close(); return flag;
    }

    // TODO: 0401
    /*���飬����book_info���е�isBorrowed�ֶ�,isBorrowed�ֶε�ֵΪ1��ʾ�����0��ʾδ���
    ����bookId����book_info���е�isBorrowed�ֶ�
    ����ɹ�Ӧ����isBorrowed�ֶε�ֵ����Ϊ1,������³ɹ��򷵻�true�����򷵻�false
    �ο�modifyUserPassword����
    */
    public static boolean borrowBookByBookId2(int bookId) {
        if(conn==null) new Dao();

        boolean flag = false;
        String sql = "update [book_info] set isBorrowed=1 where bookId=?";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bookId);
            int i = pstmt.executeUpdate();
            if(i>=0) flag = true;
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Dao.close(); return flag;
    }

    // TODO: 0403
    /*ͨ���û�id�������ڽ��ĵļ�¼
    �����û�id�������û����ڽ��ĵ�����ͼ�飬��ѯborrow�����ڽ��ĵ�ͼ��returnTime�ֶ�Ϊ��
    �ο�findBooksByAuthor����
    */
    public static List<Borrow> selectBorrowingByUserId(int userId) {
        if(conn==null) new Dao();
        List<Borrow> list = new ArrayList<>();
        String sql = "select * from [borrow] where userId=? and returnTime is null";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                Borrow borrow = new Borrow();
                borrow.setBorrowId(rs.getInt("borrowId"));
                borrow.setUserId(userId);
                borrow.setBookId(rs.getInt("bookId"));
                borrow.setBorrowTime(rs.getTimestamp("borrowTime"));
                list.add(borrow);
            }
            rs.close(); pstmt.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        Dao.close(); return list;
    }

    // TODO: 0404
    /*ͨ��bookId��ѯͼ����Ϣ
    ����bookId��ѯͼ����Ϣ����ѯbook_info��
    */
    public static BookInfo selectBookInfoByBookId(int bookId) {
        if(conn==null) new Dao();
        BookInfo bookInfo = null;
        String sql = "select * from [book_info] where bookId=?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bookId);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                bookInfo = new BookInfo();
                bookInfo.setBookId(bookId);
                bookInfo.setBookName(rs.getString("bookName"));
                bookInfo.setBookAuthor(rs.getString("bookAuthor"));
                bookInfo.setBookPrice(rs.getDouble("bookPrice"));
                bookInfo.setBookTypeId(rs.getInt("bookTypeId"));
                bookInfo.setBookDesc(rs.getString("bookDesc"));
            }
            rs.close(); pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Dao.close(); return bookInfo;
    }

    // TODO: 0405
    /*����
    ����borrowId���飬Ҫ�������ű�borrow��book_info
    ����֮�����borrow������Ӧ��¼��returnTime�ֶΣ�����Ϊ�����ʱ��
    ��Ҫ����book_info������Ӧ��¼��isBorrowed�ֶΣ�����Ϊ0
    */
    public static boolean returnBook(int borrowId) {
        if(conn==null)
            new Dao();
        boolean flag = false;
//        String sql = "update borrow,book_info " +
//                "set [borrow].returnTime = ?,[book_info].isBorrowed = 0" +
//                " where borrow.borrowId=? and borrow.bookId=book_info.bookId";
        String sql = "update borrow set borrow.returnTime = ? ,bi.isBorrowed = 0 from borrow " +
                "inner join book_info as bi on bi.bookId = borrow.bookId " +
                "where borrow.borrowId = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, sdf.format(new Date()));
            pstmt.setInt(2, borrowId);
            int i = pstmt.executeUpdate();
            if(i>0) flag = true;
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Dao.close(); return flag;
    }

    // TODO: 0407
    /*ͨ���û�id�������û����еĽ��ļ�¼
    ����userId�������û����еĽ��ļ�¼ �����������ĵļ�¼�����ڽ��ĵļ�¼ ���н��ļ�¼����borrow����
    */
    public static List<Borrow> selectAllBorrowedByUserId(int userId) {
        if(conn==null) new Dao();
        List<Borrow> list = new ArrayList<>();
        String sql = "select * from [borrow] where userId=?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                Borrow borrow = new Borrow();
                borrow.setBorrowId(rs.getInt("borrowId"));
                borrow.setUserId(userId);
                borrow.setBookId(rs.getInt("bookId"));
                borrow.setBorrowTime(rs.getTimestamp("borrowTime"));
                borrow.setReturnTime(rs.getTimestamp("returnTime"));
                list.add(borrow);
            }
            rs.close(); pstmt.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        Dao.close(); return list;
    }

    // TODO: 0503
    /*����ͼ�����
    ����bookTypeId����bookType�����³ɹ�����true��ʧ�ܷ���false
    */
    public static boolean updateBookType(BookType bookType) {
        if(conn==null) new Dao();
        boolean flag = false;
        String sql = "update book_type set bookTypeName=?,bookTypeDesc=? where bookTypeId=?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, bookType.getBookTypeName());
            pstmt.setString(2, bookType.getBookTypeDesc());
            pstmt.setInt(3, bookType.getBookTypeId());
            int i = pstmt.executeUpdate();
            if(i>0) flag = true;
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Dao.close();
        return flag;
    }

    // TODO: 0504
    /*����ͼ�����idɾ��ͼ�����
    ����bookTypeIdɾ��һ��bookType��¼��ɾ���ɹ�����true��ʧ�ܷ���false
    */
    public static boolean deleteBookTypeById(int bookTypeId) {
        if(conn==null)
            new Dao();
        boolean flag = false;
        String sql = "delete from book_type where bookTypeId=?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bookTypeId);
            int i = pstmt.executeUpdate();
            if(i>0) flag = true;
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Dao.close();
        return flag;
    }

    // TODO: 0505
    /*��ѯ����ͼ������¼
     */
    public static List<BookType> selectAllBookType() {
        if(conn==null) new Dao();
        List<BookType> list = new ArrayList<>();
        String sql = "select * from book_type";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                BookType bookType = new BookType();
                bookType.setBookTypeId(rs.getInt("bookTypeId"));
                bookType.setBookTypeName(rs.getString("bookTypeName"));
                bookType.setBookTypeDesc(rs.getString("bookTypeDesc"));
                list.add(bookType);
            }
            rs.close();  pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Dao.close();
        return list;
    }


    // TODO: 0506
    /*���ͼ��������ͼ����������Ѿ����ڣ������ʧ��
    ����bookTypeName��bookTypeDesc����һ��bookType��¼
    ����ɹ�����true��ʧ�ܷ���false�� ע��Ҫ���ж�bookTypeName�Ƿ��Ѿ������ڱ��У� ����Ѿ����ھͲ����룬���������������µļ�¼
    */
    public static boolean addBookType(String bookTypeName, String bookTypeDesc) {
        if(conn==null) new Dao();
        boolean flag = false;
        String sql = "select * from book_type where bookTypeName=?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, bookTypeName);
            ResultSet rs = pstmt.executeQuery();
            if(!rs.next()) {
                sql = "insert into book_type(bookTypeName,bookTypeDesc) values(?,?)";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, bookTypeName);
                pstmt.setString(2, bookTypeDesc);
                int i = pstmt.executeUpdate();
                if(i>0) flag = true;
            }
            rs.close(); pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Dao.close(); return flag;
    }

    // TODO: 0601
    /* ͨ��ͼ��������Ʋ�ѯͼ�����id
    �˷����ڹ���Ա��ͼ����Ϣ�����еġ�ͼ����Ϣά�����˵���ʹ�õ������ڸ���ͼ����Ϣ
    ����ͼ��������Ʒ���ͼ�����id
    ��ѯbook_type��
    */
    public static int selectBookTypeIdByBookTypeName(String bookTypeName) {
        if(conn==null) new Dao();
        BookType bookType = null;
        String sql = "select * from book_type where bookTypeName=?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, bookTypeName);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                bookType = new BookType();
                bookType.setBookTypeName(bookTypeName);
                bookType.setBookTypeId(rs.getInt("bookTypeId"));
            }
            rs.close(); pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Dao.close();
        assert bookType != null;
        return bookType.getBookTypeId();
    }

    // TODO: 0602
    /*����ͼ����Ϣ
    ����Ա�ڡ�ͼ����Ϣά���������е�����޸ġ���ť֮�󣬻���ô˷���
    ����ͼ����Ϣ�������Ƿ���³ɹ�
    ����ͼ����Ϣ������һ��bookInfo���󣬸ö����ֱ�������µ�ͼ����Ϣ
    ���ݸö����bookId���ԣ�����book_info���ж�Ӧ��ͼ����Ϣ
    */
    public static boolean updateBookInfo(BookInfo bookInfo) {
        if(conn==null)
            new Dao();
        boolean flag = false;
        String sql = "update book_info set bookName=?,bookAuthor=?,bookPrice=?,bookDesc=? where bookId=?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, bookInfo.getBookName());
            pstmt.setString(2, bookInfo.getBookAuthor());
            pstmt.setDouble(3, bookInfo.getBookPrice());
            pstmt.setString(4, bookInfo.getBookDesc());
            pstmt.setInt(5,bookInfo.getBookId());
            int i = pstmt.executeUpdate();
            if(i>0)
                flag = true;
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Dao.close();
        return flag;
    }

    // TODO: 0603
    /* ����ͼ��idɾ��ͼ����Ϣ
    ����Ա�ڡ�ͼ����Ϣά���������е����ɾ������ť֮�󣬻���ô˷���
    ɾ��һ��ͼ���¼�������Ƿ�ɾ���ɹ���ͨ��bookId��book_info����ɾ��һ��ͼ���¼
    ���ز���ֵ��ɾ���ɹ��򷵻�true��ʧ���򷵻�false
    */
    public static boolean deleteBookById(int bookId) {
        if(conn==null) new Dao();
        boolean flag = false;
        String sql = "delete from book_info where bookId=?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bookId);
            int i = pstmt.executeUpdate();
            if(i>0) flag = true;
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Dao.close();
        return flag;
    }

    // TODO: 0604
    /*���ͼ����Ϣ�����ͼ�������Ѿ����ڣ������ʧ��
    �˷����ڹ���Ա��ͼ����Ϣ�����еġ�ͼ����Ϣ��ӡ��˵���ʹ�õ�
    ����һ��bookInfo���󣬽��ö������Ϣ����book_info����
    ���ͼ�������Ѵ��ڣ������ʧ��
    ע��Ҫ����book_info���в�ѯͼ�������Ƿ���ڣ�����Ѵ����򷵻�false
    ��������ڣ�����в����������bookInfo����ĸ���������ӵ�book_info����
    */
    public static boolean addBookInfo(BookInfo bookInfo) {
        if(conn==null) new Dao();
        boolean flag = false;
        String sql = "select * from book_info where bookName=?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, bookInfo.getBookName());
            ResultSet rs = pstmt.executeQuery();
            if(!rs.next()) {
                sql = "insert into book_info(bookName,bookAuthor,bookPrice,bookTypeId,bookDesc) values(?,?,?,?,?)";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, bookInfo.getBookName());
                pstmt.setString(2, bookInfo.getBookAuthor());
                pstmt.setDouble(3, bookInfo.getBookPrice());
                pstmt.setInt(4, bookInfo.getBookTypeId());
                pstmt.setString(5,bookInfo.getBookDesc());
                int i = pstmt.executeUpdate();
                if(i>0)
                    flag = true;
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Dao.close();
        return flag;
    }

    // TODO: 0703
    /*����û���Ϣ������û����Ѿ����ڣ������ʧ��
    ����һ��user���󣬽��ö������Ϣ����user����, ����û����Ѵ��ڣ������ʧ��
    ע��Ҫ����user���в�ѯ�û����Ƿ���ڣ�����Ѵ����򷵻�false
    ��������ڣ�����в����������user����ĸ���������ӵ�user����
    */
    public static boolean addUser(User user) {
        if(conn==null) new Dao();
        boolean flag = false;
        String sql = "select * from [user] where userName=?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getUserName());
            ResultSet rs = pstmt.executeQuery();
            if(!rs.next()) {
                sql = "insert into [user](userName,userPassword) values(?,?)";
                //"insert into user(userId,userName,userPassword,issAdmin) values(?,?,?,?)";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1,user.getUserName());
                pstmt.setString(2,user.getUserPassword());
         //       pstmt.setBoolean(4,user.isAdmin());
                int i = pstmt.executeUpdate();
                if(i>0)
                    flag = true;
            }
            rs.close(); pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Dao.close(); return flag;
    }

    // TODO: 0704
    /*�����û���Ϣ
    ����һuser���󣬸���userId������user���ж�Ӧ��user��¼,�������һ����¼�򷵻�true�����򷵻�false
    */
    public static boolean updateUser(User user) {
        if(conn==null)
            new Dao();
        boolean flag = false;
        String sql = "update [user] set userName=?,userPassword=?,isAdmin=? where userId=?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,user.getUserName());
            pstmt.setString(2,user.getUserPassword());
            pstmt.setString(3,user.getUserPassword());
            pstmt.setBoolean(4,user.isAdmin());
            int i = pstmt.executeUpdate();
            if(i>0)
                flag = true;
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Dao.close();
        return flag;
    }

    // TODO: 0705
    /*�����û�idɾ���û�
    ����һ��userId�����ݸ�userId��ɾ��user���ж�Ӧ��user��¼, ɾ���ɹ��򷵻�true�����򷵻�false
    */
    public static boolean deleteUserById(int userId) {
        if (conn == null) new Dao();
        boolean flag = false;
        String sql = "delete from [user] where userId=?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            int i = pstmt.executeUpdate();
            if (i > 0) flag = true;
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Dao.close(); return flag;
    }

    // TODO: 0706
    /*��ѯ�����û���Ϣ
    ��user�������е�user��¼����, �����е�user��¼�����List<user>�У�������
    */
    public static List<User> selectAllUser() {
        if(conn==null) new Dao();
        List<User> list = new ArrayList<>();
        String sql = "select * from [user]";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("userId"));
                user.setUserName(rs.getString("userName"));
                user.setUserPassword(rs.getString("userPassword"));
                list.add(user);
            }
            rs.close();  pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Dao.close(); return list;
    }

    // TODO: 0801
    /*ͨ���û�����ѯ�û�id
    �����û���userName����ѯuser�����ظ��û���userId
    */
    public static int selectUserIdByName(String userName) {
        if(conn==null) new Dao();
        String sql = "select * from [user] where userName=?";
        User user = new User();
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userName);
            ResultSet rs = pstmt.executeQuery();
           if(rs.next()) {
               user.setUserName(userName);
               user.setUserId(rs.getInt("userId"));
            }
            rs.close();
           pstmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        Dao.close(); return user.getUserId();
    }

    // TODO: 0802
    /*��ѯ�������ڽ��ĵļ�¼ �ο�Dao���е�selectBorrowingByUserId����
    */
    public static List<Borrow> selectAllBorrowing() {
        if(conn==null) new Dao();
        List<Borrow> list = new ArrayList<>();
        String sql = "select * from borrow where returnTime is null";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                Borrow borrow = new Borrow();
                borrow.setBorrowId(rs.getInt("borrowId"));
                borrow.setBookId(rs.getInt("userId"));
                borrow.setBookId(rs.getInt("bookId"));
                borrow.setBorrowTime(rs.getTimestamp("borrowTime"));
                borrow.setReturnTime(rs.getTimestamp("returnTime"));
                list.add(borrow);
            }
            rs.close(); pstmt.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        Dao.close(); return list;
    }


    // TODO: 0803
    /*��ѯ���еĽ��ļ�¼
    ��ѯ�����û��Ľ��ļ�¼ �����ѽ���ڽ�� ����ѯborrow���е����м�¼
     */
    public static List<Borrow> selectAllBorrowed() {
        if(conn==null) new Dao();
        List<Borrow> list = new ArrayList<>();
        String sql = "select * from borrow";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                Borrow borrow = new Borrow();
                borrow.setBorrowId(rs.getInt("borrowId"));
                borrow.setUserId(rs.getInt("userId"));
                borrow.setBookId(rs.getInt("bookId"));
                borrow.setBorrowTime(rs.getTimestamp("borrowTime"));
                borrow.setReturnTime(rs.getTimestamp("returnTime"));
                list.add(borrow);
            }
            rs.close(); pstmt.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        Dao.close();
        return list;
    }

    // TODO: 0804
    /*ͨ���û�id��ѯ�û���
    ����userId����ѯ��֮��Ӧ���û���userName����ѯuser������userId����userName
    */
    public static String selectUserNameById(int userId) {
        if(conn==null) new Dao();
        User user = new User();
        String sql = "select * from [user] where userId=?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                user.setUserName(rs.getString("userName"));
            }
            rs.close(); pstmt.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        Dao.close();
        return user.getUserName();
    }
}
