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

    public AdminFrame() {//��ʼ����¼���ڵĳߴ� λ�� ��ͼ��� ʵ�ֵ�¼����İ�ť�¼� ��¼���ڿɼ���
        initSizeLocation(); initView(); initEvent(); setVisible(true);
    }

    private void initSizeLocation() {
        setTitle("ͼ�����ϵͳ����Ա�����棬��ӭ"+ Global.user.getUserName());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);//�����ڹر�
        setSize(frameWidth, frameHeight); setLocationRelativeTo(null);//null������������Ļ����
    }

    private void initView() {//�����븽ͼ
        JMenuBar menuBar = new JMenuBar();
        //��������ά���˵�
        basicMenu = new JMenu("��������ά��");
        basicMenu.setIcon(CreateIcon.getIconFromFile("basicMenu.png"));
        //ͼ���������Ӳ˵�
        bookTypeMenu = new JMenu("ͼ��������");
        bookTypeMenu.setIcon(CreateIcon.getIconFromFile("bookType.png"));
        bookTypeAddItem = new JMenuItem("ͼ��������");
        bookTypeAddItem.setIcon(CreateIcon.getIconFromFile("bookType.png"));
        bookTypeModifyItem = new JMenuItem("ͼ�����ά��");
        bookTypeModifyItem.setIcon(CreateIcon.getIconFromFile("bookType.png"));
        bookTypeMenu.add(bookTypeAddItem); bookTypeMenu.add(bookTypeModifyItem);
        //ͼ����Ϣ�����Ӳ˵�
        bookInfoMenu = new JMenu("ͼ����Ϣ����");
        bookInfoMenu.setIcon(CreateIcon.getIconFromFile("bookInfo.png"));
        bookInfoAddItem = new JMenuItem("ͼ����Ϣ���");
        bookInfoAddItem.setIcon(CreateIcon.getIconFromFile("bookInfo.png"));
        bookInfoModifyItem = new JMenuItem("ͼ����Ϣά��");
        bookInfoModifyItem.setIcon(CreateIcon.getIconFromFile("bookInfo.png"));
        bookInfoMenu.add(bookInfoAddItem); bookInfoMenu.add(bookInfoModifyItem);
        //�û������Ӳ˵�
        userMenu = new JMenu("�û���Ϣ����");
        userMenu.setIcon(CreateIcon.getIconFromFile("userInfo.png"));
        userAddItem = new JMenuItem("�û���Ϣ���");
        userAddItem.setIcon(CreateIcon.getIconFromFile("userInfo.png"));
        userModifyItem = new JMenuItem("�û���Ϣά��");
        userModifyItem.setIcon(CreateIcon.getIconFromFile("userInfo.png"));
        userMenu.add(userAddItem); userMenu.add(userModifyItem);
        //���Ĺ����Ӳ˵�
        borrowMenu = new JMenu("�������ݹ���");
        borrowMenu.setIcon(CreateIcon.getIconFromFile("borrow_return.png"));
        borrowItem = new JMenuItem("����");
        borrowItem.setIcon(CreateIcon.getIconFromFile("borrow_return.png"));
        returnItem = new JMenuItem("����");
        returnItem.setIcon(CreateIcon.getIconFromFile("borrow_return.png"));
        historyItem = new JMenuItem("��ʷ");
        historyItem.setIcon(CreateIcon.getIconFromFile("borrow_return.png"));
        borrowMenu.add(borrowItem); borrowMenu.add(returnItem); borrowMenu.add(historyItem);
        //�л��û�
        switchUserItem = new JMenuItem("�л��û�");
        switchUserItem.setIcon(CreateIcon.getIconFromFile("switchUser.png"));
        //�˳��˵�
        quitItem = new JMenuItem("�˳�ϵͳ");
        quitItem.setIcon(CreateIcon.getIconFromFile("quit.png"));

        basicMenu.add(bookTypeMenu); basicMenu.add(bookInfoMenu); basicMenu.add(userMenu);
        basicMenu.add(borrowMenu); basicMenu.add(switchUserItem); basicMenu.add(quitItem);
        //���ڲ˵�
        aboutMenu = new JMenu("����");
        aboutMenu.setIcon(CreateIcon.getIconFromFile("about.png"));
        aboutItem = new JMenuItem("���ڱ�ϵͳ");
        aboutItem.setIcon(CreateIcon.getIconFromFile("about.png"));
        aboutMenu.add(aboutItem);

        menuBar.add(basicMenu); menuBar.add(aboutMenu); setJMenuBar(menuBar);
        getContentPane().add(DESKTOP_PANE);
    }

    private void initEvent() {
        // TODO: 0204
        /*��ɡ��˳�ϵͳ���˵��Ĺ���
        �رյ�ǰ���ڣ��˳�����
        */
        quitItem.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                dispose(); System.exit(0);
            }
        });

        // TODO: 0205
        /* ��ɡ����ڡ��˵��Ĺ���
        �����Ի�����ʾ��ͼ�����ϵͳV1.0��
        */
        aboutItem.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "ͼ�����ϵͳV1.0");
            }
        });

        // TODO: 0206
        /*��ɡ��л��û����˵��Ĺ���
        �رյ�ǰ���� ��ʾ��¼����
        */
        switchUserItem.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LoginFrame();
            }
        });

        // TODO: 0501
        /* ��ͼ�������ӡ��˵��¼�����������ͼ���������Ӵ��ڣ�������������У�ͬʱ����iFrames����
        �ο�UserFrame���е�modifyPasswordItem.addActionListener
        */
        bookTypeAddItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createIFrame(BookTypeAddInternalFrame.class,"ͼ��������");
            }
        });

        // TODO: 0502
        /*��ͼ�����ά�����˵��¼�����������ͼ�����ά���Ӵ��ڣ�������������У�ͬʱ����iFrames����
        �ο�UserFrame���е�modifyPasswordItem.addActionListener
        */
        bookTypeModifyItem.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                createIFrame(BookInfoModifyInternalFrame.class,"ͼ�����ά��");
            }
        });

        // TODO: 0605
        /*��ͼ����Ϣ��ӡ��˵��¼�������ͼ����Ϣ����Ӵ��ڣ�������������У�ͬʱ����iFrames����
        ��ɡ�ͼ����Ϣ��ӡ��˵��¼�������ͼ����Ϣ����Ӵ���
        �ο�UserFrame���е�modifyPasswordItem.addActionListener
        */
        bookInfoAddItem.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                createIFrame(BookInfoAddInternalFrame.class,"ͼ����Ϣ���");
            }
        });

        // TODO: 0606
        /*��ͼ����Ϣ�޸ġ��˵��¼�������ͼ����Ϣ�޸��Ӵ��ڣ�������������У�ͬʱ����iFrames����
        ��ɡ�ͼ����Ϣ�޸ġ��˵��¼�������ͼ����Ϣ�޸��Ӵ���
        �ο�UserFrame���е�modifyPasswordItem.addActionListener
        */
        bookInfoModifyItem.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                createIFrame(BookInfoModifyInternalFrame.class,"ͼ����Ϣ�޸�");
            }
        });

        // TODO:  0701
        /*���û���Ϣ��ӡ��˵��¼��������û���Ϣ����Ӵ��ڣ�������������У�ͬʱ����iFrames����
        �����û���Ϣ����Ӵ���,�ο�UserFrame���е�modifyPasswordItem.addActionListener
        */
        userAddItem.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                createIFrame(UserAddInternalFrame.class,"�û���Ϣ���");
            }
        });

        // TODO: 0702
        /*���û���Ϣ�޸ġ��˵��¼��������û���Ϣ�޸��Ӵ��ڣ�������������У�ͬʱ����iFrames����
        �����û���Ϣ�޸��Ӵ���, �ο�UserFrame���е�modifyPasswordItem.addActionListener
        */
        userModifyItem.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                createIFrame(UserModifyInternalFrame.class,"�û���Ϣ�޸�");
            }
        });

        //����������˵��¼���������������Ӵ��ڣ�������������У�ͬʱ����iFrames����
        borrowItem.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                createIFrame(BorrowAddInternalFrame.class,"�������");
            }
        });

        //����������˵��¼���������������Ӵ��ڣ�������������У�ͬʱ����iFrames����
        returnItem.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                createIFrame(BorrowReturnInternalFrame.class,"�������");
            }
        });

        //��������ʷ���˵��¼�������������ʷ�Ӵ��ڣ�������������У�ͬʱ����iFrames����
        historyItem.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                createIFrame(BorrowHistoryInternalFrame.class,"������ʷ");
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
