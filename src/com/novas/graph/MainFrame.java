package com.novas.graph;

import com.novas.data.DataModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

/**
 * Created by novas on 2016/10/5.
 */
public class MainFrame extends JFrame
{
    helper h;
    DataModel dataModel;
    public MainFrame(helper h,DataModel dataModel)
    {
        this.dataModel=dataModel;
        this.h=h;
     //   this.setSize(1000, 800);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        VFlowLayout fl=new VFlowLayout();
        this.setLayout(fl);
        this.addLabel("DocHelper");
        try {
            this.addWantToReadTable();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        this.addEdit();
        this.pack();
        this.setVisible(true);

    }
    public  void addLabel(String labelname)
    {
        JLabel label=new JLabel(labelname);
        this.add(label);
        JPanel panel=new JPanel();
        panel.setSize(200, 20);
        this.add(panel);
    }
    public void addEdit()
    {
        final JTextArea jTextArea=new JTextArea();
        JPanel panel=new JPanel();
        JButton button1=new JButton("�ҵ��ĵ�");
        JButton button2=new JButton("����");
        panel.add(button1);
        panel.add(button2);
        this.add(panel);
        this.add(jTextArea);
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                h.myprofile();
            }
        });
       button2.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               h.search(jTextArea.getText());
           }
       });
    }
    public void addWantToReadTable()throws Exception
    {
        JLabel label=new JLabel("�������Ҫ�Ķ�");
        this.add(label);
        String columnname="��Ҫ�Ķ�";
        ArrayList<String> list=dataModel.getWantToReadList();
        MyTableModel myModel = new MyTableModel(columnname,list);// myModel��ű�������
        JTable table = new JTable(myModel);// ������table��������Դ��myModel����
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));// ������ʾ�ߴ�

        // ����һ���������������
        JScrollPane scrollPane = new JScrollPane(table);


        // �������������������봰����
        this.add(scrollPane, BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {// ע�ᴰ�ڼ�����
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

}
