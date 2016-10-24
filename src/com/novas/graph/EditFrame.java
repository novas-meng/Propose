package com.novas.graph;

import com.novas.data.DataModel;
import com.novas.data.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by novas on 2016/10/6.
 */
public class EditFrame extends JFrame
{
    JTextArea disabletextArea=new JTextArea();
    JTextArea abletextArea=new JTextArea();
    JTextArea labelarea=new JTextArea();
    DataModel dataModel;
    public EditFrame(boolean isMyProfile,String search,DataModel dataModel)
    {
        this.dataModel=dataModel;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1000, 700);
        this.setLayout(null);
        try {
            this.addProfile(isMyProfile,search);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        this.addTextAreaDisabled(disabletextArea);
        this.newArticleButton();
        this.setVisible(true);
    }
    public void showArticle(String articlename)
    {
        System.out.println("��ʾ");
        String articlecontent=null;
        try
        {
            articlecontent= FileUtils.readFile("data/" + articlename);
            System.out.println("articlecont="+articlecontent);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        disabletextArea.setText(articlecontent);
    }
    public void addTextAreaDisabled(JTextArea textArea)
    {
        textArea.setSize(600, 600);
        textArea.setAutoscrolls(true);
        textArea.setBackground(Color.gray);
        JLabel label=new JLabel("����Ԥ��");
        label.setBounds(300,0,600,50);
        textArea.setBounds(300, 50, 600, 550);
        textArea.setLineWrap(true);
        this.add(label);
        this.add(textArea);
    }
    public void newArticleButton()
    {

        JButton button=new JButton("�½�");
        button.setBounds(910,40,60,40);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new EditNewArticleFrame();
            }
        });
        this.add(button);
    }
    public void addProfile(boolean isprofile,String search)throws Exception
    {
        JPanel panel=new JPanel();
        VFlowLayout fl=new VFlowLayout();
        panel.setLayout(fl);
        /*
        ��Ҫ�Ķ�����
         */
        final MyTableModel WantToReadModel = new MyTableModel("�������Ҫ�Ķ�",dataModel.getWantToReadList());// myModel��ű�������
        JTable wanttoreadtable = new JTable(WantToReadModel);// ������table��������Դ��myModel����
        wanttoreadtable.setPreferredScrollableViewportSize(new Dimension(300, 150));// ������ʾ�ߴ�
        wanttoreadtable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = ((JTable) e.getSource()).rowAtPoint(e.getPoint()); //�����λ��

                    int col = ((JTable) e.getSource()).columnAtPoint(e.getPoint()); //�����λ��
                    String cellVal = (String) (WantToReadModel.getValueAt(row, col));
                    System.out.println(cellVal);
                    showArticle(cellVal);
                }
            }
        });
        JScrollPane wanttoreadscrollPane = new JScrollPane(wanttoreadtable);
        panel.add(wanttoreadscrollPane, BorderLayout.CENTER);

   /*
        ��һ�ܴ������ĵ�
         */
        final MyTableModel MyWriteModel = new MyTableModel("��һ�ܴ������ĵ�",dataModel.getWantToReadList());// myModel��ű�������
        JTable MyWriteTable = new JTable(MyWriteModel);// ������table��������Դ��myModel����
        MyWriteTable.setPreferredScrollableViewportSize(new Dimension(300, 150));// ������ʾ�ߴ�

        MyWriteTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = ((JTable) e.getSource()).rowAtPoint(e.getPoint()); //�����λ��

                    int col = ((JTable) e.getSource()).columnAtPoint(e.getPoint()); //�����λ��
                    String cellVal = (String) (MyWriteModel.getValueAt(row, col));
                    System.out.println(cellVal);
                    showArticle(cellVal);
                }
            }
        });
        // ����һ���������������
        JScrollPane mywritescrollPane = new JScrollPane(MyWriteTable);
        panel.add(mywritescrollPane, BorderLayout.CENTER);

 /*
        Ϊ���Ƽ�
         */
        if(!isprofile)
        {
            final MyTableModel ProposeReadModel = new MyTableModel("�������",dataModel.getProposeList(search));// myModel��ű�������

            JTable Proposetable = new JTable(ProposeReadModel);// ������table��������Դ��myModel����

            Proposetable.setPreferredScrollableViewportSize(new Dimension(300, 150));// ������ʾ�ߴ�
            Proposetable.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        int row = ((JTable) e.getSource()).rowAtPoint(e.getPoint()); //�����λ��

                        int col = ((JTable) e.getSource()).columnAtPoint(e.getPoint()); //�����λ��
                        String cellVal = (String) (ProposeReadModel.getValueAt(row, col));
                        System.out.println(cellVal);
                        showArticle(cellVal);
                    }
                }
            });
            JScrollPane ProposescrollPane = new JScrollPane(Proposetable);
            panel.add(ProposescrollPane, BorderLayout.CENTER);
        }
        panel.setBounds(0,0,300,600);
        this.add(panel);
    }
}
