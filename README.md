
# database-contrast-util
---
## 2019年12月12日首次提交
## 描述
基于SpringBoot的数据库对比工具
## 采用技术
SpringBoot2.2.0  
druid  
mybatis  
## 功能
1.对比数据库表结构、索引  
2.手动传入数据库名称列表，是否自动修正，生成文本结果   
## 项目运行
直接运行DatabaseServiceApplication即可，默认8080端口   
请求示例：
>url地址：http://localhost:8080/test  
>请求参数： 
>{
 	"databasesName":["test"],
 	"autoFixWhether":0
 }  

参数解释：
>autoFixWhether参数：0为不自动修正，1为自动修正

## 注意
>每个项目中的resources文件夹需要IDEA识别出来，否则不能读取配置文件  
![Alt text](https://github.com/quhailong/database-contrast-util/blob/master/1.png)
## 结果截图
![Alt text](https://github.com/quhailong/database-contrast-util/blob/master/2.png)  
![Alt text](https://github.com/quhailong/database-contrast-util/blob/master/3.png) 
## tips
项目有些地方还不太完善，如果有什么问题请联系  
QQ：961584293  
WX: ququhailong  
邮箱:qhl961584293@163.com  
如果觉得还行，就请点个赞把
