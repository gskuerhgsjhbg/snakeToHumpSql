package java.top.yangxiaoyang;

import top.yangxiaoyang.utils.SnakeToHumpUtils;

import java.io.FileNotFoundException;
import java.sql.SQLOutput;
import java.util.HashMap;

/**
 * java -jar XXX.jar size=20 type=4

 * size默认25
 * type默认4

 *size是水平制表的长度
 * type是输出类型
 *  1：`province_id`
 *  2：`province_id`  `provinceId`
 *  3：`province_id`  `provinceId`  int
 *  4：`province_id`  `provinceId`  int  '省市'
 *  5：`provinceId`
 *  6: a.province_id AS provinceId, 
 *  7：输出6类型的视图，重复标识出来
 *  8：输出6类型的视图，重复自动去重，只保留第一个
 *  其他数字默认转化为4
 *
 */
public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        int size=25;
        int type=4;


        //int type=6;
        //判断输入size和type
        int argSize=args.length;
        if(argSize > 0 && argSize < 5){
            for (int i = 0; i < argSize; i++) {
                if(args[i].trim().equals("-s")){
                    try {
                        size = Integer.parseInt(args[i+1]);
                        if(size < 5){
                            size = 5;
                        }
                        if(size > 50){
                            size=50;
                        }
                        System.out.println("------------------------------------------");
                        System.out.println("输入的水平制表宽度size为："+size);
                    }catch (Exception e){
                        size=25;
                        System.out.println("------------------------------------------");
                        System.out.println("格式输入有误，应该为 -s x ,x为大于0的数字");
                    }
                }
                if(args[i].trim().equals("-t")){
                    try {
                        type = Integer.parseInt(args[i+1]);
                        if(type > 8 || type < 0){
                            type = 4;
                        }
                        System.out.println("------------------------------------------");
                        System.out.println("输入的type为："+type);
                    }catch (Exception e){
                        type=4;
                        System.out.println("------------------------------------------");
                        System.out.println("格式输入有误，应该为 -t x ,x为1~8");
                    }
                }
            }
        }
        System.out.println("------------------------------------------");
        System.out.println(" *size是水平制表的长度 大于5小于50\n" +
                " * type是输出类型\n" +
                " *  1：`province_id`\n" +
                " *  2：`province_id`  `provinceId`\n" +
                " *  3：`province_id`  `provinceId`  int\n" +
                " *  4：`province_id`  `provinceId`  int  '省市'\n" +
                " *  5：`provinceId`\n" +
                " *  6：a.province_id AS provinceId\n" +
                " *  7：输出6类型的视图，重复标识出来\n" +
                " *  8：输出6类型的视图，重复自动去重，只保留第一个\n" +
                " *  其他数字默认转化为4");
        System.out.println("------------------------------------------");
        SnakeToHumpUtils utils = new SnakeToHumpUtils();
        
        //视图也可以设置size
        if(size == 25 && (type == 6 || type == 7 || type == 8)){
            size=0;
        }

        utils.run(type,size);
    }
}