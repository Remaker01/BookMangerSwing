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

//数据库工具类，与数据库相关的操作都放在这个类中
public class Dao {
    private final String dbDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";//数据库连接驱动
    private final String dbUrl = "jdbc:sqlserver://localhost:1433;DatabaseName=bookMgr";//数据库连接路径
    private final String dbUser = "sa";//数据库用户名
    private final String dbPass = "321cba";//数据库密码
    private static Connection conn = null;//数据库连接对象
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//时间格式化对象
    private Dao() {
        try {
            if(conn == null) {
                Class.forName(dbDriver);//加载数据库驱动
                conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);//连接数据库
            }
        } catch (ClassNotFoundException e) {
            System.out.println("数据库驱动加载失败");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("数据库连接失败");
            e.printStackTrace();
        }
    }

    private static void close() {//关闭数据库连接
        try {
            if(conn!=null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            conn = null;
        }
    }

    public static User loginCheck(User user) {//登录验证
        if(conn==null) new Dao();
        User newUser = null;
        String sql = "select userId, userName, userPassword, isAdmin from [user] " +
                "where userName=? and userPassword=?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);//执行SQL语句的一个接口
            pstmt.setString(1, user.getUserName());
            pstmt.setString(2, user.getUserPassword());
            ResultSet rs = pstmt.executeQuery();
            //executeQuery()方法会把数据库响应的查询结果存放在ResultSet类对象供使用。

            while(rs.next()) {//next():指针下移，取内容
                newUser = new User();
                newUser.setUserId(rs.getInt("userId"));
                newUser.setUserName(rs.getString("userName"));
                newUser.setUserPassword(rs.getString("userPassword"));
                newUser.setAdmin(rs.getBoolean("isAdmin"));
            }
            rs.close();  pstmt.close();//结果与sql关闭
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        Dao.close(); return newUser;
    }

    //注册
    public static User registerUser(User user) {
        if(conn==null) new Dao();
        // TODO: 0203
        //查询user是否已经存在于数据库中：
        //写一条sql语句，查询user表中是否有userName为user.getName()的用户
        String sql = "select userName from [user] where userName=?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getUserName());
            ResultSet rs = pstmt.executeQuery();

            if(rs.next()) {//如果用户已经存在，则返回null，注册失败
                rs.close(); pstmt.close(); return null;
            } else {//如果用户不存在，则插入该用户的数据，注册成功
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

    //修改用户密码
    public static boolean modifyUserPassword(int userId, String password) {
        if(conn==null) new Dao();

        boolean flag = false;//修改成功返回true，失败返回false
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

    //通过作者模糊查询图书信息
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

    //通过图书类别ID查询图书类别名称
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
    /*通过书名模糊查询图书信息,根据图书名对图书信息进行模糊查询
    参考findBooksByAuthor方法
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
    /*查询所有图书信息
    参考findBooksByAuthor方法
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

    //借书，在borrow表中新增借书记录
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
    /*借书，更新book_info表中的isBorrowed字段,isBorrowed字段的值为1表示借出，0表示未借出
    根据bookId更新book_info表中的isBorrowed字段
    借书成功应当把isBorrowed字段的值更新为1,如果更新成功则返回true，否则返回false
    参考modifyUserPassword方法
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
    /*通过用户id检索正在借阅的记录
    根据用户id检索该用户正在借阅的所有图书，查询borrow表，正在借阅的图书returnTime字段为空
    参考findBooksByAuthor方法
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
    /*通过bookId查询图书信息
    根据bookId查询图书信息，查询book_info表
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
    /*还书
    根据borrowId还书，要更新两张表，borrow和book_info
    还书之后更新borrow表中相应记录的returnTime字段，更新为还书的时间
    还要更新book_info表中相应记录的isBorrowed字段，更新为0
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
    /*通过用户id检索该用户所有的借阅记录
    根据userId检索该用户所有的借阅记录 包括曾经借阅的记录和正在借阅的记录 所有借阅记录都在borrow表中
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
    /*更新图书类别
    根据bookTypeId更新bookType，更新成功返回true，失败返回false
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
    /*根据图书类别id删除图书类别
    根据bookTypeId删除一条bookType记录，删除成功返回true，失败返回false
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
    /*查询所有图书类别记录
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
    /*添加图书类别，如果图书类别名称已经存在，则添加失败
    根据bookTypeName和bookTypeDesc插入一条bookType记录
    插入成功返回true，失败返回false， 注意要先判断bookTypeName是否已经存在于表中， 如果已经存在就不插入，如果不存在则插入新的记录
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
    /* 通过图书类别名称查询图书类别id
    此方法在管理员“图书信息管理”中的“图书信息维护”菜单中使用到，用于更新图书信息
    根据图书类别名称返回图书类别id
    查询book_type表
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
    /*更新图书信息
    管理员在“图书信息维护”窗口中点击“修改”按钮之后，会调用此方法
    更新图书信息，返回是否更新成功
    更新图书信息，传入一个bookInfo对象，该对象种保存的是新的图书信息
    根据该对象的bookId属性，更新book_info表中对应的图书信息
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
    /* 根据图书id删除图书信息
    管理员在“图书信息维护”窗口中点击“删除”按钮之后，会调用此方法
    删除一条图书记录，返回是否删除成功，通过bookId在book_info表中删除一条图书记录
    返回布尔值，删除成功则返回true，失败则返回false
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
    /*添加图书信息，如果图书名称已经存在，则添加失败
    此方法在管理员“图书信息管理”中的“图书信息添加”菜单中使用到
    传入一个bookInfo对象，将该对象的信息插入book_info表中
    如果图书名称已存在，则插入失败
    注意要先在book_info表中查询图书名称是否存在，如果已存在则返回false
    如果不存在，则进行插入操作，将bookInfo对象的各个属性添加到book_info表中
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
    /*添加用户信息，如果用户名已经存在，则添加失败
    传入一个user对象，将该对象的信息插入user表中, 如果用户名已存在，则插入失败
    注意要先在user表中查询用户名是否存在，如果已存在则返回false
    如果不存在，则进行插入操作，将user对象的各个属性添加到user表中
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
    /*更新用户信息
    传入一user对象，根据userId，更新user表中对应的user记录,如果更新一条记录则返回true，否则返回false
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
    /*根据用户id删除用户
    传入一个userId，根据该userId，删除user表中对应的user记录, 删除成功则返回true，否则返回false
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
    /*查询所有用户信息
    将user表中所有的user记录返回, 将所有的user记录存放在List<user>中，并返回
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
    /*通过用户名查询用户id
    传入用户名userName，查询user表，返回该用户的userId
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
    /*查询所有正在借阅的记录 参考Dao类中的selectBorrowingByUserId方法
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
    /*查询所有的借阅记录
    查询所有用户的借阅记录 包括已借和在借的 即查询borrow表中的所有记录
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
    /*通过用户id查询用户名
    传入userId，查询与之对应的用户名userName，查询user表，根据userId返回userName
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
