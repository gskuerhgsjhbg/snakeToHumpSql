# snakeToHumpSql

#### 介绍
sql文件蛇形命名转驼峰命名，以及视图的部分创建



#### 软件架构
软件架构说明
jak1.8



#### 使用说明

1.  将数据库导出的sql文件重命名为 sql.sql ，并且将当前jar包放在同一目录，运行输出为 out.sql
2.  java -jar snakeToHumpSql-x.x.jar -s 30 -t 30
3.  参数：-s 水平制表大小  ; -t 类型，具体类型如下

```txt
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
```



#### 参与贡献
杨小羊




#### 特技

1.  使用 Readme\_XXX.md 来支持不同的语言，例如 Readme\_en.md, Readme\_zh.md
2.  Gitee 官方博客 [blog.gitee.com](https://blog.gitee.com)
3.  你可以 [https://gitee.com/explore](https://gitee.com/explore) 这个地址来了解 Gitee 上的优秀开源项目
4.  [GVP](https://gitee.com/gvp) 全称是 Gitee 最有价值开源项目，是综合评定出的优秀开源项目
5.  Gitee 官方提供的使用手册 [https://gitee.com/help](https://gitee.com/help)
6.  Gitee 封面人物是一档用来展示 Gitee 会员风采的栏目 [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)

