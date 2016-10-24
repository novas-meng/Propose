package com.novas.data;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by novas on 2016/10/14.
 */
/*
生成人员和对应的阅读列表
 */
public class dataread {
    static HashMap<Character,ArrayList<String>> personMap=new HashMap<>();
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
       System.out.println(personMap);
       return personMap;
   }
    public static void toHashMap()throws Exception
    {
        Random random=new Random();
        ArrayList<String> list=new ArrayList<>();
        File[] files=new File("data").listFiles();
        System.out.println(files.length);
        for(int i=0;i<files.length;i++)
        {
            list.add(files[i].getName());
        }
        for (int i=0;i<10;i++)
        {
            personMap.put((char)(97+i),new ArrayList<String>());
        }
        for(Map.Entry<Character,ArrayList<String>> entry:personMap.entrySet())
        {
            ArrayList<String> list1=entry.getValue();
            System.out.println(list.size());
            int size=random.nextInt(list.size()-2)+1;
            for(int i=0;i<size;i++)
            {
                list1.add(list.get(random.nextInt(list.size())));
            }
            personMap.put(entry.getKey(),list1);
        }
        JSONObject jsonObject=new JSONObject();
        jsonObject.putAll(personMap);
        FileWriter fw=new FileWriter("person_article_json");
        fw.write(jsonObject.toString());
        fw.close();
        System.out.println(personMap);
    }
    public static void main(String[] args)throws Exception
    {
      //  getPersonMap("person_article_json");
        toHashMap();
    }

}
