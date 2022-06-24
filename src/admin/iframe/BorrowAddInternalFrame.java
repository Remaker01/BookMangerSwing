package admin.iframe;

import dao.Dao;
import pojo.BookInfo;
import pojo.User;
import utils.Global;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class BorrowAddInternalFrame extends JInternalFrame {

    private static final String[] columnNames = {"���", "����", "����", "�۸�", "���", "����", "״̬"};
    private final int frameWidth = 800;
    private final int frameHeight = 400;
    private JComboBox findOptionComboBox;
    private JComboBox userComboBox;
    private JTextField findContentTextField;
    private JButton findBtn;
    private JButton borrowBtn;
    private JTable findBooksTable;
    private DefaultTableModel defaultTableModel;
    private List<BookInfo> bookInfoList;

    public BorrowAddInternalFrame() {
        initSizeLocation();

        initView();

        initEvent();

        setVisible(true);
    }



    private void initSizeLocation() {
        setTitle(Global.user.getUserName()+"�������");
        setIconifiable(true);
        setClosable(true);
        setResizable(true);
        setBounds(100, 50, frameWidth, frameHeight);
    }

    private void initView() {
        setLayout(new BorderLayout());

        JPanel panel1 = new JPanel();
        JLabel findOptionLabel = new JLabel("��ѯѡ�");
        findOptionComboBox = new JComboBox(new String[]{"����", "����", "����"});
        JLabel findContentLabel = new JLabel("��ѯ���ݣ�");
        findContentTextField = new JTextField(15);
        JLabel userComboBoxLabel = new JLabel("�����û���");
        List<User> list = Dao.selectAllUser();
        String[] userNames = new String[list.size()];
        for(int i=0; i<list.size(); i++) {
            userNames[i] = list.get(i).getUserName();
        }
        userComboBox = new JComboBox(userNames);
        findBtn = new JButton("��ѯ");
        borrowBtn = new JButton("����");
        panel1.add(findOptionLabel);
        panel1.add(findOptionComboBox);
        panel1.add(findContentLabel);
        panel1.add(findContentTextField);
        panel1.add(findBtn);
        panel1.add(userComboBoxLabel);
        panel1.add(userComboBox);
        panel1.add(borrowBtn);

        JPanel panel2 = new JPanel();
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(700, 300));
        panel2.add(scrollPane);
        findBooksTable = new JTable();
        scrollPane.setViewportView(findBooksTable);
        defaultTableModel = (DefaultTableModel) findBooksTable.getModel();
        defaultTableModel.setColumnIdentifiers(columnNames);

        add(panel1, BorderLayout.NORTH);
        add(panel2, BorderLayout.CENTER);
    }

    private void initEvent() {
        findBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choice = findOptionComboBox.getSelectedIndex();
                String findContent = findContentTextField.getText();
                if(choice==0) {
                    if("".equals(findContent)||findContent==null) {
                        JOptionPane.showMessageDialog(null ,"��ѯ���ݲ���Ϊ��");
                        return;
                    }
                    findBooksByAuthor(findContent);
                } else if(choice==1) {
                    if("".equals(findContent)||findContent==null) {
                        JOptionPane.showMessageDialog(null ,"��ѯ���ݲ���Ϊ��");
                        return;
                    }
                    findBooksByName(findContent);
                } else {
                    findAllBooks();
                }
            }
        });

        borrowBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectRow;
                selectRow = findBooksTable.getSelectedRow();
                if(selectRow==-1) {
                    JOptionPane.showMessageDialog(null, "��ѡ��һ����");
                } else {
                    String flag = (String) findBooksTable.getValueAt(selectRow, 6);
                    if("���ɽ�".equals(flag)) {
                        JOptionPane.showMessageDialog(null, "�����ѽ��");
                    } else {
                        int bookId = (int) findBooksTable.getValueAt(selectRow, 0);
                        String userName = userComboBox.getSelectedItem().toString();
                        int userId = Dao.selectUserIdByName(userName);
                        boolean flag1 = Dao.borrowBookByBookId1(userId, bookId);
                        boolean flag2 = Dao.borrowBookByBookId2(bookId);
                        if(flag1&&flag2) {
                            JOptionPane.showMessageDialog(null, "����ɹ�");
                            int choice = findOptionComboBox.getSelectedIndex();
                            String findContent = findContentTextField.getText();
                            switch (choice) {
                                case 0: findBooksByAuthor(findContent); break;
                                case 1: findBooksByName(findContent);   break;
                                case 2: findAllBooks();                 break;
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "����ʧ��");
                        }
                    }
                }
            }
        });

    }

    private void findBooksByAuthor(String bookAuthor) {
        defaultTableModel.setRowCount(0);
        bookInfoList = Dao.findBooksByAuthor(bookAuthor);

        if(bookInfoList.size()==0) {
            JOptionPane.showMessageDialog(null, "û���ҵ�����鼮");
            return;
        }

        Object[] data = new Object[7];
        for (BookInfo bookInfo : bookInfoList) {
            data[0] = bookInfo.getBookId();
            data[1] = bookInfo.getBookName();
            data[2] = bookInfo.getBookAuthor();
            data[3] = bookInfo.getBookPrice();
            data[4] = Dao.selectBookTypeName(bookInfo.getBookTypeId());
            data[5] = bookInfo.getBookDesc();
            data[6] = bookInfo.isBorrowed() ? "���ɽ�" : "�ɽ�";
            defaultTableModel.addRow(data);
        }
    }

    private void findBooksByName(String bookName) {
        defaultTableModel.setRowCount(0);
        bookInfoList = Dao.findBooksByName(bookName);
        if(bookInfoList.size()==0) {
            JOptionPane.showMessageDialog(null, "û���ҵ�����鼮");
            return;
        }
        for (BookInfo info : bookInfoList) {
            Vector<Object> data = new Vector();
            data.add(info.getBookId());
            data.add(info.getBookName());
            data.add(info.getBookAuthor());
            data.add(info.getBookPrice());
            data.add(Dao.selectBookTypeName(info.getBookTypeId()));
            data.add(info.getBookDesc());
            data.add(info.isBorrowed() ? "���ɽ�" : "�ɽ�");
            defaultTableModel.addRow(data);
        }
    }

    private void findAllBooks() {
        defaultTableModel.setRowCount(0);
        bookInfoList = Dao.findAllBooks();
        if(bookInfoList.size()==0) {
            JOptionPane.showMessageDialog(null, "û���ҵ�����鼮");
            return;
        }
        for (BookInfo info : bookInfoList) {
            Vector<Object> data = new Vector<>();
            data.add(info.getBookId());
            data.add(info.getBookName());
            data.add(info.getBookAuthor());
            data.add(info.getBookPrice());
            data.add(Dao.selectBookTypeName(info.getBookTypeId()));
            data.add(info.getBookDesc());
            data.add(info.isBorrowed() ? "���ɽ�" : "�ɽ�");
            defaultTableModel.addRow(data);
        }
    }
}
