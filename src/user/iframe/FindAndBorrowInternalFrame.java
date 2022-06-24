package user.iframe;

import dao.Dao;
import pojo.BookInfo;
import utils.Global;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Vector;

public class FindAndBorrowInternalFrame extends JInternalFrame {
    private static final String[] columnNames = {"���", "����", "����", "�۸�", "���", "����", "״̬"};
    private final int frameWidth = 800,frameHeight = 400;
    private JComboBox findOptionComboBox;
    private JTextField findContentTextField;
    private JButton findBtn,borrowBtn;
    private JTable findBooksTable;
    private DefaultTableModel defaultTableModel;
    private List<BookInfo> bookInfoList;

    public FindAndBorrowInternalFrame() {
        initSizeLocation();initView(); initEvent();setVisible(true);
    }

    private void initSizeLocation() {
        setTitle(Global.user.getUserName()+"��ѯͼ��");
        setIconifiable(true); setClosable(true); setResizable(true);
        setBounds(100, 50, frameWidth, frameHeight);
    }

    private void initView() {
        setLayout(new BorderLayout());
        JPanel panel1 = new JPanel();
        JLabel findOptionLabel = new JLabel("��ѯѡ�");
        findOptionComboBox = new JComboBox(new String[]{"����", "����", "����"});
        JLabel findContentLabel = new JLabel("��ѯ���ݣ�");
        findContentTextField = new JTextField(15);
        findBtn = new JButton("��ѯ");
        borrowBtn = new JButton("����");
        panel1.add(findOptionLabel); panel1.add(findOptionComboBox);
        panel1.add(findContentLabel); panel1.add(findContentTextField);
        panel1.add(findBtn); panel1.add(borrowBtn);

        JPanel panel2 = new JPanel();
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(700, 300));
        panel2.add(scrollPane);
        findBooksTable = new JTable();
        scrollPane.setViewportView(findBooksTable);
        defaultTableModel = (DefaultTableModel) findBooksTable.getModel();
        defaultTableModel.setColumnIdentifiers(columnNames);

        add(panel1, BorderLayout.NORTH); add(panel2, BorderLayout.CENTER);
    }

    private void initEvent() {
        //����ѯ����ť�¼�
        findBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choice = findOptionComboBox.getSelectedIndex();
                String findContent = findContentTextField.getText();
                if(choice==0) {//�����ѯѡ���ǡ����ߡ������ѯ���ݲ���Ϊ��
                    if("".equals(findContent)||findContent==null) {
                        JOptionPane.showMessageDialog(null ,"��ѯ���ݲ���Ϊ��");
                        return;
                    }
                    findBooksByAuthor(findContent);
                } else if(choice==1) {//�����ѯѡ���ǡ������������ѯ���ݲ���Ϊ��
                    if("".equals(findContent)||findContent==null) {
                        JOptionPane.showMessageDialog(null ,"��ѯ���ݲ���Ϊ��");
                        return;
                    }
                    findBooksByName(findContent);
                } else {//�����ѯѡ���ǡ����С������ѯ���ݿ���Ϊ�գ������ݿ�������ͼ����Ϣ������
                    findAllBooks();
                }
            }
        });

        //�����ġ���ť�¼�
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
                        boolean flag1 = Dao.borrowBookByBookId1(Global.user.getUserId(), bookId);
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

    //���ݡ����ߡ���ѯͼ��
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

    // TODO: 0302
    /*���ݡ���������ѯͼ�飬����������ʾ�ڱ����
    �ο�findBooksByAuthor����
    */
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

    // TODO: 0303
    /*��ѯ����ͼ����Ϣ������ʾ�ڱ����
    �ο�findBooksByAuthor����
    */
    private void findAllBooks() {
        defaultTableModel.setRowCount(0); bookInfoList = Dao.findAllBooks();
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
}
