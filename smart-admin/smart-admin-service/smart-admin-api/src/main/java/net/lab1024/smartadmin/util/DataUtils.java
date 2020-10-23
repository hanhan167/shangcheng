package net.lab1024.smartadmin.util;

import java.util.List;

/**
 * @author zhouchengyan
 * @time 2020/10/23 11:40
 */
public class DataUtils {

    public static boolean checkList(List list){
        if(list!=null && list.size()>0){
            return true;
        }
        return false;
    }


}
