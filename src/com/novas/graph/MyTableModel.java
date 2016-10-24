package com.novas.graph;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;


class MyTableModel extends AbstractTableModel {
     String[] columnNames =new String[1];
    // ����и��е����ݱ����ڶ�ά����data��
     Object[][] data ;
      public MyTableModel(String columnname,ArrayList<String> articleList)
      {
          columnNames[0]=columnname;
          data=new Object[articleList.size()][1];
          for(int i=0;i<articleList.size();i++)
          {
              data[i][0]=articleList.get(i);
          }
      }
        // ����е�һ����Ҫ��ʾ�����ݴ�����ַ�������columnNames��

 
        // ������������дAbstractTableModel�еķ���������Ҫ��;�Ǳ�JTable������ã��Ա��ڱ������ȷ����ʾ����������Ա������ݲ��õ��������ͼ���ǡ��ʵ�֡�
 
        // ����е���Ŀ
        public int getColumnCount() {
            return columnNames.length;
        }
 
        // ����е���Ŀ
        public int getRowCount() {
            return data.length;
        }
 
        // ���ĳ�е����֣���Ŀǰ���е����ֱ������ַ�������columnNames��
        public String getColumnName(int col) {
            return columnNames[col];
        }
 
        // ���ĳ��ĳ�е����ݣ������ݱ����ڶ�������data��
        public Object getValueAt(int row, int col) {
            return data[row][col];
        }
 
        // �ж�ÿ����Ԫ�������
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }
 
        // ���������Ϊ�ɱ༭��
        public boolean isCellEditable(int row, int col) {
 
            if (col < 2) {
                return false;
            } else {
                return true;
            }
        }
    }
