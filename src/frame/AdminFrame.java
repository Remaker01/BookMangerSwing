package frame;

import admin.iframe.*;
import com.sun.istack.internal.NotNull;
import utils.CreateIcon;
import utils.Global;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

public class AdminFrame extends JFrame {

    private final JDesktopPane DESKTOP_PANE = new JDesktopPane();
    private Map<String, JInternalFrame> iFrames = new HashMap<>();
    private final int frameWidth = 1000,frameHeight = 600;
    JMenu basicMenu,bookTypeMenu,bookInfoMenu,userMenu,borrowMenu,aboutMenu;
    JMenuItem bookTypeAddItem,bookTypeModifyItem,bookInfoAddItem,bookInfoModifyItem,userAddItem;
    JMenuItem userModifyItem,borrowItem,returnItem,historyItem,quitItem,aboutItem,switchUserItem;

    public AdminFrame() {//初始化登录窗口的尺寸 位置 视图组件 实现登录界面的按钮事件 登录窗口可见化
        initSizeLocation(); initView(); initEvent(); setVisible(true);
    }

    private void initSizeLocation() {
        setTitle("图书管理系统管理员主界面，欢迎"+ Global.user.getUserName());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);//单窗口关闭
        setSize(frameWidth, frameHeight); setLocationRelativeTo(null);//null，窗口置于屏幕中央
    }

    private void initView() {//创建与附图
        JMenuBar menuBar = new JMenuBar();
        //基础数据维护菜单
        basicMenu = new JMenu("基本数据维护");
        basicMenu.setIcon(CreateIcon.getIconFromFile("basicMenu.png"));
        //图书类别管理子菜单
        bookTypeMenu = new JMenu("图书类别管理");
        bookTypeMenu.setIcon(CreateIcon.getIconFromFile("bookType.png"));
        bookTypeAddItem = new JMenuItem("图书类别添加");
        bookTypeAddItem.setIcon(CreateIcon.getIconFromFile("bookType.png"));
        bookTypeModifyItem = new JMenuItem("图书类别维护");
        bookTypeModifyItem.setIcon(CreateIcon.getIconFromFile("bookType.png"));
        bookTypeMenu.add(bookTypeAddItem); bookTypeMenu.add(bookTypeModifyItem);
        //图书信息管理子菜单
        bookInfoMenu = new JMenu("图书信息管理");
        bookInfoMenu.setIcon(CreateIcon.getIconFromFile("bookInfo.png"));
        bookInfoAddItem = new JMenuItem("图书信息添加");
        bookInfoAddItem.setIcon(CreateIcon.getIconFromFile("bookInfo.png"));
        bookInfoModifyItem = new JMenuItem("图书信息维护");
        bookInfoModifyItem.setIcon(CreateIcon.getIconFromFile("bookInfo.png"));
        bookInfoMenu.add(bookInfoAddItem); bookInfoMenu.add(bookInfoModifyItem);
        //用户管理子菜单
        userMenu = new JMenu("用户信息管理");
        userMenu.setIcon(CreateIcon.getIconFromFile("userInfo.png"));
        userAddItem = new JMenuItem("用户信息添加");
        userAddItem.setIcon(CreateIcon.getIconFromFile("userInfo.png"));
        userModifyItem = new JMenuItem("用户信息维护");
        userModifyItem.setIcon(CreateIcon.getIconFromFile("userInfo.png"));
        userMenu.add(userAddItem); userMenu.add(userModifyItem);
        //借阅管理子菜单
        borrowMenu = new JMenu("借阅数据管理");
        borrowMenu.setIcon(CreateIcon.getIconFromFile("borrow_return.png"));
        borrowItem = new JMenuItem("借书");
        borrowItem.setIcon(CreateIcon.getIconFromFile("borrow_return.png"));
        returnItem = new JMenuItem("还书");
        returnItem.setIcon(CreateIcon.getIconFromFile("borrow_return.png"));
        historyItem = new JMenuItem("历史");
        historyItem.setIcon(CreateIcon.getIconFromFile("borrow_return.png"));
        borrowMenu.add(borrowItem); borrowMenu.add(returnItem); borrowMenu.add(historyItem);
        //切换用户
        switchUserItem = new JMenuItem("切换用户");
        switchUserItem.setIcon(CreateIcon.getIconFromFile("switchUser.png"));
        //退出菜单
        quitItem = new JMenuItem("退出系统");
        quitItem.setIcon(CreateIcon.getIconFromFile("quit.png"));

        basicMenu.add(bookTypeMenu); basicMenu.add(bookInfoMenu); basicMenu.add(userMenu);
        basicMenu.add(borrowMenu); basicMenu.add(switchUserItem); basicMenu.add(quitItem);
        //关于菜单
        aboutMenu = new JMenu("关于");
        aboutMenu.setIcon(CreateIcon.getIconFromFile("about.png"));
        aboutItem = new JMenuItem("关于本系统");
        aboutItem.setIcon(CreateIcon.getIconFromFile("about.png"));
        aboutMenu.add(aboutItem);

        menuBar.add(basicMenu); menuBar.add(aboutMenu); setJMenuBar(menuBar);
        getContentPane().add(DESKTOP_PANE);
    }

    private void initEvent() {
        // TODO: 0204
        /*完成“退出系统”菜单的功能
        关闭当前窗口，退出程序
        */
        quitItem.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                dispose(); System.exit(0);
            }
        });

        // TODO: 0205
        /* 完成“关于”菜单的功能
        弹出对话框，显示“图书管理系统V1.0”
        */
        aboutItem.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "图书管理系统V1.0");
            }
        });

        // TODO: 0206
        /*完成“切换用户”菜单的功能
        关闭当前窗口 显示登录窗口
        */
        switchUserItem.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LoginFrame();
            }
        });

        // TODO: 0501
        /* “图书类别添加”菜单事件，弹出创建图书类别添加子窗口，添加在主窗口中，同时加入iFrames集合
        参考UserFrame类中的modifyPasswordItem.addActionListener
        */
        bookTypeAddItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createIFrame(BookTypeAddInternalFrame.class,"图书类别添加");
            }
        });

        // TODO: 0502
        /*“图书类别维护”菜单事件，创建弹出图书类别维护子窗口，添加在主窗口中，同时加入iFrames集合
        参考UserFrame类中的modifyPasswordItem.addActionListener
        */
        bookTypeModifyItem.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                createIFrame(BookInfoModifyInternalFrame.class,"图书类别维护");
            }
        });

        // TODO: 0605
        /*“图书信息添加”菜单事件，创建图书信息添加子窗口，添加在主窗口中，同时加入iFrames集合
        完成“图书信息添加”菜单事件，弹出图书信息添加子窗口
        参考UserFrame类中的modifyPasswordItem.addActionListener
        */
        bookInfoAddItem.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                createIFrame(BookInfoAddInternalFrame.class,"图书信息添加");
            }
        });

        // TODO: 0606
        /*“图书信息修改”菜单事件，创建图书信息修改子窗口，添加在主窗口中，同时加入iFrames集合
        完成“图书信息修改”菜单事件，弹出图书信息修改子窗口
        参考UserFrame类中的modifyPasswordItem.addActionListener
        */
        bookInfoModifyItem.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                createIFrame(BookInfoModifyInternalFrame.class,"图书信息修改");
            }
        });

        // TODO:  0701
        /*“用户信息添加”菜单事件，创建用户信息添加子窗口，添加在主窗口中，同时加入iFrames集合
        弹出用户信息添加子窗口,参考UserFrame类中的modifyPasswordItem.addActionListener
        */
        userAddItem.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                createIFrame(UserAddInternalFrame.class,"用户信息添加");
            }
        });

        // TODO: 0702
        /*“用户信息修改”菜单事件，创建用户信息修改子窗口，添加在主窗口中，同时加入iFrames集合
        弹出用户信息修改子窗口, 参考UserFrame类中的modifyPasswordItem.addActionListener
        */
        userModifyItem.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                createIFrame(UserModifyInternalFrame.class,"用户信息修改");
            }
        });

        //“借书管理”菜单事件，创建借书管理子窗口，添加在主窗口中，同时加入iFrames集合
        borrowItem.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                createIFrame(BorrowAddInternalFrame.class,"借书管理");
            }
        });

        //“还书管理”菜单事件，创建还书管理子窗口，添加在主窗口中，同时加入iFrames集合
        returnItem.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                createIFrame(BorrowReturnInternalFrame.class,"还书管理");
            }
        });

        //“借阅历史”菜单事件，创建借阅历史子窗口，添加在主窗口中，同时加入iFrames集合
        historyItem.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                createIFrame(BorrowHistoryInternalFrame.class,"借阅历史");
            }
        });
    }
    private void createIFrame(@NotNull Class<? extends JInternalFrame> frameClass, String key) {
        if (!iFrames.containsKey(key)||iFrames.get(key).isClosed()) {
            JInternalFrame internalFrame = null;
            try {
                internalFrame = frameClass.newInstance();
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
            iFrames.put(key,internalFrame);
            DESKTOP_PANE.add(internalFrame);
        }
    }
}
