package com.novas.data;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * Created by novas on 2016/10/7.
 */
public class FileUtils {

    //生成人员和读的文件的map
    public static HashMap<Character,ArrayList<String>> getPersonMap(String fileName)throws Exception
    {
        HashMap<Character,ArrayList<String>> personMap=new HashMap<>();
        String str=FileUtils.readFile(fileName);
        JSONObject jsonObject=JSONObject.fromObject(str);
        Iterator it=jsonObject.keys();
        while (it.hasNext())
        {
            String person=it.next().toString();
            ArrayList<String> list=new ArrayList<>();
            JSONArray value=jsonObject.getJSONArray(person);
            for(int i=0;i<value.size();i++)
            {
                list.add(value.getString(i));
            }
            personMap.put(person.charAt(0),list);
        }
      //  System.out.println(personMap);
        return personMap;
    }
    public static HashMap toHashMap(String filename)throws Exception
    {
        HashMap<String,HashMap<String,Double>> ArticleMap=new HashMap<>();

        BufferedReader br=new BufferedReader(new FileReader(filename));
        String line=br.readLine();
        br.close();
        JSONObject jsonObject=JSONObject.fromObject(line);
        Iterator it=jsonObject.keys();
        while (it.hasNext())
        {
            String articlename=it.next().toString();
            HashMap<String,Double> map=new LinkedHashMap<>();
            JSONObject value=JSONObject.fromObject(jsonObject.get(articlename));
            Iterator valit=value.keys();
            while (valit.hasNext())
            {
                String word=valit.next().toString();
                map.put(word,value.getDouble(word));
            }
            ArticleMap.put(articlename,map);
        }
        return ArticleMap;
    }
    //读取文件内容
    public static String readFile(String filename)throws Exception
    {
        byte[] bytes=new byte[1024];
        InputStream fis=new FileInputStream(filename);

        int length=fis.read(bytes);
        //	System.out.println(new String(bytes));
        byte[] res=new byte[0];
        while (length!=-1)
        {
            byte[] temp=new byte[res.length];
            System.arraycopy(res,0,temp,0,res.length);
            res=new byte[res.length+length];
            System.arraycopy(bytes,0,res,res.length-length,length);
            System.arraycopy(temp,0,res,0,temp.length);
            bytes=new byte[1024];
            length=fis.read(bytes);
        }
        return new String(res);
    }
    //读取停用词
    public static ArrayList<String> readStopWords(String file)throws Exception
    {
        BufferedReader bufferedReader=new BufferedReader(new FileReader(file));
        String line=bufferedReader.readLine();
        ArrayList<String> list=new ArrayList<>();
        while (line!=null)
        {
            list.add(line);
            line=bufferedReader.readLine();
        }
        return list;
    }
}
