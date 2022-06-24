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
    private final JDesktopPane DESKTOP_PANE = new JDesktopPane();//�����ڣ����Ӵ������������
    /* iFrames���Ӵ��ڵļ��ϣ�������Map���Լ�ֵ�Ե���ʽ�洢���ݣ������²������Ӵ��ڵ����ö�Ҫ�洢�����У�
    �洢��ʽ�ǡ��Ӵ��ڵ����֡�+���Ӵ��ڵ����á���Ŀ�ľ��Ǳ����Ӵ����ظ���
    */
    private Map<String, JInternalFrame> iFrames = new HashMap<>();
    private final int frameWidth = 1000,frameHeight = 600;
    JMenu userMenu,bookMenu;
    JMenuItem myBooksItem,modifyPasswordItem,quitItem,findAndBorrowItem,borrowedHistoryItem,switchUserItem;

    public UserFrame() {
        initSizeLocation(); initView(); initEvent(); setVisible(true);
    }

    private void initSizeLocation() {
        setTitle("ͼ�����ϵͳ�û������棬��ӭ"+Global.user.getUserName());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(frameWidth, frameHeight); setLocationRelativeTo(null);
    }

    private void initView() {//�����븽ͼ
        JMenuBar menuBar = new JMenuBar();
        //�û��˵�
        userMenu = new JMenu(Global.user.getUserName());//�û���¼֮����ʾ��ǰ�û���
        userMenu.setIcon(CreateIcon.getIconFromFile("userItem.png"));
        modifyPasswordItem = new JMenuItem("�޸�����");
        modifyPasswordItem.setIcon(CreateIcon.getIconFromFile("key.png"));
        switchUserItem = new JMenuItem("�л��û�");
        switchUserItem.setIcon(CreateIcon.getIconFromFile("switchUser.png"));
        quitItem = new JMenuItem("�û��˳�");
        quitItem.setIcon(CreateIcon.getIconFromFile("quit.png"));
        userMenu.add(modifyPasswordItem); userMenu.add(switchUserItem); userMenu.add(quitItem);
        //ͼ�����˵�
        bookMenu = new JMenu("ͼ�����"); myBooksItem = new JMenuItem("�ҵ����");
        myBooksItem.setIcon(CreateIcon.getIconFromFile("myBooks.png"));
        bookMenu.setIcon(CreateIcon.getIconFromFile("bookItem.png"));
        findAndBorrowItem = new JMenuItem("��ѯ����");
        findAndBorrowItem.setIcon(CreateIcon.getIconFromFile("findBooks.png"));
        borrowedHistoryItem = new JMenuItem("������ʷ");
        borrowedHistoryItem.setIcon(CreateIcon.getIconFromFile("borrowedHistory.png"));
        bookMenu.add(myBooksItem); bookMenu.add(findAndBorrowItem); bookMenu.add(borrowedHistoryItem);

        menuBar.add(userMenu); menuBar.add(bookMenu); setJMenuBar(menuBar);
        getContentPane().add(DESKTOP_PANE);//������������
    }

    private void initEvent() {
        // TODO: 0207
        /*��ɡ��˳�ϵͳ���˵��Ĺ���
        �رյ�ǰ���ڣ��˳�����
        */
        quitItem.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                dispose(); System.exit(0);
            }
        });
        // TODO: 0208
        /* ��ɡ��л��û����˵��Ĺ���
        �رյ�ǰ���� ��ʾ��¼����
        */
        switchUserItem.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                dispose(); new LoginFrame();
            }
        });

        //���޸����롱�˵��¼�
        modifyPasswordItem.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if(!iFrames.containsKey("�޸�����")||iFrames.get("�޸�����").isClosed()) {
                    ModifyPasswordInternalFrame internalFrame = new ModifyPasswordInternalFrame();
                    iFrames.put("�޸�����", internalFrame); DESKTOP_PANE.add(internalFrame);
                }
            }
        });

        // TODO: 0306
        /*����ѯ���ġ��˵��¼���������ѯ�����Ӵ��ڣ���������ѯ�����Ӵ��ڣ�������������У�ͬʱ����iFrames����
        �ο�modifyPasswordItem.addActionListener
        */
        findAndBorrowItem.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if(!iFrames.containsKey("��ѯ����")||iFrames.get("��ѯ����").isClosed()) {
                    FindAndBorrowInternalFrame internalFrame = new FindAndBorrowInternalFrame();
                    iFrames.put("��ѯ����", internalFrame); DESKTOP_PANE.add(internalFrame);
                }
            }
        });

        // TODO: 0402
        /*�ҵ���ܡ��˵��¼��������ҵ�����Ӵ��ڣ�������������У�ͬʱ����iFrames����
        �����ҵ�����Ӵ���
        �ο�modifyPasswordItem.addActionListener
        */
        myBooksItem.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if(!iFrames.containsKey("�ҵ����")||iFrames.get("�ҵ����").isClosed()) {
                    MyBooksInternalFrame internalFrame = new MyBooksInternalFrame();
                    iFrames.put("�ҵ����", internalFrame); DESKTOP_PANE.add(internalFrame);
                }
            }
        });

        // TODO: 0406
        /*��������ʷ���˵��¼�������������ʷ�Ӵ��ڣ�������������У�ͬʱ����iFrames����
        ����������ʷ�Ӵ���
        �ο�modifyPasswordItem.addActionListener
        */
        borrowedHistoryItem.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if(!iFrames.containsKey("������ʷ")||iFrames.get("������ʷ").isClosed()) {
                    MyBorrowHistoryInternalFrame internalFrame = new MyBorrowHistoryInternalFrame();
                    iFrames.put("������ʷ", internalFrame); DESKTOP_PANE.add(internalFrame);
                }
            }
        });
    }
}
