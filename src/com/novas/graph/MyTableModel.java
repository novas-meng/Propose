package com.novas.graph;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;


class MyTableModel extends AbstractTableModel {
     String[] columnNames =new String[1];
    // 表格中各行的内容保存在二维数组data中
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
        // 表格中第一行所要显示的内容存放在字符串数组columnNames中

 
        // 下述方法是重写AbstractTableModel中的方法，其主要用途是被JTable对象调用，以便在表格中正确的显示出来。程序员必须根据采用的数据类型加以恰当实现。
 
        // 获得列的数目
        public int getColumnCount() {
            return columnNames.length;
        }
 
        // 获得行的数目
        public int getRowCount() {
            return data.length;
        }
 
        // 获得某列的名字，而目前各列的名字保存在字符串数组columnNames中
        public String getColumnName(int col) {
            return columnNames[col];
        }
 
        // 获得某行某列的数据，而数据保存在对象数组data中
        public Object getValueAt(int row, int col) {
            return data[row][col];
        }
 
        // 判断每个单元格的类型
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }
 
        // 将表格声明为可编辑的
        public boolean isCellEditable(int row, int col) {
 
            if (col < 2) {
                return false;
            } else {
                return true;
            }
        }
    }
