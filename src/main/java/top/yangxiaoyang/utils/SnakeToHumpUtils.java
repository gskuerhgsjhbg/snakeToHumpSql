package top.yangxiaoyang.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class SnakeToHumpUtils {
    private HashMap<String,Integer> hashMap;
    public void run(int types, int size) throws FileNotFoundException {
        //数据库的字段
        String[] dataTypes = {
                " VARCHAR", " BINARY", " VARBINARY", " TEXT", " BLOB", " ENUM", " SET", " LONGTEXT",
                " TINYINT", " SMALLINT", " MEDIUMINT", " BIGINT",
                " DATETIME", " TIMESTAMP", " YEAR",
                " FLOAT", " DOUBLE",
                " DECIMAL",
                " BOOLEAN",
                " BIT",
                " varchar", " binary", " varbinary", " text", " blob", " enum", " set", " longtext",
                " tinyint", " smallint", " mediumint", " bigint",
                " datetime", " timestamp", " year",
                " float", " double",
                " decimal",
                " boolean",
                " bit"
        };
        String[] otherDataTypes = {" CHAR", " INT", " DATE", " TIME", " char", " int", " date", " time",};
        
        
        //输入文件与输出文件
        String inPath = "sql.sql";
        String outPath = "out.sql";

        //开始操作
        hashMap=new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get(inPath)), StandardCharsets.UTF_8))) {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(Paths.get(outPath)), StandardCharsets.UTF_8));
            //new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), StandardCharsets.UTF_8))) {
            String line;
            StringBuilder line1 = new StringBuilder();
            StringBuilder line2 = new StringBuilder();
            StringBuilder line3 = new StringBuilder();
            String line4 = "";
            int tableNum = 0;
            int tableAliasBase = 96;    //97=a   97+25=z
            int tableOther = 0;
            String tableAlias = "";
            while ((line = reader.readLine()) != null) {
                boolean flag = false;
                if (line.contains("`")) {
                    //  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
                    String dataType = "";
                    for (String dataType1 : dataTypes) {
                        if (line.contains(dataType1)) {
                            flag = true;
                            dataType = dataType1;
                        }
                    }

                    if (!flag) {
                        for (String type : otherDataTypes) {
                            if (line.contains(type)) {
                                flag = true;
                                dataType = type;
                            }
                        }
                    }
                    if ((flag && line.contains("INDEX")) || (flag && line.contains("CONSTRAINT"))) {
                        flag = false;
                        writer.newLine();
                    }

                    if (flag) {
                        String[] split = line.split(dataType);
                        line1 = new StringBuilder(split[0]);
                        if (line1.toString().contains("_")) {
                            //`created_time`
                            String[] ss = line1.toString().split("_");
                            line2.append(ss[0].toLowerCase());//created
                            if (ss.length > 1) {
                                for (int i = 1; i < ss.length; i++) {
                                    String sub = ss[i].substring(0, 1).toUpperCase();
                                    String subs = ss[i].substring(1).toLowerCase();
                                    line2.append(sub).append(subs);
                                }
                            }
                        }
                        line3 = new StringBuilder(dataType);
                        String ss = split[1];
                        if (ss.contains(" COMMENT")) {
                            line4 = split[1].split(" COMMENT")[1];
                        }
                        if (ss.contains(" comment")) {
                            line4 = split[1].split(" comment")[1];
                        }

                        //writer.write(String.format("%-10s%-10s%-10s%-10s", line1, line2, line3,line4));
                        //制表

                        if (line2.length() == 0) {
                            line2 = line1;
                        }

                        for (int i = line1.length(); i < size; i++) {
                            line1.append(" ");
                        }
                        for (int i = line2.length(); i < size; i++) {
                            line2.append(" ");
                        }
                        for (int i = line3.length(); i < size; i++) {
                            line3.append(" ");
                        }

                        /* * type是输出类型
                         *  1：`province_id`
                         *  2：`province_id`  `provinceId`
                         *  3：`province_id`  `provinceId`  int
                         *  4：`province_id`  `provinceId`  int '省市'
                         *  5：`provinceId`*/

                        String sss = "";
                        switch (types) {
                            case 1:
                                sss = line1 + "";
                                break;
                            case 2:
                                sss = line1 + "" + line2;
                                break;
                            case 3:
                                sss = line1 + "" + line2 + "" + line3;
                                break;
                            case 4:
                                sss = line1 + "" + line2 + "" + line3 + "" + line4;
                                break;
                            case 5:
                                sss = line2 + "";
                                break;
                            case 6:
                                sss = viewMode(line1.toString(), line2.toString(), tableAlias);
                                break;
                            case 7:
                                sss = viewModeSql(hashMap,line1.toString(), line2.toString(), tableAlias);
                                break;
                            case 8:
                                sss = viewModeSqlDeduplication(hashMap,line1.toString(), line2.toString(), tableAlias);
                                break;
                            default:
                                sss = line1 + "" + line2 + "" + line3 + "" + line4;
                                break;
                        }
                        writer.write(sss);
                        writer.newLine();
                        writer.flush();

                        line1 = new StringBuilder();
                        line2 = new StringBuilder();
                        line3 = new StringBuilder();
                        line4 = "";
                        line = "";
                    }
                }
                //表头表名
                if (line.contains("CREATE TABLE")) {
                    String[] split = line.split("`");

                    if (types != 7 || types != 8) {
                        writer.write("table name = `" + split[1] + "`");
                        writer.newLine();
                        writer.flush();
                    }

                    tableNum++;
                    while (tableNum > 26) {
                        tableNum = tableNum - 26;
                        tableOther++;
                    }
                    char tc = (char) (tableAliasBase + tableNum);
                    tableAlias = "" + tc;
                    if (tableOther > 0) {
                        tableAlias = tableAlias + tableOther;
                    }

                }
                if(line.contains("COMMENT =")){
                    String trim = line.split("COMMENT =")[1].trim();
                    int lastIndexOf = trim.lastIndexOf("'");
                    trim=trim.substring(0,lastIndexOf+1);
                    writer.write("table info = " + trim );
                    writer.newLine();
                    writer.flush();
                }
                if (line.contains("DROP TABLE")) {
                    writer.newLine();
                    writer.newLine();
                    writer.newLine();
                    writer.newLine();
                    writer.newLine();
                    writer.flush();
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 转化成视图模型，并且对属性去重
     *
     * @param hashMap
     * @param line1
     * @param line2
     * @param tableAlias
     * @return
     */
    private String viewModeSqlDeduplication(HashMap<String, Integer> hashMap, String line1, String line2, String tableAlias) {
        line1 = line1.trim();
        line2 = line2.trim();

        String hash=line1.hashCode()+"";
        if(hashMap.get(hash) == null){
            //不存在
            String s1 = line1.substring(1, line1.length() - 1);
            String s2 = line2.substring(1, line2.length() - 1);
            hashMap.put(hash,1);
            return tableAlias + "." + s1 + "  AS  " + s2 + ",";
        } else {
            return "";
        }
    }

    /**
     * 转化成视图模型，重复的标识出来
     *
     * @param hashMap
     * @param line1
     * @param line2
     * @param tableAlias
     * @return
     */
    private String viewModeSql(HashMap<String, Integer> hashMap, String line1, String line2, String tableAlias) {
        line1 = line1.trim();
        line2 = line2.trim();
        
        String hash=line1.hashCode()+"";
        if(hashMap.get(hash) == null){
            //不存在
            String s1 = line1.substring(1, line1.length() - 1);
            String s2 = line2.substring(1, line2.length() - 1);
            hashMap.put(hash,1);
            return tableAlias + "." + s1 + "  AS  " + s2 + ","; 
        } else {
           //已经存在
            String s1 = line1.substring(1, line1.length() - 1);
            String s2 = line2.substring(1, line2.length() - 1);

            //标识
            Integer integer = hashMap.get(hash)+1;
            String rs =  "\n" +
                    "重复数据   当前为第 "+integer+"条\n----------------------------------------------------------------------------" +
                    "\n"+tableAlias + "." + s1 + "  AS  " + s2 + ",\n";
            //数据更新
            hashMap.put(hash,integer);
            return rs;
        }
    }

    public String viewMode(String line1, String line2, String tableAlias) {
        line1 = line1.trim();
        line2 = line2.trim();
        String s1 = line1.substring(1, line1.length() - 1);
        String s2 = line2.substring(1, line2.length() - 1);
        return tableAlias + "." + s1 + "  AS  " + s2 + ",";
    }
}
