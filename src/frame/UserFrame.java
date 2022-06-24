package frame;

import user.iframe.FindAndBorrowInternalFrame;
import user.iframe.ModifyPasswordInternalFrame;
import user.iframe.MyBooksInternalFrame;
import user.iframe.MyBorrowHistoryInternalFrame;
import utils.CreateIcon;
import utils.Global;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;
public class UserFrame extends JFrame {
    private final JDesktopPane DESKTOP_PANE = new JDesktopPane();//主窗口，将子窗口添加在其中
    /* iFrames是子窗口的集合，类型是Map，以键值对的形式存储数据，所有新产生的子窗口的引用都要存储在其中，
    存储形式是“子窗口的名字”+“子窗口的引用”，目的就是避免子窗口重复打开
    */
    private Map<String, JInternalFrame> iFrames = new HashMap<>();
    private final int frameWidth = 1000,frameHeight = 600;
    JMenu userMenu,bookMenu;
    JMenuItem myBooksItem,modifyPasswordItem,quitItem,findAndBorrowItem,borrowedHistoryItem,switchUserItem;

    public UserFrame() {
        initSizeLocation(); initView(); initEvent(); setVisible(true);
    }

    private void initSizeLocation() {
        setTitle("图书管理系统用户主界面，欢迎"+Global.user.getUserName());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(frameWidth, frameHeight); setLocationRelativeTo(null);
    }

    private void initView() {//创建与附图
        JMenuBar menuBar = new JMenuBar();
        //用户菜单
        userMenu = new JMenu(Global.user.getUserName());//用户登录之后显示当前用户名
        userMenu.setIcon(CreateIcon.getIconFromFile("userItem.png"));
        modifyPasswordItem = new JMenuItem("修改密码");
        modifyPasswordItem.setIcon(CreateIcon.getIconFromFile("key.png"));
        switchUserItem = new JMenuItem("切换用户");
        switchUserItem.setIcon(CreateIcon.getIconFromFile("switchUser.png"));
        quitItem = new JMenuItem("用户退出");
        quitItem.setIcon(CreateIcon.getIconFromFile("quit.png"));
        userMenu.add(modifyPasswordItem); userMenu.add(switchUserItem); userMenu.add(quitItem);
        //图书管理菜单
        bookMenu = new JMenu("图书管理"); myBooksItem = new JMenuItem("我的书架");
        myBooksItem.setIcon(CreateIcon.getIconFromFile("myBooks.png"));
        bookMenu.setIcon(CreateIcon.getIconFromFile("bookItem.png"));
        findAndBorrowItem = new JMenuItem("查询借阅");
        findAndBorrowItem.setIcon(CreateIcon.getIconFromFile("findBooks.png"));
        borrowedHistoryItem = new JMenuItem("借阅历史");
        borrowedHistoryItem.setIcon(CreateIcon.getIconFromFile("borrowedHistory.png"));
        bookMenu.add(myBooksItem); bookMenu.add(findAndBorrowItem); bookMenu.add(borrowedHistoryItem);

        menuBar.add(userMenu); menuBar.add(bookMenu); setJMenuBar(menuBar);
        getContentPane().add(DESKTOP_PANE);//建立虚拟桌面
    }

    private void initEvent() {
        // TODO: 0207
        /*完成“退出系统”菜单的功能
        关闭当前窗口，退出程序
        */
        quitItem.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                dispose(); System.exit(0);
            }
        });
        // TODO: 0208
        /* 完成“切换用户”菜单的功能
        关闭当前窗口 显示登录窗口
        */
        switchUserItem.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                dispose(); new LoginFrame();
            }
        });

        //“修改密码”菜单事件
        modifyPasswordItem.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if(!iFrames.containsKey("修改密码")||iFrames.get("修改密码").isClosed()) {
                    ModifyPasswordInternalFrame internalFrame = new ModifyPasswordInternalFrame();
                    iFrames.put("修改密码", internalFrame); DESKTOP_PANE.add(internalFrame);
                }
            }
        });

        // TODO: 0306
        /*“查询借阅”菜单事件（弹出查询借阅子窗口），创建查询借阅子窗口，添加在主窗口中，同时加入iFrames集合
        参考modifyPasswordItem.addActionListener
        */
        findAndBorrowItem.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if(!iFrames.containsKey("查询借阅")||iFrames.get("查询借阅").isClosed()) {
                    FindAndBorrowInternalFrame internalFrame = new FindAndBorrowInternalFrame();
                    iFrames.put("查询借阅", internalFrame); DESKTOP_PANE.add(internalFrame);
                }
            }
        });

        // TODO: 0402
        /*我的书架”菜单事件，创建我的书架子窗口，添加在主窗口中，同时加入iFrames集合
        弹出我的书架子窗口
        参考modifyPasswordItem.addActionListener
        */
        myBooksItem.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if(!iFrames.containsKey("我的书架")||iFrames.get("我的书架").isClosed()) {
                    MyBooksInternalFrame internalFrame = new MyBooksInternalFrame();
                    iFrames.put("我的书架", internalFrame); DESKTOP_PANE.add(internalFrame);
                }
            }
        });

        // TODO: 0406
        /*“借阅历史”菜单事件，创建借阅历史子窗口，添加在主窗口中，同时加入iFrames集合
        弹出借阅历史子窗口
        参考modifyPasswordItem.addActionListener
        */
        borrowedHistoryItem.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if(!iFrames.containsKey("借阅历史")||iFrames.get("借阅历史").isClosed()) {
                    MyBorrowHistoryInternalFrame internalFrame = new MyBorrowHistoryInternalFrame();
                    iFrames.put("借阅历史", internalFrame); DESKTOP_PANE.add(internalFrame);
                }
            }
        });
    }
}
