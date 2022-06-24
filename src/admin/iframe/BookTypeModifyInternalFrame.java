package admin.iframe;

import dao.Dao;
import pojo.BookType;
import utils.Global;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Vector;

public class BookTypeModifyInternalFrame extends JInternalFrame {

    private static final String[] columnNames = { "ͼ�������", "ͼ���������", "ͼ���������" };
    private final int frameWidth = 800;
    private final int frameHeight = 400;
    private JTable bookTypeTable;
    private DefaultTableModel defaultTableModel;
    private JTextField bookTypeIdTextField,bookTypeNameTextField,bookTypeDescTextField;
    private JButton modifyBtn,deleteBtn;
    private List<BookType> bookTypeList;

    public BookTypeModifyInternalFrame() {
        initSizeLocation(); initView(); initEvent(); setVisible(true);
    }

    private void initSizeLocation() {
        setTitle(Global.user.getUserName()+"ͼ�����ά��");
        setIconifiable(true); setClosable(true); setResizable(true);
        setBounds(100, 50, frameWidth, frameHeight);
    }

    private void initView() {
        setLayout(new BorderLayout());

        JPanel panel1 = new JPanel();
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(600, 250));
        panel1.add(scrollPane);
        bookTypeTable = new JTable();
        scrollPane.setViewportView(bookTypeTable);
        defaultTableModel = (DefaultTableModel) bookTypeTable.getModel();
        defaultTableModel.setColumnIdentifiers(columnNames);
        initMyBooksTable();

        JPanel panel2 = new JPanel();
        JLabel bookTypeIdLabel = new JLabel("��ţ�");
        bookTypeIdTextField = new JTextField(12);
        bookTypeIdTextField.setEditable(false);
        JLabel bookTypeNameLabel = new JLabel("���ƣ�");
        bookTypeNameTextField = new JTextField(12);
        JLabel bookTypeDescLabel = new JLabel("������");
        bookTypeDescTextField = new JTextField(12);
        modifyBtn = new JButton("�޸�");
        deleteBtn = new JButton("ɾ��");
        panel2.add(bookTypeIdLabel); panel2.add(bookTypeIdTextField);
        panel2.add(bookTypeNameLabel); panel2.add(bookTypeNameTextField);
        panel2.add(bookTypeDescLabel); panel2.add(bookTypeDescTextField);
        panel2.add(modifyBtn); panel2.add(deleteBtn);

        add(panel1, BorderLayout.CENTER); add(panel2, BorderLayout.SOUTH);
    }

    private void initEvent() {
        bookTypeTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectRow = bookTypeTable.getSelectedRow();
                bookTypeIdTextField.setText(String.valueOf(bookTypeTable.getValueAt(selectRow, 0)));
                bookTypeNameTextField.setText((String) bookTypeTable.getValueAt(selectRow, 1));
                bookTypeDescTextField.setText((String) bookTypeTable.getValueAt(selectRow, 2));
            }
        });

        modifyBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectRow;
                selectRow = bookTypeTable.getSelectedRow();
                if(selectRow==-1) {
                    JOptionPane.showMessageDialog(null, "����ѡ��һ��ͼ�����");
                    return;
                }

                int choice = JOptionPane.showConfirmDialog(null,"ȷ���޸ĸ�ͼ�����",
                        null,JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                if(choice==0) {
                    int bookTypeId = Integer.parseInt(bookTypeIdTextField.getText());
                    String bookTypeName = bookTypeNameTextField.getText();
                    String bookTypeDesc = bookTypeDescTextField.getText();
                    BookType bookType = new BookType();
                    bookType.setBookTypeId(bookTypeId);
                    bookType.setBookTypeName(bookTypeName);
                    bookType.setBookTypeDesc(bookTypeDesc);
                    boolean flag = Dao.updateBookType(bookType);
                    if(flag) {
                        JOptionPane.showMessageDialog(null, "ͼ�������³ɹ�");
                        initMyBooksTable();
                        clearAll();
                    } else {
                        JOptionPane.showMessageDialog(null, "ͼ��������ʧ��");
                    }
                }
            }
        });

        // TODO: 0507
        /*ɾ��ѡ�е�ͼ�����
        �ο�����ķ���modifyBtn.addActionListener
        ��ѡ�б���е�ĳһ��ͼ����𣬵����ɾ������ťɾ����ͼ�����ɾ���ɹ���ˢ�±�񣬲����Ӵ��ڵײ��������ı����������
        */
        deleteBtn.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                int selectRow;
                selectRow = bookTypeTable.getSelectedRow();
                if(selectRow==-1) {
                    JOptionPane.showMessageDialog(null, "����ѡ��һ��ͼ�����");
                    return;
                }
                int choice = JOptionPane.showConfirmDialog(null,"ȷ��ɾ����ͼ�����",
                        null,JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if(choice==0) {
                    int bookTypeId = Integer.parseInt(bookTypeIdTextField.getText());
                    boolean flag = Dao.deleteBookTypeById(bookTypeId);
                    if(flag) {
                        JOptionPane.showMessageDialog(null, "ͼ�����ɾ���ɹ�");
                        initMyBooksTable(); clearAll();
                    } else {
                        JOptionPane.showMessageDialog(null, "ͼ�����ɾ��ʧ��");
                    }
                }
            }
        });
    }

    private void initMyBooksTable() {
        defaultTableModel.setRowCount(0);
        bookTypeList = Dao.selectAllBookType();

        for(int i=0; i<bookTypeList.size(); i++) {
            Vector<Object> vector = new Vector<>();
            vector.add(bookTypeList.get(i).getBookTypeId());
            vector.add(bookTypeList.get(i).getBookTypeName());
            vector.add(bookTypeList.get(i).getBookTypeDesc());
            defaultTableModel.addRow(vector);
        }

    }

    private void clearAll() {
        bookTypeIdTextField.setText("");
        bookTypeNameTextField.setText("");
        bookTypeDescTextField.setText("");
    }

}
