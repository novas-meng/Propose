package com.novas.graph;

import com.novas.data.DataModel;

/**
 * Created by novas on 2016/10/5.
 */
public class DocHelper implements helper
{
    static DataModel dataModel;
    private DocHelper()
    {
         dataModel=DataModel.getDataModelInstance();
    }
    public static DocHelper getDocHelperInstance()
    {
        return DocHolder.helper;
    }
    private static class DocHolder
    {
        static DocHelper helper=new DocHelper();
    }
    public static void main(String[] args)
    {
        MainFrame firstframe=new MainFrame(DocHelper.getDocHelperInstance(),dataModel);
    }

    @Override
    public void myprofile() {
        EditFrame frame=new EditFrame(true,null,dataModel);
    }

    @Override
    public void search(String search) {
        EditFrame frame=new EditFrame(false,search,dataModel);
    }
}
